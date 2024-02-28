package com.example.androidexample;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserQuestionActivity extends AppCompatActivity {

    private final static String SERVER_URL = "http://coms-309-034.class.las.iastate.edu:8080";

    private boolean attemptedQuestionRefresh = false;
    private RecyclerView questionList;
    private UserQuestionAdapter questionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_set);

        // Setup question list UI
        questionList = findViewById(R.id.user_question_question_list);
        questionList.setLayoutManager(new LinearLayoutManager(this));

        makeQuestionsRequest();
    }

    private void makeQuestionsRequest() {
        JsonArrayRequest questionsRequest = new JsonArrayRequest(
                Request.Method.GET,
                String.format("%s/question", SERVER_URL),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        questionAdapter = new UserQuestionAdapter(response);
                        questionList.setAdapter(questionAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!attemptedQuestionRefresh) {
                            Toast.makeText(getApplicationContext(), "Questions failed to load, reattempting to get data from server", Toast.LENGTH_LONG).show();
                            makeQuestionsRequest();
                            attemptedQuestionRefresh = true;
                        } else {
                            Toast.makeText(getApplicationContext(), "Unable to reach server", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(questionsRequest);
    }

    private void makeEditQuestionRequest(String question, String answer) throws JSONException {
        JsonObjectRequest questionsRequest = new JsonObjectRequest(
                Request.Method.PUT,
                String.format("%s/question/1", SERVER_URL),
                new JSONObject() {
                    {
                        put("question", question);
                        put("answer", answer);
                        put("questionType", "Berries");
                    }
                },
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
    }

    private void addBlankQuestion() {

    }
}
