package com.example.androidexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class achievementPage1 extends Fragment {

    TextView achievementsUnlocked;
    String username;

    int totalAnswered, totalCorrect, totalIncorrect, winStreak, wins, losses, questionsSubmitted, gamesPlayed, numberOfFriends;

    public achievementPage1() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_achievement, container, false);
        SharedPreferences prefs = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        username = prefs.getString("USERNAME", "");
        achievementsUnlocked = rootView.findViewById(R.id.unlocked);

        List<Achievement> achievementList = new ArrayList<>();
        achievementList.add(new Achievement("Create Account", "Create an account", 1, true));
        achievementList.add(new Achievement("Newcomer", "Played your first game.", 2, false));
        achievementList.add(new Achievement("Just Getting Started", "Answered your first question.", 5, false));
        achievementList.add(new Achievement("Trivia Master", "Answered 10 questions", 7, false));
        achievementList.add(new Achievement("Trivia Champion", "Answered 20 questions", 8, false));
        achievementList.add(new Achievement("Trivia Guru", "Answered 30 questions", 9, false));
        achievementList.add(new Achievement("Trivia Prodigy", "Answered 40 questions", 10, false));
        achievementList.add(new Achievement("Trivia Legend", "Answered 50 questions", 11, false));
        achievementList.add(new Achievement("Trivial", "Answered 100 questions", 12, false));
        achievementList.add(new Achievement("Knowledge Seeker", "Played 5 games", 3, false));
        achievementList.add(new Achievement("Seasoned Player", "Played 10 games.", 4, false));
        achievementList.add(new Achievement("Quiz Expert", "Played 20 games", 6, false));
        achievementList.add(new Achievement("Quiz Me!", "Played 30 games", 13, false));
        achievementList.add(new Achievement("Devs are evil", "You can never unlock this achievement.", 14, false));
        achievementList.add(new Achievement("QUIZ ME!!!!", "Played 40 games", 15, false));
        achievementList.add(new Achievement("QUIZ ME!!!!!! but louder", "Played 50 games", 16, false));
        achievementList.add(new Achievement("Addiction", "Played 100 games", 17, false));
        achievementList.add(new Achievement("Contributor", "Submitted your first question", 18, false));
        achievementList.add(new Achievement("Curious Inquirer", "Submitted 10 questions", 19, false));
        achievementList.add(new Achievement("Knowledge Sharer", "Submitted 20 questions", 20, false));
        achievementList.add(new Achievement("Trivia Scholar", "Submitted 30 questions", 21, false));
        achievementList.add(new Achievement("Trivia Connoisseur", "Submitted 40 questions", 22, false));
        achievementList.add(new Achievement("Trivia Architect", "Submitted 50 questions", 23, false));
        achievementList.add(new Achievement("I GIVE TRIVIA", "Submitted 100 questions", 24, false));

        SharedPreferences statsPrefs = requireContext().getSharedPreferences("UserStatistics", Context.MODE_PRIVATE);
        if (statsPrefs != null) {
            totalAnswered = statsPrefs.getInt("TOTAL_ANSWERS", 0);
            totalCorrect = statsPrefs.getInt("TOTAL_CORRECT", 0);
            totalIncorrect = statsPrefs.getInt("TOTAL_INCORRECT", 0);
            winStreak = statsPrefs.getInt("WIN_STREAK", 0);
            wins = statsPrefs.getInt("WINS", 0);
            losses = statsPrefs.getInt("LOSSES", 0);
            questionsSubmitted = statsPrefs.getInt("QUESTIONS_SUBMITTED", 0);
            gamesPlayed = statsPrefs.getInt("GAMES_PLAYED", 0);
            numberOfFriends = statsPrefs.getInt("NUMBER_OF_FRIENDS", 0);

            updateAchievements(achievementList);
            updateServerWithStats();
        }

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview);
        AchievementAdapter adapter = new AchievementAdapter(achievementList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    private void updateAchievements(List<Achievement> achievements) {
        for (Achievement achievement : achievements) {
            switch (achievement.getId()) {
                case 2: // Newcomer
                    achievement.setUnlocked(gamesPlayed > 0);
                    break;
                case 5: // Just Getting Started
                    achievement.setUnlocked(totalAnswered > 0);
                    break;
                case 7: // Trivia Master
                    achievement.setUnlocked(totalAnswered >= 10);
                    break;
                case 8: // Trivia Champion
                    achievement.setUnlocked(totalAnswered >= 20);
                    break;
                case 9: // Trivia Guru
                    achievement.setUnlocked(totalAnswered >= 30);
                    break;
                case 10: // Trivia Prodigy
                    achievement.setUnlocked(totalAnswered >= 40);
                    break;
                case 11: // Trivia Legend
                    achievement.setUnlocked(totalAnswered >= 50);
                    break;
                case 12: // Trivial
                    achievement.setUnlocked(totalAnswered >= 100);
                    break;
                case 3: // Knowledge Seeker
                    achievement.setUnlocked(gamesPlayed >= 5);
                    break;
                case 4: // Seasoned Player
                    achievement.setUnlocked(gamesPlayed >= 10);
                    break;
                case 6: // Quiz Expert
                    achievement.setUnlocked(gamesPlayed >= 20);
                    break;
                case 13: // Quiz Me!
                    achievement.setUnlocked(gamesPlayed >= 30);
                    break;
                case 14: // Placeholder
                    achievement.setUnlocked(false);
                    break;
                case 15: // QUIZ ME!!!!
                    achievement.setUnlocked(gamesPlayed >= 40);
                    break;
                case 16: // QUIZ ME!!!!!! but louder
                    achievement.setUnlocked(gamesPlayed >= 50);
                    break;
                case 17: // Addiction
                    achievement.setUnlocked(gamesPlayed >= 100);
                    break;
                case 18: // Contributor
                    achievement.setUnlocked(questionsSubmitted > 0);
                    break;
                case 19: // Curious Inquirer
                    achievement.setUnlocked(questionsSubmitted >= 10);
                    break;
                case 20: // Knowledge Sharer
                    achievement.setUnlocked(questionsSubmitted >= 20);
                    break;
                case 21: // Trivia Scholar
                    achievement.setUnlocked(questionsSubmitted >= 30);
                    break;
                case 22: // Trivia Connoisseur
                    achievement.setUnlocked(questionsSubmitted >= 40);
                    break;
                case 23: // Trivia Architect
                    achievement.setUnlocked(questionsSubmitted >= 50);
                    break;
                case 24: // I GIVE TRIVIA
                    achievement.setUnlocked(questionsSubmitted >= 100);
                    break;
                default:
                    // Handle other cases if needed
            }
        }
    }

    private void updateServerWithStats() {
        StatisticsService stats = new StatisticsService(getContext());
        stats.updateGameStats(username, gamesPlayed, totalAnswered, questionsSubmitted, new StatisticsService.StatsCallback() {
            @Override
            public void onSuccess(UserStats userStats) {
                Log.d("StatsUpdate", "Stats updated successfully");
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("StatsUpdate", "Error updating stats: " + errorMessage);
            }
        });
    }
}
