package com.project.trivia.Leaderboard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.trivia.User.User;
import jakarta.persistence.*;

@Entity
public class Leaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userPoints;
    private int weeklyPoints;
    private int monthlyPoints;
    private int yearlyPoints;
    private int lifetimePoints;
    private String name;
    @OneToOne(mappedBy = "leaderboard")
    @JsonIgnore
    private User user;

    public Leaderboard(int userPoints, int weeklyPoints, int monthlyPoints, int yearlyPoints, int lifetimePoints, String name, User user) {
        this.userPoints = userPoints;
        this.weeklyPoints = weeklyPoints;
        this.monthlyPoints = monthlyPoints;
        this.yearlyPoints = yearlyPoints;
        this.lifetimePoints = lifetimePoints;
        this.name = name;
        this.user = user;
    }

    public Leaderboard(int userPoints,int weeklyPoints, int monthlyPoints, int yearlyPoints, int lifetimePoints, String name) {
        this.userPoints = userPoints;
        this.weeklyPoints = weeklyPoints;
        this.monthlyPoints = monthlyPoints;
        this.yearlyPoints = yearlyPoints;
        this.lifetimePoints = lifetimePoints;
        this.name = name;
    }
    public Leaderboard() {};

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(int userPoints) {
        this.userPoints = userPoints;
    }

    public int getWeeklyPoints() {
        return weeklyPoints;
    }

    public void setWeeklyPoints(int weeklyPoints) {
        this.weeklyPoints = weeklyPoints;
    }

    public int getMonthlyPoints() {
        return monthlyPoints;
    }

    public void setMonthlyPoints(int monthlyPoints) {
        this.monthlyPoints = monthlyPoints;
    }

    public int getYearlyPoints() {
        return yearlyPoints;
    }

    public void setYearlyPoints(int yearlyPoints) {
        this.yearlyPoints = yearlyPoints;
    }

    public int getLifetimePoints() {
        return lifetimePoints;
    }

    public void setLifetimePoints(int lifetimePoints) {
        this.lifetimePoints = lifetimePoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}
}