package com.project.trivia.FriendsList;

import com.project.trivia.User.User;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Friends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "friends")
    private List<User> user;
    private String username;


    public Friends(String username) {
        this.username = username;
    }

    public Friends() {

    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }
}
