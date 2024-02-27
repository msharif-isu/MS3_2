package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button strBtn, jsonObjBtn, jsonArrBtn, imgBtn, loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        strBtn = findViewById(R.id.btnStringRequest);
        jsonObjBtn = findViewById(R.id.btnJsonObjRequest);
        jsonArrBtn = findViewById(R.id.btnJsonArrRequest);
        imgBtn = findViewById(R.id.btnImageRequest);
        loginBtn = findViewById(R.id.btnLogin);

        /* button click listeners */
        strBtn.setOnClickListener(this);
        jsonObjBtn.setOnClickListener(this);
        jsonArrBtn.setOnClickListener(this);
        imgBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnStringRequest) {
            startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
        } else if (id == R.id.btnJsonObjRequest) {
            startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
        } else if (id == R.id.btnJsonArrRequest) {
            startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
        } else if (id == R.id.btnImageRequest) {
            startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
        } else if (id == R.id.btnLogin) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
}
