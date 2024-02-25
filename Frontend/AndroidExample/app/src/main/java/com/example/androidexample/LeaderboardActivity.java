package com.example.androidexample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

public class LeaderboardActivity extends AppCompatActivity {

    private final static String SERVER_URL = "http://coms-309-034.class.las.iastate.edu:8080/leaderboard";
    private JSONArray leaderboardData;
    private boolean attemptedLeaderboardRefresh = false;
    private RecyclerView recyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private Button dailyButton;
    private Button weeklyButton;
    private Button monthlyButton;
    private Button yearlyButton;
    private Button lifetimeButton;
    private LeaderboardEnum time_frame = LeaderboardEnum.DAILY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        makeLeaderboardRequest();

        recyclerView = findViewById(R.id.leaderboard_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dailyButton = findViewById(R.id.daily_button);
        weeklyButton = findViewById(R.id.weekly_button);
        monthlyButton = findViewById(R.id.monthly_button);
        yearlyButton = findViewById(R.id.yearly_button);
        lifetimeButton = findViewById(R.id.lifetime_button);

        dailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaderboardAdapter.time_frame = LeaderboardEnum.DAILY;
                leaderboardAdapter.notifyDataSetChanged();
            }
        });
        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaderboardAdapter.time_frame = LeaderboardEnum.WEEKLY;
                leaderboardAdapter.notifyDataSetChanged();

            }
        });
        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaderboardAdapter.time_frame = LeaderboardEnum.MONTHLY;
                leaderboardAdapter.notifyDataSetChanged();

            }
        });
        yearlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaderboardAdapter.time_frame = LeaderboardEnum.YEARLY;
                leaderboardAdapter.notifyDataSetChanged();

            }
        });
        lifetimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaderboardAdapter.time_frame = LeaderboardEnum.LIFETIME;
                leaderboardAdapter.notifyDataSetChanged();

            }
        });
    }

    public void makeLeaderboardRequest() {
        JsonArrayRequest leaderboardRequest = new JsonArrayRequest(
                Request.Method.GET,
                SERVER_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        leaderboardData = response;
                        leaderboardAdapter = new LeaderboardAdapter(leaderboardData, LeaderboardEnum.DAILY);
                        recyclerView.setAdapter(leaderboardAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!attemptedLeaderboardRefresh) {
                            Toast.makeText(getApplicationContext(), "Leaderboard failed to load, reattempting to get data from server", Toast.LENGTH_LONG).show();
                            makeLeaderboardRequest();
                            attemptedLeaderboardRefresh = true;
                        } else {
                            Toast.makeText(getApplicationContext(), "Unable to reach server", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(leaderboardRequest);
    }
}
