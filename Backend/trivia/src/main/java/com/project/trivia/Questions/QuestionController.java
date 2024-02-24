package com.project.trivia.Questions;
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
public class QuestionController {

    @Autowired
    QuestionRepository questionRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/question")
    List<Question> getAllQuestion() {
        return questionRepository.findAll();
    }

    @GetMapping(path = "/question/{id}")
    Question getQuestionById( @PathVariable int id){
        return questionRepository.findById(id);
    }

    @PostMapping(path = "/question")
    String createQuestion(@RequestBody Question question){
        if (question == null)
            return failure;
        questionRepository.save(question);
        return success;
    }

    @PutMapping("/question/{id}")
    Question updateQuestion(@PathVariable int id, @RequestBody Question request){
        Question question = questionRepository.findById(id);
        if(question == null)
            return null;
        questionRepository.save(request);
        return questionRepository.findById(id);
    }
}