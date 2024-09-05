package com.example.tastyhub.common.domain.post.entity;

import com.example.tastyhub.common.domain.village.entity.Village;
import com.example.tastyhub.common.utils.TimeStamped;
import com.example.tastyhub.common.domain.comment.entity.Comment;
import com.example.tastyhub.common.domain.post.dtos.PostUpdateRequest;
import com.example.tastyhub.common.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "posts",indexes = {
    @Index(name = "idx_address", columnList = "addressTownName")
})
public class Post extends TimeStamped {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tasty_hub_sequence")
    @SequenceGenerator(name = "tasty_hub_sequence", sequenceName = "thesq", allocationSize = 10)
    @Column(name = "post_id")
    private long id;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private PostState postState;

    public enum PostState {
        Start, Continue, Complete
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, targetEntity = User.class)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post")
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @Embedded
    private Village village;

    public static Post createPost(String title, String text, PostState postState,User user) {
        return Post.builder()
            .title(title)
            .content(text)
            .postState(postState)
            .user(user)
            .village(user.getVillage())
            .build();
    }

    public void update(PostUpdateRequest postUpdateRequest) {
        this.title = postUpdateRequest.getTitle();
        this.content = postUpdateRequest.getTitle();
        this.postState = postUpdateRequest.getPostState();
    }

}