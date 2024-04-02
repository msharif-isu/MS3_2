package com.example.androidexample;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import url.RequestURLs;

public class QueryActivity extends AppCompatActivity {
    private boolean attemptedRefresh = false;
    private List<Question> questionDataset;
    private QueryListAdapter questionListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);


        RecyclerView questionList = findViewById(R.id.query_list);
        questionList.setLayoutManager(new LinearLayoutManager(this));

        questionDataset = new ArrayList<>();
        questionListAdapter = new QueryListAdapter(questionDataset);
        questionList.setAdapter(questionListAdapter);

        EditText searchBar = findViewById(R.id.query_type_search_bar);
        CheckBox userGenerated = findViewById(R.id.query_user_generated_check_box);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                requestQuestions(searchBar.getText().toString(), userGenerated.isChecked());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        userGenerated.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                requestQuestions(searchBar.getText().toString(), userGenerated.isChecked());
            }
        });

        requestQuestions(searchBar.getText().toString(), false);
    }

    /**
     * Makes a request to the server to get all questions based on given filters
     */
    public void requestQuestions(String type, boolean isUserGenerated) {
        String serverUrl = RequestURLs.SERVER_HTTP_QUESTION_QUERY_URL;

        if (type.trim().isEmpty()) {
            serverUrl = String.format("%s/userGenerated/%d", serverUrl, (isUserGenerated) ? 1 : 0);
            Log.d("TryTryTryAgain", "Filtered by user-generated");
        } else {
            serverUrl = String.format("%s/topic/%s", serverUrl, type);
            Log.d("TryTryTryAgain", "Filtered by user-generated and type");
        }
            JsonArrayRequest questionRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    serverUrl,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            questionDataset.clear();

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    Question question = new Question(response.getJSONObject(i));
                                    questionDataset.add(question);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            questionDataset.sort(new Comparator<Question>() {
                                @Override
                                public int compare(Question q1, Question q2) {
                                    return q1.getQuestion().compareTo(q2.getQuestion());
                                }
                            });

                            if (questionListAdapter != null) {
                                questionListAdapter.notifyDataSetChanged();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("VolleyError", error.toString());
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
