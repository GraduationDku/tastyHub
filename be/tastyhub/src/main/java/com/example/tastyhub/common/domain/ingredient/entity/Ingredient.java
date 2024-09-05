package com.example.tastyhub.common.domain.ingredient.entity;

import com.example.tastyhub.common.domain.ingredient.dtos.IngredientCreateDto;
import com.example.tastyhub.common.domain.ingredient.dtos.IngredientDto;
import com.example.tastyhub.common.utils.TimeStamped;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
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
@Table(name = "ingredient")
public class Ingredient extends TimeStamped {


  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasty_hub_sequence")
  @SequenceGenerator(name = "tasty_hub_sequence", sequenceName = "thesq", allocationSize = 10)
  @Column(name = "ingredient_id")
  private Long id;
  private String ingredientName;
  private String amount;

//    @Enumerated(EnumType.STRING)
//    private IngredientType ingredientType;
//    public enum IngredientType{
//        seasoning, Source, Vegetable, Meat, Fish, Etc
//    }

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, targetEntity = Recipe.class)
  @JoinColumn(name = "recipe_id", nullable = false)
  private Recipe recipe;


  public static Ingredient makeIngredient(IngredientCreateDto ingredientCreateDto) {
    return Ingredient.builder()
        .ingredientName(ingredientCreateDto.getIngredientName())
        .amount(ingredientCreateDto.getAmount())
        .build();
  }

  public void updateByUpdateDto(IngredientDto ingredientDto) {
    this.ingredientName = ingredientDto.getIngredientName();
    this.amount = ingredientDto.getAmount();

  }

  public void setRecipe(Recipe recipe) {
    this.recipe = recipe;
  }

  public void updateRelation(Recipe recipe) {

    this.recipe = recipe;
  }

}
