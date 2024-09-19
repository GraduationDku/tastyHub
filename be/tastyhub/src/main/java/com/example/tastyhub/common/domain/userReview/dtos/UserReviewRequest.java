package com.example.tastyhub.common.domain.userReview.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserReviewRequest {
  private Long grade;
  private String content;
}
