import cv2
import torch
import torchvision.transforms as transforms




# def processing_frame(video_path, frame_interval=5):
#     """
#     주어진 비디오에서 일정 간격으로 프레임을 추출하고 전처리한 결과를 반환합니다.
#
#     Args:
#         video_path (str): 비디오 파일 경로
#         frame_interval (int): 프레임 간격 (기본값: 5)
#
#     Returns:
#         List[torch.Tensor]: 전처리된 프레임 리스트
#     """
#     # 비디오 파일 로드
#     cap = cv2.VideoCapture(video_path)
#     frame_rate = int(cap.get(cv2.CAP_PROP_FPS))
#     total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
#
#     # 전처리 파이프라인 정의
#     preprocess = transforms.Compose([
#         transforms.ToPILImage(),
#         transforms.Resize((256, 256)),
#         transforms.CenterCrop((224, 224)),
#         transforms.ToTensor(),
#         transforms.Normalize(mean=[0.45, 0.45, 0.45], std=[0.225, 0.225, 0.225])
#     ])
#
#     processed_frames = []  # 전처리된 프레임 저장 리스트
#
#     while cap.isOpened():
#         ret, frame = cap.read()
#         if not ret:
#             break
#
#         current_frame = int(cap.get(cv2.CAP_PROP_POS_FRAMES))
#
#         # 일정 간격에 맞는 프레임만 처리
#         if current_frame % frame_interval == 0:
#             # BGR -> RGB 변환 및 전처리 적용
#             processed_frame = preprocess(cv2.cvtColor(frame, cv2.COLOR_BGR2RGB))
#             processed_frames.append(processed_frame)
#
#     cap.release()
#     return processed_frames
#
# def processingVideo(video, cookStep):
#     # 비디오 행동 분석
#     # https://epic-kitchens.github.io/2024
#     # 1. 영상 프레임 쪼개기 - 기존 코드 사용, 전처리 프레임 추출 시간 감소
#     frame = processing_frame(video)
#
#     # 행동 분석 진행
#
#     # 행동 분석결과와 cookStep - 조리과정.txt 비교
#
#     # 비교 결과에 따른 타임라인 정리
#
#     timeline = []
#
#     # 타임라인 정리 후 반환
#     return timeline


import torch
import cv2
import torchvision.transforms as transforms
from torchvision import models  # TBN에 필요한 경우 대체 모듈 임포트

# TBN 모델 로드 (여기서는 기본 PyTorch 모델을 예시로 사용)
# 실제 TBN 모델은 TBN 저장소에서 가져와야 함
def load_tbn_model():
    model = torch.hub.load('facebookresearch/pytorchvideo', 'slowfast_r50', pretrained=True)
    model.eval()
    return model

# 전처리 함수
def processing_frame(video_path, fast_interval=5, slow_interval=30):
    cap = cv2.VideoCapture(video_path)
    preprocess = transforms.Compose([
        transforms.ToPILImage(),
        transforms.Resize((256, 256)),
        transforms.CenterCrop((224, 224)),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.45, 0.45, 0.45], std=[0.225, 0.225, 0.225])
    ])

    fast_pathway = []
    slow_pathway = []
    frame_count = 0

    while cap.isOpened():
        ret, frame = cap.read()
        if not ret:
            break
        # BGR to RGB and preprocess
        processed_frame = preprocess(cv2.cvtColor(frame, cv2.COLOR_BGR2RGB))

        # Fast 경로: 짧은 간격으로 프레임 샘플링
        if frame_count % fast_interval == 0:
            fast_pathway.append(processed_frame)

        # Slow 경로: 긴 간격으로 프레임 샘플링
        if frame_count % slow_interval == 0:
            slow_pathway.append(processed_frame)

        frame_count += 1

    cap.release()

    # 텐서로 변환하고 (C, T, H, W) 형태로 변환
    fast_tensor = torch.stack(fast_pathway,
                              dim=1) if fast_pathway else torch.empty(0)
    slow_tensor = torch.stack(slow_pathway,
                              dim=1) if slow_pathway else torch.empty(0)

    return [slow_tensor, fast_tensor]


# 행동 인식 예측 함수
def analyze_actions(model, frames):
    actions = []
    with torch.no_grad():
        for clip in frames:
            output = model(clip)  # slowfast 모델에 slow, fast 경로로 전달
            _, predicted = torch.max(output, 1)
            actions.append(predicted.item())
    return actions



# 조리 과정 비교 및 타임라인 생성
def processingVideo(video, cookStep):
    model = load_tbn_model()  # TBN 모델 로드
    frames = processing_frame(video)  # 프레임 추출 및 전처리
    actions = analyze_actions(model, frames)  # 행동 예측

    # 행동 예측 결과와 cookStep 비교
    timeline = []
    for action in actions:
        # 예: 예측된 action ID를 cookStep 정보와 비교하여 타임라인 생성
        if action in cookStep:
            timeline.append((action, cookStep[action]))
        else:
            timeline.append((action, '알 수 없는 단계'))

    return timeline
