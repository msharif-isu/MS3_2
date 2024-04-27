package com.project.trivia.Multiplayer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.trivia.FriendsList.Friends;
import com.project.trivia.Leaderboard.Leaderboard;
import com.project.trivia.Lobby.Lobby;
import com.project.trivia.Questions.Question;
import com.project.trivia.Queryboard.Query;
import com.project.trivia.User.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Multiplayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * the max size of the room
     */
    @ManyToMany(mappedBy = "multiplayer")
    private List<Question> questions;
    /**
     * Many questions can be attributed to many different multiplayer instances
     */
    private boolean finished;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lobby_id", referencedColumnName = "id")
    private Lobby lobby;


    public Multiplayer(Lobby lobby) {
        this.lobby = lobby;
        finished = false;
        questions = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Question> getQuestion() {
        return questions;
    }

    public void setQuestion(List<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        this.questions.add(question);
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }
}
