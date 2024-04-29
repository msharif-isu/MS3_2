package com.project.trivia.MPQuestions;

import com.project.trivia.Questions.Question;
import com.project.trivia.Questions.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AnswerController {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

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
    Answer createAnswer(@RequestBody Answer answer){
        answerRepository.save(answer);
        return answer;
    }

    @PutMapping(path = "/answer/{id}")
    String createAnswer(@PathVariable int id, @RequestBody Answer answer){
        Answer newAnswer = answerRepository.findById(id);
        newAnswer.setQuestion(answer.getQuestion());
        newAnswer.setUserName(answer.getUserName());
        newAnswer.setAnswer(answer.getAnswer());

        answerRepository.save(newAnswer);
        return success;
    }

    @PostMapping(path = "/answer/create/add/{quest_id}")
    Answer createAnswerToQuestion(@RequestBody Answer answer, @PathVariable int quest_id) {
        if (answer == null)
            return null;
        Question question = questionRepository.findById(quest_id);
        question.addAnswer(answer);
        questionRepository.save(question);
        answerRepository.save(answer);

        return answer;
    }



}