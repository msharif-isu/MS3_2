package com.project.trivia.Questions;

import com.project.trivia.MPQuestions.Answer;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;

import java.util.ArrayList;
import java.util.List;

@Entity
@DynamicUpdate
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String question;
    private String answer;
    private String questionType;
    private boolean used;

    private boolean userCreated;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private List<Answer> answerList;


    public Question(String question, String answer, String questionType, boolean used, boolean userCreated) {
        this.question = question;
        this.answer = answer;
        this.questionType = questionType;
        this.used = used;
        this.userCreated = userCreated;
        answerList = new ArrayList<>();
    }
    public Question() {answerList = new ArrayList<>();};

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public boolean getUsed() {return used;}

    public void setUsed(boolean used) {this.used = used;}

    public List<Answer> getAnswers(){return answerList;}
    public void addAnswer(Answer ans) {
        answerList.add(ans);
        //answerList.add(ans);
        ans.setQuestion(this);
    }
    public void setAnswerList(List<Answer> answerList) {this.answerList = answerList;}

    public void removeAnswer(Answer ans) {
        answerList.remove(ans);
        ans.setQuestion(null);
    }

    public boolean getUserCreated() {return userCreated;}

    public void setUserCreated(boolean userCreated) {this.userCreated = userCreated;}




}