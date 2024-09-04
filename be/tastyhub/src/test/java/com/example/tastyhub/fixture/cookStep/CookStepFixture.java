package com.example.tastyhub.fixture.cookStep;

import com.example.tastyhub.common.domain.cookstep.dtos.CookStepCreateRequest;
import com.example.tastyhub.common.domain.cookstep.dtos.CookStepDto;
import com.example.tastyhub.common.domain.cookstep.entity.CookStep;
import java.util.Collections;
import java.util.List;

public class CookStepFixture {

    public static final CookStep COOK_STEP = CookStep.builder()
        .id(1L)
        .stepNumber(1L)
        .content("text")
        .stepImgUrl("refactor")
        .build();

    public static final CookStepCreateRequest COOK_STEP_CREATE_DTO = CookStepCreateRequest.builder()
        .stepNumber(1L)
        .content("text")
        .stepImg("ImgUrl")
        .build();


    public static final CookStepDto COOK_STEP_DTO = CookStepDto.builder()
        .cookStepId(1L)
        .stepNumber(1L)
        .content("text")
        .stepImgUrl("ImgUrl")
        .build();

    public static final List<CookStepDto> COOK_STEP_UPDATE_DTO_LIST = Collections.singletonList(
        COOK_STEP_DTO);

}
