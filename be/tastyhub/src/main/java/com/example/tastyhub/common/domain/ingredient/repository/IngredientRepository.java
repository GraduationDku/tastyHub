package com.example.tastyhub.common.domain.ingredient.repository;

import com.example.tastyhub.common.domain.ingredient.entity.Ingredient;
import java.nio.file.LinkOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}
