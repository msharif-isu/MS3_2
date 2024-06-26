package com.project.trivia.Leaderboard;
import java.util.List;

import com.project.trivia.Questions.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

        //Send a get request to websocket server to update leaderboard clients
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForEntity("http://localhost:8080/leaderboard/update", String.class);

        return success;
    }

    @PostMapping(path = "/leaderboard")
    Leaderboard createLeaderboardUser(@RequestBody Leaderboard lb){
        leaderboardRepository.save(lb);
        return lb;
    }

    @PutMapping(path = "/leaderboard/{id}")
    Leaderboard changePoints(@PathVariable int id, @RequestBody Leaderboard request) {
        Leaderboard lb = leaderboardRepository.findById(id);
        if (lb == null)
            return null;
        lb.setName(request.getName());
        lb.setUserPoints(request.getUserPoints());
        lb.setWeeklyPoints(request.getWeeklyPoints());
        lb.setMonthlyPoints(request.getMonthlyPoints());
        lb.setYearlyPoints(request.getYearlyPoints());
        lb.setLifetimePoints(request.getLifetimePoints());
        leaderboardRepository.save(lb);
        return leaderboardRepository.findById(id);
    }

}