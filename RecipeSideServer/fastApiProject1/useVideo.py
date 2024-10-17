import cv2
import torch
import torchvision.transforms as transforms




def processing_frame(video_path, frame_interval=5):
    """
    주어진 비디오에서 일정 간격으로 프레임을 추출하고 전처리한 결과를 반환합니다.

    Args:
        video_path (str): 비디오 파일 경로
        frame_interval (int): 프레임 간격 (기본값: 5)

    Returns:
        List[torch.Tensor]: 전처리된 프레임 리스트
    """
    # 비디오 파일 로드
    cap = cv2.VideoCapture(video_path)
    frame_rate = int(cap.get(cv2.CAP_PROP_FPS))
    total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))

    # 전처리 파이프라인 정의
    preprocess = transforms.Compose([
        transforms.ToPILImage(),
        transforms.Resize((256, 256)),
        transforms.CenterCrop((224, 224)),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.45, 0.45, 0.45], std=[0.225, 0.225, 0.225])
    ])

    processed_frames = []  # 전처리된 프레임 저장 리스트

    while cap.isOpened():
        ret, frame = cap.read()
        if not ret:
            break

        current_frame = int(cap.get(cv2.CAP_PROP_POS_FRAMES))

        # 일정 간격에 맞는 프레임만 처리
        if current_frame % frame_interval == 0:
            # BGR -> RGB 변환 및 전처리 적용
            processed_frame = preprocess(cv2.cvtColor(frame, cv2.COLOR_BGR2RGB))
            processed_frames.append(processed_frame)

    cap.release()
    return processed_frames

def processingVideo(video, cookStep):
    # 비디오 행동 분석
    # https://epic-kitchens.github.io/2024
    # 1. 영상 프레임 쪼개기 - 기존 코드 사용, 전처리 프레임 추출 시간 감소
    frame = processing_frame(video)

    # 행동 분석 진행

    # 행동 분석결과와 cookStep - 조리과정.txt 비교

    # 비교 결과에 따른 타임라인 정리

    timeline = []

    # 타임라인 정리 후 반환
    return timeline


