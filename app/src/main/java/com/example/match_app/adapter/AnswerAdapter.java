package com.example.match_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.match_app.R;
import com.example.match_app.dto.PublicPostDTO;


import java.util.ArrayList;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {
    ArrayList<PublicPostDTO> dto;
    Context context;

    public AnswerAdapter(Context context, ArrayList<PublicPostDTO> dto) {
        this.context = context;
        this.dto = dto;
    }

    @NonNull
    @Override
    public AnswerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_answer, parent, false);
        AnswerAdapter.ViewHolder viewHolder = new AnswerAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull  AnswerAdapter.ViewHolder holder, int position) {
        holder.tv_answer.setText(dto.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return dto.size();
    }


    public void filterList(ArrayList<PublicPostDTO> filteredList) {
        dto = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_answer;
        ConstraintLayout layout_answer;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_answer = itemView.findViewById(R.id.tv_answer);
            layout_answer = itemView.findViewById(R.id.layout_answer);

        }
    }
}
