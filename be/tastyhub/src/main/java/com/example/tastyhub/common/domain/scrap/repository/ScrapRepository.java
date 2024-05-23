package com.example.tastyhub.common.domain.scrap.repository;

import com.example.tastyhub.common.domain.scrap.entity.Scrap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap,Long>, ScrapRepositoryQuery {


    void deleteByRecipeIdAndUserId(Long recipeId, Long userId);


    boolean existsByRecipeIdAndUserId(Long recipeId, Long userId);

}
