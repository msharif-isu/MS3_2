package com.project.trivia.UserStats;

import com.project.trivia.User.User;
import com.project.trivia.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

public class UserStatsController {

    @Autowired
    UserStatsRepository statsRepo;

    @Autowired
    UserRepository userRepo;



    @GetMapping(path = "/correctRatio/{username}")
    public double getCorrectRatio(@PathVariable String username) {
        UserStats stats = statsRepo.findById(1);//TODO implement way to get the user connected to those stats

        if(stats.getTotalAnswered() == 0){
            return 0;
        }
        return stats.getTotalCorrect() / stats.getTotalAnswered();

    }

    @GetMapping(path = "/incorrectRatio/{username}")
    public double getIncorrectRatio(@PathVariable String username) {
        UserStats stats = statsRepo.findById(1);//TODO

        if(stats.getTotalAnswered() <= 0){
            return 0;
        }
        return stats.getTotalIncorrect() / stats.getTotalAnswered();
    }

    @PutMapping(path = "/updateStats/{correct}/{incorrect}/{total}/{username}")
    public UserStats updateStats(@PathVariable Double correct, @PathVariable Double incorrect,
                                 @PathVariable Double total, @PathVariable String username) {
       User user = userRepo.findByUsername(username);
       UserStats stats = user.getStats();
       stats.setTotalAnswered(stats.getTotalAnswered() + total);
       stats.setTotalCorrect(stats.getTotalAnswered() + correct);
       stats.setTotalIncorrect(stats.getTotalAnswered() + incorrect);

       userRepo.save(user);

        return user.getStats();
    }




}
