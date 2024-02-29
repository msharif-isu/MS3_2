package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class ResultsActivity extends AppCompatActivity {

    private Button playAgainButton, continueButton;
    //num points is the textbox displaying points, points is the number.
    private TextView numPoints, usernameMessage;
    private String username;
    private int correctAnswers, points, usernameId;

    private String backendUrl = "http://10.0.2.2:8080/";


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
            givePoints(username, points);
//            Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
//            intent.putExtra("USERNAME", username);
//            startActivity(intent);
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("USERNAME");
            correctAnswers = extras.getInt("CORRECTANSWERS");
            points = extras.getInt("POINTS");
            usernameId = extras.getInt("USERID");
            numPoints.setText("Total points: " + points);
            usernameMessage.setText("Congrats " + username + "! You got " + correctAnswers + " questions correct!");
        } else {
            usernameMessage.setText("Not logged in");
        }

    }

    private void givePoints(String username, int points) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = backendUrl + "users/" + username + "/" + points;

        // Request a JSON object response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Points updated successfully
                        Toast.makeText(ResultsActivity.this, "Points updated successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
                        intent.putExtra("USERNAME", username);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Failed to update points
                Toast.makeText(ResultsActivity.this, "Failed to update points", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

}
