package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SinglePlayerQuestionActivity extends AppCompatActivity {

    private TextView questionTextView, pointsTextView, timeLeftTextView, usernameTextView;
    private EditText answerBoxEditText;
    private Button submitButton;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;

    private int usernameId;

    //this exists because the server decided to stop working :)
    private String backendUrl = "http://10.0.2.2:8080/";
    private String questionCorrectAnswer;

    private String username;
    private List<Integer> questionIds = new ArrayList<>();
    private static final long COUNTDOWN_TIME = 60000; // 60 seconds

    protected void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_singleplayer);

        // Initialize views
        usernameTextView = findViewById(R.id.usernametext);
        questionTextView = findViewById(R.id.question);
        pointsTextView = findViewById(R.id.points);
        timeLeftTextView = findViewById(R.id.time_left);
        answerBoxEditText = findViewById(R.id.answer_box);
        submitButton = findViewById(R.id.submit_button);

        pointsTextView.setText("Points: 0"); // TODO: this will be updated to pull points based on username

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("USERNAME");
            usernameId = extras.getInt("USERID");
            usernameTextView.setText(username); // this will come from LoginActivity
        } else {
            usernameTextView.setText("Not logged in");
        }

        // Fetch question IDs from the backend
        fetchQuestionIds();

        // Start countdown timer
        startCountDown();

        // Set OnClickListener for the submit button
        submitButton.setOnClickListener(view -> {
            String userAnswer = answerBoxEditText.getText().toString();
            handleSubmitButtonClick();
            if (currentQuestionIndex < questionIds.size()) {
                showNextQuestion();
            } else {
                // Display result and handle end of quiz
                displayResult();
            }
        });
    }

    private void handleSubmitButtonClick() {
        String userAnswer = answerBoxEditText.getText().toString();
        // Check if the user's answer is correct
        if (userAnswer.equalsIgnoreCase(questionCorrectAnswer)) {
            correctAnswers++;
            Toast.makeText(SinglePlayerQuestionActivity.this, "Correct Answer", Toast.LENGTH_SHORT).show();
            updatePoints();
        }
        // if it isn't correct...
        else {
            Toast.makeText(SinglePlayerQuestionActivity.this, "Incorrect Answer", Toast.LENGTH_SHORT).show();
        }
        currentQuestionIndex++;
        if (currentQuestionIndex < questionIds.size()) {
            showNextQuestion();
        } else {
            // Display result and handle end of quiz
            displayResult();
        }
    }

    // Method to fetch question IDs from the backend
    private void fetchQuestionIds() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = backendUrl + "getPuestions";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                questionIds.add(response.getInt(i));
                            }
                            showNextQuestion();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(SinglePlayerQuestionActivity.this, "Failed to fetch question IDs", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        queue.add(jsonArrayRequest);
    }

    // Method to fetch question details by ID from the backend
    private void fetchQuestionDetails(int questionId) {
        // Initialize Volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = backendUrl + "pelican/" + questionId; // Replace with  backend URL
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the JSON response to get question details
                            String question = response.getString("question");
                            questionCorrectAnswer = response.getString("answer");
                            // Display the question
                            questionTextView.setText(question);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(SinglePlayerQuestionActivity.this, "Failed to fetch question details", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        queue.add(jsonObjectRequest);
    }

    /**
     * Method to display the next question. Also clears the textbox.
     */
    private void showNextQuestion() {
        answerBoxEditText.setText("");
        int questionId = questionIds.get(currentQuestionIndex);
        fetchQuestionDetails(questionId);
    }

    /**
     * Method to display result and handle end of quiz
     */
    private void displayResult() {
        // Display result
        Toast.makeText(getApplicationContext(), "Quiz Finished! Correct Answers: " + correctAnswers, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SinglePlayerQuestionActivity.this, ResultsActivity.class);
        intent.putExtra("USERNAME", username);
        intent.putExtra("USERID", usernameId);
        intent.putExtra("POINTS", correctAnswers * 100);
        intent.putExtra("CORRECTANSWERS", correctAnswers);
        startActivity(intent);
    }

    // Method to start countdown timer
    private void startCountDown() {
        countDownTimer = new CountDownTimer(COUNTDOWN_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                // Display a message or handle end of quiz when timer finishes
                Toast.makeText(getApplicationContext(), "Time's up!", Toast.LENGTH_SHORT).show();
                displayResult();
            }
        }.start();
    }

    // Method to update countdown text
    private void updateCountDownText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        String timeLeftFormatted = String.format("%02d", seconds);
        timeLeftTextView.setText("Time Left: " + timeLeftFormatted);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    /**
     * Updates points, as well as question activity for the leaderboard.
     */
    private void updatePoints() {
        sendCorrectAnswer(questionIds.get(currentQuestionIndex), username);
        int totalPoints = correctAnswers * 100;
        pointsTextView.setText("Points: " + totalPoints);
    }

    /**
     * Sends the user based on the question that the user got correct.
     * @param id question id
     * @param username current user's username
     */
    private void sendCorrectAnswer(int id, String username) {
        String url = backendUrl + "correct/" + id + "/" + username;
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("id", id);
            requestData.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Create a request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle response if needed
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.e("Error", "Error occurred: " + error.getMessage());
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}