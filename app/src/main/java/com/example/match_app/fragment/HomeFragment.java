package com.example.match_app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.match_app.MainActivity;
import com.example.match_app.R;
import com.example.match_app.dto.MemberDTO;


import static com.example.match_app.MainActivity.user;
import static com.example.match_app.MainActivity.ImageUri;

public class HomeFragment extends Fragment {

    ImageView imageView;
    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        textView = viewGroup.findViewById(R.id.home_user_email);
        imageView = viewGroup.findViewById(R.id.homeImage);
        if (ImageUri != null)
            imageView.setImageURI(ImageUri);
        textView.setText(user.getEmailId());
        return viewGroup;
    }
}