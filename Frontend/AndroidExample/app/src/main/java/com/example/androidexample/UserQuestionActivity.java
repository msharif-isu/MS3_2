package com.example.androidexample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.List;

/**
 * The activity responsible for the user question screen's functionality
 */
public class UserQuestionActivity extends AppCompatActivity {

    private final static String SERVER_URL = "http://coms-309-034.class.las.iastate.edu:8080";

    private boolean attemptedQuestionRefresh = false;
    private RecyclerView questionList;
    private UserQuestionAdapter questionAdapter;
    private ImageButton addQuestionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_set);

        // Setup question list UI
        questionList = findViewById(R.id.user_question_question_list);
        questionList.setLayoutManager(new LinearLayoutManager(this));

        addQuestionButton = findViewById(R.id.user_question_add_question_button);
        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    addBlankQuestion();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        makeQuestionsRequest();
    }

    /**
     * Makes a request to the server to get user questions
     */
    private void makeQuestionsRequest() {
        JsonArrayRequest questionsRequest = new JsonArrayRequest(
                Request.Method.GET,
                String.format("%s/question", SERVER_URL),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<UserQuestionListItem> dataSet = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject questionData = response.getJSONObject(i);
                                int id = questionData.getInt("id");
                                String question = questionData.getString("question");
                                String answer = questionData.getString("answer");
                                String questionType = questionData.getString("questionType");
                                dataSet.add(new UserQuestionListItem(id, question, answer, questionType));
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        questionAdapter = new UserQuestionAdapter(getApplicationContext(), SERVER_URL, dataSet);
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

    /**
     * Makes a request to the server to add a blank question
     * @throws JSONException
     */
    private void addBlankQuestion() throws JSONException {
        JsonObjectRequest questionsRequest = new JsonObjectRequest(
                Request.Method.POST,
                String.format("%s/question", SERVER_URL),
            new JSONObject() {
                    {
                        put("question", "");
                        put("answer", "");
                        put("questionType", "");
                    }
                },
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        makeQuestionsRequest();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Failed to add question", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(questionsRequest);
    }
}
