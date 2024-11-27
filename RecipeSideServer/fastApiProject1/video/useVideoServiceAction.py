import torch
import cv2
import numpy as np
from torchvision import transforms
from PIL import Image
import pandas as pd

# Apple Silicon M1에서 가속화를 위해 mps 장치 확인
device = torch.device("cpu")

# SlowFast 모델 로드 (Epic-Kitchens 사전 학습된 가중치 로드 포함)
model = torch.hub.load("facebookresearch/pytorchvideo", "slowfast_r50", pretrained=True)
model.to(device)
model.eval()

# 사전 학습된 가중치 로드
pretrained_weights_path = 'video/data/SlowFast.pyth'
checkpoint = torch.load(pretrained_weights_path, map_location=device)
model.load_state_dict(checkpoint['model_state'], strict=False)

# 100개 클래스 라벨 파일 경로
verb_label_file = 'video/data/EPIC_100_verb_classes.csv'
noun_label_file = 'video/data/EPIC_100_noun_classes_v2.csv'


# CSV 파일을 읽어 라벨을 로드하는 함수 정의
def load_verb_labels_extended(label_file_path):
    df = pd.read_csv(label_file_path)
    return {row['id']: {'key': row['key'], 'instances': eval(row['instances']), 'category': row['category']}
            for _, row in df.iterrows()}


def load_noun_labels_extended(label_file_path):
    df = pd.read_csv(label_file_path)
    return {row['id']: {'key': row['key'], 'instances': eval(row['instances']), 'category': row['category']}
            for _, row in df.iterrows()}


# 라벨 로드
verb_id_to_class = load_verb_labels_extended(verb_label_file)
noun_id_to_class = load_noun_labels_extended(noun_label_file)

# 전처리 변환 정의
preprocess = transforms.Compose([
    transforms.Resize(512),  # 해상도를 512로 설정
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
])


# 예측 결과 매칭 함수
def match_prediction_extended(prediction_id, prediction_prob, label_dict):
    label_info = label_dict.get(prediction_id, {})
    key = label_info.get('key', 'Unknown')
    instances = label_info.get('instances', [])
    category = label_info.get('category', 'Unknown')

    return {'label': key, 'probability': prediction_prob, 'instances': instances, 'category': category}


# 모델을 사용하여 행동 예측 함수 (상위 K 확률 설정)
def predict_action_extended(model, frames, verb_id_to_class, noun_id_to_class, topk=5):
    frames = frames[:32]  # Use the first 32 frames for consistency

    fast_pathway = torch.stack(frames).unsqueeze(0).permute(0, 2, 1, 3, 4).to(device)  # [B, C, T, H, W]
    slow_pathway = fast_pathway[:, :, ::4, :, :]  # Downsampling by a factor of 4

    input_tensor = [slow_pathway, fast_pathway]  # List containing both pathways

    with torch.no_grad():
        output = model(input_tensor)

    if isinstance(output, torch.Tensor):
        verb_logits = output
        noun_logits = output
    else:
        verb_logits, noun_logits = output

    # Apply softmax and get top-k predictions
    verb_probs = torch.nn.functional.softmax(verb_logits, dim=1)
    noun_probs = torch.nn.functional.softmax(noun_logits, dim=1)

    verb_topk_probs, verb_topk_indices = torch.topk(verb_probs, topk)
    noun_topk_probs, noun_topk_indices = torch.topk(noun_probs, topk)

    verb_predictions = [match_prediction_extended(idx.item(), prob.item(), verb_id_to_class) for idx, prob in
                        zip(verb_topk_indices[0], verb_topk_probs[0])]
    noun_predictions = [match_prediction_extended(idx.item(), prob.item(), noun_id_to_class) for idx, prob in
                        zip(noun_topk_indices[0], noun_topk_probs[0])]

    return verb_predictions, noun_predictions


