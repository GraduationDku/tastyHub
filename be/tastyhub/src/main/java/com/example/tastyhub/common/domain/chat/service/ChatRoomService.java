package com.example.tastyhub.common.domain.chat.service;

import com.example.tastyhub.common.domain.chat.dtos.ChatDto;
import com.example.tastyhub.common.domain.chat.dtos.ChatRoomDto;
import com.example.tastyhub.common.domain.chat.dtos.CheckDto;
import com.example.tastyhub.common.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomService {

    void createChatRoom(Long postId, User user);

    Page<ChatRoomDto> getChatRoomList(User user, Pageable pageable);

    List<ChatDto> getChatRoom(Long roomId, User user);

    void enterNewChatRoom(Long roomId, User user);

    void outChatRoom(Long roomId, User user);

    void deleteChatRoom(Long roomId, Long postId, User user);

    CheckDto checkRoomCondition(Long postId);
}
