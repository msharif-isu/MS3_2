package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * An adapter to generate the list items for the user question screen
 */
public class QueryListAdapter extends RecyclerView.Adapter<QueryListAdapter.ViewHolder> {

    /**
     * An array of UserQuestionListItems to pull data from
     */
    private List<Question> questionDataSet;

    /**
     * A class to reference the views to edit in the user question screen
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView question;
        private final TextView answer;
        private final TextView type;
        private final CheckBox userGenerated;

        public ViewHolder(View view) {
            super(view);

            question = view.findViewById(R.id.query_row_question);
            answer = view.findViewById(R.id.query_row_answer);
            type = view.findViewById(R.id.query_row_type);
            userGenerated = view.findViewById(R.id.query_row_user_generated_check_box);
        }

        public TextView getQuestionView() {
            return question;
        }
        public TextView getAnswerView() {
            return answer;
        }
        public TextView getTypeView() { return type; }
        public CheckBox getUserGeneratedCheckBox() { return userGenerated; }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet JSONArray containing the data to populate views to be used
     * by RecyclerView
     */
    public QueryListAdapter(List<Question> dataSet) {
        questionDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.query_row_item, viewGroup, false);

        return new QueryListAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Question userData = questionDataSet.get(position);
        viewHolder.getQuestionView().setText(userData.getQuestion());
        viewHolder.getAnswerView().setText(userData.getAnswer());
        viewHolder.getTypeView().setText(userData.getQuestionType());
        viewHolder.getUserGeneratedCheckBox().setChecked(userData.isUserCreated());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return questionDataSet.size();
    }
}
