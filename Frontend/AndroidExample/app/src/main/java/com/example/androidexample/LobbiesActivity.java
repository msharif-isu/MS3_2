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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
    private LobbyPlayerAdapter adapter;
    private TextView msgTv;

    private List<String> playerUsernames = new ArrayList<>();
    private String selectedNewHost;
    private WebSocketManager webSocketManager;

    private String backendUrl = RequestURLs.SERVER_HTTP_URL + "/";
    private boolean isInLobby = false;
    private boolean joinedLobby = false;
    private ArrayAdapter<String> playerAdapter;

    private String currentHostUsername;

    private boolean isHost = false;

    private Button startGame, changeHost;
    private TextView changeHostText;
    private Spinner changeHostSpinner;
    Slider numQuestionsSlider;
    TextView numQuestions;
    TextView numQuestionsText, category;
    EditText enterCategory;

    private String questionID;
    private boolean gameStarted = false;

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
        webSocketManager = new WebSocketManager();

        adapter = new LobbyPlayerAdapter(getApplicationContext(), friendsList, new LobbyPlayerAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                UserFriend friend = friendsList.get(position);
                String friendName = friend.getUsername();
//                getUserIdByUsername(friendName, roomId);
            }
        }, isHost);

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
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        getLobbyDetails(roomId);
        SharedPreferences.Editor editor = getSharedPreferences("roomID", MODE_PRIVATE).edit();
        editor.putInt("ROOM_ID", (int) roomId);
        editor.apply();

        Log.e("Room id", "Room ID is currently " + roomId);

        Button joinRoom = dialog.findViewById(R.id.buttonJoinRoom);
        Button leaveRoom = dialog.findViewById(R.id.buttonLeaveRoom);
        startGame = dialog.findViewById(R.id.buttonStartGame);
        changeHost = dialog.findViewById(R.id.buttonChangeHost);
        changeHostSpinner = dialog.findViewById(R.id.changeHostSpinner);
        changeHostText = dialog.findViewById(R.id.textView11);
        RecyclerView playerListRecyclerView = dialog.findViewById(R.id.recycleView);
        numQuestionsSlider = dialog.findViewById(R.id.numQuestionsSlider);
        numQuestions = dialog.findViewById(R.id.numQuestions);
        numQuestionsText = dialog.findViewById(R.id.numQuestionsText);
        category = dialog.findViewById(R.id.textView8);
        enterCategory = dialog.findViewById(R.id.editQuestionCatagory);
        numQuestions.setText(String.valueOf(3));
        numQuestionsSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser) {
                numQuestions.setText(String.valueOf((int) value));
            }
        });


        // A little sketchy, but i need a websocket room and i didnt want to make another class.
        String chatUrl = RequestURLs.SERVER_WEBSOCKET_URL_MULTIPLAYER + "/chat/" + roomId * -1 + "/" + username;
        WebSocketManager.getInstance().connectWebSocket(chatUrl);
        WebSocketManager.getInstance().setWebSocketListener(LobbiesActivity.this);
        if (isHost) {
            startGame.setVisibility(View.VISIBLE);
            changeHost.setVisibility(View.VISIBLE);
            changeHostSpinner.setVisibility(View.VISIBLE);
            changeHostText.setVisibility(View.VISIBLE);
            numQuestionsSlider.setVisibility(View.VISIBLE);
            numQuestionsText.setVisibility(View.VISIBLE);
            numQuestions.setVisibility(View.VISIBLE);
            enterCategory.setVisibility(View.VISIBLE);
            category.setVisibility(View.VISIBLE);
        } else {
            startGame.setVisibility(View.GONE);
            changeHost.setVisibility(View.GONE);
            changeHostSpinner.setVisibility(View.GONE);
            changeHostText.setVisibility(View.GONE);
            numQuestionsSlider.setVisibility(View.GONE);
            numQuestionsText.setVisibility(View.GONE);
            numQuestions.setVisibility(View.GONE);
            enterCategory.setVisibility(View.GONE);
            category.setVisibility(View.GONE);
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new LobbyPlayerAdapter(getApplicationContext(), friendsList, new LobbyPlayerAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                UserFriend friend = friendsList.get(position);
                String friendName = friend.getUsername();
                getUserIdByUsername(friendName, roomId);
            }
        }, isHost);


        playerListRecyclerView.setLayoutManager(layoutManager);
        playerListRecyclerView.setItemAnimator(new DefaultItemAnimator());
        playerListRecyclerView.setAdapter(adapter);
        changeHostSpinner.setAdapter(getUsernamesInLobby(adapter));
        // TODO: Start game
        startGame.setOnClickListener(v -> {
            if (isInLobby && joinedLobby) {
                joinLobby(roomId, userId);
                getLobbyDetails(roomId);
                if (enterCategory.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please choose a catagory", Toast.LENGTH_SHORT).show();
                } else {
                    createMultiplayer(roomId, enterCategory.getText().toString(), numQuestions.getText().toString(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String questionID) {
                                    Log.e("QuestionIDs in Lobbies", questionID); // Verify questionID
                                    WebSocketManager.getInstance().sendMessage("lobbyStart!");
                                    WebSocketManager.getInstance().closeWebSocket();
                                    beginGame(roomId, questionID); // Start MultiplayerActivity with questionID
                                    gameStarted = true;
                                }
                            });
                }
            } else {
                Toast.makeText(LobbiesActivity.this, "You must join a lobby first", Toast.LENGTH_SHORT).show();
            }
        });
        leaveRoom.setOnClickListener(v -> {
            leaveRoom(roomId, userId);
            isInLobby = false;
            isHost = false;
            adapter.updateHostStatus(isHost);
            WebSocketManager.getInstance().sendMessage("leftLobby");
            if (isHost) {
                WebSocketManager.getInstance().sendMessage("changeHost");
                isHost = false;
            }
            WebSocketManager.getInstance().closeWebSocket();
            dialog.dismiss();
        });
        joinRoom.setOnClickListener(v -> {
            joinLobby(roomId, userId);
            isInLobby = true;
            getLobbyDetails(roomId);
            WebSocketManager.getInstance().sendMessage("joinedLobby");
            refreshLobbyList();
        });
        changeHost.setOnClickListener(v -> {
            if (selectedNewHost != null) {
                isHost = false;
                changeHost(roomId, selectedNewHost);
                WebSocketManager.getInstance().sendMessage("changeHost to " + selectedNewHost);
                adapter.updateHostStatus(isHost);
                startGame.setVisibility(View.GONE);
                changeHost.setVisibility(View.GONE);
                changeHostSpinner.setVisibility(View.GONE);
                changeHostText.setVisibility(View.GONE);
                numQuestionsSlider.setVisibility(View.GONE);
                numQuestionsText.setVisibility(View.GONE);
                numQuestions.setVisibility(View.GONE);
                enterCategory.setVisibility(View.GONE);
                category.setVisibility(View.GONE);
            } else {
                Toast.makeText(LobbiesActivity.this, "Please select a player to become the new host", Toast.LENGTH_SHORT).show();
            }
        });
        changeHostSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedNewHost = playerUsernames.get(position);
                Log.e("Bruh", "onItemSelected: " + selectedNewHost);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedNewHost = null;
                Log.e("Bruh", "BRUHAHUHSUHRAUSHRUHSALFASKLJF");
            }
        });

    }

    private void createMultiplayer(long roomId, String topic, String numQuestions, Response.Listener<String> questionIdListener) {
        String url = backendUrl + "multiplayer/" + roomId + "/" + topic + "/" + numQuestions;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            StringBuilder questionIdsBuilder = new StringBuilder();
                            for (int i = 0; i < response.length(); i++) {
                                if (i > 0) {
                                    questionIdsBuilder.append("-");
                                }
                                questionIdsBuilder.append(response.getInt(i));
                            }
                            questionID = questionIdsBuilder.toString();
                            // Invoke the callback with the retrieved questionID
                            questionIdListener.onResponse(questionID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing error
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(LobbiesActivity.this, "Failed to create multiplayer: " + error.getMessage() + url, Toast.LENGTH_SHORT).show();
                        Log.d("Multiplayer", url);
                    }
                });
        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(request);
    }

    private ArrayAdapter<String> getUsernamesInLobby(LobbyPlayerAdapter lobbyPlayerAdapter) {
        playerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, playerUsernames);
        playerUsernames.clear();
        for (int i = 0; i < lobbyPlayerAdapter.getItemCount(); i++) {
            String username = lobbyPlayerAdapter.getUsernameByIndex(i);
            if (username != null) {
                playerUsernames.add(username);
                Log.d("Username In lobby", username);
            }
        }
        playerAdapter.notifyDataSetChanged();
        return playerAdapter;
    }


    private void changeHost(long roomId, String newHostUsername) {
        String url = backendUrl + "changeHost/" + roomId + "/" + newHostUsername;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            if (message.equals("success")) {
                                Toast.makeText(LobbiesActivity.this, "Host changed successfully", Toast.LENGTH_SHORT).show();
                                isHost = false;
                                adapter.updateHostStatus(isHost);
                            } else {
                                Toast.makeText(LobbiesActivity.this, "Failed to change host", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LobbiesActivity.this, "Failed to change host: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(request);
    }


    private void beginGame(long roomId, String questionID) {
        Intent intent = new Intent(LobbiesActivity.this, MultiplayerActivity.class);
        intent.putExtra("ROOM_ID", roomId);
        intent.putExtra("QUESTION_ID", questionID);
        SharedPreferences.Editor editor = getSharedPreferences("QuestionIds", MODE_PRIVATE).edit();
        editor.putString("QuestionIds", questionID);
        editor.apply();
//        SharedPreferences s
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
                            adapter.notifyDataSetChanged(); // Update RecyclerView
                            // Populate spinner after data is fetched
                            getUsernamesInLobby(adapter); // This also updates the spinner adapter
                            isHost = lobby.getHost().equals(username);
                            adapter.updateHostStatus(isHost);


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
        getLobbyDetails(roomId);
    }

    private void createLobbyDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.create_room_dialog);
        dialog.show();

        Button createLobby = dialog.findViewById(R.id.createLobby);
        Button cancelButton = dialog.findViewById(R.id.Cancel);
        TextView lobbySizeTextView = dialog.findViewById(R.id.lobbySize);
        Slider slider = dialog.findViewById(R.id.slider);
        lobbySizeTextView.setText(String.valueOf(2));
        slider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(Slider slider, float value, boolean fromUser) {
                lobbySizeTextView.setText(String.valueOf((int) value));
            }
        });
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        createLobby.setOnClickListener(v -> {
            int lobbySize = (int) slider.getValue();
            if (lobbySize != 0 && lobbySize != 1) {
                Toast.makeText(this, "Creating lobby with size: " + lobbySize, Toast.LENGTH_SHORT).show();
                createLobby(lobbySize, userId);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Can't create lobby with size 0 or 1", Toast.LENGTH_SHORT).show();
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
        isHost = true;

    }

    private Lobby parseLobbyResponse(JSONObject response) throws JSONException {
        String playerUsername = username;
        int roomSize = response.getInt("roomSize");
        int playerCount = response.getInt("playerCount");
        String host = response.getString("host");
        currentHostUsername = host;
        boolean finished = response.getBoolean("finished");
        long lobbyId = response.getLong("id");
        if (username.equals(host)) {
            WebSocketManager.getInstance().sendMessage("changeHost to " + username);
            isHost = true;
            adapter.updateHostStatus(isHost);
        }
        // Parse players' details
        List<UserFriend> players = new ArrayList<>();
        JSONArray playersArray = response.getJSONArray("players");
        for (int i = 0; i < playersArray.length(); i++) {
            JSONObject playerObject = playersArray.getJSONObject(i);
            String username = playerObject.getString("username");
            String bio = playerObject.getString("bio");
            UserFriend player = new UserFriend(username, bio, null);
            players.add(player);
            if (player.getUsername().equals(playerUsername)) {
                joinedLobby = true;
            }
        }
        return new Lobby(host, playerCount, roomSize, lobbyId, players, host);
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
                            lobbyList.add(new Lobby(host, playerCount, roomSize, id, null, null));
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
            SharedPreferences sharedPreferences = getSharedPreferences("roomID", MODE_PRIVATE);
            int roomId = sharedPreferences.getInt("ROOM_ID", -1);
            String s = msgTv.getText().toString();
            msgTv.setText(s + "\n" + message);
            if (message.contains("lobbyStart!")) {
                if (!gameStarted) {
                    beginGame(roomId, questionID);
                }
            } else if (message.contains("joinedLobby")) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getLobbyDetails(roomId);
                refreshLobbyList();
            } else if (message.contains("leftLobby")) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (message.contains(currentHostUsername)) {
                    if (username.equals(currentHostUsername)) {
                        startGame.setVisibility(View.VISIBLE);
                        changeHost.setVisibility(View.VISIBLE);
                        changeHostSpinner.setVisibility(View.VISIBLE);
                        changeHostText.setVisibility(View.VISIBLE);
                        numQuestionsSlider.setVisibility(View.VISIBLE);
                        numQuestionsText.setVisibility(View.VISIBLE);
                        numQuestions.setVisibility(View.VISIBLE);
                        enterCategory.setVisibility(View.VISIBLE);
                        category.setVisibility(View.VISIBLE);
                        isHost = true;
                        adapter.updateHostStatus(isHost);
//                        adapter.notifyDataSetChanged();
                    }
                }

                getLobbyDetails(roomId);
                refreshLobbyList();
            } else if (message.endsWith("changeHost to " + username)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startGame.setVisibility(View.VISIBLE);
                changeHost.setVisibility(View.VISIBLE);
                changeHostSpinner.setVisibility(View.VISIBLE);
                changeHostText.setVisibility(View.VISIBLE);
                enterCategory.setVisibility(View.VISIBLE);
                category.setVisibility(View.VISIBLE);
                numQuestionsSlider.setVisibility(View.VISIBLE);
                numQuestionsText.setVisibility(View.VISIBLE);
                numQuestions.setVisibility(View.VISIBLE);
                isHost = false;
                adapter.updateHostStatus(isHost);
//                adapter.notifyDataSetChanged();
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
        if (webSocketManager != null) {
            webSocketManager.closeWebSocket();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        // Close WebSocket connection
        WebSocketManager.getInstance().closeWebSocket();
    }


}
