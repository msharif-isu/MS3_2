package com.project.trivia.Questions;

import com.project.trivia.MPQuestions.Answer;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String question;
    private String answer;
    private String questionType;
    private boolean used;

    @OneToOne
    private Answer ans;

    public Question(String question, String answer, String questionType, boolean used) {
        this.question = question;
        this.answer = answer;
        this.questionType = questionType;
        this.used = used;
    }
    public Question() {};

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


}