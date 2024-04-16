package com.example.tastyhub.common.domain.foodInformation.entity;

import com.example.tastyhub.common.utils.TimeStamped;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "food_information")
public class FoodInformation extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_information_id")
    private Long id;


    private String text;

    private Long cookingTime;

    // 몇 인분
    private String serving;

    //연관 관계
    @OneToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;


    public void update(FoodInformationUpdateDto foodInformationUpdateDto) {
        this.text = foodInformationUpdateDto.getText();
        this.cookingTime = foodInformationUpdateDto.getCookingTime();
        this.serving = foodInformationUpdateDto.getServing();
    }
}
