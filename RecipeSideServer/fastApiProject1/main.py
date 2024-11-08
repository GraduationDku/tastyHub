import uvicorn
from fastapi import FastAPI, File, UploadFile
import boto3
from botocore.exceptions import NoCredentialsError
from intoGPT import create_prediction
from video.useVideoServiceAction import processingVideo,temp


app = FastAPI()

# AWS S3 설정 (Bucket 이름, AWS 인증 정보)
S3_BUCKET = "your-s3-bucket-name"
S3_ACCESS_KEY = "your-access-key"
S3_SECRET_KEY = "your-secret-key"
S3_REGION = "your-region"


# S3에 파일 업로드 함수
def upload_to_s3(file, bucket, filename):
    s3 = boto3.client(
        "s3",
        aws_access_key_id=S3_ACCESS_KEY,
        aws_secret_access_key=S3_SECRET_KEY,
        region_name=S3_REGION
    )
    try:
        s3.upload_fileobj(file, bucket, filename)
        return f"https://{bucket}.s3.{S3_REGION}.amazonaws.com/{filename}"
    except NoCredentialsError:
        return {"error": "AWS credentials not available"}


@app.get("/")
async def root():
    return {"message": "Hello World"}


@app.get("/hello/{foodName}/{step}")
async def say_hello(foodName: str, step: str):
    temp_cookStep = create_prediction(foodName,step)

    return {"message": f"Hello {temp_cookStep}"}


# 영상만 타임라인 받는 엔드포인트
@app.post("/recipe/video/timeline")
async def upload_video(steps: str, foodName: str, video: UploadFile = File(...)):
    # gpt 프로프트
    temp_cookStep = create_prediction(foodName,steps)

    # 영상 처리 로직 진행
    videoTimeLine = processingVideo(video, temp_cookStep)
    print(videoTimeLine)

    # 영상 처리 후 저장
    filename = video.filename
    file_location = f"videos/{filename}"  # S3에 저장될 경로

    # S3에 파일 업로드
    s3_url = upload_to_s3(video.file, S3_BUCKET, file_location)

    if "error" in s3_url:
        return {"error": "Failed to upload to S3"}

    # 추가적으로 타임라인 처리 로직이 여기에 들어갈 수 있음


    return {"message": "Video uploaded successfully", "s3_url": s3_url}

@app.get("/video")
async def read_video():
    l = temp()
    return l


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8084)
