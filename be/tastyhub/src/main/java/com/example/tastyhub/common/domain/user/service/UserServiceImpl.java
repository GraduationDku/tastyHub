package com.example.tastyhub.common.domain.user.service;

import com.example.tastyhub.common.domain.user.dtos.DuplicatedNickName;
import com.example.tastyhub.common.domain.user.dtos.DuplicatedUserName;
import com.example.tastyhub.common.domain.user.dtos.LoginRequest;
import com.example.tastyhub.common.domain.user.dtos.SignupRequest;
import com.example.tastyhub.common.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

//    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void checkDuplicatedUsername(DuplicatedUserName duplicatedUserName) {
        if (!userRepository.existsByUsername(duplicatedUserName.getUsername())) {
            return;
        }
        throw new IllegalArgumentException("이미 존재하는 username입니다");
    }

    @Override
    @Transactional
    public void checkDuplicatedNickname(DuplicatedNickName duplicatedNickName) {
        if (!userRepository.existsByNickname(duplicatedNickName.getNickname())) {
            return;
        }
        throw new IllegalArgumentException("이미 존재하는 nickname입니다");
    }

    @Override
    public void signup(SignupRequest signupRequest) {

    }

    @Override
    public void login(LoginRequest loginRequest, HttpServletResponse response) {

    }


}
