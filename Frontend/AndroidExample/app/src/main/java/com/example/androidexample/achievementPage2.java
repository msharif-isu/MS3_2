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

public class achievementPage2 extends Fragment {

    TextView achievementsUnlocked;

    public achievementPage2() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_user_stats, container, false);

        List<Achievement> achievementList = new ArrayList<>();
        achievementList.add(new Achievement("Games Played", "0", 1, true));
        achievementList.add(new Achievement("Number of Friends", "0", 2, true));
        achievementList.add(new Achievement("Questions Submitted", "0", 3, true));
        achievementList.add(new Achievement("Total answered", "0", 4, true));
        achievementList.add(new Achievement("Total Correct", "0", 4, true));
        achievementList.add(new Achievement("Total Incorrect", "0", 4, true));
        achievementList.add(new Achievement("Win Streak", "0", 4, true));
        achievementList.add(new Achievement("Losses", "0", 4, true));
        achievementList.add(new Achievement("Wins", "0", 4, true));


        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview);
        AchievementAdapter adapter = new AchievementAdapter(achievementList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

}
