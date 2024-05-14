package com.example.tastyhub.common.domain.chat.service;

import com.example.tastyhub.common.domain.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private ChatRepository chatRepository;

}
