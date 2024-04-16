package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.androidexample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button strBtn, jsonObjBtn, jsonArrBtn, imgBtn, questionBtn, queryButton, jeopardyButton;

    private ImageButton profileButton;

    private String username;
    private int userId;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        username = prefs.getString("USERNAME", "");
        userId = prefs.getInt("USER_ID", 0);
        Log.d("MainActivity", "Username from SharedPreferences: " + userId + username);

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new PlayFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.play) {
                replaceFragment(new PlayFragment());
            } else if (itemId == R.id.edit) {
                replaceFragment(new EditFragment());
            } else if (itemId == R.id.leaderboard) {
                replaceFragment(new LeaderboardFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });

        strBtn = findViewById(R.id.btnStringRequest);
        jsonObjBtn = findViewById(R.id.btnJsonObjRequest);
        jsonArrBtn = findViewById(R.id.btnJsonArrRequest);
        imgBtn = findViewById(R.id.btnImageRequest);
        questionBtn = findViewById(R.id.btnQuestionSinglePlayer);
        profileButton = findViewById(R.id.profileButton);
        queryButton = findViewById(R.id.btnQuery);
        jeopardyButton = findViewById(R.id.btnJeopardy);

        /* button click listeners */
        strBtn.setOnClickListener(this);
        jsonObjBtn.setOnClickListener(this);
        jsonArrBtn.setOnClickListener(this);
        imgBtn.setOnClickListener(this);
        questionBtn.setOnClickListener(this);
        profileButton.setOnClickListener(this);
        queryButton.setOnClickListener(this);
        jeopardyButton.setOnClickListener(this);


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
            startActivity(new Intent(MainActivity.this, LobbiesActivity.class));
        } else if (id == R.id.btnJsonObjRequest) {
            startActivity(new Intent(MainActivity.this, SinglePlayerQuestionActivity.class));
        } else if (id == R.id.btnJsonArrRequest) {
            startActivity(new Intent(MainActivity.this, UserQuestionActivity.class));
        } else if (id == R.id.btnImageRequest) {
            startActivity(new Intent(MainActivity.this, LeaderboardActivity.class));
        } else if (id == R.id.btnQuestionSinglePlayer) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else if (id == R.id.profileButton) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        } else if (id == R.id.btnQuery) {
            startActivity(new Intent(MainActivity.this, QueryActivity.class));
        } else if (id == R.id.btnJeopardy) {
            startActivity(new Intent(MainActivity.this, JeopardyActivity.class));
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
