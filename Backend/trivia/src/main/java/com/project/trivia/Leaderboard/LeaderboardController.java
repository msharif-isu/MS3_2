package com.project.trivia.Leaderboard;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LeaderboardController {

    @Autowired
    LeaderboardRepository leaderboardRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/leaderboard")
    List<Leaderboard> getAllLeaderboard() {
        return leaderboardRepository.findAll();
    }

    @GetMapping(path = "/leaderboard/{id}")
    Leaderboard getLeaderboardById(@PathVariable int id){
        return leaderboardRepository.findById(id);
    }

    @PostMapping(path = "/leaderboard/addpoints/{id}/{amount}")
    String addPoints(@PathVariable int id, @PathVariable int amount){
        Leaderboard lb1 = getLeaderboardById(id);
        lb1.setUserPoints(lb1.getUserPoints() + amount);
        lb1.setWeeklyPoints(lb1.getWeeklyPoints() + amount);
        lb1.setMonthlyPoints(lb1.getMonthlyPoints() + amount);
        lb1.setYearlyPoints(lb1.getYearlyPoints() + amount);
        lb1.setLifetimePoints(lb1.getLifetimePoints() + amount);

        leaderboardRepository.save(lb1);
        return success;
    }

    @PutMapping(path = "/leaderboard/{id}/{amount}")
    String changePoints(@PathVariable int id, @PathVariable int amount) {
        Leaderboard lb2 = getLeaderboardById(id);
        lb2.setUserPoints(lb2.getUserPoints() + amount);
        lb2.setWeeklyPoints(lb2.getWeeklyPoints() + amount);
        lb2.setMonthlyPoints(lb2.getMonthlyPoints() + amount);
        lb2.setYearlyPoints(lb2.getYearlyPoints() + amount);
        lb2.setLifetimePoints(lb2.getLifetimePoints() + amount);
        
        leaderboardRepository.save(lb2);
        return success;
    }

}