# 베이스 이미지 선택 (Python 3.9 사용)
FROM python:3.9

# 작업 디렉토리 설정
WORKDIR /app

# 종속성 설치를 위한 requirements.txt 파일 복사
COPY requirements.txt .

# 종속성 설치
RUN pip install --no-cache-dir -r requirements.txt

# FastAPI 애플리케이션 코드 복사
COPY . .

# uvicorn을 통해 FastAPI 서버 실행 (포트 8084)
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8084"]
