package com.example.tastyhub.common.domain.post.repository;

import com.example.tastyhub.common.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Long>,PostRepositoryQuery {

}
