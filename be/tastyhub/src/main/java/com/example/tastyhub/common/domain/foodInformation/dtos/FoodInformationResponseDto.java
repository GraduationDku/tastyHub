package com.example.tastyhub.common.domain.foodInformation.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FoodInformationResponseDto {

    private Long foodInformationId;

    private String text;

    private Long cookingTime;

    private String serving;

}
