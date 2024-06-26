package com.project.trivia.UserStats;

import com.project.trivia.User.User;
import com.project.trivia.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserStatsController {

    @Autowired
    UserStatsRepository statsRepo;

    @Autowired
    UserRepository userRepo;


    @GetMapping(path = "/userStats/{username}")
    public UserStats getUserStats(@PathVariable String username) {
        UserStats userStats = userRepo.findByUsername(username).getStats();

        if (userStats == null) { //if a user doesn't have any stats gives them stats
            User user = userRepo.findByUsername(username);
            userStats = new UserStats(user, username);
            user.setStats(userStats);

            userRepo.save(user);
            statsRepo.save(userStats);
        }

        return userStats;
    }


    @GetMapping(path = "/correctRatio/{username}")
    public int getCorrectRatio(@PathVariable String username) throws Exception {
        User user = userRepo.findByUsername(username);
        int totalCorrect = user.getStats().getTotalCorrect();
        int totalAnswered = user.getStats().getTotalAnswered();

        if (user == null){
            throw new Exception("User not found with username: " + username);
        }

        if(totalAnswered == 0){
            return 0;
        }
        return (totalCorrect/totalAnswered) * 100;

    }

    @GetMapping(path = "/incorrectRatio/{username}")
    public int getIncorrectRatio(@PathVariable String username) throws Exception {
        User user = userRepo.findByUsername(username);
        int totalIncorrect = user.getStats().getTotalIncorrect();
        int totalAnswered = user.getStats().getTotalAnswered();

        if (user == null){
            throw new Exception("User not found with username: " + username);
        }

        if(totalAnswered == 0){
            return 0;
        }
        return (totalIncorrect/totalAnswered) * 100;

    }

    @PutMapping(path = "/{username}/updateStats/{correct}/{incorrect}/{total}")
    public UserStats updateGameStats(@PathVariable int correct, @PathVariable int incorrect,
                                     @PathVariable int total, @PathVariable String username) throws Exception {
       User user = userRepo.findByUsername(username);

        if(user == null){
            throw new Exception("User not found with username: " + username);
        }

       UserStats stats = user.getStats();

       //updating the Question stats
       stats.setTotalAnswered(stats.getTotalAnswered() + total);
       stats.setTotalCorrect(stats.getTotalCorrect() + correct);
       stats.setTotalIncorrect(stats.getTotalIncorrect() + incorrect);


       userRepo.save(user);
       statsRepo.save(stats);
       return user.getStats();
    }

    @PutMapping(path = "/{username}/updateWins")
    public UserStats updateWins(@PathVariable String username) throws Exception {
        User user = userRepo.findByUsername(username);
        if(user == null){
            throw new Exception("User not found with username: " + username);
        }

        UserStats stats = user.getStats();

        stats.setWins(stats.getWins() + 1);
        stats.setWinStreak(stats.getWinStreak() + 1);

        userRepo.save(user);
        statsRepo.save(stats);
        return user.getStats();

    }

    @PutMapping(path = "/{username}/updateLoses")
    public UserStats updateLoses(@PathVariable String username) throws Exception {
        User user = userRepo.findByUsername(username);
        if(user == null){
            throw new Exception("User not found with username: " + username);
        }

            UserStats stats = user.getStats();

        stats.setLosses(stats.getLosses() + 1);
        stats.setWinStreak(0);

        userRepo.save(user);
        statsRepo.save(stats);
        return user.getStats();

    }

    @PutMapping(path = "/{username}/updateGameStats/{gamesPlayed}/{totalAnswered}/{questionsSubmitted}")
    public UserStats updateGameStats(@PathVariable String username,
                                     @PathVariable int gamesPlayed,
                                     @PathVariable int totalAnswered,
                                     @PathVariable int questionsSubmitted) throws Exception {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new Exception("User not found with username: " + username);
        }
        UserStats stats = user.getStats();
        stats.setGamesPlayed(gamesPlayed);
        stats.setTotalAnswered(totalAnswered);
        stats.setQuestionsSubmitted(questionsSubmitted);
        userRepo.save(user);
        statsRepo.save(stats);
        return user.getStats();
    }


}
