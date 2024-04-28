package com.example.androidexample;

import url.RequestURLs;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AchievementService {

    private static final String BASE_URL = RequestURLs.SERVER_HTTP_URL;
    private RequestQueue requestQueue;


    public interface AchievementListener {
        void onSuccess(List<AchievementsList> achievements);

        void onError(String message);
    }

    public void getAllAchievements(final AchievementListener listener) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL + "/achievement", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<AchievementsList> achievements = new ArrayList<>();
                try {
                    boolean account = response.getBoolean("account");
                    boolean singlePlayer = response.getBoolean("singlePlayer");
                    boolean jeopardy = response.getBoolean("jeopardy");
                    boolean multiPlayer = response.getBoolean("multiPlayer");
                    AchievementsList achievement = new AchievementsList(account, singlePlayer, jeopardy, multiPlayer);
                    achievements.add(achievement);
                    listener.onSuccess(achievements);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.onError("Error parsing achievements");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onError("Error fetching achievements");
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void addAchievement(boolean account, boolean singlePlayer, boolean jeopardy, boolean multiPlayer, final AchievementListener listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("account", account);
            jsonObject.put("singlePlayer", singlePlayer);
            jsonObject.put("jeopardy", jeopardy);
            jsonObject.put("multiPlayer", multiPlayer);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError("Error creating JSON object");
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BASE_URL + "/achievement", jsonObject, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError("Error adding achievement: " + error.getMessage());
            }
        });
        requestQueue.add(request);
    }

    public void changeAchievement(int id, boolean account, boolean singlePlayer, boolean jeopardy, boolean multiPlayer, final VolleyCallback listener) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("account", account);
            jsonObject.put("singlePlayer", singlePlayer);
            jsonObject.put("jeopardy", jeopardy);
            jsonObject.put("multiPlayer", multiPlayer);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError("Error creating JSON object");
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, BASE_URL + "/achievement/" + id, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            if (message.equals("success")) {
                                listener.onSuccess(); // Assuming you have a onSuccess method in VolleyCallback for success cases
                            } else {
                                listener.onError("Failed to update achievement");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError("Error parsing response");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                listener.onError("Error updating achievement: " + error.getMessage());
            }
        });

        requestQueue.add(request);
    }

    public interface VolleyCallback {
        void onSuccess();
        void onError(String message); // This was missing
    }

}