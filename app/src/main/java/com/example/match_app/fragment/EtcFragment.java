package com.example.match_app.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.match_app.IntroActivity;
import com.example.match_app.MainActivity;
import com.example.match_app.R;
import com.google.firebase.auth.FirebaseAuth;
import static com.example.match_app.MainActivity.ImageUri;
public class EtcFragment extends Fragment {

    private Button button;
    private Button profilebutton;
    private FirebaseAuth mAuth;
    private ImageView imageView;
    private final int GET_GALLERY_IMAGE = 200;

    MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_etc, container, false);

        activity = (MainActivity) getActivity();

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

        imageView = viewGroup.findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);

            }
        });

        profilebutton = viewGroup.findViewById(R.id.profilebutton);
        profilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);*/
            }
        });

        return viewGroup;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri selectedImageUri = data.getData();
        ImageUri = selectedImageUri;
        imageView.setImageURI(selectedImageUri);

    }
}