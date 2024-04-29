package com.example.androidexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class achievementPage2 extends Fragment {

    TextView achievementsUnlocked;
    RecyclerView recyclerView;
    AchievementAdapter adapter;

    String username;

    public achievementPage2() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_user_stats, container, false);

        SharedPreferences prefs = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        username = prefs.getString("USERNAME", "");

        recyclerView = rootView.findViewById(R.id.recyclerview);
        achievementsUnlocked = rootView.findViewById(R.id.unlocked);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AchievementAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        StatisticsService statisticsService = new StatisticsService(getContext());
        statisticsService.getUserStats(username, new StatisticsService.StatsCallback() {
            @Override
            public void onSuccess(UserStats userStats) {
                List<Achievement> userAchievements = convertUserStatsToAchievements(userStats);
                adapter.updateAchievements(userAchievements);
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
            }
        });

        return rootView;
    }

    private List<Achievement> convertUserStatsToAchievements(UserStats userStats) {
        List<Achievement> achievements = new ArrayList<>();
        // Add user stats as achievements because I do not want to make another recycleview.
        achievements.add(new Achievement("Games Played", String.valueOf(userStats.getGamesPlayed()), 1, true));
        achievements.add(new Achievement("Number of Friends", String.valueOf(userStats.getNumberOfFreinds()), 2, true));
        achievements.add(new Achievement("Questions Submitted", String.valueOf(userStats.getQuestionsSumbitted()), 3, true));
        achievements.add(new Achievement("Total answered", String.valueOf(userStats.getTotalAnswered()), 4, true));
        achievements.add(new Achievement("Total Correct", String.valueOf(userStats.getTotalCorrect()), 5, true)); // Changed position to 5
        achievements.add(new Achievement("Total Incorrect", String.valueOf(userStats.getTotalIncorrect()), 6, true)); // Changed position to 6
        achievements.add(new Achievement("Win Streak", String.valueOf(userStats.getWinStreak()), 7, true)); // Changed position to 7
        achievements.add(new Achievement("Losses", String.valueOf(userStats.getLosses()), 8, true)); // Changed position to 8
        achievements.add(new Achievement("Wins", String.valueOf(userStats.getWins()), 9, true)); // Changed position to 9
        return achievements;
    }
}
