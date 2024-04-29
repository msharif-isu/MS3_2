package com.project.trivia.Achievements;

import com.project.trivia.PelicanQuestions.PelicanQuestion;
import com.project.trivia.Questions.Question;
import com.project.trivia.Questions.QuestionRepository;
import com.project.trivia.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AchievementController {

    @Autowired
    AchievementRepository achievementRepository;

    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/achievement")
    List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }

    @GetMapping(path = "/achievement/{id}")
    Achievement getAchievementById(@PathVariable int id) {
        if (achievementRepository.findById(id) == null)
            return null;
        return achievementRepository.findById(id);
    }

    @PostMapping(path = "/achievement")
    Achievement addAchievement(@RequestBody Achievement achievement){
        if (achievement == null)
            return null;
        achievementRepository.save(achievement);
        return achievement;
    }

    @PutMapping(path = "/achievement/{id}")
    Achievement changeAchievement(@RequestBody Achievement achievement, @PathVariable int id) {
        Achievement ach = achievementRepository.findById(id);
        if (ach == null)
            return null;
        ach.setUser(achievement.getUsername());
        ach.setAccount(achievement.isAccount());
        ach.setSinglePlayer(achievement.isSinglePlayer());
        ach.setJeopardy(achievement.isJeopardy());
        ach.setMultiPlayer(achievement.isMultiPlayer());
        achievementRepository.save(ach);
        return achievementRepository.findById(id);
    }

    @DeleteMapping(path = "/achievement/{id}")
    String removeAchievement(@PathVariable int id) {
        if (achievementRepository.findById(id) == null)
            return failure;
        achievementRepository.deleteById(id);
        return success;
    }
}