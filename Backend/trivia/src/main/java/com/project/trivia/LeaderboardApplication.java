package com.project.trivia;

import com.project.trivia.Leaderboard.Leaderboard;
import com.project.trivia.Leaderboard.LeaderboardRepository;
import com.project.trivia.Questions.Question;
import com.project.trivia.Questions.QuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LeaderboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeaderboardApplication.class, args);
    }

    @Bean
    CommandLineRunner initLeaderboard(LeaderboardRepository leaderboardRepository) {
        return args -> {
            Leaderboard lb1 = new Leaderboard(100, 200, 400, 1000, 2000);
            Leaderboard lb2 = new Leaderboard(0, 0, 1000, 5000, 100000);
            leaderboardRepository.save(lb1);
            leaderboardRepository.save(lb2);
        };
    }
}
