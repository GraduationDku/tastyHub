package com.example.tastyhub.common.domain.userReview.entity;

import com.example.tastyhub.common.utils.TimeStamped;
import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.domain.userReview.dtos.UserReviewUpdateRequest;

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
@Table(name = "user_reviews")
public class UserReview extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_review_id")
    private Long id;

    //    private
    private long grade;

    private String text;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, targetEntity = User.class)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE, targetEntity = User.class)
    @JoinColumn(name = "reader_id", nullable = false)
    private User reader;

    public static UserReview createUserReview(long grade, String text, User writer, User reader){
        return UserReview.builder()
                .grade(grade)
                .text(text)
                .writer(writer)
                .reader(reader)
                .build();
    }

    public void update(UserReviewUpdateRequest userReviewUpdateRequest) {
        this.grade = userReviewUpdateRequest.getGrade();
        this.text = userReviewUpdateRequest.getText();
    }
}
