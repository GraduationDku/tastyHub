package com.example.tastyhub.common.domain.chat.service;

import com.example.tastyhub.common.domain.chat.dtos.ChatDto;

public interface ChatService {

    ChatDto sendMessage(Long roomId, ChatDto chatMessage);
}
