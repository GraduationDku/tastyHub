package com.example.tastyhub.common.domain.chat.dtos;

import com.example.tastyhub.common.domain.chat.entity.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {

    private String from;

    private String text;

    private String time;


    public ChatDto(Chat chat) {
        this.from = chat.getSender();
        this.text = chat.getText();
        this.time = String.valueOf(chat.getLocalDateTime());
    }
}
