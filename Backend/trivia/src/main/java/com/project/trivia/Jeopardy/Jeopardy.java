package com.project.trivia.Jeopardy;

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
    private static UserRepository userRepo;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public void setUserRepository(UserRepository repo) {userRepo = repo;}

    private static LeaderboardRepository lbRepo;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public void setLeaderboardRepository(LeaderboardRepository repo) {lbRepo = repo;}

    public void addPoints(int points, User user) {

    }

}