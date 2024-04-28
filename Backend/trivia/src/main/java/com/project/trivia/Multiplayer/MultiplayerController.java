package com.project.trivia.Multiplayer;

import com.project.trivia.Lobby.Lobby;
import com.project.trivia.Lobby.LobbyRepository;
import com.project.trivia.Queryboard.Query;
import com.project.trivia.Questions.Question;
import com.project.trivia.Questions.QuestionRepository;
import com.project.trivia.User.User;
import com.project.trivia.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MultiplayerController {

    @Autowired
    LobbyRepository lobbyRepo;
    @Autowired
    MultiplayerRepository multiplayerRepository;
    @Autowired
    QuestionRepository questionRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/multiplayer")
    public List<Multiplayer> getMultiplayer() {
        return multiplayerRepository.findAll();
    }

    @GetMapping(path = "/multiplayer/{id}")
    public Multiplayer getMultiplayer(@PathVariable int id) {
        return multiplayerRepository.findById(id);
    }

    @PostMapping(path = "/multiplayer/{lobbyId}/{topic}/{limit}")
    public List<Integer> createMultiplayer(@PathVariable int lobbyId, @PathVariable String topic, @PathVariable int limit) {
        Lobby lobby = lobbyRepo.findById(lobbyId);
        Multiplayer mp = new Multiplayer(lobby);
        List<Question> allQuestions = questionRepository.findAll();
        Query.topicFilter(allQuestions, topic);
        Collections.shuffle(allQuestions);
        List<Question> limitedQuestions = allQuestions.stream()
                .limit(limit)
                .collect(Collectors.toList());
        for (Question question : limitedQuestions) {
            mp.addQuestion(question);
        }
        multiplayerRepository.save(mp);
        List<Integer> questionIds = limitedQuestions.stream()
                .map(Question::getId)
                .collect(Collectors.toList());
        return questionIds;
    }


    @DeleteMapping(path = "/multiplayer/{id}")
    public String deleteMultiplayer(@PathVariable int id) {
        multiplayerRepository.deleteById(id);
        return success;
    }

    @PutMapping(path = "/multiplayer/{id}/{lobbyId}/{topic}/{limit}")
    public Multiplayer changeMultiplayer(@PathVariable int id, @PathVariable int lobbyId, @PathVariable String topic, @PathVariable int limit) {
        Lobby lobby = lobbyRepo.findById(lobbyId);
        Multiplayer mp = multiplayerRepository.findById(id);

        mp.setQuestion(questionRepository.findAll());
        Query.topicFilter(mp.getQuestion(), topic);
        Query.limitList(mp.getQuestion(), limit);
        for (int i = 0; i < mp.getQuestion().size(); i++) {
            Question temp = mp.getQuestion().get(i);
            temp.addMultiplayer(mp);
            questionRepository.save(temp);
        }

        lobbyRepo.save(lobby);
        multiplayerRepository.save(mp);

        return mp;
    }

}
