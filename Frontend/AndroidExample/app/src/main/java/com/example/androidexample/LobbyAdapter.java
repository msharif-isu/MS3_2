package com.example.androidexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LobbyAdapter extends RecyclerView.Adapter<LobbyAdapter.myViewHolder> {

    private ArrayList<Lobby> lobbyList;

    public LobbyAdapter(ArrayList<Lobby> lobbyList) {
        this.lobbyList = lobbyList;
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView lobbyName, roomSize, playerCount;
        Button joinButton;
        ImageView box;

        public myViewHolder(final View itemView) {
            super(itemView);
            lobbyName = itemView.findViewById(R.id.lobbyName);
            roomSize = itemView.findViewById(R.id.roomSize);
            playerCount = itemView.findViewById(R.id.playerCount);
            box = itemView.findViewById(R.id.box);
            joinButton = itemView.findViewById(R.id.button2);
        }
    }

    @NonNull
    @Override
    public LobbyAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lobby_row_item, parent, false);
        return new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LobbyAdapter.myViewHolder holder, int position) {
        String lobbyName = lobbyList.get(position).getLobbyName();
        int roomSize = lobbyList.get(position).getRoomSize();
        int playerCount = lobbyList.get(position).getPlayerCount();
        holder.lobbyName.setText(lobbyName);
        holder.roomSize.setText("Maximum Room Size: " + String.valueOf(roomSize));
        holder.playerCount.setText("Number of Players:" + String.valueOf(playerCount));

        holder.joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click
            }
        });
    }

    @Override
    public int getItemCount() {
        return lobbyList.size();
    }


}
