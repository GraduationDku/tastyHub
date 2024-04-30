package com.example.tastyhub.common.domain.user.entity;

import com.example.tastyhub.common.domain.comment.entity.Comment;
import com.example.tastyhub.common.domain.like.entity.Like;
import com.example.tastyhub.common.domain.post.entity.Post;
import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import com.example.tastyhub.common.domain.recipeReview.entity.RecipeReview;
import com.example.tastyhub.common.domain.scrap.entity.Scrap;
import com.example.tastyhub.common.domain.user.dtos.UserUpdateRequest;
import com.example.tastyhub.common.domain.userReview.entity.UserReview;
import com.example.tastyhub.common.domain.village.entity.Village;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

@Table(name = "users")
@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;

    private String password;

    private String userImg;

    private String nickname;

    private String email;

    @Enumerated(EnumType.STRING)
    private userType userType;

    public enum userType {
        ADMIN, COMMON
    }

    // 연관관계 작성

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default

    private List<Recipe> recipes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RecipeReview> recipeReviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Scrap> scraps = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Like> likes = new ArrayList<>();


    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserReview> userWriterReviews = new ArrayList<>();

    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserReview> userReaderReviews = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Village village;

    public void updateUserInfo(UserUpdateRequest userUpdateRequest){
        this.nickname = userUpdateRequest.getNickname();
    }
}
