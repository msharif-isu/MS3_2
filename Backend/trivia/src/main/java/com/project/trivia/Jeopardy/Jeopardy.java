package com.project.trivia.Jeopardy;

import com.project.trivia.Leaderboard.Leaderboard;
import com.project.trivia.Leaderboard.LeaderboardRepository;
import com.project.trivia.MPQuestions.AnswerRepository;
import com.project.trivia.Questions.Question;
import com.project.trivia.Questions.QuestionRepository;
import com.project.trivia.User.User;
import com.project.trivia.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jeopardy {

    public Leaderboard leaderboardAddPoints(int amount, Leaderboard lb1) {

        lb1.setUserPoints(lb1.getUserPoints() + amount);
        lb1.setWeeklyPoints(lb1.getWeeklyPoints() + amount);
        lb1.setMonthlyPoints(lb1.getMonthlyPoints() + amount);
        lb1.setYearlyPoints(lb1.getYearlyPoints() + amount);
        lb1.setLifetimePoints(lb1.getLifetimePoints() + amount);

        return lb1;
    }
    public void addPoints(int points, User user) {
        Leaderboard lb = user.getLeaderboard();
        leaderboardAddPoints(points, lb);
    }








}