package com.project.trivia.UserStats;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Statstics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int totalAnswered;

    private int totalCorrect;

    private int totalIncorrect;

    private int winStreak;

    private int questionsSumbitted;

    private int numberOfFreinds;

    private int gamesPlayed;



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
}
