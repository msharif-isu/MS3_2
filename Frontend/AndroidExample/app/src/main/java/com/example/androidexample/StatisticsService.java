package com.example.androidexample;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import url.RequestURLs;

public class StatisticsService {

    private static final String BASE_URL = RequestURLs.SERVER_HTTP_URL + "/";

    private RequestQueue requestQueue;
    private Context context;

    public StatisticsService(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getUserStats(String username, final StatsCallback callback) {
        String url = BASE_URL + "userStats/" + username;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            UserStats userStats = parseUserStats(response);
                            callback.onSuccess(userStats);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Error parsing response.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("Error getting user stats.");
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    public void updateStats(String username, int correct, int incorrect, int total, final StatsCallback callback) {
        String url = BASE_URL + username + "/updateStats/" + correct + "/" + incorrect + "/" + total;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            UserStats userStats = parseUserStats(response);
                            callback.onSuccess(userStats);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Error parsing response.");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("Error updating user stats.");
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private UserStats parseUserStats(JSONObject json) throws JSONException {
        UserStats userStats = new UserStats();
        userStats.setTotalCorrect(json.getInt("totalCorrect"));
        userStats.setTotalIncorrect(json.getInt("totalIncorrect"));
        userStats.setTotalAnswered(json.getInt("totalAnswered"));
        userStats.setWins(json.getInt("wins"));
        userStats.setLosses(json.getInt("losses"));
        userStats.setWinStreak(json.getInt("winStreak"));
        userStats.setQuestionsSumbitted(json.getInt("questionsSumbitted"));
        userStats.setGamesPlayed(json.getInt("gamesPlayed"));
        userStats.setNumberOfFreinds(json.getInt("numberOfFreinds"));
        return userStats;
    }

    public interface StatsCallback {
        void onSuccess(UserStats userStats);

        void onError(String errorMessage);
    }
}
