# Use the official Python base image
FROM python:3.11-slim

# Install build dependencies and additional tools
RUN apt-get update && apt-get install -y \
    gcc \
    g++ \
    make \
    libarchive-dev \
    && rm -rf /var/lib/apt/lists/*

# Set the working directory
WORKDIR /app

# Copy the requirements files into the container
COPY requirements-docker.txt /app/requirements.txt

# Install the dependencies
RUN pip install --no-cache-dir -r requirements.txt

# Install uvicorn explicitly
RUN pip install uvicorn[standard]

# Copy the rest of the application code
COPY . /app

# Set the environment variable for FastAPI
ENV PYTHONUNBUFFERED 1

# Expose port 8084 for the FastAPI server
EXPOSE 8084

# Command to run the FastAPI server
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8084"]
