package com.example.tastyhub.common.domain.comment.service;

import lombok.Generated;
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
        Post post = getPost(postId);
        Comment comment = Comment.builder().post(post).content(commentCreateRequest.getContent()).user(user).isAlive(true).build();
        commentRepository.save(comment);
    }

    @Override
    public void updateComment(Long commentId, CommentRequest commentUpdateRequest, User user) {
        Comment comment = findById(commentId);
        comment.updateByCommentRequest(commentUpdateRequest);
    }

    @Override
    public void deleteComment(Long commentId, User user) {
        Comment comment = findById(commentId);
        comment.delete();
    }

    @Generated
    private Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("해당 댓글은 존재하지 않습니다"));
    }

    @Generated
    private Post getPost(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));
    }

}
