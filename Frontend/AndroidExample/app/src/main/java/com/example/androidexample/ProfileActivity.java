package com.example.androidexample;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.util.Log;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import url.RequestURLs;

public class ProfileActivity extends AppCompatActivity {

    private ArrayList<UserFriend> friendsList;
    private RecyclerView recyclerView;
    ImageView imgView;
    ImageButton addFriends, editBioButton;
    //Button addFriend = findViewById(R.id.addFriend);
    TextView questionsAnswered, achievementsUnlocked, userBiography, usernameText, friendsListText;
    private String username;

    private int userId;
    private String backendUrl = RequestURLs.SERVER_HTTP_URL + "/";
    //"http://localhost:8080/";
    //RequestURLs.SERVER_HTTP_URL;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        username = prefs.getString("USERNAME", "");
        userId = prefs.getInt("USER_ID", 0);

        friendsList = new ArrayList<>();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        imgView = findViewById(R.id.imgView);
        addFriends = findViewById(R.id.addFriends);
        questionsAnswered = findViewById(R.id.questionsAnswered);
        achievementsUnlocked = findViewById(R.id.AcheivementsUnlocked);
        userBiography = findViewById(R.id.userBiography);
        usernameText = findViewById(R.id.username);
        friendsListText = findViewById(R.id.freindsListText);
        recyclerView = findViewById(R.id.friendList);
        editBioButton = findViewById(R.id.editBioButton);

        questionsAnswered.setText("Add Friends");
        achievementsUnlocked.setText("");
        getBio();
//        userBiography.setText(temp);
        usernameText.setText(username);
        //friendsListText.setText("Friends:");


        setFriendInfo();
        setAdapter();

        addFriends.setOnClickListener(view -> {
            addFriendsDialog();
        });
        editBioButton.setOnClickListener(view -> {
            editButtonDialog();
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setFriendInfo();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    private void editButtonDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.edit_bio);
        dialog.show();

        Button save = dialog.findViewById(R.id.save);
        Button cancelButton = dialog.findViewById(R.id.cancel);
        EditText bioEdit = dialog.findViewById(R.id.bioEdit);

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        save.setOnClickListener(v -> {

            String newBio = bioEdit.getText().toString();
            editBio(newBio);
            dialog.dismiss();
        });


    }

    private void editBio(String newBio) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("bio", newBio);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = backendUrl + "editBio/" + username + "/" + newBio;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String message = response.getString("message");
                    userBiography.setText(newBio);
                    Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                    Log.e("ProfileActivity", "find this:" + newBio);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ProfileActivity", "Error parsing response: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ProfileActivity", "Error editing bio: " + error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
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
            addFriends(friend, dialog);
            dialog.dismiss();
        });
    }

    private void addFriends(String friendId, Dialog dialog) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("userId", userId);
            requestBody.put("friendId", friendId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = backendUrl + userId + "/addFriend/" + friendId;
        Log.e("ProfileActivity", "URL = " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String message = response.getString("message");
                    Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                    Toast.makeText(ProfileActivity.this, "Friend added", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ProfileActivity", "Error parsing response: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Toast.makeText(ProfileActivity.this, "Error adding friend", Toast.LENGTH_SHORT).show();
                Log.e("ProfileActivity", "Error adding friend: " + error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void setAdapter() {
        FriendsListAdapter adapter = new FriendsListAdapter(friendsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setFriendInfo() {
//        friendsList.add(new UserFriend("Alok1", "This is the real alok", null));
//        friendsList.add(new UserFriend("Alok2", "This is actually the real alok", null));
//        friendsList.add(new UserFriend("Alok3", "Nah, This is the real alok!", null));
//        friendsList.add(new UserFriend("Mahdi", "I will give Owais an A on this demo.", null));
//        friendsList.add(new UserFriend("Alok4", "Nope, its actually me", null));
//        friendsList.add(new UserFriend("Alok5", "Alok4 is lying.", null));
//        friendsList.add(new UserFriend("Osamson", "I agree with Alok5", null));
//        friendsList.add(new UserFriend("Aldaco", "Hello, I am definitly the real Dr. Aldaco", null));
//        friendsList.add(new UserFriend("Alok8", "I am the real alok", null));
//        friendsList.add(new UserFriend("Alok9", "Nah.", null));
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = backendUrl + "friends/" + userId;
        Log.d("ProfileActivity", "Fetching friend details: " + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String jsonResponse = response.toString();
                Log.d("ProfileActivity", "Friend Details JSON Response: " + jsonResponse);
                try {
                    // Clear the existing data in the friendsList. This avoids duplicate entries.
                    friendsList.clear();
                    JSONArray userArray = response.getJSONArray("user");
                    for (int i = 0; i < userArray.length(); i++) {
                        JSONObject userObject = userArray.getJSONObject(i);
                        String username = userObject.getString("username");
                        String bio = userObject.isNull("bio") ? "" : userObject.getString("bio");
                        String filePath = userObject.optString("filePath", "");
                        Log.d("ProfileActivity", "Username: " + username);
                        Log.d("ProfileActivity", "Bio: " + bio);
                        Log.d("ProfileActivity", "File Path: " + filePath);
                        friendsList.add(new UserFriend(username, bio, filePath));
                    }

                    // Notify the adapter that the data has changed
                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("ProfileActivity", "Error parsing friend details JSON: " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ProfileActivity", "Error fetching friend details: " + error.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void getBio() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = backendUrl + "users/getBio/" + username;
        Log.e("ProfileActivity", "Bio url:" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                userBiography.setText(response);
                Log.e("ProfileActivity", "Bio received:" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ProfileActivity", "Error fetching bio: " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }


}