package com.example.tastyhub.common.domain.chat.controller;

import com.example.tastyhub.common.domain.chat.dtos.ChatDto;
import com.example.tastyhub.common.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;


    @MessageMapping("/chat/rooms/{roodId}/send")
    @SendTo("/topic/public/rooms/{roomId}")
    public ChatDto sendMessage(@PathVariable Long roomId, @Payload ChatDto chatMessage) {

        return chatService.createChat(roomId, chatMessage);
    }
}
