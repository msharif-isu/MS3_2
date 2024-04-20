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
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

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
        View view = inflater.inflate(R.layout.activity_leaderboard, container, false);

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
                    displayData.addAll(parseLeaderboardMessage(message));
                    leaderboardAdapter.notifyDataSetChanged();
                });
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        Intent serviceIntent = new Intent(requireActivity(), LeaderboardWebSocketService.class);

        LocalBroadcastManager
                .getInstance(requireActivity())
                .registerReceiver(messageReceiver, new IntentFilter("WebSocketMessageReceived"));
        serviceIntent.setAction("CONNECT");
        serviceIntent.putExtra("key", "leaderboard");
        String websocketURL = RequestURLs.SERVER_WEBSOCKET_LEADERBOARD_URL + "/" + new Random().nextFloat();
        serviceIntent.putExtra("url", websocketURL);
        requireActivity().startService(serviceIntent);
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(messageReceiver);
        super.onStop();
    }

    /**
     * Parses a WebSocket message into the appropriate list of LeaderboardListItems
     * @param message - WebSocket message
     * @return list of LeaderboardListItems
     */
    private List<LeaderboardListItem> parseLeaderboardMessage(String message) {
        Scanner scnr = new Scanner(message);

        ArrayList<LeaderboardListItem> data = new ArrayList<>();

        while(scnr.hasNextLine()) {
            String lbString = scnr.nextLine();
            Scanner parser = new Scanner(lbString);

            int id = Integer.parseInt(parser.next());

            HashMap<LeaderboardTimeFrameEnum, Integer> points = new HashMap<>();

            points.put(LeaderboardTimeFrameEnum.DAILY, Integer.parseInt(parser.next()));
            points.put(LeaderboardTimeFrameEnum.WEEKLY, Integer.parseInt(parser.next()));
            points.put(LeaderboardTimeFrameEnum.MONTHLY, Integer.parseInt(parser.next()));
            points.put(LeaderboardTimeFrameEnum.YEARLY, Integer.parseInt(parser.next()));
            points.put(LeaderboardTimeFrameEnum.LIFETIME, Integer.parseInt(parser.next()));
            parser.close();

            LeaderboardListItem item = new LeaderboardListItem(id, points);
            data.add(item);
        }
        scnr.close();

        return data;
    }
}
