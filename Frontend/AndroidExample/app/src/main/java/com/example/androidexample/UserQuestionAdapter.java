package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserQuestionAdapter extends RecyclerView.Adapter<UserQuestionAdapter.ViewHolder> {

    /**
     * The JSONArray pulled from the database containing the question data
     */
    private JSONArray questionsDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final EditText question;
        private final EditText answer;

        public ViewHolder(View view) {
            super(view);

            question = view.findViewById(R.id.question_row_question_input);
            answer = view.findViewById(R.id.question_row_answer_input);
        }

        public TextView getQuestionView() {
            return question;
        }

        public TextView getAnswerView() {
            return answer;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet JSONArray containing the data to populate views to be used
     * by RecyclerView
     */
    public UserQuestionAdapter(JSONArray dataSet) {
        questionsDataSet = dataSet;
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
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return questionsDataSet.length();
    }
}
