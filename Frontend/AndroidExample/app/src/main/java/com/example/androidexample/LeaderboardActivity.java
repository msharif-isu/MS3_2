package com.example.androidexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import url.RequestURLs;

public class LeaderboardActivity extends AppCompatActivity {
    //    private boolean attemptedLeaderboardRefresh = false;
    private RecyclerView leaderboardListUI;
    private LeaderboardAdapter leaderboardAdapter;
    private List<LeaderboardListItem> displayData;
    private EditText idInputField;
    private EditText pointsInputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        // Setup leaderboard list UI

        displayData = new ArrayList<>();
        leaderboardAdapter = new LeaderboardAdapter(displayData, LeaderboardTimeFrameEnum.DAILY);
        leaderboardListUI = findViewById(R.id.leaderboard_list);
        leaderboardListUI.setLayoutManager(new LinearLayoutManager(this));
        leaderboardListUI.setAdapter(leaderboardAdapter);

        /**
         * NOTE: All of the commented code below is functionality that is
         *       planned to be removed as the project continues.
         *
         *       These features were implemented before the true scope of
         *       this project was realized. They
         */

//        // Set buttons to change the type of points displayed
//        Button dailyButton = findViewById(R.id.daily_button);
//        Button weeklyButton = findViewById(R.id.weekly_button);
//        Button monthlyButton = findViewById(R.id.monthly_button);
//        Button yearlyButton = findViewById(R.id.yearly_button);
//        Button lifetimeButton = findViewById(R.id.lifetime_button);
//
//        Button addPointsButton = findViewById(R.id.add_points_button);
//        idInputField = findViewById(R.id.id_input_field);
//        pointsInputField = findViewById(R.id.points_input_field);
//
//        dailyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (leaderboardAdapter != null) {
//                    leaderboardAdapter.time_frame = LeaderboardTimeFrameEnum.DAILY;
//                    leaderboardAdapter.notifyDataSetChanged();
//                }
//            }
//        });
//        weeklyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (leaderboardAdapter != null) {
//                    leaderboardAdapter.time_frame = LeaderboardTimeFrameEnum.WEEKLY;
//                    leaderboardAdapter.notifyDataSetChanged();
//                }
//            }
//        });
//        monthlyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (leaderboardAdapter != null) {
//                    leaderboardAdapter.time_frame = LeaderboardTimeFrameEnum.MONTHLY;
//                    leaderboardAdapter.notifyDataSetChanged();
//                }
//            }
//        });
//        yearlyButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (leaderboardAdapter != null) {
//                    leaderboardAdapter.time_frame = LeaderboardTimeFrameEnum.YEARLY;
//                    leaderboardAdapter.notifyDataSetChanged();
//                }
//            }
//        });
//        lifetimeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (leaderboardAdapter != null) {
//                    leaderboardAdapter.time_frame = LeaderboardTimeFrameEnum.LIFETIME;
//                    leaderboardAdapter.notifyDataSetChanged();
//                }
//            }
//        });
//
//        // Send a request to add points to user
//        addPointsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (idInputField.getText().toString().trim().isEmpty() || pointsInputField.getText().toString().trim().isEmpty()) {
//                    Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
//                } else {
//                    int id = Integer.parseInt(idInputField.getText().toString());
//                    int points = Integer.parseInt(pointsInputField.getText().toString());
//                    idInputField.setText("");
//                    pointsInputField.setText("");
//                    Toast.makeText(getApplicationContext(), "Awaiting server response...", Toast.LENGTH_SHORT).show();
////                    addPointsToUserID(id, points);
//                }
//            }
//        });
        // Get leaderboard data
//        makeLeaderboardRequest();
    }

    // For receiving messages
    private final BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra("key");
            if ("leaderboard".equals(key)){
                runOnUiThread(() -> {
                    String message = intent.getStringExtra("message");
                    displayData.clear();
                    displayData.addAll(parseLeaderboardMessage(message));
                    leaderboardAdapter.notifyDataSetChanged();
                });
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        Intent serviceIntent = new Intent(this, LeaderboardWebSocketService.class);

        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(messageReceiver, new IntentFilter("WebSocketMessageReceived"));
        serviceIntent.setAction("CONNECT");
        serviceIntent.putExtra("key", "leaderboard");
        serviceIntent.putExtra("url", RequestURLs.SERVER_WEBSOCKET_LEADERBOARD_URL + "/" + new Random().nextFloat());
        startService(serviceIntent);
        Log.d("HiHello", "onStart: Tried to start");
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
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
