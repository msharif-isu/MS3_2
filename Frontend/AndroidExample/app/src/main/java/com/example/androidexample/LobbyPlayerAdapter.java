package com.example.androidexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import url.RequestURLs;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

public class LobbyPlayerAdapter extends RecyclerView.Adapter<LobbyPlayerAdapter.MyViewHolder> {

    private ArrayList<UserFriend> friendList;
    private Context context;
    private OnDeleteClickListener onDeleteClickListener; // Interface instance

    private String backendUrl = RequestURLs.SERVER_HTTP_URL + "/";

    // Interface definition for click events
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public LobbyPlayerAdapter(Context context, ArrayList<UserFriend> friendList, OnDeleteClickListener onDeleteClickListener) {
        this.context = context;
        this.friendList = friendList;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView friendUsername, friendBio;
        ImageButton deleteButton;
        ImageView profilePic; // Declare as local variable

        public MyViewHolder(View view) {
            super(view);
            profilePic = view.findViewById(R.id.profilePic); // Initialize profilePic here
            friendUsername = view.findViewById(R.id.friendUsername);
            friendBio = view.findViewById(R.id.friendBio);
            deleteButton = view.findViewById(R.id.removeFriend);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_row_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserFriend friend = friendList.get(position);

        // Set friend username
        String username = friend.getUsername();
        holder.friendUsername.setText(username);

        // Set friend bio
        String bio = friend.getUserBio();
        holder.friendBio.setText(bio);

        // Fetch and set profile picture
        getProfilePic(username, holder.profilePic); // Pass profilePic ImageView to getProfilePic()
        String profilePicUrl = backendUrl + "images/" + username;


        // Set click listener for delete button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteClickListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onDeleteClickListener.onDeleteClick(position);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }


    private void getProfilePic(String username, ImageView profilePic) {
        // Make a request to fetch the profile picture
        ImageRequest request = new ImageRequest(
                backendUrl + "images/" + username,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        // Apply circular cropping using Glide
                        Glide.with(context)
                                .load(response)
                                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                                .into(profilePic);
                    }
                },
                0,
                0,
                ImageView.ScaleType.CENTER_INSIDE,
                null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });
        // Add the request to the RequestQueue
        Volley.newRequestQueue(context).add(request);
    }

    public String getUsernameByIndex(int index) {
        if (index >= 0 && index < friendList.size()) {
            UserFriend friend = friendList.get(index);
            return friend.getUsername();
        }
        return null;
    }


}
