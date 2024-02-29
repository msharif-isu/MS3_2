package com.example.androidexample;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class ResultsActivity extends AppCompatActivity {

    private Button playAgainButton, continueButton;
    //num points is the textbox displaying points, points is the number.
    private TextView numPoints, usernameMessage;
    private String username;
    private int correctAnswers, points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Initialize views
        playAgainButton = findViewById(R.id.button);
        numPoints = findViewById(R.id.numberOfCorrectQuestions);
        continueButton = findViewById(R.id.btnContinue);
        usernameMessage = findViewById(R.id.Username);

        // Set onClickListener for play again button
        playAgainButton.setOnClickListener(view -> {
            Intent intent = new Intent(ResultsActivity.this, SinglePlayerQuestionActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        });

        // Set onClickListener for continue button
        continueButton.setOnClickListener(view -> {
            Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("USERNAME");
            correctAnswers = extras.getInt("CORRECTANSWERS");
            points = extras.getInt("POINTS");
            numPoints.setText("Total points: " + points);
            usernameMessage.setText("Congrats " + username + "! You got " + correctAnswers + " questions correct!");
        } else {
            usernameMessage.setText("Not logged in");
        }

    }
}
