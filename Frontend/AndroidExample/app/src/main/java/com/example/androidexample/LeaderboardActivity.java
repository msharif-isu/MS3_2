package com.example.androidexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class LeaderboardActivity extends AppCompatActivity {
//    private boolean attemptedLeaderboardRefresh = false;
    private RecyclerView recyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private List<LeaderboardListItem> displayData;
    private Button dailyButton;
    private Button weeklyButton;
    private Button monthlyButton;
    private Button yearlyButton;
    private Button lifetimeButton;
    private Button addPointsButton;
    private EditText idInputField;
    private EditText pointsInputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Setup leaderboard list UI
        displayData = new ArrayList<>();
        leaderboardAdapter = new LeaderboardAdapter(displayData, LeaderboardTimeFrameEnum.DAILY);
        recyclerView = findViewById(R.id.leaderboard_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(leaderboardAdapter);

        // Set buttons to change the type of points displayed
        dailyButton = findViewById(R.id.daily_button);
        weeklyButton = findViewById(R.id.weekly_button);
        monthlyButton = findViewById(R.id.monthly_button);
        yearlyButton = findViewById(R.id.yearly_button);
        lifetimeButton = findViewById(R.id.lifetime_button);

        addPointsButton = findViewById(R.id.add_points_button);
        idInputField = findViewById(R.id.id_input_field);
        pointsInputField = findViewById(R.id.points_input_field);

        dailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leaderboardAdapter != null) {
                    leaderboardAdapter.time_frame = LeaderboardTimeFrameEnum.DAILY;
                    leaderboardAdapter.notifyDataSetChanged();
                }
            }
        });
        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leaderboardAdapter != null) {
                    leaderboardAdapter.time_frame = LeaderboardTimeFrameEnum.WEEKLY;
                    leaderboardAdapter.notifyDataSetChanged();
                }
            }
        });
        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leaderboardAdapter != null) {
                    leaderboardAdapter.time_frame = LeaderboardTimeFrameEnum.MONTHLY;
                    leaderboardAdapter.notifyDataSetChanged();
                }
            }
        });
        yearlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leaderboardAdapter != null) {
                    leaderboardAdapter.time_frame = LeaderboardTimeFrameEnum.YEARLY;
                    leaderboardAdapter.notifyDataSetChanged();
                }
            }
        });
        lifetimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leaderboardAdapter != null) {
                    leaderboardAdapter.time_frame = LeaderboardTimeFrameEnum.LIFETIME;
                    leaderboardAdapter.notifyDataSetChanged();
                }
            }
        });

        // Send a request to add points to user
        addPointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idInputField.getText().toString().trim().isEmpty() || pointsInputField.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                } else {
                    int id = Integer.parseInt(idInputField.getText().toString());
                    int points = Integer.parseInt(pointsInputField.getText().toString());
                    idInputField.setText("");
                    pointsInputField.setText("");
                    Toast.makeText(getApplicationContext(), "Awaiting server response...", Toast.LENGTH_SHORT).show();
//                    addPointsToUserID(id, points);
                }
            }
        });
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

        serviceIntent.setAction("CONNECT");
        serviceIntent.putExtra("key", "leaderboard");
        serviceIntent.putExtra("url", "ws://10.0.2.2:8080/leaderboard/1010");
        startService(serviceIntent);
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(messageReceiver, new IntentFilter("WebSocketMessageReceived"));

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
