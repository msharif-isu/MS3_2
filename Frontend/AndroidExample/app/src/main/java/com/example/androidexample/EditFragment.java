package com.example.androidexample;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import url.RequestURLs;


public class EditFragment extends Fragment {
    private EditText questionInput;
    private EditText answerInput;
    private AutoCompleteTextView questionTypeInput;
    private FloatingActionButton submitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_submission, container, false);

        questionInput = view.findViewById(R.id.question_submission_question_input);
        answerInput = view.findViewById(R.id.question_submission_answer_input);
        questionTypeInput = view.findViewById(R.id.question_submission_question_type_input);
        submitButton = view.findViewById(R.id.question_submission_submit_button);

        ArrayAdapter<String> questionTypeAdapter = new ArrayAdapter<>(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        getQuestionTypes(questionTypeAdapter);

        questionTypeInput.setAdapter(questionTypeAdapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = questionInput.getText().toString();
                String answer = answerInput.getText().toString();
                String questionType = questionTypeInput.getText().toString();

                if (question.length() == 0 || answer.length() == 0 || questionType.length() == 0) {
                    Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Question submission = new Question(-1, question, answer, questionType, false, true);

                    questionInput.setText("");
                    answerInput.setText("");
                    questionTypeInput.setText("");
                    try {
                        submitQuestion(submission);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }


            }
        });

        return view;
    }

    private void submitQuestion(Question question) throws JSONException {
        JsonObjectRequest questionSubmissionRequest = new JsonObjectRequest(
                Request.Method.POST,
                String.format("%s/question", RequestURLs.SERVER_HTTP_URL),
                question.toJSON(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Toast.makeText(requireContext(), "Question successfully submitted to database", Toast.LENGTH_SHORT).show();
                        incrementQuestionsSubmitted();

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("EditFragment", "Failed to submit question");
                        Toast.makeText(requireContext(), "Failed to submit question", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        VolleySingleton.getInstance(requireContext()).addToRequestQueue(questionSubmissionRequest);
    }

    private void incrementQuestionsSubmitted() {
        SharedPreferences statsPrefs = requireContext().getSharedPreferences("UserStatistics", Context.MODE_PRIVATE);
        int questionsSubmitted = statsPrefs.getInt("QUESTIONS_SUBMITTED", 0);
        questionsSubmitted++;
        Log.d("QUESTIONSUBMITTED", "incrementQuestionsSubmitted: " + questionsSubmitted);
        SharedPreferences.Editor editor = statsPrefs.edit();
        editor.putInt("QUESTIONS_SUBMITTED", questionsSubmitted);
        editor.apply();
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