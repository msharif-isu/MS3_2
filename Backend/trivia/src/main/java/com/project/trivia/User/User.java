package com.project.trivia.User;

import com.project.trivia.Leaderboard.Leaderboard;
import com.project.trivia.MPQuestions.Answer;
import jakarta.persistence.*;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.trivia.roomChat.Message;
import jakarta.persistence.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;

    private long points;

    @OneToMany(mappedBy="user")
    private List<Answer> ans;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "leaderboard_id")
    private Leaderboard leaderboard;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Message> messages;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        points = 0;
        messages = new ArrayList<>();
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

    public Leaderboard getLeaderboard() {return leaderboard;}

    public List<Message> getMessages() {
        return messages;
    }

    public void setLeaderboard(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
    }
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
