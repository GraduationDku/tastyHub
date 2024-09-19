package com.example.tastyhub.common.domain.recipeReview.entity;

import com.example.tastyhub.common.domain.recipeReview.dtos.RecipeReviewRequest;
import com.example.tastyhub.common.utils.TimeStamped;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import com.example.tastyhub.common.domain.user.entity.User;
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
@Table(name = "recipe_reviews")
public class RecipeReview extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasty_hub_sequence")
    @SequenceGenerator(name = "tasty_hub_sequence", sequenceName = "thesq", allocationSize = 10)
    @Column(name = "recipe_review_id")
    private long id;

    private long grade;

    private String content;

    //연관 관계
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, targetEntity = Recipe.class)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public void update(RecipeReviewRequest recipeReviewUpdateRequest) {
        this.grade = recipeReviewUpdateRequest.getGrade();
        this.content = recipeReviewUpdateRequest.getContent();
    }


    public static RecipeReview createRecipeReview(RecipeReviewRequest recipeReviewCreateRequest,
        User user, Recipe recipe) {
        return RecipeReview.builder()
            .user(user)
            .recipe(recipe)
            .grade(recipeReviewCreateRequest.getGrade())
            .content(recipeReviewCreateRequest.getContent())
            .build();
    }
}
