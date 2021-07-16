package com.example.match_app.etc;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.match_app.R;
import com.example.match_app.adapter.PublicPosterAdapter;

import static com.example.match_app.Common.MyService.publicPostDTO;

public class Btn05 extends AppCompatActivity {

    RecyclerView rv_publicPost;
    PublicPosterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btn05);

        publicPostDTO.remove(publicPostDTO.size()-1);

        findViewById(R.id.iv_back2).setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { finish(); }});

        rv_publicPost = findViewById(R.id.rv_publicPost);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        rv_publicPost.setLayoutManager(mLayoutManager);

        adapter = new PublicPosterAdapter(this, publicPostDTO);
        rv_publicPost.setAdapter(adapter);


    }
}