package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button strBtn, jsonObjBtn, jsonArrBtn, imgBtn, questionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        strBtn = findViewById(R.id.btnStringRequest);
        jsonObjBtn = findViewById(R.id.btnJsonObjRequest);
        jsonArrBtn = findViewById(R.id.btnJsonArrRequest);
        imgBtn = findViewById(R.id.btnImageRequest);
        questionBtn = findViewById(R.id.btnQuestionSinglePlayer);

        /* button click listeners */
        strBtn.setOnClickListener(this);
        jsonObjBtn.setOnClickListener(this);
        jsonArrBtn.setOnClickListener(this);
        imgBtn.setOnClickListener(this);
        questionBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnStringRequest) {
            startActivity(new Intent(MainActivity.this, StringReqActivity.class));
        } else if (id == R.id.btnJsonObjRequest) {
            startActivity(new Intent(MainActivity.this, JsonObjReqActivity.class));
        } else if (id == R.id.btnJsonArrRequest) {
            startActivity(new Intent(MainActivity.this, UserQuestionActivity.class));
        } else if (id == R.id.btnImageRequest) {

            Intent serviceIntent = new Intent(MainActivity.this, LeaderboardWebSocketService.class);
            serviceIntent.setAction("CONNECT");
            serviceIntent.putExtra("key", "leaderboard");
            serviceIntent.putExtra("url", "ws://10.0.2.2:8080/leaderboard/1010");
            startService(serviceIntent);

            Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        } else if (id == R.id.btnQuestionSinglePlayer) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
}