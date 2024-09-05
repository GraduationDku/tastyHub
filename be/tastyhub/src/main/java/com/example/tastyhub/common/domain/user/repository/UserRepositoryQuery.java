package com.example.tastyhub.common.domain.user.repository;

import com.example.tastyhub.common.domain.user.dtos.UserDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryQuery {

    Page<UserDto> findAllByNickname(String nickname, Pageable pageable);
}
