package com.example.tastyhub.common.domain.post.controller;

import static com.example.tastyhub.common.config.APIConfig.POST_API;
import static com.example.tastyhub.common.utils.HttpResponseEntity.DELETE_SUCCESS;
import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_CREATED;
import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_OK;

import com.example.tastyhub.common.domain.post.dtos.PagingPostResponse;
import com.example.tastyhub.common.domain.post.dtos.PostResponse;
import com.example.tastyhub.common.utils.page.RestPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tastyhub.common.domain.post.dtos.PostCreateRequest;
import com.example.tastyhub.common.domain.post.dtos.PostUpdateRequest;
import com.example.tastyhub.common.domain.post.service.PostService;
import com.example.tastyhub.common.dto.StatusResponse;
import com.example.tastyhub.common.utils.Jwt.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(POST_API)
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @PostMapping("/create")
  public ResponseEntity<StatusResponse> createPost(
      @RequestBody PostCreateRequest postCreateRequest,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    postService.createPost(postCreateRequest, userDetails.getUser());
    return RESPONSE_CREATED;
  }

  @PatchMapping("/modify/{postId}")
  public ResponseEntity<StatusResponse> updatePost(@PathVariable Long postId,
      @RequestBody PostUpdateRequest postUpdateRequest,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    postService.updatePost(postId, postUpdateRequest, userDetails.getUser());
    return RESPONSE_OK;
  }

  @DeleteMapping("/delete/{postId}")
  public ResponseEntity<StatusResponse> deletePost(@PathVariable Long postId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    postService.deletePost(postId, userDetails.getUser());
    return DELETE_SUCCESS;
  }

  // 게시글 전체 조회,실시간 조회, 단건 조회 로직 - skyriv213
  @GetMapping("/list")
  public ResponseEntity<RestPage<PagingPostResponse>> getAllPost(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable
  ) {
    return ResponseEntity.ok().body(postService.getAllPost(
        userDetails.getUser(), pageable));
  }

  @GetMapping("/recent/list")
  public ResponseEntity<RestPage<PagingPostResponse>> getAllRecentPost(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable
  ) {
    return ResponseEntity.ok().body((RestPage<PagingPostResponse>) postService.getAllRecentPost(
        userDetails.getUser(), pageable));
  }

  @GetMapping("/detail/{postId}")
  public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
    PostResponse postResponse = postService.getPost(postId);
    return ResponseEntity.ok().body(postResponse);
  }
}