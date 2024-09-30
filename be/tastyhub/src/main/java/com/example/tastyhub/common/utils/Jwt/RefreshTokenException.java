package com.example.tastyhub.common.utils.Jwt;

public class RefreshTokenException extends RuntimeException {

  public enum ErrorType {
    CREATION_FAILED,
    VALIDATION_FAILED,
    UPDATE_FAILED,
    DELETION_FAILED
  }

  private final ErrorType errorType;

  public RefreshTokenException(String message, ErrorType errorType) {
    super(message);
    this.errorType = errorType;
  }

  public RefreshTokenException(String message, Throwable cause, ErrorType errorType) {
    super(message, cause);
    this.errorType = errorType;
  }

  public ErrorType getErrorType() {
    return errorType;
  }
}