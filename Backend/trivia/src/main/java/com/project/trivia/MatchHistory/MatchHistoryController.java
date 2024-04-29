package com.project.trivia.MatchHistory;

import com.project.trivia.User.User;
import com.project.trivia.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MatchHistoryController {

    @Autowired
    MatchHistoryRepository matchHistoryRepo;

    @Autowired
    UserRepository userRepo;


    @PostMapping(path = "/{username}/saveGame")
    public MatchHistory savePastMatch(@PathVariable String username, @RequestBody MatchHistory matchHistory) {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            return null;
        }

        user.getPastMatches().add(matchHistory);
        matchHistory.setUser(user);

        //updates number of games the under played
        user.getStats().setNumberOfFreinds(user.getStats().getGamesPlayed() + 1);

        matchHistoryRepo.save(matchHistory);
        userRepo.save(user);

        return matchHistory;
    }


    @GetMapping(path = "/{username}/matchHistory")
    public List<MatchHistory> getPlayersMatchHistory(@PathVariable String username) {
        return userRepo.findByUsername(username).getPastMatches();
    }


}
