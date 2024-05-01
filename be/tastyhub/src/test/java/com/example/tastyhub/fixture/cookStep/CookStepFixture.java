package com.example.tastyhub.fixture.cookStep;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepCreateDto;
import com.example.tastyhub.common.domain.cookstep.dtos.CookStepResponseDto;
import com.example.tastyhub.common.domain.cookstep.dtos.CookStepUpdateDto;
import com.example.tastyhub.common.domain.cookstep.entity.CookStep;
import org.mockito.internal.matchers.Null;

public class CookStepFixture {

    public static final CookStep COOK_STEP = CookStep.builder()
        .id(1L)
        .stepNumber(1L)
        .text("text")
        .stepImgUrl("refactor")
        .build();

    public static final CookStepCreateDto COOK_STEP_CREATE_DTO = CookStepCreateDto.builder()
        .stepNumber(1L)
        .text("text")
        .stepImg(null)
        .build();

    public static final CookStepResponseDto COOK_STEP_RESPONSE_DTO = CookStepResponseDto.builder()
        .cookStepId(1L)
        .stepNumber(1L)
        .text("text")
        .stepImgUrl("refactor")
        .build();

    public static final CookStepUpdateDto COOK_STEP_UPDATE_DTO = CookStepUpdateDto.builder()
        .cookStepId(1L)
        .stepNumber(1L)
        .text("text")
        .stepImg(null)
        .build();

}
