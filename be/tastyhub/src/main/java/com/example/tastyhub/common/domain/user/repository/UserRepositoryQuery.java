package com.example.tastyhub.common.domain.user.repository;

import com.example.tastyhub.common.domain.user.dtos.UserDto;
import java.util.List;

public interface UserRepositoryQuery {

    List<UserDto> findAllByNickname(String nickname);
}
