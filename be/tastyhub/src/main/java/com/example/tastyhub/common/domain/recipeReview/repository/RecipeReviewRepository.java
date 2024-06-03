package com.example.tastyhub.common.domain.recipeReview.repository;

import com.example.tastyhub.common.domain.recipeReview.entity.RecipeReview;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeReviewRepository extends JpaRepository<RecipeReview,Long>, RecipeReviewRepositoryQuery {


}
