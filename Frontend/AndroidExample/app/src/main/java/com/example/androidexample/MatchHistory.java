package com.example.androidexample;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A class that represents each match in a user's match history
 */
public class MatchHistory {
    private String placement;
    private String questionPlayedType;
    private int pointsEarned;
    private String username;

    /**
     * Constructs a container to store match history data
     *
     * @param placement           - the placement that the user came in
     * @param questionPlayedType - the type of questions used in the match
     * @param pointsEarned        - the amount of points the user earned in the match
     * @param username            - the username of the user
     */
    public MatchHistory(String placement, String questionPlayedType, int pointsEarned, String username) {
        this.placement = placement;
        this.questionPlayedType = questionPlayedType;
        this.pointsEarned = pointsEarned;
        this.username = username;
    }

    /**
     * Constructs a container to store match history data from the appropriate <code>JSONObject</code>
     *
     * @param jsonBody - <code>JSONObject</code>
     */
    public MatchHistory(JSONObject jsonBody) throws JSONException {
        this(
                jsonBody.getString("placement"),
                jsonBody.getString("questionPlayedType"),
                jsonBody.getInt("pointsEarned"),
                jsonBody.getString("username")
        );
    }

    /**
     * Returns the points earned in this match
     *
     * @return points earned
     */
    public int getPointsEarned() {
        return pointsEarned;
    }


    /**
     * Returns the type of questions played in this match
     *
     * @return question type
     */
    public String getQuestionSet() {
        return questionPlayedType;
    }


    /**
     * Returns the placement achieved in this match
     * @return match placement/standing
     */
    public String getPlacement() {
        return placement;
    }

    /**
     * Returns the username of the user who was in this match
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the appropriate request body to send this <code>MatchHistory</code> to the server
     * @return jsonBody - <code>JSONObject</code>
     * @throws JSONException
     */
    public JSONObject toJSON() throws JSONException {

        return new JSONObject() {{
            put("placement", placement);
            put("questionPlayedType", questionPlayedType);
            put("pointsEarned", pointsEarned);
            put("username", username);
        }};
    }
}
