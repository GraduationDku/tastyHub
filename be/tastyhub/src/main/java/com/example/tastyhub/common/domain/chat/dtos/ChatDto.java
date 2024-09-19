package com.example.tastyhub.common.domain.chat.dtos;

import com.example.tastyhub.common.domain.chat.entity.Chat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {

    private String sender;

    private String content;

    private LocalDateTime time;


    public ChatDto(Chat chat) {
        this.sender = chat.getSender();
        this.content = chat.getContent();
        this.time = chat.getLocalDateTime();
    }
}
