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

    private String placement;

    private String questionPlayedType;

    private int pointsEarned;

    private String username;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    public MatchHistory() {

    }

    public MatchHistory(String placement, String questionPlayedType, int pointsEarned, String username, User user) {
        this.placement = placement;
        this.questionPlayedType = questionPlayedType;
        this.pointsEarned = pointsEarned;
        this.username = username;
        this.user = user;
    }

    public int getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(int pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public String getQuestionPlayedType() {
        return questionPlayedType;
    }

    public void setQuestionPlayedType(String questionSet) {
        this.questionPlayedType = questionSet;
    }

    public String getPlacement() {
        return placement;
    }

    public void setPlacement(String placement) {
        this.placement = placement;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
