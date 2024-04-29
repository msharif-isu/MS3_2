package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.androidexample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private String username;
    private int userId;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        username = prefs.getString("USERNAME", "");
        userId = prefs.getInt("USER_ID", 0);
        Log.d("MainActivity", "Username from SharedPreferences: " + userId + username);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new PlayFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.play) {
                replaceFragment(new PlayFragment());
            } else if (itemId == R.id.edit) {
                replaceFragment(new QueryFragment());
            } else if (itemId == R.id.leaderboard) {
                replaceFragment(new LeaderboardFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });

        StatisticsService statisticsService = new StatisticsService(this);
        statisticsService.getUserStats(username, new StatisticsService.StatsCallback() {
            @Override
            public void onSuccess(UserStats userStats) {
                // Update SharedPreferences with user stats
                updateSharedPreferences(userStats);
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error if needed
            }
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
        fragmentTransaction.commit();
    }
    private void updateSharedPreferences(UserStats userStats) {

        Log.d("UpdateSharedPreferences", "Total Answers: " + userStats.getTotalAnswered());
        Log.d("UpdateSharedPreferences", "Total Correct: " + userStats.getTotalCorrect());
        Log.d("UpdateSharedPreferences", "Total Incorrect: " + userStats.getTotalIncorrect());
        Log.d("UpdateSharedPreferences", "Win Streak: " + userStats.getWinStreak());
        Log.d("UpdateSharedPreferences", "Wins: " + userStats.getWins());
        Log.d("UpdateSharedPreferences", "Losses: " + userStats.getLosses());
        Log.d("UpdateSharedPreferences", "Questions Submitted: " + userStats.getQuestionsSubmitted());
        Log.d("UpdateSharedPreferences", "Games Played: " + userStats.getGamesPlayed());
        Log.d("UpdateSharedPrefrences", "Number of Friends: " + userStats.getNumberOfFreinds());

        SharedPreferences statsPrefs = getSharedPreferences("UserStatistics", MODE_PRIVATE);
        SharedPreferences.Editor editor = statsPrefs.edit();
        editor.putInt("TOTAL_ANSWERS", userStats.getTotalAnswered());
        editor.putInt("TOTAL_CORRECT", userStats.getTotalCorrect());
        editor.putInt("TOTAL_INCORRECT", userStats.getTotalIncorrect());
        editor.putInt("WIN_STREAK", userStats.getWinStreak());
        editor.putInt("WINS", userStats.getWins());
        editor.putInt("LOSSES", userStats.getLosses());
        editor.putInt("QUESTIONS_SUBMITTED", userStats.getQuestionsSubmitted());
        editor.putInt("GAMES_PLAYED", userStats.getGamesPlayed());
        editor.putInt("NUMBER_OF_FRIENDS", userStats.getNumberOfFreinds());
        editor.apply();
    }
}