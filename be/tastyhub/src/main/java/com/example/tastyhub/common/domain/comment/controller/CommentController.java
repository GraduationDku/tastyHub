package com.example.tastyhub.common.domain.comment.controller;

import static com.example.tastyhub.common.config.APIConfig.COMMENT_API;
import static com.example.tastyhub.common.utils.HttpResponseEntity.DELETE_SUCCESS;
import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_CREATED;
import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_OK;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.tastyhub.common.domain.comment.dtos.CommentRequest;
import com.example.tastyhub.common.domain.comment.service.CommentService;
import com.example.tastyhub.common.dto.StatusResponse;
import com.example.tastyhub.common.utils.Jwt.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(COMMENT_API)
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create/{postId}")
    public ResponseEntity<StatusResponse> createComment(@PathVariable Long postId, @RequestBody CommentRequest commentCreateRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
                commentService.createComment(postId,commentCreateRequest, userDetails.getUser());
        return RESPONSE_CREATED;
    }
    @PatchMapping("/modify/{commentId}")
    public ResponseEntity<StatusResponse> updateComment(@PathVariable Long commentId, @RequestBody CommentRequest commentCreateRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
                commentService.updateComment(commentId,commentCreateRequest, userDetails.getUser());
        return RESPONSE_OK;
    }
    @PatchMapping("/delete/{commentId}")
    public ResponseEntity<StatusResponse> deleteComment(@PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
                commentService.deleteComment(commentId, userDetails.getUser());
        return DELETE_SUCCESS;
    }



}
