package com.example.tastyhub.common.domain.cookstep.dtos;

import com.example.tastyhub.common.domain.cookstep.entity.CookStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CookStepDto {

    private Long cookStepId;
    private Long stepNumber;
    private String stepImgUrl;
    private String content;

    public CookStepDto(CookStep cookStep) {
        this.cookStepId = cookStep.getId();
        this.stepNumber = cookStep.getStepNumber();
//        this.stepImgUrl = cookStep.get;
        this.content = cookStep.getContent();
    }
}
