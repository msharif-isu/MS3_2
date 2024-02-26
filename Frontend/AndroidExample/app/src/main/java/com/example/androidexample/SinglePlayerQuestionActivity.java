package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

public class SinglePlayerQuestionActivity extends AppCompatActivity {

    private TextView questionTextView, pointsTextView, timeLeftTextView;
    private EditText answerBoxEditText;
    private Button submitButton;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_singleplayer);

        // Initialize views
        questionTextView = findViewById(R.id.question);
        pointsTextView = findViewById(R.id.points);
        timeLeftTextView = findViewById(R.id.time_left);
        answerBoxEditText = findViewById(R.id.answer_box);
        submitButton = findViewById(R.id.submit_button);

        // Set initial text
        questionTextView.setText("Your question goes here");
        pointsTextView.setText("Points: 0");

        // Set initial time to 30 seconds
        timeLeftInMillis = 30 * 1000; // 30 seconds
        updateCountDownText();

        // Start the countdown timer
        startCountDown();

        // Set OnClickListener for the submit button
        submitButton.setOnClickListener(view -> {
            // Handle submit button click
            String answer = answerBoxEditText.getText().toString();
            // Do something with the answer
        });
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "Out of time.", Toast.LENGTH_LONG).show();
                // Switch activities when timer finishes
                startActivity(new Intent(SinglePlayerQuestionActivity.this, MainActivity.class));
                finish();
            }
        }.start();
    }

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
}