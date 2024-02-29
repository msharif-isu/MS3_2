package com.project.trivia.Questions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuestionController {

    @Autowired
    QuestionRepository questionRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


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
        question.setQuestion(request.getQuestion());
        question.setAnswer(request.getAnswer());
        question.setQuestionType(request.getQuestionType());
        return questionRepository.findById(id);
    }
}