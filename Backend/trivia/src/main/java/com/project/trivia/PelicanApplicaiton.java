package com.project.trivia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
//            PelicanQuestion pq4 = new PelicanQuestion("What is pelican in Romanian?", "pelican", "open");
//            PelicanQuestion pq5 = new PelicanQuestion("What is the name of the famous species of pelican found in Australia?", "australian", "open");
//            PelicanQuestion pq6 = new PelicanQuestion("What is the characteristic sound made by pelicans?", "squawk", "open");
//
//
//            pepository.save(pq1);
//            pepository.save(pq2);
//            pepository.save(pq3);
//            pepository.save(pq4);
//            pepository.save(pq5);
//            pepository.save(pq6);
//        };
//    }
}
