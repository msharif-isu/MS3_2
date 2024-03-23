package com.example.androidexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
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
        recyclerView = findViewById(R.id.leaderboard_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra("key");
            Log.d("MessageReceiver", "Received leaderboard data");
            if ("leaderboard".equals(key)){
                runOnUiThread(() -> {
                    List<LeaderboardListItem> data = parseLeaderboardMessage(intent.getStringExtra("message"));
                    leaderboardAdapter = new LeaderboardAdapter(data, LeaderboardTimeFrameEnum.DAILY);
                    recyclerView.setAdapter(leaderboardAdapter);
                });
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(messageReceiver, new IntentFilter("WebSocketMessageReceived"));
    }

    @Override
    protected void onStop() {
        Log.d("Destroyed", "Receiver terminated");
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

//    public void makeLeaderboardRequest() {
//        JsonArrayRequest leaderboardRequest = new JsonArrayRequest(
//                Request.Method.GET,
//                String.format("%s/leaderboard", SERVER_URL),
//                null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        ArrayList<LeaderboardListItem> leaderboardData = new ArrayList<>();
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//                                JSONObject jsonData = response.getJSONObject(i);
//                                HashMap<LeaderboardTimeFrameEnum, Integer> points = new HashMap<>();
//
//                                points.put(LeaderboardTimeFrameEnum.DAILY, jsonData.getInt("userPoints"));
//                                points.put(LeaderboardTimeFrameEnum.WEEKLY, jsonData.getInt("weeklyPoints"));
//                                points.put(LeaderboardTimeFrameEnum.MONTHLY, jsonData.getInt("monthlyPoints"));
//                                points.put(LeaderboardTimeFrameEnum.YEARLY, jsonData.getInt("yearlyPoints"));
//                                points.put(LeaderboardTimeFrameEnum.LIFETIME, jsonData.getInt("lifetimePoints"));
//
//                                leaderboardData.add(new LeaderboardListItem(jsonData.getInt("id"), points));
//                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//
//                        leaderboardAdapter = new LeaderboardAdapter(leaderboardData, LeaderboardTimeFrameEnum.DAILY);
//                        recyclerView.setAdapter(leaderboardAdapter);
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("VolleyError", error.toString());
//                        if (!attemptedLeaderboardRefresh) {
//                            Toast.makeText(getApplicationContext(), "Leaderboard failed to load, reattempting to get data from server", Toast.LENGTH_LONG).show();
//                            makeLeaderboardRequest();
//                            attemptedLeaderboardRefresh = true;
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Unable to reach server", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }
//        );
//
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(leaderboardRequest);
//    }
//    public void addPointsToUserID(int id, int pointsToAdd) {
//        JsonObjectRequest pointsAddition = new JsonObjectRequest(
//                Request.Method.POST,
//                String.format("%s/leaderboard/addpoints/%d/%d", SERVER_URL, id, pointsToAdd),
//                null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        if (leaderboardAdapter != null) {
//                            makeLeaderboardRequest();
//                        }
//                        Toast.makeText(getApplicationContext(), "Points added", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), "Incorrect format", Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
//
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(pointsAddition);
//    }
}
