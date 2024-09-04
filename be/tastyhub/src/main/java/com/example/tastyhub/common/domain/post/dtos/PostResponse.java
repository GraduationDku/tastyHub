package com.example.tastyhub.common.domain.post.dtos;

import com.example.tastyhub.common.domain.comment.dtos.CommentDto;
import com.example.tastyhub.common.domain.comment.entity.Comment;
import com.example.tastyhub.common.domain.post.entity.Post;
import com.example.tastyhub.common.domain.post.entity.Post.PostState;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
    private String content;
    private String latestUpdateTime;

    private List<CommentDto> commentDtos;

    @QueryProjection
    public PostResponse(Long postId, String title, PostState postState, String nickname, String userImg, String content,
        LocalDateTime latestUpdateTime, List<Comment> comments) {
        this.postId = postId;
        this.title = title;
        this.postState = postState;
        this.nickname = nickname;
        this.userImg = userImg;
        this.content = content;
        this.latestUpdateTime = String.valueOf(latestUpdateTime);
        this.commentDtos = comments.stream().map(CommentDto::new).collect(Collectors.toList());
    }

    public static PostResponse createPostResponse(Post post) {
        return PostResponse.builder()
            .title(post.getTitle())
            .postState(post.getPostState())
            .userImg(post.getUser().getUserImg())
            .nickname(post.getUser().getNickname())
            .commentDtos(
                post.getComments().stream().map(CommentDto::new).collect(Collectors.toList()))
            .postId(post.getId())
            .content(post.getContent())
            .latestUpdateTime(String.valueOf(post.getModifiedAt()))
            .build();
    }

    // 댓글 기능 업데이트 시 댓글리스트 추가 예정
//    public PostResponse(Post post) {
//        this.postId = post.getId();
//        this.title = post.getTitle();
//        this.text = post.getText();
//        this.postState = post.getPostState();
//        this.nickname = post.getUser().getNickname();
//        this.userImg = post.getUser().getUserImg();
//        this.latestUpdateTime = String.valueOf(post.getModifiedAt());
//        this.comments = post.getComments();
//    }

}
