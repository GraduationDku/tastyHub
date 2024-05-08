package com.example.tastyhub.common.domain.post.dtos;

import com.example.tastyhub.common.domain.comment.entity.Comment;
import com.example.tastyhub.common.domain.post.entity.Post.PostState;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagingPostResponse {

    private Long postId;
    private String title;
    private PostState postState;
    private String nickname;
    private String userImg;
}
