package com.example.tastyhub.common.domain.cookstep.repository;

import com.example.tastyhub.common.domain.cookstep.entity.CookStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CookStepRepository extends JpaRepository<CookStep, Long> {


}
