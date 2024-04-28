package com.project.trivia.MatchHistory;

import com.project.trivia.User.User;
import com.project.trivia.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MatchHistoryController {

    @Autowired
    MatchHistoryRepository matchHistoryRepo;

    @Autowired
    UserRepository userRepo;


    @PostMapping(path = "/{username}/saveGame/{placement}/{questionSet}/{points}")
    public MatchHistory savePastMatch(@PathVariable String username, @PathVariable String placement,
                                           @PathVariable int points, @PathVariable String questionSet) {
        User user = userRepo.findByUsername(username);
        MatchHistory matchHistory = new MatchHistory(placement, questionSet, points, username, user);

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
