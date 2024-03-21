package com.example.androidexample;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

public class MultiplayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        // Find views by their IDs
        TextView questionTextView = findViewById(R.id.question);
        TextView pointsTextView = findViewById(R.id.points);
        EditText answerEditText = findViewById(R.id.answer_box);
        Button submitButton = findViewById(R.id.submit_button);
        TextView timeLeftTextView = findViewById(R.id.time_left);
        TextView userAnswersTextView = findViewById(R.id.tx1);

        // Now you can set listeners or manipulate these views as needed
        // For example:
        questionTextView.setText("Your question goes here");
        pointsTextView.setText("Points: 0");
        answerEditText.setHint("Please Answer Here");
        submitButton.setOnClickListener(view -> {
            // Handle submit button click
        });
        timeLeftTextView.setText("Time Left: 10 minutes");
        userAnswersTextView.setText("User answers will appear here:\n");
    }
}
