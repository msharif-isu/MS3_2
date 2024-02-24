package com.example.androidexample;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SinglePlayerQuestionActivity extends AppCompatActivity {

    private TextView questionTextView, pointsTextView, timeLeftTextView;
    private EditText answerBoxEditText;
    private Button submitButton;

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
        timeLeftTextView.setText("Time Left: 10 minutes");

        // Set OnClickListener for the submit button
        submitButton.setOnClickListener(view -> {
            // Handle submit button click
            String answer = answerBoxEditText.getText().toString();
            // Do something with the answer
        });
    }
}
