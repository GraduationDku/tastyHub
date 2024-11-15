import os
from typing import List

import uvicorn
from fastapi import FastAPI, File, UploadFile
import boto3
from botocore.exceptions import NoCredentialsError
from pydantic import BaseModel

from intoGPT import create_prediction, RecipeRequest
from video.useVideoServiceAction import processingVideo,temp
from video.useYoutubeServiceAction import youtubeAnalysis



app = FastAPI()

# AWS S3 설정 (Bucket 이름, AWS 인증 정보)
S3_BUCKET = os.getenv("S3_BUCKET")
S3_ACCESS_KEY = os.getenv("S3_ACCESS_KEY")
S3_SECRET_KEY = os.getenv("S3_SECRET_KEY")
S3_REGION = os.getenv("S3_REGION")
# Define the input data structure for cookSteps
class CookStep(BaseModel):
    stepNumber: int
    timeLine: str
    content: str

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

# 유튜브 레시피 분석 엔드포인트
@app.post("/video/youtube/link")
async def upload_video(youtubeUrl: str):

    # 영상 처리 로직 진행
    response_body = youtubeAnalysis(youtubeUrl)
    return response_body


@app.post("/gpt")
async def testGpt(request: RecipeRequest):
    # create_prediction 함수 호출
    result = create_prediction(request.foodName, request.cookSteps)
    return {"predictions": result}


@app.post("/youtube/test")
async def upload_video():
    link = "https://www.youtube.com/watch?v=pLiO21Hhb-U"
    # 영상 처리 로직 진행
    response_body = youtubeAnalysis(link)
    return response_body

# 영상만 타임라인 받는 엔드포인트
@app.post("/recipe/video/timeline")
async def upload_video(steps: List[CookStep], foodName: str, video: UploadFile = File(...)):
    # Convert steps to a list of content strings for processing
    temp_cookStep = create_prediction(foodName,[step.content for step in steps])


    # 영상 처리 로직 진행
    cookSteps = processingVideo(video, temp_cookStep,steps )
    print(cookSteps)

    # 영상 처리 후 저장
    filename = video.filename
    file_location = f"videos/{filename}"  # S3에 저장될 경로

    # S3에 파일 업로드
    s3_url = upload_to_s3(video.file, S3_BUCKET, file_location)

    if "error" in s3_url:
        return {"error": "Failed to upload to S3"}

    # Return the video URL and the processed timeline
    return {"message": "Video uploaded successfully", "s3_url": s3_url, "cookSteps": cookSteps}
@app.get("/video")
async def read_video():
    l = temp()
    return {"message": "Video uploaded successfully", "s3_url": "s3_url", "cookSteps": l}


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)


#
# import subprocess
#
# def check_package_versions(requirements_file):
#     with open(requirements_file) as file:
#         for line in file:
#             if line.strip() and not line.startswith('#'):
#                 package = line.strip()
#                 try:
#                     subprocess.run(
#                         ["pip", "install", package],
#                         check=True,
#                         stdout=subprocess.PIPE,
#                         stderr=subprocess.PIPE
#                     )
#                     print(f"{package} 설치 가능")
#                 except subprocess.CalledProcessError as e:
#                     print(f"{package}는 현재 Python 버전과 호환되지 않음")
#
# check_package_versions("requirements.txt")
