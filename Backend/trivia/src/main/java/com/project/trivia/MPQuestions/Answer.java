package com.project.trivia.MPQuestions;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.trivia.Questions.Question;
import com.project.trivia.User.User;
import jakarta.persistence.*;

import lombok.Data;

@Entity
public class Answer {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String userName;

    @Column
    private boolean correct;

    @Lob
    private String answer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent")
    private Date sent = new Date();

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

	public Answer() {};
	
	public Answer(String userName, String answer, boolean correct) {
		this.userName = userName;
		this.answer = answer;
        this.correct = correct;
        //this.question = question;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean getCorrect() {
        return correct;
    }
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public Question getQuestion() {return question;}

    public void setQuestion(Question question) {this.question = question;}

    
}
