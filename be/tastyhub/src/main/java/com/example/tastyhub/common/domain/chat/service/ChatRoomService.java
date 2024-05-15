package com.example.tastyhub.common.domain.chat.service;

import com.example.tastyhub.common.domain.chat.dtos.ChatDto;
import com.example.tastyhub.common.domain.chat.dtos.ChatRoomDto;
import com.example.tastyhub.common.domain.user.entity.User;
import java.util.List;

public interface ChatRoomService {

    void createChatRoom(Long postId, User user);

    List<ChatRoomDto> getChatRoomList(User user);

    List<ChatDto> getChatRoom(Long roomId, User user);

    void enterNewChatRoom(Long roomId, User user);

    void deleteChatRoom(Long roomId, User user);
}
