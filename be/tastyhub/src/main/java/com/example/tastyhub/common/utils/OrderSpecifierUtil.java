package com.example.tastyhub.common.utils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Pageable;

public class OrderSpecifierUtil {

  public static <T> OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, Class<T> entityClass, String alias) {
    return pageable.getSort().stream()
        .map(order -> {
          PathBuilder<T> pathBuilder = new PathBuilder<>(entityClass, alias);
          return new <T> OrderSpecifier(
              order.isAscending() ? Order.ASC : Order.DESC,
              pathBuilder.get(order.getProperty()));
        })
        .toArray(OrderSpecifier[]::new);
  }
}
