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
def load_verb_labels(label_file_path):
    df = pd.read_csv(label_file_path)
    return dict(zip(df['id'], df['key']))

def load_noun_labels(label_file_path):
    df = pd.read_csv(label_file_path)
    return dict(zip(df['id'], df['key']))

# 라벨 로드
verb_id_to_class = load_verb_labels(verb_label_file)
noun_id_to_class = load_noun_labels(noun_label_file)

# 전처리 변환 정의
preprocess = transforms.Compose([
    transforms.Resize(256),
    transforms.CenterCrop(224),
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225])
])

# 모델을 사용하여 행동 예측 함수
def predict_action(model, frames, verb_id_to_class, noun_id_to_class):
    fast_pathway = torch.stack(frames).unsqueeze(0).permute(0, 2, 1, 3, 4).to(device)
    slow_pathway = fast_pathway[:, :, ::4, :, :]
    input_tensor = [slow_pathway, fast_pathway]

    with torch.no_grad():
        output = model(input_tensor)

    if isinstance(output, torch.Tensor):
        verb_logits = output
        noun_logits = output
    else:
        verb_logits, noun_logits = output

    verb_probs = torch.nn.functional.softmax(verb_logits, dim=1)
    noun_probs = torch.nn.functional.softmax(noun_logits, dim=1)

    topk = 3
    verb_topk_probs, verb_topk_indices = torch.topk(verb_probs, topk)
    noun_topk_probs, noun_topk_indices = torch.topk(noun_probs, topk)

    verb_predictions = [(verb_id_to_class.get(idx.item(), "Unknown"), prob.item()) for idx, prob in
                        zip(verb_topk_indices[0], verb_topk_probs[0])]
    noun_predictions = [(noun_id_to_class.get(idx.item(), "Unknown"), prob.item()) for idx, prob in
                        zip(noun_topk_indices[0], noun_topk_probs[0])]

    return verb_predictions, noun_predictions

# 세그먼트 수를 조정하여 예측하는 함수 (num_segments를 64로 설정)
def predict_actions_on_segments(video_path, num_segments=64, segment_count=32):  # 기본 세그먼트 수를 64로 설정
    cap = cv2.VideoCapture(video_path)
    if not cap.isOpened():
        print(f"Error: Cannot open video file {video_path}")
        return []

    total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
    fps = cap.get(cv2.CAP_PROP_FPS) or 30
    frames_per_segment = total_frames // num_segments

    predicted_actions = []
    start_times = []  # 각 세그먼트의 시작 시간을 저장할 리스트

    for i in range(num_segments):
        start_frame = int(i * frames_per_segment)
        start_time = start_frame / fps  # 시작 시간을 초 단위로 계산
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

            frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
            frame = Image.fromarray(frame)
            frame = preprocess(frame)
            frames.append(frame)

        if frames and len(frames) == segment_count:
            verb_predictions, noun_predictions = predict_action(model, frames, verb_id_to_class, noun_id_to_class)
            predicted_actions.append((verb_predictions, noun_predictions))
        else:
            predicted_actions.append(([], []))

    cap.release()
    return predicted_actions, start_times

# 전체 프로세싱 함수
def processingVideo(video_path, cooking_steps):
    num_segments = len(cooking_steps)
    predicted_actions, start_times = predict_actions_on_segments(video_path, num_segments)

    result = []
    for i in range(num_segments):
        step = cooking_steps[i]
        verb_predictions, noun_predictions = predicted_actions[i]
        result.append({
            "Cooking Step": step,
            "Start Time (s)": start_times[i],  # 타임라인 추가
            "Verb Predictions": verb_predictions,
            "Noun Predictions": noun_predictions
        })

    return result

# 요리 과정 정의
# 요리 과정 정의 (영어)
cooking_steps = [
    "prepare pork bones",
    "soak bones in water to remove blood",
    "place bones in pot, cover with water, bring to boil",
    "boil bones, remove scum and impurities",
    "transfer bones to stock pot, simmer to reduce liquid",
    "roll pork belly, secure with string",
    "prepare sauce with sugar, soy sauce, mirin, sake",
    "simmer pork belly in sauce for hours",
    "remove broth, strain bones, start second boiling",
    "repeat boiling and reduction two more times",
    "combine broths for rich base",
    "slice green onions, mushrooms for toppings",
    "marinate eggs in soy sauce, mirin",
    "prepare kaeshi sauce with soy sauce, mirin, sake, salt",
    "mix broth with kaeshi sauce at ratio 10:1",
    "add noodles, arrange toppings"
]


# 비디오 파일 경로 예시
video_path = "video/data/sample2.mp4"

# 프로세스 실행
def temp():
    result = processingVideo(video_path, cooking_steps)
    for item in result:
        print(f"Cooking Step: {item['Cooking Step']}")
        print(f"Start Time: {item['Start Time (s)']:.2f} seconds")  # 시작 시간 출력
        print("Verb Predictions:")
        for label, prob in item['Verb Predictions']:
            print(f"  {label}: {prob:.4f}")
        print("Noun Predictions:")
        for label, prob in item['Noun Predictions']:
            print(f"  {label}: {prob:.4f}")
        print("-" * 50)
    return result
