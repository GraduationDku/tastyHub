package com.example.tastyhub.common.domain.userReview.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagingUserReviewResponse {
    private String nickname;
    private Long grade;
    private String content;
}
