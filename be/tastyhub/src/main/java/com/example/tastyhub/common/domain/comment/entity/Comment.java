package com.example.tastyhub.common.domain.comment.entity;

import com.example.tastyhub.common.utils.TimeStamped;
import com.example.tastyhub.common.domain.comment.dtos.CommentRequest;
import com.example.tastyhub.common.domain.post.entity.Post;
import com.example.tastyhub.common.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "comments")
public class Comment extends TimeStamped {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long id;

    private String text;

    private Boolean isAlive;

    //연관관계
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, targetEntity = Post.class)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static Comment createComment(String text, Boolean isAlive, Post post, User user){
        return Comment.builder()
                .text(text)
                .isAlive(isAlive)
                .post(post)
                .user(user)
                .build();
    }
    public void update(CommentRequest commentUpdateRequest) {
        this.text = commentUpdateRequest.getText();
    }

    public void delete() {
        this.isAlive =false;
    }


}
