package com.project.trivia.UserStats;

import com.project.trivia.User.User;
import jakarta.persistence.*;

@Entity
public class UserStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double totalAnswered;

    private double totalCorrect;

    private double totalIncorrect;

    private int winStreak;

    private double wins;

    private double losses;

    private int questionsSumbitted;

    private int numberOfFreinds;

    private int gamesPlayed;

    private String username;

    @OneToOne(mappedBy = "stats", cascade = CascadeType.ALL)
    private User user;

    public UserStats(User user, String username) {
        this.user = user;
        this.username = username;
        totalAnswered = 0;
        totalCorrect = 0;
        totalIncorrect = 0;
        winStreak = 0;
        wins = 0;
        losses = 0;
        questionsSumbitted = 0;
        gamesPlayed = 0;
    }

    public UserStats() {
        totalAnswered = 0;
        totalCorrect = 0;
        totalIncorrect = 0;
        winStreak = 0;
        wins = 0;
        losses = 0;
        questionsSumbitted = 0;
        gamesPlayed = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getTotalAnswered() {
        return totalAnswered;
    }

    public void setTotalAnswered(double totalAnswered) {
        this.totalAnswered = totalAnswered;
    }

    public double getTotalCorrect() {
        return totalCorrect;
    }

    public void setTotalCorrect(double totalCorrect) {
        this.totalCorrect = totalCorrect;
    }

    public double getTotalIncorrect() {
        return totalIncorrect;
    }

    public void setTotalIncorrect(double totalIncorrect) {
        this.totalIncorrect = totalIncorrect;
    }

    public int getWinStreak() {
        return winStreak;
    }

    public void setWinStreak(int winStreak) {
        this.winStreak = winStreak;
    }

    public int getQuestionsSumbitted() {
        return questionsSumbitted;
    }

    public void setQuestionsSumbitted(int questionsSumbitted) {
        this.questionsSumbitted = questionsSumbitted;
    }

    public int getNumberOfFreinds() {
        return numberOfFreinds;
    }

    public void setNumberOfFreinds(int numberOfFreinds) {
        this.numberOfFreinds = numberOfFreinds;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public double getLosses() {
        return losses;
    }

    public void setLosses(double losses) {
        this.losses = losses;
    }

    public double getWins() {
        return wins;
    }

    public void setWins(double wins) {
        this.wins = wins;
    }
}
