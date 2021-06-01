package com.example.match_app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.match_app.IntroActivity;
import com.example.match_app.R;
import com.google.firebase.auth.FirebaseAuth;

public class EtcFragment extends Fragment {

    private Button button;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_etc, container, false);

        mAuth = FirebaseAuth.getInstance();
        button = viewGroup.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), IntroActivity.class));
                getActivity().finish();
            }
        });
        return viewGroup;
    }
}