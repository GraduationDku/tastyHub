package com.example.tastyhub.common.domain.user.service;

import com.example.tastyhub.common.domain.user.dtos.DuplicatedNickName;
import com.example.tastyhub.common.domain.user.dtos.DuplicatedUserName;
import com.example.tastyhub.common.domain.user.dtos.LoginRequest;
import com.example.tastyhub.common.domain.user.dtos.SignupRequest;
import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.user.entity.User.userType;
import com.example.tastyhub.common.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


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
    @Transactional
    public void signup(SignupRequest signupRequest) {
        String username = signupRequest.getUsername();
        String password = signupRequest.getPassword() + username.substring(0,2); // 레인보우 테이블을 취약 -> salt 사용을 통해 해결
        String userImg = "refact"; // s3 연결 후
//        String village = signupRequest.get village CRUD 이후
        User user = User.builder()
            .username(username)
            .password(passwordEncoder.encode(password))
            .userImg(userImg)
            .email(signupRequest.getEmail())
            .nickname(signupRequest.getNickname())
            .village(null)
            .userType(userType.COMMON)
            .build();
        userRepository.save(user);
    }

    @Override
    public void login(LoginRequest loginRequest, HttpServletResponse response) {

    }


}
