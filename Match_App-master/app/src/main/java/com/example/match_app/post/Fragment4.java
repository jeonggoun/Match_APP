package com.example.match_app.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.match_app.MainActivity;
import com.example.match_app.R;

public class Fragment4 extends Fragment {

    MainActivity mainActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment4, container, false);









        return viewGroup;
    }

}