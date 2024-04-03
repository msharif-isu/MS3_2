package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.myViewHolder> {

    private ArrayList<UserFriend> friendList;
    private OnDeleteClickListener onDeleteClickListener; // Interface instance

    // Interface definition for click events
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public FriendsListAdapter(ArrayList<UserFriend> friendList, OnDeleteClickListener onDeleteClickListener) {
        this.friendList = friendList;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private ImageView profilePic;
        private ImageButton deleteButton;
        private TextView friendUsername, friendBio;

        public myViewHolder(final View view) {
            super(view);
            profilePic = view.findViewById(R.id.profilePic);
            friendUsername = view.findViewById(R.id.friendUsername);
            friendBio = view.findViewById(R.id.friendBio);
            deleteButton = view.findViewById(R.id.removeFriend);

            // Set click listener for delete button
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onDeleteClickListener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public FriendsListAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_row_item, parent, false);
        return new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsListAdapter.myViewHolder holder, int position) {
        UserFriend friend = friendList.get(position);

        // Set friend username
        String username = friend.getUsername();
        holder.friendUsername.setText(username);

        // Set friend bio
        String bio = friend.getUserBio();
        holder.friendBio.setText(bio);

        // TODO Set profile picture
        //if (friend.getProfilePic() != null) {

        //} else {
        // Set a default profile picture if the friend's profile pic is null
        holder.profilePic.setImageResource(R.drawable.ic_launcher_foreground);
        //}
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }
}
