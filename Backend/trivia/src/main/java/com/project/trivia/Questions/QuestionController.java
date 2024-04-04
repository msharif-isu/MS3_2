package com.project.trivia.Questions;

import com.project.trivia.MPQuestions.Answer;
import com.project.trivia.MPQuestions.AnswerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class QuestionController {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

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

    @GetMapping("/question/answers/{id}")
    List<Answer> getAnswer(@PathVariable int id) {
        Question question = questionRepository.findById(id);
        return question.getAnswers();
    }


    @PostMapping(path = "/answer/add/{quest_id}")
    Answer createAnswerToQuestion(@RequestBody Answer answer, @PathVariable int quest_id) {
        if (answer == null)
            return null;
        Question question = questionRepository.findById(quest_id);
        question.addAnswer(answer);
        questionRepository.save(question);

        return answer;
    }

    @PutMapping(path = "/answer/addQuestion/{quest_id}/{ans_id}")
    Question createAnswerWithID(@PathVariable int quest_id, @PathVariable int ans_id) {
        Question question = questionRepository.findById(quest_id);
        Answer ans = answerRepository.findById(ans_id);
        question.addAnswer(ans);
        questionRepository.save(question);

        return question;
    }

}