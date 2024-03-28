package com.example.tastyhub.common.domain.userReview.repository;

import com.example.tastyhub.common.domain.userReview.entity.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReviewRepository extends JpaRepository<UserReview,Long> {

}
