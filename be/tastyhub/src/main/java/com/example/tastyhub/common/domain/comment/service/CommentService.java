package com.example.tastyhub.common.domain.comment.service;

import com.example.tastyhub.common.domain.comment.dtos.CommentRequest;
import com.example.tastyhub.common.domain.user.entity.User;

public interface CommentService {


    void createComment(Long postId, CommentRequest commentCreateRequest, User user);

    void updateComment(Long commentId, CommentRequest commentCreateRequest, User user);

}
