package com.example.androidexample;

import java.util.List;

public class Lobby {

    private long id; // Unique identifier for the lobby
    private String lobbyName;
    private int playerCount;
    private int roomSize;
    private boolean finished;
    private List<UserFriend> players; // List of players in the lobby

    public Lobby(String lobbyName, int playerCount, int roomSize, long id, List<UserFriend> players) {
        this.id = id;
        this.lobbyName = lobbyName;
        this.playerCount = playerCount;
        this.roomSize = roomSize;
        this.finished = false;
        this.players = players;
    }

    public long getId() {
        return id;
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

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public List<UserFriend> getPlayers() {
        return players;
    }

    public void setPlayers(List<UserFriend> players) {
        this.players = players;
    }
}
