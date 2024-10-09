import uvicorn
from fastapi import FastAPI, File, UploadFile

app = FastAPI()


@app.get("/")
async def root():
    return {"message": "Hello World"}


@app.get("/hello/{name}")
async def say_hello(name: str):
    return {"message": f"Hello {name}"}

# 영상만 타임라인 받는 엔드포인트
@app.post("/recipe/video/timeline")
async def upload_video(video: UploadFile = File(...)):
    #영상 타임라인 처리 로직
    return #영상 타임라인 반환


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8084)
