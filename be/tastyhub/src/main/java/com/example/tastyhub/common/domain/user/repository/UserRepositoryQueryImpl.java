package com.example.tastyhub.common.domain.user.repository;

import static com.example.tastyhub.common.domain.user.entity.QUser.user;

import com.example.tastyhub.common.domain.user.dtos.UserDto;
import com.example.tastyhub.common.domain.user.entity.User;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;


@RequiredArgsConstructor
public class UserRepositoryQueryImpl implements UserRepositoryQuery{

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<UserDto> findAllByNickname(String nickname, Pageable pageable) {
        List<UserDto> userDtoList = jpaQueryFactory.select(Projections.constructor(UserDto.class,
                user.id,
                user.nickname,
                user.userImg))
            .from(user)
            .where(user.nickname.contains(nickname))
            .orderBy(getOrderSpecifiers(pageable)) // Dynamic sorting
            .offset(pageable.getOffset()) // Set offset for paging
            .limit(pageable.getPageSize()) // Set page size
            .fetch(); // Fetch the result

        // Count total number of users matching the query
        long totalSize = countQuery().fetchOne();

        // Return the page using PageableExecutionUtils to avoid unnecessary count query
        return PageableExecutionUtils.getPage(userDtoList, pageable, () -> totalSize);
    }
    private JPAQuery<Long> countQuery() {
        return jpaQueryFactory.select(Wildcard.count)
            .from(user);
    }


    // Helper method to dynamically add sort specifiers with direction

    // Helper method to dynamically add sort specifiers with direction
    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        return pageable.getSort().stream()
            .map(order -> {
                PathBuilder pathBuilder = new PathBuilder<>(User.class, "user");
                // Determine sorting direction (ascending or descending)
                return new OrderSpecifier<>(
                    order.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(order.getProperty())); // Use the property name for sorting
            })
            .toArray(OrderSpecifier[]::new);
    }
}
