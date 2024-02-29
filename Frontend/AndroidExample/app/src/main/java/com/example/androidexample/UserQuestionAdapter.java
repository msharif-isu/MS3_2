package com.example.androidexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserQuestionAdapter extends RecyclerView.Adapter<UserQuestionAdapter.ViewHolder> {

    /**
     * The JSONArray pulled from the database containing the question data
     */
    private JSONArray questionsDataSet;

    private final String SERVER_URL;
    private final Context context;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final EditText question;
        private final EditText answer;
        private final ImageButton editButton;
        private final TextView questionNumber;

        public ViewHolder(View view) {
            super(view);

            question = view.findViewById(R.id.question_row_question_input);
            answer = view.findViewById(R.id.question_row_answer_input);
            editButton = view.findViewById(R.id.question_row_edit_toggle_button);
            questionNumber = view.findViewById(R.id.question_row_question_number);
        }

        public TextView getQuestionView() {
            return question;
        }
        public TextView getAnswerView() {
            return answer;
        }
        public ImageButton getEditButton() { return editButton; }
        public TextView getQuestionNumber() { return questionNumber; }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet JSONArray containing the data to populate views to be used
     * by RecyclerView
     */
    public UserQuestionAdapter(JSONArray dataSet, String SERVER_URL, Context context) {
        questionsDataSet = dataSet;
        this.SERVER_URL = SERVER_URL;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.question_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        try {
            JSONObject userData = questionsDataSet.getJSONObject(position);
            viewHolder.getQuestionView().setText(userData.getString("question"));
            viewHolder.getAnswerView().setText(userData.getString("answer"));
            viewHolder.getQuestionNumber().setText("#" + (position + 1));
            viewHolder.getEditButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        makeEditQuestionRequest(
                                viewHolder.getQuestionView().getText().toString(),
                                viewHolder.getAnswerView().getText().toString(),
                                position
                        );
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void makeEditQuestionRequest(String question, String answer, int position) throws JSONException {
        JsonObjectRequest questionsRequest = new JsonObjectRequest(
                Request.Method.PUT,
                String.format("%s/question/%d", SERVER_URL, position+1),
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
                        Toast.makeText(context, "Question edited", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        VolleySingleton.getInstance(context).addToRequestQueue(questionsRequest);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return questionsDataSet.length();
    }
}
