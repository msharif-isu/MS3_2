package com.example.androidexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import url.RequestURLs;

public class LeaderboardFragment extends Fragment {

    // DEBUG ONLY VARIABLE (Remove after testing)
    private static final String TAG = "LeaderboardFragment";
    private RecyclerView leaderboardListUI;
    private LeaderboardAdapter leaderboardAdapter;
    private List<LeaderboardListItem> displayData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        // Set up leaderboard ui
        leaderboardListUI = view.findViewById(R.id.leaderboard_list);

        displayData = new ArrayList<>();
        leaderboardAdapter = new LeaderboardAdapter(displayData, LeaderboardTimeFrameEnum.DAILY);

        leaderboardListUI.setLayoutManager(new LinearLayoutManager(getContext()));
        leaderboardListUI.setAdapter(leaderboardAdapter);

        return view;
    }

    // For receiving messages
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            String key = intent.getStringExtra("key");
            if ("leaderboard".equals(key)){
                requireActivity().runOnUiThread(() -> {
                    String message = intent.getStringExtra("message");
                    Log.d("TAG", "onReceive: " + message);
                    displayData.clear();
                    List<LeaderboardListItem> list = LeaderboardListItem.parseLeaderboardMessage(message);

                    list.sort(Comparator.comparingInt(LeaderboardListItem::getDailyPoints).reversed());

                    displayData.addAll(list);
                    leaderboardAdapter.notifyDataSetChanged();
                });
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Intent serviceIntent = new Intent(requireActivity(), LeaderboardWebSocketService.class) {{
            setAction("CONNECT");
            putExtra("key", "leaderboard");
        }};

        LocalBroadcastManager
                .getInstance(requireActivity())
                .registerReceiver(messageReceiver, new IntentFilter("WebSocketMessageReceived"));

        String websocketURL = RequestURLs.SERVER_WEBSOCKET_LEADERBOARD_URL + "/" + new Random().nextFloat();
        serviceIntent.putExtra("url", websocketURL);
        requireActivity().startService(serviceIntent);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager
                .getInstance(requireActivity())
                .unregisterReceiver(messageReceiver);
    }
}
