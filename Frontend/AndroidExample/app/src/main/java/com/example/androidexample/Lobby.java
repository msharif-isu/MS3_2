package com.example.androidexample;

public class Lobby {

    //probably just username's lobby
    private String lobbyName;
    private int playerCount;
    private int roomSize;

    public Lobby(String lobbyName, int playerCount, int roomSize) {
        this.lobbyName = lobbyName;
        this.playerCount = playerCount;
        this.roomSize = roomSize;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public void setLobbyName(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
        return playerCount;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public int setRoomSize(int roomSize) {
        this.roomSize = roomSize;
        return roomSize;
    }
}
