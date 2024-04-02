package com.example.androidexample;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.List;
import java.util.Optional;

import url.RequestURLs.*;

public class QueryActivity extends AppCompatActivity {
    private boolean attemptedRefresh = false;
    private List<Question> questionDataset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        RecyclerView questionList = findViewById(R.id.query_list);
        EditText searchBar = findViewById(R.id.query_search_bar);
    }

    /**
     * Makes a request to the server to get all questions based on filters
     */
    public void requestQuestions(Optional<String> type, Optional<Boolean> isUserGenerated) {
        String url = "";

        if (type.isPresent()) {

        }

        JsonArrayRequest questionRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!attemptedRefresh) {
                            Toast.makeText(getApplicationContext(), "Leaderboard failed to load, reattempting to get data from server", Toast.LENGTH_LONG).show();
                            requestQuestions(type, isUserGenerated);
                            attemptedRefresh = true;
                        } else {
                            Toast.makeText(getApplicationContext(), "Unable to reach server", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(questionRequest);
    }
}
