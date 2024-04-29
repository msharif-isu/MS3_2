package com.project.trivia.Questions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.trivia.MPQuestions.Answer;
import com.project.trivia.User.User;
import com.project.trivia.Multiplayer.Multiplayer;
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

    @ManyToMany
    @JoinTable(
            name = "multiplayer_questions",
            joinColumns = @JoinColumn(name = "multiplayer_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    @JsonIgnore
    private List<Multiplayer> multiplayer;


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

    @Override
    public boolean equals(Object o) {
        if (o == null) {return false;}
        if (o.getClass() != this.getClass()) {return false;}
        final Question other = (Question) o;
        if ((this.question == null) ? (other.question != null) : !this.question.equals(other.question)) {return false;}
        if ((this.answer == null) ? (other.answer != null) : !this.answer.equals(other.answer)) {return false;}
        return true;
    }

    public List<Multiplayer> getMultiplayer() {
        return multiplayer;
    }

    public void setMultiplayer(List<Multiplayer> multiplayer) {
        this.multiplayer = multiplayer;
    }

    public void addMultiplayer(Multiplayer multiplayer) {
        this.multiplayer.add(multiplayer);
    }


}