package com.example.tastyhub.common.domain.user.repository;

import com.example.tastyhub.common.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByNickname(String nickname);

    boolean existsByUsername(String username);

    User findByUsername(String username);
}
