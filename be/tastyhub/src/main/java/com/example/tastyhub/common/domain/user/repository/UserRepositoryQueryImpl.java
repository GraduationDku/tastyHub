package com.example.tastyhub.common.domain.user.repository;

import static com.example.tastyhub.common.domain.user.entity.QUser.user;

import com.example.tastyhub.common.domain.user.dtos.UserDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryQueryImpl implements UserRepositoryQuery{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<UserDto> findAllByNickname(String nickname) {
        List<UserDto> userDtoList = jpaQueryFactory.select(Projections.constructor(UserDto.class,
                user.id,
                user.nickname,
                user.userImg)).from(user)
            .where(user.nickname.contains(nickname))
            .fetch();
        return userDtoList;
    }
}
