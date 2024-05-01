package com.example.tastyhub.common.domain.recipe.repository;

import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(domainClass = Recipe.class, idClass = Long.class)
public interface RecipeRepository extends JpaRepository<Recipe,Long>, RecipeRepositoryQuery {

}
