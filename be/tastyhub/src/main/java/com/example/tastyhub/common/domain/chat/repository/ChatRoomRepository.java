package com.example.tastyhub.common.domain.chat.repository;

import com.example.tastyhub.common.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryQuery {

  boolean existsByPostId(Long postId);
  ChatRoom findByPostId(Long postId);
}
