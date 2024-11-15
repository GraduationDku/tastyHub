package com.example.tastyhub.common.domain.cookstep.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CookStepCreateRequest {

    private Long stepNumber;
    private String timeLine;
    private String stepImg;
    private String content;
}
