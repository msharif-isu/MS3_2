package com.example.androidexample;

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

public class achievementPage1 extends Fragment {

    TextView achievementsUnlocked;

    public achievementPage1() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_achievement, container, false);

        achievementsUnlocked = rootView.findViewById(R.id.unlocked);

        List<Achievement> achievementList = new ArrayList<>();
        achievementList.add(new Achievement("Create Account", "Create an account", 1, true));
        achievementList.add(new Achievement("Very Lonely", "Play a game of singleplayer", 2, false));
        achievementList.add(new Achievement("Jeopardy? Nah, JeoparCY!", "Play a game of Jeopardy", 3, false));
        achievementList.add(new Achievement("Multiplayer Maverick", "Show off your skills in multiplayer mode!", 4, false));

        int unlockedCount = 0;
        for (Achievement achievement : achievementList) {
            if (achievement.isUnlocked()) {
                unlockedCount++;
            }
        }
        int totalAchievements = achievementList.size();
        achievementsUnlocked.setText(unlockedCount + "/" + totalAchievements);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview);
        AchievementAdapter adapter = new AchievementAdapter(achievementList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

}
