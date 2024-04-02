package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.myViewHolder> {

    private ArrayList<UserFriend> friendList;

    public FriendsListAdapter(ArrayList<UserFriend> friendList) {
        this.friendList = friendList;
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private ImageView profilePic;
        private TextView friendUsername, friendBio;

        public myViewHolder(final View view) {
            super(view);
            profilePic = view.findViewById(R.id.profilePic);
            friendUsername = view.findViewById(R.id.friendUsername);
            friendBio = view.findViewById(R.id.friendBio);
        }

    }

    @NonNull
    @Override
    public FriendsListAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from (parent.getContext()).inflate(R.layout.friend_row_item, parent, false);
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
