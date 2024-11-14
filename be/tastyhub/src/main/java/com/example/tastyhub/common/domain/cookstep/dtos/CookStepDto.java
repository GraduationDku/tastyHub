package com.example.tastyhub.common.domain.cookstep.dtos;

import com.example.tastyhub.common.domain.cookstep.entity.CookStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CookStepDto {

    private Long stepNumber;
    private String timeLine;
    private String stepImgUrl;
    private String content;

    public CookStepDto(CookStep cookStep) {
        this.stepNumber = cookStep.getStepNumber();
        this.timeLine = cookStep.getTimeline();
//        this.stepImgUrl = cookStep.get;
        this.content = cookStep.getContent();
    }
}
