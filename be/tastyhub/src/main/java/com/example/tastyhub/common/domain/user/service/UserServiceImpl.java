package com.example.tastyhub.common.domain.user.service;

import static com.example.tastyhub.common.utils.Jwt.JwtUtil.AUTHORIZATION_HEADER;
import static com.example.tastyhub.common.utils.Jwt.JwtUtil.REFRESH_HEADER;

import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import com.example.tastyhub.common.domain.user.dtos.ChangePasswordRequest;
import com.example.tastyhub.common.domain.user.dtos.UserDeleteRequest;
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
import com.example.tastyhub.common.utils.S3.S3Uploader;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import java.util.List;

import java.util.Optional;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public static final long REFRESH_TOKEN_TIME = 60 * 60 * 60 * 1000L;


    private final RedisUtil redisUtil;

    private final JwtUtil jwtUtill;

    private final S3Uploader s3Uploader;


    @Override
    @Transactional
    public void checkDuplicatedUsername(String username) {
        if (!userRepository.existsByUsername(username)) {
            return;
        }
        throw new IllegalArgumentException("이미 존재하는 username입니다");
    }

    @Override
    @Transactional
    public void checkDuplicatedNickname(String nickname) {
        if (!userRepository.existsByNickname(nickname)) {
            return;
        }
        throw new IllegalArgumentException("이미 존재하는 nickname입니다");
    }

    @Override
    @Transactional
    public void signup(SignupRequest signupRequest, MultipartFile img) throws java.io.IOException {
        String username = signupRequest.getUsername();
        String password = signupRequest.getPassword() + username.substring(0,
            2); // 레인보우 테이블을 취약 -> salt 사용을 통해 해결
        String imgUrl = "refact"; // 기본 이미지 url
        

        try {
            if(!img.isEmpty()){
                imgUrl = s3Uploader.upload(img,"image/userImg");
            }
            User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .userImg(imgUrl)
                .email(signupRequest.getEmail())
                .nickname(signupRequest.getNickname())
                .village(null)
                .userType(userType.COMMON)
                .build();
            userRepository.save(user);
            } catch (Exception e) {
                // 레시피 저장에 실패한 경우, S3에서 이미지 삭제
                if (!imgUrl.isEmpty()) {
                    try {
                        s3Uploader.delete(imgUrl);
                    } catch (IOException ioException) {
                        log.error("Failed to delete uploaded image from S3", ioException);
                    }
                }
                throw e; // 예외를 다시 던져 트랜잭션 롤백 활성화
            }
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
        User user1 = findByUsername(user.getUsername());
        String password = changePasswordRequest.getBeforePassword()+user1.getUsername().substring(0,2);
        if (!passwordEncoder.matches(password, user1.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지않습니다.");
        }
        user.updatePassword(changePasswordRequest.getChangePassword());
    }

    @Override
    @Transactional
    public List<UserDto> getUserList(SearchUserDto searchUserDto) {
        List<UserDto> userDtoList = userRepository.findAllByNickname(searchUserDto.getNickname());
        return userDtoList;
    }

    @Generated
    private User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("해당 유저는 존재하지않습니다."));
    }

    @Generated
    private User findByEmail(FindIdRequest findIdRequest) {
        return userRepository.findByEmail(findIdRequest.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("해당 회원은 존재하지 않습니다."));
    }
    @Override
    public void delete(UserDeleteRequest deleteRequest, User user) throws java.io.IOException{
        String username = deleteRequest.getUsername();
        String password = deleteRequest.getPassword()+username.substring(0,2);
        String imgUrl = user.getUserImg();
        
        boolean isCorrectedPassword=passwordEncoder.matches(password, user.getPassword());
        if (!isCorrectedPassword) {
            throw new IllegalArgumentException("비밀번호가 일치하지않습니다.");
        }
        userRepository.delete(user);
        s3Uploader.delete(imgUrl);
    }

    @Override
    public void updateUserInfo(UserUpdateRequest userUpdateRequest,MultipartFile img, User user) throws java.io.IOException {
        String imgUrl = new String();
        
        try {
            if(!img.isEmpty()){
                imgUrl = s3Uploader.upload(img,"image/userImg");
            }
            User find_user = userRepository.findByUsername(user.getUsername()).orElseThrow(()-> new IllegalArgumentException("해당 유저는 존재하지 않습니다."));
            find_user.updateUserInfo(userUpdateRequest,imgUrl);
            } catch (Exception e) {
                // 레시피 저장에 실패한 경우, S3에서 이미지 삭제
                if (!imgUrl.isEmpty()) {
                    try {
                        s3Uploader.delete(imgUrl);
                    } catch (IOException ioException) {
                        log.error("Failed to delete uploaded image from S3", ioException);
                    }
                }
                throw e; // 예외를 다시 던져 트랜잭션 롤백 활성화
            }
    }


}
