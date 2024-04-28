package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.java_websocket.handshake.ServerHandshake;

import java.util.ArrayList;
import java.util.List;

import url.RequestURLs;

public class MultiplayerActivity extends AppCompatActivity implements WebSocketListener {

    private CountDownTimer countDownTimer;
    private TextView timeLeftTextView;
    private long timeLeftInMillis;
    private int userPoints = 0;
    private TextView msgTv;
    TextView pointsTextView;

    //TODO PROPER QUESTION IMPLEMENTAITON FOR MULTIPLAYER
    private List<Integer> questionIds = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private String questionCorrectAnswer = "Joe Biden";
    TextView questionTextView;

    String questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        //get username to connect to websocket
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String username = sharedPreferences.getString("USERNAME", "");
        long roomId = getIntent().getLongExtra("ROOM_ID", -1);
//        String questions = getIntent().getStringExtra("QUESTION_ID");
        SharedPreferences prefs = getSharedPreferences("QuestionIds", Context.MODE_PRIVATE);
        questions = prefs.getString("QuestionIds", "");


        Log.d("QuestionIDs in Multiplayer", "Question ids: " + questions);
//        Toast.makeText(MultiplayerActivity.this, "Welcome " + username, Toast.LENGTH_SHORT).show();

        // Find views by their IDs
        questionTextView = findViewById(R.id.question);
        pointsTextView = findViewById(R.id.points);
        EditText answerEditText = findViewById(R.id.answer_box);
        Button submitButton = findViewById(R.id.submit_button);
        timeLeftTextView = findViewById(R.id.time_left);
        msgTv = findViewById(R.id.friendsList);

        // Set initial time in minutes TODO make customizable?
        int timeInMinutes = 10;
        timeLeftInMillis = timeInMinutes * 60 * 1000;
        startCountDownTimer();


        //Todo add question support from database. Also add point support using database for multiple user support
        questionTextView.setText("Who is the current president of the United States?");
        pointsTextView.setText("Points: " + userPoints);
        answerEditText.setHint("Please Answer Here");
        msgTv.setText("User answers will appear here:\n");

        //automatically connect to websocket based on username.
        String chatUrl = RequestURLs.SERVER_WEBSOCKET_URL_MULTIPLAYER + "/chat/" + roomId + "/";
        //todo if username is blank, give error.
        String serverUrl = chatUrl + username;


        // Establish WebSocket connection and set listener
        WebSocketManager.getInstance().connectWebSocket(serverUrl);
        WebSocketManager.getInstance().setWebSocketListener(MultiplayerActivity.this);
//        if (!questions.equals("")) {
//            WebSocketManager.getInstance().sendMessage("/question " + questions);
//            Log.d("QuestionIDs in Multiplayer", "/question " + questions);
//        }

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
                //TODO make something happen when time is up
                timeLeftInMillis = 0;
                updateCountDownText();
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
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            if (message.startsWith("Question: ")) {
                String question = message.substring("Question: ".length());
                questionTextView.setText(question);
                //} else if (message.equals("Correct!")) {
            } else if (message.equals("Game is now over congrats!") || message.equals("All questions answered!")) {
                Intent intent = new Intent(MultiplayerActivity.this, ResultsActivity.class);
                //TODO fix this, it currently stores in username, when it shoudlnt
                intent.putExtra("USERNAME", "multiplayer");
                startActivity(intent);

            } else {
                msgTv.setText(s + "\n" + message);
            }

        });
    }


    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        // Connection is established, send the /question message
        if (!questions.equals("")) {
            WebSocketManager.getInstance().sendMessage("/question " + questions);
            Log.d("QuestionIDs in Multiplayer", "/question " + questions);
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Close WebSocket connection
        WebSocketManager.getInstance().closeWebSocket();
    }

}
