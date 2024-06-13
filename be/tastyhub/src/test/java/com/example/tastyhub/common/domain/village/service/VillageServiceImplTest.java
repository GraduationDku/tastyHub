package com.example.tastyhub.common.domain.village.service;

import static com.example.tastyhub.fixture.user.UserFixture.USER;
import static com.example.tastyhub.fixture.village.VillageFixture.LOCATION_REQUEST;
import static com.example.tastyhub.fixture.village.VillageFixture.VILLAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.tastyhub.common.domain.village.repository.VillageRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VillageServiceImplTest {

    @Mock
    VillageRepository villageRepository;

    @InjectMocks
    VillageServiceImpl villageService;

//    @Test
//    @DisplayName("지역정보 저장 성공")
//    void location() {
//        villageService.setLocation(LOCATION_REQUEST, USER);
//        verify(villageRepository, times(1)).save(any());
//    }
//
//    @Test
//    @DisplayName("지역정보 수정 성공")
//    void modifyLocation() {
//        given(villageRepository.findByUserId(any())).willReturn(Optional.ofNullable(VILLAGE));
//        villageService.modifyLocation(LOCATION_REQUEST, USER);
//        verify(villageRepository, times(1)).findByUserId(any());
//
//    }
}