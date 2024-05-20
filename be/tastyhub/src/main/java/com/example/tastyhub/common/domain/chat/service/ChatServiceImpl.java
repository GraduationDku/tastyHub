package com.example.tastyhub.common.domain.chat.service;

import com.example.tastyhub.common.domain.chat.dtos.ChatDto;
import com.example.tastyhub.common.domain.chat.entity.Chat;
import com.example.tastyhub.common.domain.chat.entity.ChatRoom;
import com.example.tastyhub.common.domain.chat.repository.ChatRepository;
import com.example.tastyhub.common.domain.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private ChatRepository chatRepository;
    private ChatRoomRepository chatRoomRepository;


    @Override
    public ChatDto createChat(Long roomId, ChatDto chatMessage) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new IllegalArgumentException("해당 채팅방은 존재하지않습니다"));

        Chat chat = Chat.builder().chatRoom(chatRoom).sender(chatMessage.getFrom())
            .text(chatMessage.getText())
            .build();
        chatRepository.save(chat);
        return ChatDto.builder().from(chat.getSender()).text(chat.getText()).time(
            String.valueOf(chat.getLocalDateTime())).build();
    }
}
