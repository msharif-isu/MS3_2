package com.project.trivia.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.trivia.FriendsList.Friends;
import com.project.trivia.Lobby.Lobby;
import jakarta.persistence.*;


import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToMany
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friends_id"))
    @JsonIgnore
    private List<Friends> friends;
    private String username;
    private String password;
    private String email;
    private String bio;
    private String filePath;
    private long points;

    @ManyToOne
    @JoinColumn(name="lobby_id")
    @JsonIgnore
    private Lobby lobby;


    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        bio = "";
        points = 0;
    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    public List<Friends> getFriends() {
        return friends;
    }

    public void setFriends(List<Friends> friends) {
        this.friends = friends;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public Lobby getLobby() {
        return lobby;
    }
    public void setLobby(Lobby lobbyId) {
        this.lobby = lobbyId;
    }

}
