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

    @PostMapping(path = "/addpoints/{id}/{amount}")
    String addPoints(@PathVariable int id, @PathVariable int amount){
        Leaderboard lb = getLeaderboardById(id);
        lb.setUserPoints(lb.getUserPoints() + amount);
        lb.setWeeklyPoints(lb.getWeeklyPoints() + amount);
        lb.setMonthlyPoints(lb.getMonthlyPoints() + amount);
        lb.setYearlyPoints(lb.getYearlyPoints() + amount);
        lb.setLifetimePoints(lb.getLifetimePoints() + amount);

        leaderboardRepository.save(lb);
        return success;
    }

}