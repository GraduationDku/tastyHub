package com.example.tastyhub.common.domain.user.service;

import static com.example.tastyhub.common.utils.Jwt.JwtUtil.AUTHORIZATION_HEADER;
import static com.example.tastyhub.common.utils.Jwt.JwtUtil.REFRESH_HEADER;

import com.example.tastyhub.common.domain.user.dtos.ChangePasswordRequest;
import com.example.tastyhub.common.domain.user.dtos.UserDeleteRequest;
import com.example.tastyhub.common.domain.user.dtos.DuplicatedNickName;
import com.example.tastyhub.common.domain.user.dtos.DuplicatedUserName;
import com.example.tastyhub.common.domain.user.dtos.FindIdRequest;
import com.example.tastyhub.common.domain.user.dtos.LoginRequest;
import com.example.tastyhub.common.domain.user.dtos.SearchUserDto;
import com.example.tastyhub.common.domain.user.dtos.SignupRequest;
import com.example.tastyhub.common.domain.user.dtos.UserDto;
import com.example.tastyhub.common.domain.user.dtos.UserUpdateRequest;
import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.user.entity.User.userType;
import com.example.tastyhub.common.domain.user.repository.UserRepository;
import com.example.tastyhub.common.utils.Jwt.JwtUtil;
import com.example.tastyhub.common.utils.Redis.RedisUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public static final long REFRESH_TOKEN_TIME = 60 * 60 * 60 * 1000L;


    private final RedisUtil redisUtil;

    private final JwtUtil jwtUtill;


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
        String password = signupRequest.getPassword() + username.substring(0,
            2); // 레인보우 테이블을 취약 -> salt 사용을 통해 해결
        String userImg = "refact"; // s3 연결 후
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
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword() + username.substring(0, 2);
        User byUsername = findByUsername(username);
        boolean a = passwordEncoder.matches(password, byUsername.getPassword());
        if (!passwordEncoder.matches(password, byUsername.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지않습니다.");
        }
        String accessToken = jwtUtill.createAccessToken(byUsername.getUsername(),
            byUsername.getUserType());
        String refreshToken = jwtUtill.createRefreshToken(byUsername.getUsername(),
            byUsername.getUserType());

        redisUtil.setDataExpire(REFRESH_HEADER, refreshToken, REFRESH_TOKEN_TIME);
        response.addHeader(AUTHORIZATION_HEADER, accessToken);
        response.addHeader(REFRESH_HEADER, refreshToken);
    }

    @Override
    public String findId(FindIdRequest findIdRequest) {
        User user = findByEmail(findIdRequest);
        String subId = user.getUsername().substring(0, user.getUsername().length() - 4);
        return subId+"****";
    }


    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest, User user) {
        user.updatePassword(changePasswordRequest.getChangePassword());
    }

    @Override
    public List<UserDto> getUserList(SearchUserDto searchUserDto) {
        List<UserDto> userDtoList = userRepository.findAllByNickname(searchUserDto.getNickname());
        return userDtoList;
    }

    private User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지않습니다."));
    }

    private User findByEmail(FindIdRequest findIdRequest) {
        return userRepository.findByEmail(findIdRequest.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않습니다."));
    }
    @Override
    public void delete(UserDeleteRequest deleteRequest, User user){
        String username = deleteRequest.getUsername();
        String password = deleteRequest.getPassword()+username.substring(0,2);
        
        boolean isCorrectedPassword=passwordEncoder.matches(password, user.getPassword());
        if (!isCorrectedPassword) {
            throw new IllegalArgumentException("비밀번호가 일치하지않습니다.");
        }
        userRepository.delete(user);
    }

    @Override
    public void updateUserInfo(UserUpdateRequest userUpdateRequest, User user) {
        user.updateUserInfo(userUpdateRequest);
        userRepository.save(user);
    }


}
