package com.example.androidexample;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import url.RequestURLs;

public class QueryFragment extends Fragment {
    private boolean attemptedRefresh = false;
    private List<Question> questionDataset;
    private QueryListAdapter questionListAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_query, container, false);


        RecyclerView questionList = view.findViewById(R.id.query_list);
        AutoCompleteTextView searchBar = view.findViewById(R.id.query_type_search_bar);
        CheckBox userGenerated = view.findViewById(R.id.query_user_generated_check_box);
        Button addQuestionButton = view.findViewById(R.id.query_add_question_button);

        questionDataset = new ArrayList<>();
        questionListAdapter = new QueryListAdapter(questionDataset);
        questionList.setAdapter(questionListAdapter);
        questionList.setLayoutManager(new LinearLayoutManager(requireContext()));

        ArrayAdapter<String> searchBarAdapter = new ArrayAdapter<>(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        getQuestionTypes(searchBarAdapter);
        searchBar.setAdapter(searchBarAdapter);

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
                requestQuestions(searchBar.getText().toString(), b);
            }
        });
        requestQuestions(searchBar.getText().toString(), false);

        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).replaceFragment(new EditFragment());
            }
        });

        return view;
    }

    /**
     * Makes a request to the server to get all questions based on given filters
     */
    private void requestQuestions(String type, boolean isUserGenerated) {
        String serverUrl = RequestURLs.SERVER_HTTP_QUESTION_QUERY_URL;

        if (type.trim().isEmpty()) {
            if (isUserGenerated) {
                serverUrl = String.format("%s/userCreated/1", serverUrl);
            } else {
                serverUrl = RequestURLs.SERVER_HTTP_QUESTION_URL;
            }
        } else {
            serverUrl = String.format("%s/multiple/%s/%d", serverUrl, type, (isUserGenerated) ? 1 : 0);
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

                            if (questionListAdapter != null) {
                                questionListAdapter.notifyDataSetChanged();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (getContext() != null) {
                                if (!attemptedRefresh) {
                                    Toast.makeText(requireContext(), "Leaderboard failed to load, reattempting to get data from server", Toast.LENGTH_LONG).show();
                                    requestQuestions(type, isUserGenerated);
                                    attemptedRefresh = true;
                                } else {
                                    Toast.makeText(requireContext(), "Unable to reach server", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
            );

            VolleySingleton.getInstance(requireContext()).addToRequestQueue(questionRequest);
    }

    private void getQuestionTypes(ArrayAdapter<String> adapter) {
        JsonArrayRequest questionTypesRequest = new JsonArrayRequest(
                Request.Method.GET,
                String.format("%s/query/topic", RequestURLs.SERVER_HTTP_URL),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        List<String> questionTypes = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                questionTypes.add(jsonArray.get(i).toString());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }

                        adapter.addAll(questionTypes);
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(questionTypesRequest);
    }
}
