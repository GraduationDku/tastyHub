package com.example.tastyhub.common.domain.userChat.entity;

import com.example.tastyhub.common.domain.chat.entity.ChatRoom;
import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.utils.TimeStamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Time;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_chat_rooms")
@Entity
public class UserChatRoom extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    public static UserChatRoom createUserChatRoom(User user, ChatRoom chatRoom){
        return UserChatRoom.builder()
                .user(user)
                .chatRoom(chatRoom)
                .build();
    }
}