# 베이스 이미지로 Miniconda를 사용
FROM continuumio/miniconda3

# 작업 디렉토리 설정
WORKDIR /app

# 필요한 파일 복사
COPY . /app

# 환경 설정 및 패키지 설치
RUN conda init bash && \
    conda create -n myenv python=3.9 -y && \
    echo "source activate myenv" > ~/.bashrc

# 쉘을 바꿔서 conda 환경을 활성화하고 requirements.txt 설치
SHELL ["conda", "run", "-n", "myenv", "/bin/bash", "-c"]

# requirements.txt 파일에서 패키지 설치
RUN pip install -r requirements.txt

# FastAPI 실행에 필요한 Uvicorn 설치
RUN pip install fastapi uvicorn

# FastAPI 애플리케이션 실행을 위한 포트 설정
EXPOSE 8000

# FastAPI 애플리케이션 실행
CMD ["conda", "run", "-n", "myenv", "uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
