package com.project.trivia.Questions;

import com.project.trivia.MPQuestions.Answer;
import com.project.trivia.MPQuestions.AnswerRepository;
import com.project.trivia.Queryboard.Query;
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