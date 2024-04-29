package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SelectModeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_singleplayer, container, false);

        ImageButton singleplayerButton = view.findViewById(R.id.singlePlayerButton);
        ImageButton jeopardyButton = view.findViewById(R.id.jeopardyButton);

        singleplayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), SinglePlayerQuestionActivity.class);
                startActivity(intent);
            }
        });


        jeopardyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), JeopardyActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
