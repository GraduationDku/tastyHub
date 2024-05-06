package com.example.tastyhub.common.domain.comment.service;

import com.example.tastyhub.common.domain.comment.dtos.CommentCreateRequest;
import com.example.tastyhub.common.domain.user.entity.User;

public interface CommentService {


    void createComment(Long postId, CommentCreateRequest commentCreateRequest, User user);

}
