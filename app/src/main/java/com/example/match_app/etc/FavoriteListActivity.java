package com.example.match_app.etc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.match_app.R;
import com.example.match_app.adapter.MyPostAdapter;
import com.example.match_app.dto.PostDTO;
import com.example.match_app.post.PostDetailActivity;

import java.util.ArrayList;

import static com.example.match_app.Common.MyService.notiDTO;
import static com.example.match_app.Common.MyService.postsDTO;

public class FavoriteListActivity extends AppCompatActivity {
    private static final String TAG = "favo";
    private MyPostAdapter adapter1;
    private ListView listView1;
    private PostDTO selected;
    private ArrayList<PostDTO> favoriteDTO = null;
    private TextView tv_null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        listView1 = findViewById(R.id.listView1);
        tv_null = findViewById(R.id.tv_null);

        findViewById(R.id.iv_back2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        favoriteDTO = new ArrayList<>();
        for (int j = 0; j < postsDTO.size(); j++) {
            for (int i = 0; i < notiDTO.size(); i++) {
                if (notiDTO.get(i).isLike()==true) {
                    if (postsDTO.get(j).getPostKey().equals(notiDTO.get(i).getPostToken())) {
                        if (!favoriteDTO.contains(postsDTO.get(j)))
                        favoriteDTO.add(postsDTO.get(j));
                    } else {

                    }//if
                }//for i
            }// forj
        }
        Log.d(TAG, "size"+favoriteDTO.size());

        if (favoriteDTO.size() != 0) {
            tv_null.setVisibility(View.GONE);
            adapter1 = new MyPostAdapter(favoriteDTO, getApplicationContext());
            listView1.setAdapter(adapter1); listView1.setVisibility(View.VISIBLE);
        }else {
            tv_null.setVisibility(View.VISIBLE);
        }


        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                selected = new PostDTO();
                selected = (PostDTO) favoriteDTO.get(i);
                Intent intent = new Intent(FavoriteListActivity.this, PostDetailActivity.class);
                intent.putExtra("post", selected);
                startActivity(intent);
            }
        });
    }
}