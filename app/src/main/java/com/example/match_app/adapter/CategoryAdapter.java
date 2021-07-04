package com.example.match_app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.match_app.R;
import com.example.match_app.dto.PostDTO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.match_app.Common.MyService.qaDTO;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    public interface OnitemClickListener {
        void onItemClick(View v, int position);
    } //Adapter 내에서 커스텀 인터페이스 리스너 정의

    private OnitemClickListener mListener = null;
    // 리스너 객체 참조를 저장할 변수 선언

    public void setOnItemClickListener(OnitemClickListener listener) {
        this.mListener = listener;
    } // onitemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드

    String[] category;
    Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public CategoryAdapter(Context context, String[] category) {
        this.category = category;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.tv_category.setText(category[position]);


/*        Query myPost = database.getReference().orderByChild("writerToken").equalTo(category[position]);
        myPost.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }*/
           
            

    }

    @Override
    public int getItemCount() {
        return category.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout_category;
        TextView tv_category;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_category = itemView.findViewById(R.id.layout_category);
            tv_category = itemView.findViewById(R.id.tv_category);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출
                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });
        }
    }
}
