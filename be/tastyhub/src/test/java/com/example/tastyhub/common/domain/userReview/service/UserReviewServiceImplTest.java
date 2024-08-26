package com.example.tastyhub.common.domain.userReview.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import static com.example.tastyhub.fixture.userReview.UserReviewFixture.USER_REVIEW;
import static com.example.tastyhub.fixture.userReview.UserReviewFixture.USER_REVIEW_CREATE_REQUEST;
import static com.example.tastyhub.fixture.userReview.UserReviewFixture.USER_REVIEW_UPDATE_REQUEST;
import static com.example.tastyhub.fixture.userReview.UserReviewFixture.PAGING_USER_REVIEW_RESPONSES;
import static com.example.tastyhub.fixture.user.UserFixture.USER;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.tastyhub.common.domain.user.repository.UserRepository;
import com.example.tastyhub.common.domain.userReview.repository.UserReviewRepository;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserReviewServiceImplTest {
    @Mock
    UserReviewRepository userReviewRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserReviewServiceImpl userReviewService;

    @Test
    @DisplayName("유저 리뷰 생성")
    void createUserReview() {
        when(userRepository.findById(USER.getId())).thenReturn(Optional.of(USER));
        userReviewService.createUserReview(USER.getId(), USER_REVIEW_CREATE_REQUEST, USER);
        verify(userReviewRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("유저 리뷰 업데이트")
    void updatePost() {
        given(userReviewRepository.findById(any())).willReturn(Optional.ofNullable(USER_REVIEW));
        userReviewService.updateUserReviewByUserReviewUpdateRequest(USER_REVIEW.getId(), USER_REVIEW_UPDATE_REQUEST, USER);
        verify(userReviewRepository, times(1)).findById(any());

    }
    @Test
    @DisplayName("유저 리뷰 삭제")
    void deletePost() {
        userReviewService.deleteUserReview(USER_REVIEW.getId(), USER);
        verify(userReviewRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("유저 리뷰 리스트 조회")
    void getAllPost() {
        given(userReviewRepository.findAllUserReviewResponse(USER.getId()))
        .willReturn(PAGING_USER_REVIEW_RESPONSES);
        userReviewService.getUserReviews(USER.getId());
        verify(userReviewRepository, times(1)).findAllUserReviewResponse(any());
    }
    
}
