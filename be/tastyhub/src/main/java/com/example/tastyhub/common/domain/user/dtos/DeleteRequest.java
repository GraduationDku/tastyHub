package com.example.tastyhub.common.domain.user.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeleteRequest {

    private String username;
    private String password;

}
