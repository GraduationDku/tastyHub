package com.example.tastyhub.common.domain.foodInformation.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FoodInformationDto {

    private Long foodInformationId;

    private String text;

    private Long cookingTime;

    private String serving;

}
