package com.example.tastyhub.fixture.foodInformation;

import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationCreateDto;
import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationDto;
import com.example.tastyhub.common.domain.foodInformation.entity.FoodInformation;

public class FoodInformationFixture {

    public static final FoodInformation FOOD_INFORMATION =
        FoodInformation.builder()
            .id(1L)
            .content("text")
            .cookingTime(50L)
            .serving("1")
            .build();

    public static final FoodInformationDto FOOD_INFORMATION_DTO = FoodInformationDto.builder()
        .foodInformationId(1L)
        .content("text")
        .cookingTime(50L)
        .serving("1")
        .build();
    public static final FoodInformationCreateDto FOOD_INFORMATION_CREATE_DTO = FoodInformationCreateDto.builder()
        .content("text")
        .cookingTime(50L)
        .serving("1")
        .build();


}
