package com.example.androidexample;

public class UserStats {

    private int id;

    private int totalAnswered;

    private int totalCorrect;

    private int totalIncorrect;

    private int winStreak;

    private int wins;

    private int losses;

    private int questionsSumbitted;

    private int numberOfFriends;

    private int gamesPlayed;

    private String username;


    public UserStats(String username) {
        this.username = username;
        totalAnswered = 0;
        totalCorrect = 0;
        totalIncorrect = 0;
        winStreak = 0;
        wins = 0;
        losses = 0;
        questionsSumbitted = 0;
        gamesPlayed = 0;
        numberOfFriends = 0;
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
        numberOfFriends = 0;
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

    public int getQuestionsSumbitted() {
        return questionsSumbitted;
    }

    public void setQuestionsSumbitted(int questionsSumbitted) {
        this.questionsSumbitted = questionsSumbitted;
    }

    public int getNumberOfFreinds() {
        return numberOfFriends;
    }

    public void setNumberOfFreinds(int numberOfFreinds) {
        this.numberOfFriends = numberOfFreinds;
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
