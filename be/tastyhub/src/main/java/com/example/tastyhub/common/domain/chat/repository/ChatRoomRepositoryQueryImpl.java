package com.example.tastyhub.common.domain.chat.repository;

import static com.example.tastyhub.common.domain.chat.entity.QChatRoom.chatRoom;
import static com.example.tastyhub.common.domain.userChat.entity.QUserChatRoom.userChatRoom;

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
        return jpaQueryFactory.select(
                Projections.constructor(ChatRoomDto.class,
                    chatRoom.id,
                    chatRoom.chatRoomTitle
                ))
            .from(userChatRoom)
            .join(userChatRoom.chatRoom, chatRoom)
            .where(userChatRoom.user.eq(user))
            .fetch();
//        return null;
    }

}
