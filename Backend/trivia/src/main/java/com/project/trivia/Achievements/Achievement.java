package com.project.trivia.Achievements;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.trivia.Questions.Question;
import com.project.trivia.User.User;
import jakarta.persistence.*;

import lombok.Data;

@Entity
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Lob
    private Long id;

    /*
    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

     */

    @Column
    private String username;

    @Column
    private boolean account;
    //achievement to make an account

    @Column
    private boolean singlePlayer;
    //achievement to play singleplayer;

    @Column
    private boolean jeopardy;
    //achievement to play jeopardy

    @Column
    private boolean multiPlayer;
    //achievement to play multiplayer



    public Achievement() {
    }

    ;

    public Achievement(String username, boolean account, boolean singlePlayer, boolean jeopardy, boolean multiPlayer) {
        this.username = username;
        this.account = account;
        this.singlePlayer = singlePlayer;
        this.jeopardy = jeopardy;
        this.multiPlayer = multiPlayer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUser(String username) {
        this.username = username;
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
