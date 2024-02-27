package com.project.trivia.PelicanQuestions;

import com.project.trivia.Questions.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PelicanQuestionController {

    @Autowired
    PelicanQuestionRepository pelicanQuestionRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @GetMapping(path = "/pelican")
    List<PelicanQuestions> getAllUser() {
        return pelicanQuestionRepository.findAll();
    }

    @GetMapping(path = "/pQuestion/{id}")
    PelicanQuestions getQuestionById(@PathVariable int id){
        return pelicanQuestionRepository.findById(id);
    }

    @PostMapping(path = "/pQuestion")
    String createQuestion(@RequestBody PelicanQuestions question){
        if (question == null)
            return failure;
        pelicanQuestionRepository.save(question);
        return success;
    }

    @DeleteMapping(path = "/users/{id}")
    public String deleteUser(@PathVariable int id){
        pelicanQuestionRepository.deleteById(id);
        return success;
    }

}
