package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.handshake.ServerHandshake;

public class MultiplayerActivity extends AppCompatActivity implements WebSocketListener{

    private String BASE_URL = "ws://10.0.2.2:8080/chat/";
    private CountDownTimer countDownTimer;
    private TextView timeLeftTextView;
    private long timeLeftInMillis;

    private TextView msgTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        //get username to connect to websocket
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        String username = sharedPreferences.getString("USERNAME", "");
        Log.d("MainActivity", "Username from SharedPreferences: " + username);
        Toast.makeText(MultiplayerActivity.this, "Welcome " + username, Toast.LENGTH_SHORT).show();

        // Find views by their IDs
        TextView questionTextView = findViewById(R.id.question);
        TextView pointsTextView = findViewById(R.id.points);
        EditText answerEditText = findViewById(R.id.answer_box);
        Button submitButton = findViewById(R.id.submit_button);
        timeLeftTextView = findViewById(R.id.time_left);
        msgTv = findViewById(R.id.tx1);

        // Set initial time in minutes
        int timeInMinutes = 1;
        timeLeftInMillis = timeInMinutes * 60 * 1000; // 10 minutes
        startCountDownTimer();

        questionTextView.setText("Your question goes here");
        pointsTextView.setText("Points: 0");
        answerEditText.setHint("Please Answer Here");
        submitButton.setOnClickListener(view -> {
            // Handle submit button click
        });
        msgTv.setText("User answers will appear here:\n");

        //automatically connect to websocket based on username.
        String serverUrl = BASE_URL + username;
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
            msgTv.setText(s + "\n" + message);
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
}
