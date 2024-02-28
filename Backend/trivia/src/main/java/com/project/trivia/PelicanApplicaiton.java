package com.project.trivia;

import com.project.trivia.PelicanQuestions.PelicanQuestion;
import com.project.trivia.PelicanQuestions.PelicanQuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PelicanApplicaiton {
    public static void main(String[] args) {
        SpringApplication.run(com.project.trivia.PelicanApplicaiton.class, args);
    }


//    @Bean
//    CommandLineRunner initUser(PelicanQuestionRepository pepository) {
//        return args -> {
//            PelicanQuestion pq1 = new PelicanQuestion("What is the primary color of a pelican's beak?", "orange", "open");
//            PelicanQuestion pq2 = new PelicanQuestion("What type of fish do pelicans primarily feed on?", "fish", "open");
//            PelicanQuestion pq3 = new PelicanQuestion("What are the large pouches beneath a pelican's beak used for?", "feeding", "open");
//            pepository.save(pq1);
//            pepository.save(pq2);
//            pepository.save(pq3);
//        };
//    }
}
