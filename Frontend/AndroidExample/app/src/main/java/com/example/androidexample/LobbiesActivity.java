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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.slider.Slider;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import url.RequestURLs;

public class LobbiesActivity extends AppCompatActivity implements WebSocketListener {

    //TODO MAKE X BUTTON REMOVE USERS FROM LOBBY, ONLY ACCESSABLE BY HOST
    private RecyclerView recyclerView;
    private LobbyAdapter lobbyAdapter;
    private ArrayList<Lobby> lobbyList;
    private ArrayList<UserFriend> friendsList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton createLobbyButton;
    private String username;
    private int userId;
    private FriendsListAdapter adapter;
    private TextView msgTv;

    private WebSocketManager webSocketManager;

    private String backendUrl = RequestURLs.SERVER_HTTP_URL + "/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        username = prefs.getString("USERNAME", "");
        userId = prefs.getInt("USER_ID", 0);

        recyclerView = findViewById(R.id.lobbyList);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        createLobbyButton = findViewById(R.id.createLobbyButton);

        friendsList = new ArrayList<>();
        lobbyList = new ArrayList<>();
        lobbyAdapter = new LobbyAdapter(lobbyList, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(lobbyAdapter);

        msgTv = findViewById(R.id.textView9);


        refreshLobbyList();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLobbyList();
            }
        });

        createLobbyButton.setOnClickListener(view -> {
            createLobbyDialog();
        });
    }

    void joinLobbyDialog(long roomId) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_lobbies);
        dialog.show();

        getLobbyDetails(roomId);


        SharedPreferences.Editor editor = getSharedPreferences("roomID", MODE_PRIVATE).edit();
        editor.putInt("ROOM_ID", (int)roomId);
        editor.apply();

        Button joinRoom = dialog.findViewById(R.id.buttonJoinRoom);
        Button leaveRoom = dialog.findViewById(R.id.buttonLeaveRoom);
        Button startGame = dialog.findViewById(R.id.buttonStartGame);
        RecyclerView playerListRecyclerView = dialog.findViewById(R.id.recycleView);

        // A little sketchy, but i need a websocket room and i didnt want to make another class.
        String chatUrl = RequestURLs.SERVER_WEBSOCKET_URL_MULTIPLAYER + "/chat/" + roomId * -1 + "/" + username;
        WebSocketManager.getInstance().connectWebSocket(chatUrl);
        WebSocketManager.getInstance().setWebSocketListener(LobbiesActivity.this);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new FriendsListAdapter(friendsList, new FriendsListAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                UserFriend friend = friendsList.get(position);
                String friendName = friend.getUsername();
                getUserIdByUsername(friendName, roomId); // Pass roomId to the method
            }

        });

        playerListRecyclerView.setLayoutManager(layoutManager);
        playerListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        playerListRecyclerView.setAdapter(adapter);

        // TODO: Start game
        startGame.setOnClickListener(v -> {
            joinLobby(roomId, userId);
            getLobbyDetails(roomId);
            WebSocketManager.getInstance().sendMessage("lobbyStart!");
//            beginGame(roomId);
        });
        leaveRoom.setOnClickListener(v -> {
            leaveRoom(roomId, userId);
            dialog.dismiss();
        });
        joinRoom.setOnClickListener(v -> {
            joinLobby(roomId, userId);
            getLobbyDetails(roomId);
            //dialog.dismiss();
        });


    }

    private void beginGame(long roomId) {
        Intent intent = new Intent(LobbiesActivity.this, MultiplayerActivity.class);
        intent.putExtra("ROOM_ID", roomId);
        startActivity(intent);
    }

    private void leaveRoom(long roomId, int userId) {
        String url = backendUrl + "leave/" + roomId + "/" + userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(LobbiesActivity.this, "Left the room successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LobbiesActivity.this, "Failed to leave the room: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(request);
    }


    private void getLobbyDetails(long roomId) {
        String url = backendUrl + "lobbies/" + roomId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            friendsList.clear();
                            Lobby lobby = parseLobbyResponse(response);

                            // Parse players from the JSON response
                            JSONArray playersArray = response.getJSONArray("players");
                            List<UserFriend> players = new ArrayList<>();
                            for (int i = 0; i < playersArray.length(); i++) {
                                JSONObject playerObject = playersArray.getJSONObject(i);
                                String username = playerObject.getString("username");
                                String bio = playerObject.getString("bio");
                                UserFriend player = new UserFriend(username, bio, null);
                                Log.d("PlayerInfo", "Username: " + username + ", Bio: " + bio);
                                friendsList.add(new UserFriend(username, bio, null));
                                players.add(player);
                            }

                            // Set players to the lobby object
                            lobby.setPlayers(players);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LobbiesActivity.this, "Failed to parse lobby details", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("LobbiesActivity", "Lobby details response: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LobbiesActivity.this, "Failed to get lobby details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("LobbiesActivity", "Error getting lobby details: " + error.toString());
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }


    private void joinLobby(long roomId, int userId) {
        String url = backendUrl + "joinRoom/" + roomId + "/" + userId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Lobby lobby = parseLobbyResponse(response);
                    if (lobby != null) {
                        //TODO FIX THIS Toast.makeText(LobbiesActivity.this, "Joined lobby successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(LobbiesActivity.this, "Failed to join lobby", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LobbiesActivity.this, "Failed to join lobby: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    private void createLobbyDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.create_room_dialog);
        dialog.show();

        Button createLobby = dialog.findViewById(R.id.createLobby);
        Button cancelButton = dialog.findViewById(R.id.Cancel);
        TextView lobbySizeTextView = dialog.findViewById(R.id.lobbySize);
        Slider slider = dialog.findViewById(R.id.slider);
        lobbySizeTextView.setText(String.valueOf(slider.getValue()));
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser) {
                lobbySizeTextView.setText(String.valueOf((int) value));
            }
        });
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        createLobby.setOnClickListener(v -> {
            int lobbySize = (int) slider.getValue();
            if (lobbySize != 0) {
                Toast.makeText(this, "Creating lobby with size: " + lobbySize, Toast.LENGTH_SHORT).show();
                createLobby(lobbySize, userId);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Can't create lobby with size 0", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createLobby(int roomSize, int userId) {
        String url = backendUrl + "create/" + userId + "/" + roomSize;
        JSONObject requestBody = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Lobby lobby = parseLobbyResponse(response);
                    lobbyList.add(lobby);
                    lobbyAdapter.notifyDataSetChanged();
                    Toast.makeText(LobbiesActivity.this, "Lobby created successfully", Toast.LENGTH_SHORT).show();
                    joinLobbyDialog(lobby.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LobbiesActivity.this, "Failed to create lobby: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(request);
    }

    private Lobby parseLobbyResponse(JSONObject response) throws JSONException {
        int roomSize = response.getInt("roomSize");
        int playerCount = response.getInt("playerCount");
        String host = response.getString("host");
        boolean finished = response.getBoolean("finished");
        long lobbyId = response.getLong("id");
        // Parse players' details
        List<UserFriend> players = new ArrayList<>();
        JSONArray playersArray = response.getJSONArray("players");
        for (int i = 0; i < playersArray.length(); i++) {
            JSONObject playerObject = playersArray.getJSONObject(i);
            String username = playerObject.getString("username");
            String bio = playerObject.getString("bio");
            UserFriend player = new UserFriend(username, bio, null);
            players.add(player);
        }
        return new Lobby(host, playerCount, roomSize, lobbyId, players);
    }


    private void refreshLobbyList() {
        swipeRefreshLayout.setRefreshing(true);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, backendUrl + "lobbies", null, new Response.Listener<JSONArray>() {
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
                        long id = jsonObject.getLong("id");
                        //boolean started = jsonObject.getBoolean("started");
                        if (finished != true) {
                            lobbyList.add(new Lobby(host, playerCount, roomSize, id, null));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                lobbyAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LobbiesActivity", "Error: " + error.toString());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        queue.add(jsonArrayRequest);
    }

    private void getUserIdByUsername(String username, long roomId) {
        String url = backendUrl + "users/getIdByUsername/" + username;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int userIdToDelete = Integer.parseInt(response);
                        leaveRoom(roomId, userIdToDelete);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LobbiesActivity.this, "Failed to get user ID: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("LobbiesActivity", "Error getting user ID: " + error.toString());
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }



    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "\n" + message);
            if (message.contains("lobbyStart!")) {
                SharedPreferences sharedPreferences = getSharedPreferences("roomID", MODE_PRIVATE);
                int roomId = sharedPreferences.getInt("ROOM_ID", -1);
                beginGame(roomId);
            }
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        // WebSocket connection opened
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        // WebSocket connection closed
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    @Override
    public void onWebSocketError(Exception ex) {
        // WebSocket error occurred
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close WebSocket connection
        webSocketManager.closeWebSocket();
    }

    public void onBackPressed() {
        super.onBackPressed();
        // Close WebSocket connection
        WebSocketManager.getInstance().closeWebSocket();
    }


}
