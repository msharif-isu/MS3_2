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
import android.widget.Toast;


import com.android.volley.Request;
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

import url.RequestURLs;

public class MultiplayerActivity extends AppCompatActivity implements WebSocketListener {

    private CountDownTimer countDownTimer;
    private TextView timeLeftTextView;
    private long timeLeftInMillis;
    private int userPoints = 0, numQuestions = -1;
    private TextView msgTv;


    //TODO PROPER QUESTION IMPLEMENTAITON FOR MULTIPLAYER
    private List<Integer> questionIds = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private String questionCorrectAnswer = "Joe Biden";
    TextView questionTextView;

    private String questions, username, questionsPlayedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        //get username to connect to websocket
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        username = sharedPreferences.getString("USERNAME", "");
        long roomId = getIntent().getLongExtra("ROOM_ID", -1);
//        String questions = getIntent().getStringExtra("QUESTION_ID");
        SharedPreferences prefs = getSharedPreferences("QuestionIds", Context.MODE_PRIVATE);
        questions = prefs.getString("QuestionIds", "");
        questionsPlayedType = prefs.getString("questionsPlayedType", "");


        Log.d("QuestionIDs in Multiplayer", "Question ids: " + questions);
//        Toast.makeText(MultiplayerActivity.this, "Welcome " + username, Toast.LENGTH_SHORT).show();

        // Find views by their IDs
        questionTextView = findViewById(R.id.question);
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
                numQuestions++;
                //} else if (message.equals("Correct!")) {
            } else if (message.equals("Game is now over congrats!") || message.equals("All questions answered!")) {
                Intent intent = new Intent(MultiplayerActivity.this, ResultsActivity.class);
                //TODO fix this, it currently stores in username, when it shoudlnt
                intent.putExtra("USERNAME", "multiplayer");
                intent.putExtra("NUM_QUESTIONS", numQuestions);
                intent.putExtra("POINTS", numQuestions * 100);
                try {
                    sentMatchHistory();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                SharedPreferences statsPrefs = getSharedPreferences("UserStatistics", MODE_PRIVATE);
                SharedPreferences.Editor editor = statsPrefs.edit();
                int totalAnswered = statsPrefs.getInt("TOTAL_ANSWERS", 0);
                int gamesPlayed = statsPrefs.getInt("GAMES_PLAYED", 0);
                totalAnswered += numQuestions;
                gamesPlayed += 1;
                editor.putInt("TOTAL_ANSWERS", totalAnswered);
                editor.putInt("GAMES_PLAYED", gamesPlayed);
                editor.apply();

                startActivity(intent);

            } else {
                msgTv.setText(s + "\n" + message);
            }

        });
    }

    private void sentMatchHistory() throws JSONException {
        String url = RequestURLs.SERVER_HTTP_URL + "/" + username + "/saveGame";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new MatchHistory("0", questionsPlayedType, numQuestions * 100, username).toJSON(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Bruh", "This worked. Yay!");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(MultiplayerActivity.this, "Failed to send match history: " + error.getMessage() + url, Toast.LENGTH_SHORT).show();
                        Log.d("Multiplayer", url);
                    }
                });
        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(request);
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
