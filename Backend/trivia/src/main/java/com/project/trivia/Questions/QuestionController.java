package com.project.trivia.Questions;


import org.hibernate.annotations.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.project.trivia.Queryboard.Query;

import java.util.ArrayList;
import java.util.List;
import com.project.trivia.Jeopardy.Jeopardy;

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

    @GetMapping(path = "/question")
    List<Question> getAllQuestion() {
        return questionRepository.findAll();
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

    @GetMapping("/query/topic/{topic}")
    List<Question> getTopics(@PathVariable String topic){
        List<Question> allTopics = questionRepository.findAll();
        allTopics.removeIf(n -> (!n.getQuestionType().equals(topic)));

        return allTopics;
    }

    @GetMapping("/query/userCreated/{userCreated}")
    List<Question> getUserCreated(@PathVariable Boolean userCreated){
        List<Question> allTopics = questionRepository.findAll();
        allTopics.removeIf(n -> (n.getUserCreated() != userCreated));

        return allTopics;
    }

    @GetMapping("/query/multiple/{topic}/{userCreated}")
    List<Question> getMultipleFilters(@PathVariable String topic, @PathVariable Boolean userCreated) {
        List<Question> allTopics = questionRepository.findAll();
        allTopics.removeIf(n -> ((!n.getQuestionType().equals(topic)) && (n.getUserCreated() == userCreated)));
        allTopics.removeIf(n -> ((!n.getQuestionType().equals(topic)) && (n.getUserCreated() != userCreated)));
        return allTopics;
    }

    @GetMapping("/test")
    List<Question> testFunction() {
        List<Question> allTopics = questionRepository.findAll();
        List<Question> small1 = Query.limitList(allTopics, 3);
        List<Question> small2 = Query.limitList(allTopics, 5);

        return Query.joinList(small1, small2);
    }


}