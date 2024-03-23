package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    /**
     * The JSONArray pulled from the database containing the leaderboard data
     */
    private List<LeaderboardListItem> leaderboardDataSet;
    /**
     * Keeps track of the type of points to display
     */
    public LeaderboardTimeFrameEnum time_frame;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView userName;
        private final TextView points;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            userName = view.findViewById(R.id.userName);
            points = view.findViewById(R.id.points);
        }

        public TextView getUserNameView() {
            return userName;
        }

        public TextView getPointsView() {
            return points;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet JSONArray containing the data to populate views to be used
     * by RecyclerView
     */
    public LeaderboardAdapter(List<LeaderboardListItem> dataSet, LeaderboardTimeFrameEnum time_frame) {
        leaderboardDataSet = dataSet;
        this.time_frame = time_frame;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.leaderboard_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        LeaderboardListItem userPointData = leaderboardDataSet.get(position);

        viewHolder.getUserNameView().setText(Integer.toString(userPointData.getId()));

        switch (time_frame) {
            case DAILY:
                viewHolder.getPointsView().setText(Integer.toString(userPointData.getDailyPoints()));
                break;
            case WEEKLY:
                viewHolder.getPointsView().setText(Integer.toString(userPointData.getWeeklyPoints()));
                break;
            case MONTHLY:
                viewHolder.getPointsView().setText(Integer.toString(userPointData.getMonthlyPoints()));
                break;
            case YEARLY:
                viewHolder.getPointsView().setText(Integer.toString(userPointData.getYearlyPoints()));
                break;
            case LIFETIME:
                viewHolder.getPointsView().setText(Integer.toString(userPointData.getLifetimePoints()));
                break;
            default:
                return;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return leaderboardDataSet.size();
    }
}
