package com.example.match_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.match_app.R;
import com.example.match_app.dto.PostDTO;
import com.example.match_app.post.PostDetailActivity;

import java.util.ArrayList;

import static com.example.match_app.Common.CommonMethod.memberDTO;

public class MyPostAdapter extends BaseAdapter {

    Context context;
    ArrayList<PostDTO> dto;


    public MyPostAdapter(ArrayList<PostDTO> dto, Context c) {
        this.context = c;
        this.dto = dto;
    }

    @Override
    public int getCount() {
        return dto.size();
    }

    @Override
    public Object getItem(int position) {
        return dto.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.row_mypost, parent, false);

        LinearLayout post_layout = convertView.findViewById(R.id.post_layout);
        ImageView post_img = convertView.findViewById(R.id.post_img);
        TextView post_class = convertView.findViewById(R.id.post_class);
        TextView post_title = convertView.findViewById(R.id.post_title);
        TextView post_location = convertView.findViewById(R.id.post_location);
        TextView post_date = convertView.findViewById(R.id.post_date);
        TextView post_place = convertView.findViewById(R.id.post_place);
        String filePath = "https://firebasestorage.googleapis.com/v0/b/match-app-b8c4a.appspot.com/o/matchapp%2FpostImg%2F" + dto.get(i).getImgPath() + "?alt=media";

        post_class.setText(dto.get(i).getGame());
        post_title.setText(dto.get(i).getTitle());
        post_location.setText(memberDTO.getAddress());
        post_date.setText(dto.get(i).getTime());
        post_place.setText(dto.get(i).getPlace());
        Glide.with(convertView).load(filePath).into(post_img);

        return convertView;
    }
}
