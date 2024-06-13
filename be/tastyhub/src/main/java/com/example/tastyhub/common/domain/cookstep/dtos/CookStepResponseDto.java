package com.example.tastyhub.common.domain.cookstep.dtos;

import com.example.tastyhub.common.domain.cookstep.entity.CookStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@AllArgsConstructor
public class CookStepResponseDto {

    private Long cookStepId;
    private Long stepNumber;
    private String stepImgUrl;
    private String text;

    public CookStepResponseDto(CookStep cookStep) {
        this.cookStepId = cookStep.getId();
        this.stepNumber = cookStep.getStepNumber();
//        this.stepImgUrl = cookStep.get;
        this.text = cookStep.getText();
    }
}
