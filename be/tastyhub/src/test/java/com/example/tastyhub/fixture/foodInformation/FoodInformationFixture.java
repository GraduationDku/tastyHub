package com.example.tastyhub.fixture.foodInformation;

import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationDto;
import com.example.tastyhub.common.domain.foodInformation.entity.FoodInformation;

public class FoodInformationFixture {

    public static final FoodInformation FOOD_INFORMATION =
        FoodInformation.builder()
            .id(1L)
            .text("text")
            .cookingTime(50L)
            .serving("1")
            .build();

    public static final FoodInformationDto FOOD_INFORMATION_DTO = FoodInformationDto.builder()
        .foodInformationId(1L)
        .text("text")
        .cookingTime(50L)
        .serving("1")
        .build();



}
