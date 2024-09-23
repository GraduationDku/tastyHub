package com.example.tastyhub.common.domain.chat.repository;

import com.example.tastyhub.common.domain.chat.dtos.ChatRoomDto;
import com.example.tastyhub.common.domain.user.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomRepositoryQuery {

    List<ChatRoomDto> findAllByUserQuery(User user);
}
