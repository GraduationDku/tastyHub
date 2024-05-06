package com.example.tastyhub.common.domain.comment.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long userId;
    private String nickname;
    private String userImg;
    private String text;
    private Boolean state;
    private String latestUpdateTime;
}
