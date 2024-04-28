package com.example.androidexample;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A class used to store the data of every question
 */
public class Question {
    private int id;
    private String question;
    private String answer;
    private String questionType;
    private boolean used;
    private boolean userCreated;

    /**
     * Constructs a container to store question data
     * @param id - id of question
     * @param question - question
     * @param answer - answer to question
     * @param questionType - category of question
     * @param used - if this question has been used
     * @param userCreated - if this question is user created
     */
    public Question(int id, String question, String answer, String questionType, boolean used, boolean userCreated) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.questionType = questionType;
        this.used = used;
        this.userCreated = userCreated;
    }

    /**
     * Constructs a container to store question data from the appropriate <code>JSONObject</code>
     * @param jsonBody - <code>JSONObject</code>
     */
    public Question(JSONObject jsonBody) throws JSONException {
        this(
                jsonBody.getInt("id"),
                jsonBody.getString("question"),
                jsonBody.getString("answer"),
                jsonBody.getString("questionType"),
                jsonBody.getBoolean("used"),
                false
//                jsonBody.getBoolean("userCreated")
        );
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
     * @return questionType - category of question
     */
    public String getQuestionType() {
        return questionType;
    }


    /**
     * Returns if this question has been used before
     * @return used - if this question has been used
     */
    public boolean isUsed() {
        return used;
    }

    /**
     * Returns if this question is user-created
     * @return userCreated - if this question is user created
     */
    public boolean isUserCreated() {
        return userCreated;
    }


    /**
     * Returns the appropriate request body to send this <code>Question</code> to the server
     * @return jsonBody - <code>JSONObject</code>
     * @throws JSONException
     */
    public JSONObject toJSON() throws JSONException {
        return new JSONObject() {{
            put("question", question);
            put("answer", answer);
            put("questionType", questionType);
            put("used", used);
            put("userCreated", userCreated);
        }};
    }
}
