package com.example.tastyhub.common.domain.cookstep.dtos;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class CookStepUpdateDto {

    private Long cookStepId;
    private Long stepNumber;
    private MultipartFile stepImg;
    private String text;

}
