package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button strBtn, jsonObjBtn, jsonArrBtn, imgBtn, questionBtn;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        username = prefs.getString("USERNAME","");
        Log.d("MainActivity", "Username from SharedPreferences: " + username);

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

        // Check if the username exists in SharedPreferences
        //SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        //String username = sharedPreferences.getString("username", null);
        if (username != null) {
            // Username exists in SharedPreferences, show btnStringRequest
            strBtn.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, "Welcome " + username, Toast.LENGTH_SHORT).show();

        } else {
            // Username does not exist in SharedPreferences, hide btnStringRequest
            strBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnStringRequest) {
            startActivity(new Intent(MainActivity.this, MultiplayerActivity.class));
        } else if (id == R.id.btnJsonObjRequest) {
            startActivity(new Intent(MainActivity.this, JsonObjReqActivity.class));
        } else if (id == R.id.btnJsonArrRequest) {
            startActivity(new Intent(MainActivity.this, UserQuestionActivity.class));
        } else if (id == R.id.btnImageRequest) {
            startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
        } else if (id == R.id.btnQuestionSinglePlayer) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
}
