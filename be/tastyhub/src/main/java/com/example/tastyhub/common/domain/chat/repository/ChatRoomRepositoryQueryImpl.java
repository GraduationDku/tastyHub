package com.example.tastyhub.common.domain.chat.repository;

import static com.example.tastyhub.common.domain.chat.entity.QChatRoom.chatRoom;

import com.example.tastyhub.common.domain.chat.dtos.ChatRoomDto;
import com.example.tastyhub.common.domain.user.entity.User;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatRoomRepositoryQueryImpl implements ChatRoomRepositoryQuery{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ChatRoomDto> findAllByUserQuery(User user) {
        List<ChatRoomDto> chatRoomDtoList = jpaQueryFactory.select(
                Projections.constructor(ChatRoomDto.class,
                    chatRoom.id,
                    chatRoom.chatRoomTitle
                ))
            .from(chatRoom)
            .leftJoin(chatRoom.users).fetchJoin()
            .where(chatRoom.users.contains(user))
            .fetch();

        return chatRoomDtoList;
    }

}
