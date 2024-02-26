package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    /**
     * The JSONArray pulled from the database containing the leaderboard data
     */
    private JSONArray leaderboardDataSet;
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
    public LeaderboardAdapter(JSONArray dataSet, LeaderboardTimeFrameEnum time_frame) {
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
        try {
            JSONObject userData = leaderboardDataSet.getJSONObject(position);
            viewHolder.getUserNameView().setText(userData.getString("id"));

            String points = "";
            switch (time_frame) {
                case DAILY:
                    points = userData.getString("userPoints");
                    break;
                case WEEKLY:
                    points = userData.getString("weeklyPoints");
                    break;
                case MONTHLY:
                    points = userData.getString("monthlyPoints");
                    break;
                case YEARLY:
                    points = userData.getString("yearlyPoints");
                    break;
                case LIFETIME:
                    points = userData.getString("lifetimePoints");
                    break;
                default:
                    return;
            }
            viewHolder.getPointsView().setText(points);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return leaderboardDataSet.length();
    }
}
