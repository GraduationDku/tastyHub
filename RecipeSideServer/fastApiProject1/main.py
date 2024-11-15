import os
import json
from typing import List
from fastapi import FastAPI, File, UploadFile, Form, Body
from pydantic import BaseModel
from starlette.middleware.cors import CORSMiddleware
import boto3
from botocore.exceptions import NoCredentialsError
import uvicorn

# Local imports
from intoGPT import create_prediction, RecipeRequest
from video.useVideoServiceAction import processingVideo, temp
from video.useYoutubeServiceAction import youtubeAnalysis

app = FastAPI()

# CORS 설정
app.add_middleware(
    CORSMiddleware,
    allow_origins=["https://localhost:3000", "https://example.com"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# AWS S3 설정
S3_BUCKET = os.getenv("S3_BUCKET")
S3_ACCESS_KEY = os.getenv("S3_ACCESS_KEY")
S3_SECRET_KEY = os.getenv("S3_SECRET_KEY")
S3_REGION = os.getenv("S3_REGION")


# 데이터 모델 정의
class CookStep(BaseModel):
    stepNumber: int
    timeLine: str
    content: str

class BeforeCookStep(BaseModel):
    stepNumber: int
    content: str


class YouTubeLinkRequest(BaseModel):
    youtubeUrl: str


# S3 파일 업로드 함수
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
    temp_cookStep = create_prediction(foodName, step)
    return {"message": f"Hello {temp_cookStep}"}


@app.post("/video/youtube/link")
async def upload_video(request: YouTubeLinkRequest):
    response_body = youtubeAnalysis(request.youtubeUrl)
    return response_body

@app.post("/gpt")
async def test_gpt(request: RecipeRequest):
    result = create_prediction(request.foodName, request.cookSteps)
    return {"predictions": result}


@app.post("/youtube/test")
async def youtube_test():
    link = "https://www.youtube.com/watch?v=pLiO21Hhb-U"
    response_body = youtubeAnalysis(link)
    return response_body


@app.post("/video/media/action")
async def upload_video(
    foodName: str = Form(...),
    cookSteps: str = Form(...),
    foodVideo: UploadFile = File(...)
):
    # JSON 형식의 cookSteps 파싱
    cookSteps = json.loads(cookSteps)
    cookSteps = [BeforeCookStep(**step) for step in cookSteps]

    # 조리 단계 예측 생성
    temp_cookStep = create_prediction(foodName, [step.content for step in cookSteps])

    # 영상 처리 로직
    processedCookSteps = processingVideo(foodVideo, temp_cookStep, cookSteps)
    print(processedCookSteps)

    # S3에 파일 저장
    filename = foodVideo.filename
    file_location = f"videos/{filename}"
    s3_url = upload_to_s3(foodVideo.file, S3_BUCKET, file_location)

    if "error" in s3_url:
        return {"error": "Failed to upload to S3"}

    return {"message": "Video uploaded successfully", "s3_url": s3_url, "cookSteps": processedCookSteps}


@app.get("/video")
async def read_video():
    l = temp()
    return {"message": "Video uploaded successfully", "s3_url": "s3_url", "cookSteps": l}


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)
