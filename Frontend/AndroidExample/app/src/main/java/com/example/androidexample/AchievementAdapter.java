package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {

    private List<Achievement> achievementList;

    public AchievementAdapter(List<Achievement> achievementList) {
        this.achievementList = achievementList;
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.achievement_row_item, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        Achievement achievement = achievementList.get(position);
        holder.bind(achievement);
    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }

    public static class AchievementViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView bioTextView;
        ImageView achievementImageView, lock, locked;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            // Misspelled. I know. Im not going to fix it either.
            titleTextView = itemView.findViewById(R.id.acheivementName);
            bioTextView = itemView.findViewById(R.id.acheivementBio);
            achievementImageView = itemView.findViewById(R.id.acheivementPic);
            lock = itemView.findViewById(R.id.lock);
            locked = itemView.findViewById(R.id.locked);
        }

        public void bind(Achievement achievement) {
            titleTextView.setText(achievement.getTitle());
            bioTextView.setText(achievement.getBio());
//            achievementImageView.setImageResource(achievement.getImageId());

            // Set visibility based on achievement unlocked status
            if (achievement.isUnlocked()) {
                lock.setVisibility(View.INVISIBLE);
                locked.setVisibility(View.INVISIBLE);
            } else {
                lock.setVisibility(View.VISIBLE);
                lock.setVisibility(View.VISIBLE);
            }
        }
    }
}
