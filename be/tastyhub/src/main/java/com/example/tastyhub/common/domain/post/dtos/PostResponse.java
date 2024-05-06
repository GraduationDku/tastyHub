package com.example.tastyhub.common.domain.post.dtos;

import com.example.tastyhub.common.domain.comment.entity.Comment;
import com.example.tastyhub.common.domain.post.entity.Post;
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
public class PostResponse {

    private Long postId;
    private String title;
    private PostState postState;
    private String nickname;
    private String userImg;
    private String latestUpdateTime;

    private List<Comment> comments;


    // 댓글 기능 업데이트 시 댓글리스트 추가 예정
    public PostResponse(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.postState = post.getPostState();
        this.nickname = post.getUser().getNickname();
        this.userImg = post.getUser().getUserImg();
        this.latestUpdateTime = String.valueOf(post.getModifiedAt());
        this.comments = post.getComments();
    }

}
