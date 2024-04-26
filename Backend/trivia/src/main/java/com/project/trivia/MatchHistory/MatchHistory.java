package com.project.trivia.MatchHistory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.trivia.User.User;
import jakarta.persistence.*;
import jakarta.persistence.Entity;

@Entity
public class MatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int placement;

    private String questionSet;

    private int pointsEarned;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    public MatchHistory() {

    }

    public MatchHistory(int placement, String questionSet, int pointsEarned) {
        this.placement = placement;
        this.questionSet = questionSet;
        this.pointsEarned = pointsEarned;
    }

    public int getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(int pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public String getQuestionSet() {
        return questionSet;
    }

    public void setQuestionSet(String questionSet) {
        this.questionSet = questionSet;
    }

    public int getPlacement() {
        return placement;
    }

    public void setPlacement(int placement) {
        this.placement = placement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
