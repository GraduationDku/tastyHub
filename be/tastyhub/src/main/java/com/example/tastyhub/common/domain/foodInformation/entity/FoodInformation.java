package com.example.tastyhub.common.domain.foodInformation.entity;

import com.example.tastyhub.common.domain.foodInformation.dtos.FoodInformationDto;
import com.example.tastyhub.common.domain.recipe.dtos.RecipeCreateDto;
import com.example.tastyhub.common.utils.TimeStamped;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasty_hub_sequence")
  @SequenceGenerator(name = "tasty_hub_sequence", sequenceName = "thesq", allocationSize = 10)
  @Column(name = "food_information_id")
  private Long id;

  @Size(max = 1024)
  private String content;

  private Long cookingTime;

  // 몇 인분
  private String serving;

  //연관 관계
  @OneToOne
  @JoinColumn(name = "recipe_id", nullable = false)
  private Recipe recipe;


  public void updateByFoodInformationDto(FoodInformationDto foodInformationDto) {
    this.content = foodInformationDto.getContent();
    this.cookingTime = foodInformationDto.getCookingTime();
    this.serving = foodInformationDto.getServing();
  }

  public void setRecipe(Recipe recipe) {
    this.recipe = recipe;
  }

  public void updateRelation(Recipe recipe) {
    this.recipe = recipe;
  }

  public static FoodInformation createFoodInformation(RecipeCreateDto recipeCreateDto) {
    return FoodInformation.builder()
        .content(recipeCreateDto.getFoodInformation().getContent())
        .serving(recipeCreateDto.getFoodInformation().getServing())
        .cookingTime(recipeCreateDto.getFoodInformation().getCookingTime())
        .build();
  }
}
