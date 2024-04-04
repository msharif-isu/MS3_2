package com.example.androidexample;

/**
 * A class used to store the data of every user question
 */
public class UserQuestionListItem {
    private int id;
    private String answer;
    private String question;
    private String questionType;

    /**
     * Constructs a container to store question data
     * @param id - id of question
     * @param question - question
     * @param answer - answer to question
     * @param questionType - user-made category of question
     */
    public UserQuestionListItem(int id, String question, String answer, String questionType) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.questionType = questionType;
    }

    /**
     * Returns the id of this question
     * @return id of this question
     */
    public int getId() {
        return id;
    }

    /**
     * Returns this question
     * @return question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Returns the answer to this question
     * @return answer - answer to this question
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Returns the questionType of this question
     * @return questionType - user-made category of question
     */
    public String getQuestionType() {
        return questionType;
    }
}
