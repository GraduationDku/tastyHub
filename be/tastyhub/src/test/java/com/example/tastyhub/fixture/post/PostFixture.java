package com.example.tastyhub.fixture.post;


import static com.example.tastyhub.fixture.comment.CommentFixture.COMMENT_DTOS;
import static com.example.tastyhub.fixture.user.UserFixture.USER;

import com.example.tastyhub.common.domain.post.dtos.PagingPostResponse;
import com.example.tastyhub.common.domain.post.dtos.PostCreateRequest;
import com.example.tastyhub.common.domain.post.dtos.PostResponse;
import com.example.tastyhub.common.domain.post.dtos.PostUpdateRequest;
import com.example.tastyhub.common.domain.post.entity.Post;
import com.example.tastyhub.common.domain.post.entity.Post.PostState;
import com.example.tastyhub.common.utils.page.RestPage;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PostFixture {

    public static final Post POST = Post.builder()
        .id(1L)
        .postState(PostState.Start)
        .title("title")
        .user(USER)
        .content("Text")
        .build();

    public static final PostCreateRequest POST_CREATE_REQUEST = PostCreateRequest.builder()
        .title("title")
        .content("Text")
        .build();

    public static final PostResponse POST_RESPONSE = PostResponse.builder()
        .postId(1L)
        .postState(PostState.Start)
        .title("title")
        .latestUpdateTime("time")
        .nickname("nickname")
        .content("text")
        .userImg("img")
        .commentDtos(COMMENT_DTOS)
        .build();

    public static final PostUpdateRequest POST_UPDATE_REQUEST = PostUpdateRequest.builder()
        .content("text")
        .title("title")
        .postState(PostState.Start)
        .build();


    public static final PagingPostResponse PAGING_POST_RESPONSE = PagingPostResponse.builder()
        .postId(1L)
        .postState(PostState.Start)
        .nickname("nickname")
        .userImg("userImg")
        .title("title")
        .build();
    public static Pageable pageable = PageRequest.of(0, 1);


    public static final List<PagingPostResponse> LIST_PAGE_POST = Collections.singletonList(
        PAGING_POST_RESPONSE);

    public static final RestPage<PagingPostResponse> PAGING_POST_RESPONSES = new RestPage<>(LIST_PAGE_POST

        , pageable, 1);
}
