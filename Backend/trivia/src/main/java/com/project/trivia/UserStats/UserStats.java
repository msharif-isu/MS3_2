package com.project.trivia.UserStats;

import com.project.trivia.User.User;
import jakarta.persistence.*;

@Entity
public class UserStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int totalAnswered;

    private int totalCorrect;

    private int totalIncorrect;

    private int winStreak;

    private int wins;

    private int losses;

    private int questionsSubmitted;

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
        questionsSubmitted = 0;
        gamesPlayed = 0;
    }

    public UserStats() {
        totalAnswered = 0;
        totalCorrect = 0;
        totalIncorrect = 0;
        winStreak = 0;
        wins = 0;
        losses = 0;
        questionsSubmitted = 0;
        gamesPlayed = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalAnswered() {
        return totalAnswered;
    }

    public void setTotalAnswered(int totalAnswered) {
        this.totalAnswered = totalAnswered;
    }

    public int getTotalCorrect() {
        return totalCorrect;
    }

    public void setTotalCorrect(int totalCorrect) {
        this.totalCorrect = totalCorrect;
    }

    public int getTotalIncorrect() {
        return totalIncorrect;
    }

    public void setTotalIncorrect(int totalIncorrect) {
        this.totalIncorrect = totalIncorrect;
    }

    public int getWinStreak() {
        return winStreak;
    }

    public void setWinStreak(int winStreak) {
        this.winStreak = winStreak;
    }

    public int getQuestionsSubmitted() {
        return questionsSubmitted;
    }

    public void setQuestionsSubmitted(int questionsSubmitted) {
        this.questionsSubmitted = questionsSubmitted;
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

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }
}
