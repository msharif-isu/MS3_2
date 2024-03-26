package com.project.trivia.MPQuestions;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

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
	
	
	public Answer() {};
	
	public Answer(String userName, String answer, boolean correct) {
		this.userName = userName;
		this.answer = answer;
        this.correct = correct;
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

    
}
