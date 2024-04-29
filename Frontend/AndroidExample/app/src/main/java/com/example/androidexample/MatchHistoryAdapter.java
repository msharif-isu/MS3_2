package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * The adapter that binds a leaderboard's dataset to its <code>RecyclerView</code>'s UI elements
 */
public class MatchHistoryAdapter extends RecyclerView.Adapter<MatchHistoryAdapter.ViewHolder> {

    private List<MatchHistory> matchesList;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView points;
        private final TextView position;
        private final TextView questionType;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            position = view.findViewById(R.id.match_history_row_item_placement);
            points = view.findViewById(R.id.match_history_row_item_points_earned);
            questionType = view.findViewById(R.id.match_history_row_item_question_type);
        }
        public TextView getPointsView() {
            return points;
        }
        public TextView getPositionView() {
            return position;
        }
        public TextView getQuestionTypeView() {
            return questionType;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet JSONArray containing the data to populate views to be used
     * by RecyclerView
     */
    public MatchHistoryAdapter(List<MatchHistory> dataSet) {
        matchesList = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.match_history_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        MatchHistory mh = matchesList.get(position);
        viewHolder.getPointsView().setText(Integer.toString(mh.getPointsEarned()));
        viewHolder.getPositionView().setText("#" + mh.getPlacement());
        viewHolder.getQuestionTypeView().setText(mh.getQuestionSet());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return matchesList.size();
    }
}
