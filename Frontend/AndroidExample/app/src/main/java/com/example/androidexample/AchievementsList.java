package com.example.androidexample;

public class AchievementsList {
    private boolean account;
    //achievement to make an account

    private boolean singlePlayer;
    //achievement to play singleplayer;
    private boolean jeopardy;
    //achievement to play jeopardy
    private boolean multiPlayer;
    //achievement to play multiplayer

    public AchievementsList(boolean account, boolean singlePlayer, boolean jeopardy, boolean multiPlayer) {
        this.account = account;
        this.singlePlayer = singlePlayer;
        this.jeopardy = jeopardy;
        this.multiPlayer = multiPlayer;
    }

    public boolean isAccount() {
        return account;
    }

    public void setAccount(boolean account) {
        this.account = account;
    }

    public boolean isSinglePlayer() {
        return singlePlayer;
    }

    public void setSinglePlayer(boolean singlePlayer) {
        this.singlePlayer = singlePlayer;
    }

    public boolean isJeopardy() {
        return jeopardy;
    }

    public void setJeopardy(boolean jeopardy) {
        this.jeopardy = jeopardy;
    }

    public boolean isMultiPlayer() {
        return multiPlayer;
    }

    public void setMultiPlayer(boolean multiPlayer) {
        this.multiPlayer = multiPlayer;
    }
}
