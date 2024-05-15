package com.example.tastyhub.common.domain.chat.service;

import com.example.tastyhub.common.domain.chat.dtos.ChatDto;

public interface ChatService {

    ChatDto createChat(Long roomId, ChatDto chatMessage);
}
