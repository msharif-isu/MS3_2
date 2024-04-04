package com.example.androidexample;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import url.RequestURLs;

public class JeopardyActivity extends AppCompatActivity {

    private List<Button> questionButtons;
    private List<Question> questions;
    private TextView pointsText;

    private String username;
    private int userID;
    private int points;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeopardy);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        username = prefs.getString("USERNAME","");
        userID = prefs.getInt("USER_ID", 0);

        questionButtons = new ArrayList<>();
        questions = new ArrayList<>();

        points = 0;
        pointsText = findViewById(R.id.jeopardy_point_total_text);

        pointsText.setText("Total Points: " + points);

        Button questionButton00 = findViewById(R.id.jeopardy_button_00);
        Button questionButton10 = findViewById(R.id.jeopardy_button_10);
        Button questionButton20 = findViewById(R.id.jeopardy_button_20);
        Button questionButton01 = findViewById(R.id.jeopardy_button_01);
        Button questionButton11 = findViewById(R.id.jeopardy_button_11);
        Button questionButton21 = findViewById(R.id.jeopardy_button_21);
        Button questionButton02 = findViewById(R.id.jeopardy_button_02);
        Button questionButton12 = findViewById(R.id.jeopardy_button_12);
        Button questionButton22 = findViewById(R.id.jeopardy_button_22);

        questionButtons.add(questionButton00);
        questionButtons.add(questionButton10);
        questionButtons.add(questionButton20);
        questionButtons.add(questionButton01);
        questionButtons.add(questionButton11);
        questionButtons.add(questionButton21);
        questionButtons.add(questionButton02);
        questionButtons.add(questionButton12);
        questionButtons.add(questionButton22);

        Question placeholder = new Question(99, "99?", "69", "Number", false, false);
        // If no questions are loaded yet, show this message
        questionButtons.forEach(q -> {
            q.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "Question hasn't loaded", Toast.LENGTH_SHORT).show();
                    questionDialog(q, placeholder);
                }
            });
        });

        requestJeopardyQuestions();
    }

    /**
     * Gives the category buttons their corresponding questions
     */
    private void requestJeopardyQuestions() {
        JsonArrayRequest questionRequest = new JsonArrayRequest(
                Request.Method.GET,
                RequestURLs.SERVER_HTTP_QUESTION_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        questions.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                questions.add(new Question(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        Collections.shuffle(questions);

                        for (int i = 0; i < questionButtons.size(); i++) {
                            Button b = questionButtons.get(i);
                            Question q = questions.get(i);
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    questionDialog(b, q);
                                    b.setEnabled(false);
                                }
                            });
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(questionRequest);
    }

    /**
     * Creates a popup dialog to allow users to answer a question
     * @param question
     */
    private void questionDialog(Button button, Question question) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.jeopardy_question_dialog);
        dialog.show();

        TextView questionPoints = dialog.findViewById(R.id.jeopardy_dialog_point_worth);
        TextView questionDisplay = dialog.findViewById(R.id.jeopardy_dialog_question);
        EditText answerText = dialog.findViewById(R.id.jeopardy_dialog_answer);
        Button submitButton = dialog.findViewById(R.id.jeopardy_dialog_submit_button);

        questionPoints.setText(button.getText());
        questionDisplay.setText(question.getQuestion());
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = answerText.getText().toString();
                if (!answer.trim().isEmpty()) {
                    saveUserAnswer(question.getId(), answer);
                    checkAnswer(button, dialog, question.getId(), answer);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter an answer", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Saves the current user's answer to the given question
     * @param questionID
     * @param givenAnswer
     */
    private void saveUserAnswer(int questionID, String givenAnswer) {
        //TODO make post request
    }

    /**
     * Checks to see if an answer to a question is correct
     * @param dialog
     * @param questionID
     * @param givenAnswer
     */
    private void checkAnswer(Button button, Dialog dialog, int questionID, String givenAnswer) {
        JsonArrayRequest answerRequest = new JsonArrayRequest(
                Request.Method.GET,
                String.format("%s/%d", RequestURLs.SERVER_HTTP_ANSWER_CHECKER_URL, questionID),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                String correctAnswer = response.getJSONObject(i).getString("answer");
                                if (givenAnswer.equals(correctAnswer)) {
                                    int pointsToAdd = Integer.parseInt(button.getText().toString());
                                    points += pointsToAdd;
                                    pointsText.setText("Total Points: " + points);
                                    addUserPoints(pointsToAdd);
                                    break;
                                }


                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(answerRequest);
    }

    /**
     * Adds the given points to the current user
     * @param pointsToAdd
     */
    private void addUserPoints(int pointsToAdd) {
        JsonObjectRequest addUserPointsRequest = new JsonObjectRequest(
                Request.Method.PUT,
                String.format("%s/%d/%d", RequestURLs.SERVER_HTTP_USER_ADD_POINTS_URL, username, pointsToAdd),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JeopardyActivity", "Successfully added user points");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("JeopardyActivity", "Failed to add user points");
                    }
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(addUserPointsRequest);
    }
}
