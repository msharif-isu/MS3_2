package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MultiplayerActivity extends AppCompatActivity implements WebSocketListener {

    private CountDownTimer countDownTimer;
    private TextView timeLeftTextView;
    private long timeLeftInMillis;
    private int userPoints = 0;
    private TextView msgTv;
    TextView pointsTextView;

    //TODO PROPER QUESTION IMPLEMENTAITON FOR MULTIPLAYER
    private List<Integer> questionIds = new ArrayList<>();
    private String backendUrl = "http://10.0.2.2:8081/";
    private int currentQuestionIndex = 0;
    private String questionCorrectAnswer = "Joe Biden";
    TextView questionTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        //get username to connect to websocket
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String username = sharedPreferences.getString("USERNAME", "");
//        Log.d("MainActivity", "Username from SharedPreferences: " + username);
//        Toast.makeText(MultiplayerActivity.this, "Welcome " + username, Toast.LENGTH_SHORT).show();

        // Find views by their IDs
        questionTextView = findViewById(R.id.question);
        pointsTextView = findViewById(R.id.points);
        EditText answerEditText = findViewById(R.id.answer_box);
        Button submitButton = findViewById(R.id.submit_button);
        timeLeftTextView = findViewById(R.id.time_left);
        msgTv = findViewById(R.id.tx1);

        // Set initial time in minutes
        int timeInMinutes = 10;
        timeLeftInMillis = timeInMinutes * 60 * 1000;
        startCountDownTimer();

        fetchQuestionIds();


        //Todo add question support from database. Also add point support using database for multiple user support
        questionTextView.setText("Who is the current president of the United States?");
        pointsTextView.setText("Points: " + userPoints);
        answerEditText.setHint("Please Answer Here");
        msgTv.setText("User answers will appear here:\n");

        //automatically connect to websocket based on username.
        String chatUrl = "ws://10.0.2.2:8080/chat/";
        //todo if username is blank, give error.
        String serverUrl = chatUrl + username;


        // Establish WebSocket connection and set listener
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(MultiplayerActivity.this);

        submitButton.setOnClickListener(view -> {
            try {
                // send message
                WebSocketManager.getInstance().sendMessage(answerEditText.getText().toString());
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
            }
            answerEditText.setText("");
        });
    }

    private void startCountDownTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                // Perform any action when the timer finishes
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        timeLeftTextView.setText("Time Left: " + timeLeftFormatted);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {

    }

    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            Spannable spannable = new SpannableString(s + "\n" + message);

            // Split the message into username and answer
            String[] parts = message.split(":");
            if (parts.length == 2) {
                String username = parts[0];
                String answer = parts[1].trim(); // Trim to remove extra spaces

                // Check if the answer is correct
                if (answer.equalsIgnoreCase(questionCorrectAnswer)) {
                    userPoints += 100;
                    pointsTextView.setText("Points: " + userPoints);

                    // Change the color of the correct answer message
                    SpannableStringBuilder builder = new SpannableStringBuilder(username + " got the correct answer!");
                    builder.setSpan(new ForegroundColorSpan(0xFF00FF00), 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    msgTv.append("\n");
                    msgTv.append(builder);
                } else {
                    msgTv.setText(s + "\n" + message);
                }
            }
        });
    }


    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    @Override
    public void onWebSocketError(Exception ex) {

    }


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
                        Toast.makeText(MultiplayerActivity.this, "Failed to fetch question IDs", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonArrayRequest);
    }

    private void showNextQuestion() {
        //answerBoxEditText.setText("");
        int questionId = questionIds.get(currentQuestionIndex);
        fetchQuestionDetails(questionId);
    }

    private void fetchQuestionDetails(int questionId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = backendUrl + "pelican/" + questionId;
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
                        Toast.makeText(MultiplayerActivity.this, "Failed to fetch question details", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }

}
