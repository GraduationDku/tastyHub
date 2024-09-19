package com.example.tastyhub.common.domain.chat.entity;

import com.example.tastyhub.common.domain.userChat.entity.UserChatRoom;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "chat_rooms")
public class ChatRoom {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasty_hub_sequence")
    @SequenceGenerator(name = "tasty_hub_sequence", sequenceName = "thesq", allocationSize = 10)
    @Column(name = "chat_room_id")
    private Long id;

    private Long postId;

    private String chatRoomTitle;


    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<Chat> chats = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserChatRoom> userChatRooms = new ArrayList<>();
//    public void updateUser(List<User> users) {
//        this.users = users;
//
//    }


}
