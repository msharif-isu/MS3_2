package com.project.trivia.Lobby;

import com.project.trivia.User.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Lobby {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * the max size of the room
     */
    private int roomSize;
    /**
     * Number of poeple current in room
     */
    private int playerCount;
    private String host;
    @OneToMany(mappedBy = "lobby")
    private List<User> players;

    private Boolean finished;


    public Lobby(int roomSize, String host) {
        this.roomSize = roomSize;
        playerCount = 1;
        this.host = host;
        players = new ArrayList<>();
        finished = false;

    }

    public Lobby() {
        players = new ArrayList<>();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
}
