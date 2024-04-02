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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * An adapter to generate the list items for the user question screen
 */
public class UserQuestionAdapter extends RecyclerView.Adapter<UserQuestionAdapter.ViewHolder> {

    /**
     * An array of UserQuestionListItems to pull data from
     */
    private List<Question> questionsDataSet;

    /**
     * The url of the server to send responses to
     */
    private final String SERVER_URL;
    /**
     * Context of application that this adapter is used in
     */
    private final Context context;

    /**
     * A class to reference the views to edit in the user question screen
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
    public UserQuestionAdapter(Context context, String SERVER_URL, List<Question> dataSet) {
        this.context = context;
        this.SERVER_URL = SERVER_URL;
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
        Question userData = questionsDataSet.get(position);
        viewHolder.getQuestionView().setText(userData.getQuestion());
        viewHolder.getAnswerView().setText(userData.getAnswer());
        viewHolder.getQuestionNumber().setText(String.format("#%d", position + 1));

        // If user wants to edit a question,
        // use the text from the TextViews to send a edit request
        viewHolder.getEditButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    makeEditQuestionRequest(
                            viewHolder.getQuestionView().getText().toString(),
                            viewHolder.getAnswerView().getText().toString(),
                            userData.getId()
                    );
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Makes a request to the server to edit a question
     * @param question - question
     * @param answer - answer to question
     * @param id - id of question
     * @throws JSONException
     */
    private void makeEditQuestionRequest(String question, String answer, int id) throws JSONException {
        JsonObjectRequest questionsRequest = new JsonObjectRequest(
                Request.Method.PUT,
                String.format("%s/question/%d", SERVER_URL, id),
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
        return questionsDataSet.size();
    }
}
