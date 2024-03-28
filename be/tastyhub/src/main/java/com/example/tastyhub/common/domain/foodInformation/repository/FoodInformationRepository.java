package com.example.tastyhub.common.domain.foodInformation.repository;

import com.example.tastyhub.common.domain.foodInformation.entity.FoodInformation;
import java.nio.file.LinkOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodInformationRepository extends JpaRepository<FoodInformation, Long> {

}
