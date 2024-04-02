package com.example.androidexample;

import android.app.Dialog;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private ArrayList<UserFriend> friendsList;
    private RecyclerView recyclerView;
    ImageView imgView;
    ImageButton addFriends;
    //Button addFriend = findViewById(R.id.addFriend);

    TextView questionsAnswered, achievementsUnlocked, userBiography, username, friendsListText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        friendsList = new ArrayList<>();

        imgView = findViewById(R.id.imgView);
        addFriends = findViewById(R.id.addFriends);
        questionsAnswered = findViewById(R.id.questionsAnswered);
        achievementsUnlocked = findViewById(R.id.AcheivementsUnlocked);
        userBiography = findViewById(R.id.userBiography);
        username = findViewById(R.id.username);
        friendsListText = findViewById(R.id.freindsListText);
        recyclerView = findViewById(R.id.friendList);

        questionsAnswered.setText("Add Friends");
        achievementsUnlocked.setText("");
        userBiography.setText("I am the real alok");
        username.setText("AlokShrestha");
        //friendsListText.setText("Friends:");

        setFriendInfo();
        setAdapter();

        addFriends.setOnClickListener(view -> {
            addFriendsDialog();
        });

    }

    private void addFriendsDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_friend);
        dialog.show();

        Button addFriend = dialog.findViewById(R.id.addFriend);
        Button cancelButton = dialog.findViewById(R.id.Cancel);
        EditText friendUsernameEdit = dialog.findViewById(R.id.friendUsernameEdit);

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        addFriend.setOnClickListener(v -> {

            String friend = friendUsernameEdit.getText().toString();
            addFriends(friend);
            dialog.dismiss();
        });
    }

    private void addFriends(String friendId) {
        // Check if any field is empty
//        if (userId.isEmpty() || friendId.isEmpty()) {
//            Toast.makeText(this, "No username added", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // Create a JSONObject with the request parameters
//        JSONObject requestBody = new JSONObject();
//        try {
//            requestBody.put("userId", userId);
//            requestBody.put("friendId", friendId);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = "http://your_api_endpoint.com/" + userId + "/addFriend/" + friendId;
//
//        // Request a JSON response from the provided URL
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // Handle successful response
//                        try {
//                            String message = response.getString("message");
//                            Toast.makeText(AddFriendActivity.this, message, Toast.LENGTH_SHORT).show();
//                            finish(); // Close the activity after adding friend
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Log.e(TAG, "Error parsing response: " + e.getMessage());
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // Handle error response
//                Toast.makeText(AddFriendActivity.this, "Error adding friend", Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "Error adding friend: " + error.getMessage());
//            }
//        });
//
//        // Add the request to the RequestQueue.
//        queue.add(jsonObjectRequest);

    }

    private void setAdapter() {
        FriendsListAdapter adapter = new FriendsListAdapter(friendsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setFriendInfo() {
        friendsList.add(new UserFriend("Alok1", "This is the real alok", null));
        friendsList.add(new UserFriend("Alok2", "This is actually the real alok", null));
        friendsList.add(new UserFriend("Alok3", "Nah, This is the real alok!", null));
        friendsList.add(new UserFriend("Mahdi", "I will give Owais an A on this demo.", null));
        friendsList.add(new UserFriend("Alok4", "Nope, its actually me", null));
        friendsList.add(new UserFriend("Alok5", "Alok4 is lying.", null));
        friendsList.add(new UserFriend("Osamson", "I agree with Alok5", null));
        friendsList.add(new UserFriend("Aldaco", "Hello, I am definitly the real Dr. Aldaco", null));
        friendsList.add(new UserFriend("Alok8", "I am the real alok", null));
        friendsList.add(new UserFriend("Alok9", "Nah.", null));

    }

}