package com.example.tastyhub.common.domain.foodInformation.dtos;

import com.example.tastyhub.common.domain.foodInformation.entity.FoodInformation;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FoodInformationUpdateDto {

    private Long foodInformationId;

    private String text;

    private Long cookingTime;

    private String serving;

}
