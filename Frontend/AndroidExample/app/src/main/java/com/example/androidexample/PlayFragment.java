package com.example.androidexample;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.content.Intent;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageButton singlePlayerButton;
    private ImageButton multiPlayerButton;
    private ImageButton jeopardyButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlayFragment newInstance(String param1, String param2) {
        PlayFragment fragment = new PlayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_play, container, false);

        singlePlayerButton = view.findViewById(R.id.singlePlayerButton);
        multiPlayerButton = view.findViewById(R.id.multiPlayerButton);
        jeopardyButton = view.findViewById(R.id.jeopardyButton);

        singlePlayerButton.setOnClickListener(this);
        multiPlayerButton.setOnClickListener(this);
        jeopardyButton.setOnClickListener(this);

        return view;
    }

    public void onClick(View v) {
        if (v.getId() == R.id.singlePlayerButton) {
            startActivity(new Intent(getActivity(), SinglePlayerQuestionActivity.class));
        } else if (v.getId() == R.id.multiPlayerButton) {
            startActivity(new Intent(getActivity(), LobbiesActivity.class));
        } else if (v.getId() == R.id.jeopardyButton) {
            startActivity(new Intent(getActivity(), JeopardyActivity.class));
        }
    }
}