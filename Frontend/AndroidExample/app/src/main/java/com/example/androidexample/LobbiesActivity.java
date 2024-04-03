package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import url.RequestURLs;

public class LobbiesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LobbyAdapter lobbyAdapter;
    private ArrayList<Lobby> lobbyList;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String backendUrl = RequestURLs.SERVER_HTTP_URL + "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        recyclerView = findViewById(R.id.lobbyList);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        lobbyList = new ArrayList<>();
        lobbyAdapter = new LobbyAdapter(lobbyList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(lobbyAdapter);

        refreshLobbyList();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLobbyList();
            }
        });
    }

    private void refreshLobbyList() {
        swipeRefreshLayout.setRefreshing(true);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                backendUrl + "lobbies",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        lobbyList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Log.e("LobbiesActivity", "Response: " + jsonObject.toString());
                                int roomSize = jsonObject.getInt("roomSize");
                                int playerCount = jsonObject.getInt("playerCount");
                                String host = jsonObject.getString("host");
                                boolean finished = jsonObject.getBoolean("finished");
                                //boolean started = jsonObject.getBoolean("started");
                                // TODO parse over players.
                                if (finished != true) {
                                    lobbyList.add(new Lobby(host, roomSize, playerCount));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        lobbyAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LobbiesActivity", "Error: " + error.toString());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        queue.add(jsonArrayRequest);
    }
}
