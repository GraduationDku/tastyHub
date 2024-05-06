package com.example.tastyhub.common.domain.comment.service;

import org.springframework.stereotype.Service;

import com.example.tastyhub.common.domain.comment.dtos.CommentRequest;
import com.example.tastyhub.common.domain.comment.entity.Comment;
import com.example.tastyhub.common.domain.comment.repository.CommentRepository;
import com.example.tastyhub.common.domain.post.entity.Post;
import com.example.tastyhub.common.domain.post.repository.PostRepository;
import com.example.tastyhub.common.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override
    public void createComment(Long postId, CommentRequest commentCreateRequest, User user) {
        Post post = postRepository.findById(postId).get();
        Comment comment = Comment.builder().post(post).text(commentCreateRequest.getText()).user(user).isAlive(true).build();
        commentRepository.save(comment);
    }

    @Override
    public void updateComment(Long commentId, CommentRequest commentUpdateRequest, User user) {
        Comment comment = commentRepository.findById(commentId).get();
        comment.update(commentUpdateRequest);
    }

}
