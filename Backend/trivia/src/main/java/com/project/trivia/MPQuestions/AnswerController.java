package com.project.trivia.MPQuestions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AnswerController {

    @Autowired
    AnswerRepository answerRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @GetMapping(path = "/answer/{id}")
    Answer getAnswerById( @PathVariable int id){
        return answerRepository.findById(id);
    }

    @GetMapping(path = "/answer")
    List<Answer> getAllAnswer() {
        return answerRepository.findAll();
    }

    @PostMapping(path = "/answer")
    String createAnswer(@RequestBody Answer answer){
        if (answer == null)
            return failure;
        answerRepository.save(answer);
        return success;
    }

}