# 시간적 스무딩 적용: 각 세그먼트의 예측에서 확률값만 평균화
def smooth_predictions(predictions, smoothing_window=3):
    smoothed_predictions = []

    for i in range(len(predictions) - smoothing_window + 1):
        # Get verb and noun predictions in the current smoothing window
        verb_window = [predictions[i + j][0] for j in range(smoothing_window)]
        noun_window = [predictions[i + j][1] for j in range(smoothing_window)]

        # Average the probabilities for verbs and nouns
        smoothed_verb = []
        smoothed_noun = []

        for verb_pred in zip(*verb_window):
            label, _ = verb_pred[0]['label'], verb_pred[0]['category']
            avg_prob = np.mean([p['probability'] for p in verb_pred])
            smoothed_verb.append({'label': label, 'probability': avg_prob, 'category': verb_pred[0]['category']})

        for noun_pred in zip(*noun_window):
            label, _ = noun_pred[0]['label'], noun_pred[0]['category']
            avg_prob = np.mean([p['probability'] for p in noun_pred])
            smoothed_noun.append({'label': label, 'probability': avg_prob, 'category': noun_pred[0]['category']})

        smoothed_predictions.append((smoothed_verb, smoothed_noun))

    # Pad smoothed predictions to match original length
    while len(smoothed_predictions) < len(predictions):
        smoothed_predictions.append(smoothed_predictions[-1])

    return smoothed_predictions


# 세그먼트 수를 조정하여 예측하는 함수
# def predict_actions_on_segments(video_path, num_segments=128, segment_count=64):
#     cap = cv2.VideoCapture(video_path)
#     if not cap.isOpened():
#         print(f"Error: Cannot open video file {video_path}")
#         return []
#
#     total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
#     fps = cap.get(cv2.CAP_PROP_FPS) or 30
#     frames_per_segment = total_frames // num_segments
#
#     predicted_actions = []
#     start_times = []
#
#     for i in range(num_segments):
#         start_frame = int(i * frames_per_segment)
#         start_time = start_frame / fps
#         start_times.append(start_time)
#
#         end_frame = int(start_frame + frames_per_segment)
#         frame_indices = np.linspace(start_frame, end_frame - 1, num=segment_count, dtype=int)
#
#         frames = []
#         for idx in frame_indices:
#             cap.set(cv2.CAP_PROP_POS_FRAMES, idx)
#             ret, frame = cap.read()
#             if not ret:
#                 print(f"Warning: Could not read frame at index {idx}")
#                 continue
#
#             frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
#             frame = Image.fromarray(frame)
#             frame = preprocess(frame)
#             frames.append(frame)
#
#         if frames and len(frames) == segment_count:
#             verb_predictions, noun_predictions = predict_action_extended(model, frames, verb_id_to_class,
#                                                                          noun_id_to_class)
#             predicted_actions.append((verb_predictions, noun_predictions))
#         else:
#             predicted_actions.append(([], []))
#
#     cap.release()
#
#     # Apply smoothing on predictions
#     smoothed_predictions = smooth_predictions(predicted_actions)
#     return smoothed_predictions, start_times
def predict_actions_on_segments(video_path, num_segments=128, segment_count=64):
    cap = cv2.VideoCapture(video_path)
    if not cap.isOpened():
        raise ValueError(f"Cannot open video file: {video_path}")

    total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
    fps = cap.get(cv2.CAP_PROP_FPS) or 30
    frames_per_segment = total_frames // num_segments

    predicted_actions = []
    start_times = []

    for i in range(num_segments):
        start_frame = int(i * frames_per_segment)
        start_time = start_frame / fps
        start_times.append(start_time)

        end_frame = int(start_frame + frames_per_segment)
        frame_indices = np.linspace(start_frame, end_frame - 1, num=segment_count, dtype=int)

        frames = []
        for idx in frame_indices:
            cap.set(cv2.CAP_PROP_POS_FRAMES, idx)
            ret, frame = cap.read()
            if not ret:
                print(f"Warning: Could not read frame at index {idx}")
                continue

            try:
                frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
                frame = Image.fromarray(frame)
                frame = preprocess(frame)
                frames.append(frame)
            except Exception as e:
                print(f"Error processing frame at index {idx}: {e}")

        if len(frames) == segment_count:
            try:
                verb_predictions, noun_predictions = predict_action_extended(
                    model, frames, verb_id_to_class, noun_id_to_class
                )
                predicted_actions.append((verb_predictions, noun_predictions))
            except Exception as e:
                print(f"Error during prediction: {e}")
                predicted_actions.append(([], []))
        else:
            print(f"Insufficient frames for segment {i}. Expected: {segment_count}, Got: {len(frames)}")
            predicted_actions.append(([], []))

    cap.release()

    # Apply smoothing on predictions
    smoothed_predictions = smooth_predictions(predicted_actions)
    return smoothed_predictions, start_times



