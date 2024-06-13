package com.example.tastyhub.common.domain.userChat.repository;

import com.example.tastyhub.common.domain.chat.entity.ChatRoom;
import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.userChat.entity.UserChatRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {

    List<UserChatRoom> findByUser(User user);
    Optional<UserChatRoom> findByUserAndChatRoom(User user, ChatRoom chatRoom);
    boolean existsByChatRoomAndUser(ChatRoom chatRoom, User user);


}
