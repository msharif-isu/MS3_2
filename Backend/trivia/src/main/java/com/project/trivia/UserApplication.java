package com.project.trivia;

import com.project.trivia.User.User;
import com.project.trivia.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
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
