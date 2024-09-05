package com.example.tastyhub.common.domain.comment.dtos;


import com.example.tastyhub.common.domain.comment.entity.Comment;
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
    private String content;
    private Boolean state;
    private String latestUpdateTime;

    public CommentDto (Comment comment){
        this.userId = comment.getUser().getId();
        this.nickname = comment.getUser().getNickname();
        this.userImg = comment.getUser().getUserImg();
        this.content = comment.getContent();
        this.state = comment.getIsAlive();
        this.latestUpdateTime = String.valueOf(comment.getModifiedAt());
    }
}