def format_time(seconds):
    hours = int(seconds // 3600)
    minutes = int((seconds % 3600) // 60)
    seconds = int(seconds % 60)
    return f"{hours:02}:{minutes:02}:{seconds:02}"


# 전체 프로세싱 함수
# 전체 프로세싱 함수
# def processingVideo(video_path, cooking_steps):
#     num_segments = len(cooking_steps)
#     predicted_actions, start_times = predict_actions_on_segments(video_path, num_segments)
#
#     # Use the minimum of num_segments and the length of predicted_actions
#     results_length = min(len(predicted_actions), num_segments)
#
#     result = []
#     for i in range(results_length):
#         step = cooking_steps[i]
#         verb_predictions, noun_predictions = predicted_actions[i]
#         formatted_time = format_time(start_times[i])
#         result.append({
#             "Cooking Step": step,
#             "Start Time (s)": formatted_time,
#             "Verb Predictions": verb_predictions,
#             "Noun Predictions": noun_predictions
#         })
#
#     return result

# 전체 프로세싱 함수
def processingVideo(video_path, temp_cooking_steps, realSteps):
    num_segments = len(temp_cooking_steps)
    predicted_actions, start_times = predict_actions_on_segments(video_path, num_segments)

    # Use the minimum of num_segments and the length of predicted_actions
    results_length = min(len(predicted_actions), num_segments)

    cookSteps = []
    for i in range(results_length):
        step = realSteps[i].get("content")
        formatted_time = format_time(start_times[i])

        cookSteps.append({
            "stepNumber": i + 1,  # Step number starts from 1
            "timeLine": formatted_time,  # Timeline in HH:MM:SS format
            "content": step # Cooking step content
        })

    return cookSteps  # Return in the requested structure


# 요리 과정 정의
cooking_steps = [
    "prepare and soak pork bones to remove blood",
    "boil bones and transfer to stock pot",
    "simmer pork belly rolled with sauce ingredients",
    "remove and reduce broth for concentrated flavor",
    "combine broths for a rich base",
    "slice green onions and marinate eggs",
    "prepare kaeshi sauce with soy and mirin",
    "mix broth with kaeshi sauce and add noodles, arrange toppings"
]

real_cooking_steps = [
    "돼지 뼈를 준비하고 핏물을 제거하기 위해 물에 담가 둡니다",
    "뼈를 끓이고 육수 냄비로 옮깁니다",
    "양념 재료와 함께 돼지 삼겹살을 말아서 약한 불로 끓입니다",
    "진한 맛을 내기 위해 육수를 제거하고 졸입니다",
    "진한 맛을 위한 육수를 혼합합니다",
    "파를 썰고 계란을 간장에 절입니다",
    "간장과 미림으로 카에시 소스를 준비합니다",
    "카에시 소스와 육수를 섞고 면을 넣어 토핑을 올립니다"
]

# 비디오 파일 경로 예시
video_path2 = "video/data/sample2.mp4"


# # 프로세스 실행
# result = processingVideo(video_path, cooking_steps)
# # 보기 편한 형식으로 출력
# for item in result:
#     print(f"Cooking Step: {item['Cooking Step']}")
#     print(f"Start Time: {item['Start Time (s)']}")
#     print("Verb Predictions:")
#     for pred in item['Verb Predictions']:
#         print(f"  {pred['label']} (Category: {pred['category']}): {pred['probability']:.4f}")
#     print("Noun Predictions:")
#     for pred in item['Noun Predictions']:
#         print(f"  {pred['label']} (Category: {pred['category']}): {pred['probability']:.4f}")
#     print("-" * 50)

# 프로세스 실행
def temp():
    result = processingVideo(video_path2, cooking_steps,real_cooking_steps)
    # for item in result:
    #     print(f"Cooking Step: {item['Cooking Step']}")
    #     print(f"Start Time: {item['Start Time (s)']:.2f} seconds")  # 시작 시간 출력
    #     print("Verb Predictions:")
    #     for label, prob in item['Verb Predictions']:
    #         print(f"  {label}: {prob:.4f}")
    #     print("Noun Predictions:")
    #     for label, prob in item['Noun Predictions']:
    #         print(f"  {label}: {prob:.4f}")
    #     print("-" * 50)
    return result
