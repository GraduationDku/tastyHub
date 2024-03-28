package com.example.tastyhub.common.domain.village.repository;

import com.example.tastyhub.common.domain.village.entity.Village;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VillageRepository extends JpaRepository<Village, Long> {

}
