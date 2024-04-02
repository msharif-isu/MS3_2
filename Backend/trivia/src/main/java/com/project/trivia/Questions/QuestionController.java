package com.project.trivia.Questions;

import org.hibernate.annotations.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        for (int i=1; i<allTopics.size(); i++) {
            if(!allTopics.get(i).getQuestionType().equals(topic)) {
                allTopics.remove(i);
            }
        }
        return allTopics;
    }

    @GetMapping("/query/userCreated/{userCreated}")
    List<Question> getUserCreated(@PathVariable Boolean userCreated){
        List<Question> allTopics = questionRepository.findAll();
        ArrayList<Question> temp = new ArrayList<Question>();
        ArrayList<Boolean> tempVal = new ArrayList<Boolean>();
        int[] toBeRemoved = new int[allTopics.size()];
        int count = 0;

        for (int i=1; i<allTopics.size(); i++) {
            if(allTopics.get(i).getUserCreated() == userCreated) {
                toBeRemoved[i] = i;
                count++;
            }
        }
        int j=0;
        while (j<count) {
            allTopics.remove(toBeRemoved[j]);
        }
        return temp;
    }


}