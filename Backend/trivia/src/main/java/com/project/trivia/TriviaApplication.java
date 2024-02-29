package com.project.trivia;

import com.project.trivia.Leaderboard.Leaderboard;
import com.project.trivia.Leaderboard.LeaderboardRepository;
import com.project.trivia.Questions.Question;
import com.project.trivia.Questions.QuestionRepository;

import com.project.trivia.User.User;
import com.project.trivia.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TriviaApplication {

	public static void main(String[] args) {
		SpringApplication.run(TriviaApplication.class, args);
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
	@Bean
	CommandLineRunner initQuestion(QuestionRepository questionRepository) {
		return args -> {
			Question q1 = new Question("Who is the coolest?", "Raphael", "not a question");
			Question q2 = new Question("Who is the 2nd coolest?", "Also Raphael", "not a question");
			questionRepository.save(q1);
			questionRepository.save(q2);
		};
	}
	@Bean
    CommandLineRunner initUser(UserRepository userRepository) {
        return args -> {
            User user1 = new User("alok", "password123", "aloks@iastate.edu");
            if(!userRepository.existsByUsername(user1.getUsername())){
                userRepository.save(user1);
            }
        };

    }
}
