package com.example.tastyhub.common.domain.chat.entity;

import com.example.tastyhub.common.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "chat_room_id")
    private Long id;

    private Long postId;

    private String chatRoomTitle;


    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<Chat> chats = new ArrayList<>();

    @ManyToMany(mappedBy = "chatRooms")
    @Builder.Default
    private List<User> users = new ArrayList<>();

    public void updateUser(List<User> users) {
        this.users = users;
    }


}
