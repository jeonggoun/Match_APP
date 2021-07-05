package com.example.match_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.match_app.ChattingActivity;
import com.example.match_app.R;
import com.example.match_app.dto.PublicPostDTO;
import com.example.match_app.etc.PublicPostActivity;

import java.util.ArrayList;

public class PublicPosterAdapter extends RecyclerView.Adapter<PublicPosterAdapter.ViewHolder> {

    ArrayList<PublicPostDTO> dto;
    Context context;

    public PublicPosterAdapter(Context context, ArrayList<PublicPostDTO> dto) {
        this.context = context;
        this.dto = dto;
    }

    @NonNull
    @Override
    public PublicPosterAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_publicpost, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PublicPosterAdapter.ViewHolder holder, int position) {
        holder.tv_publicPost.setText(dto.get(position).getTitle());
        holder.tv_time.setText(dto.get(position).getDate());
        holder.layout_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "액티비티 → 공지사항 title, content, time(추가), read(추가)", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context , PublicPostActivity.class);
                intent.putExtra("dto", dto.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dto.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout_public;
        TextView tv_publicPost, tv_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_public = itemView.findViewById(R.id.layout_public);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_publicPost = itemView.findViewById(R.id.tv_publicPost);
        }
    }
}
