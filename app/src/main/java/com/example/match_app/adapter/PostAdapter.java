package com.example.match_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.match_app.R;
import com.example.match_app.dto.PostDTO;
import com.example.match_app.post.PostDetailActivity;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>
        implements com.example.match_app.adapter.PostOnClickListener {
    //cardView 어댑터 보고 여기 채워넣기?(태그 찾아놓고 글 집어넣고)

    private static final String TAG = "ListItemAdapter ";

    // 1. 리스너 선언
    com.example.match_app.adapter.PostOnClickListener listener;

    // 메인에서 넘겨 받는것
    ArrayList<PostDTO> dtos;
    Context context;
    LayoutInflater inflater;

    public PostAdapter(ArrayList<PostDTO> dtos, Context context) {
        this.dtos = dtos;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }


    // 2. 화면을 인플레이트 시키면서 클릭리스너를 달고 들어간다
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_list_view,
                parent, false);
        Log.d(TAG, "onCreateViewHolder: ");
        return new ViewHolder(itemView, this);
    }

    // 인플레이트 시킨 화면에 데이터를 셋팅시킨다
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);

        PostDTO dto = dtos.get(position);
        // 뷰홀더에 만들어 놓은 setDto에 선택된 dto를 넘긴다
        holder.setDto(dto);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , PostDetailActivity.class);

                intent.putExtra("post", getItem(position));
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dtos==null ? 0: dtos.size();
    }

    // ArrayList<SuperDTO>에 dto를 추가할수 있도록 매소드를 만든다
    public void addDto(PostDTO dto){
        dtos.add(dto);
        notifyItemInserted(dtos.size()-1);
    }

    // ArrayList<SuperDTO>에 특정위치에 dto를 제거할수 있도록 매소드를 만든다
    public void removeDto(int position){
        dtos.remove(position);
    }

    // 6. 메인에서 클릭한 위치에 있는 dto 가져오기
    public PostDTO getItem(int position){
        return dtos.get(position);
    }

    // 4. 메인에서 클릭리스너를 클릭했을때 어댑터의 리스너와 연결해준다
    public void setOnItemClickListener(com.example.match_app.adapter.PostOnClickListener listener){
        this.listener = listener;
    }

    // 인터페이스에 정의된 매소드 구현
    // 5. 뷰홀더의 클릭리스너를 실행해 클릭한 위치를 메인에게 알려준다
    @Override
    public void onItemClick(ViewHolder holderm, View view, int position) {
        if(listener != null){
            listener.onItemClick(holderm, view, position);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle, imageLayout, tvGame, tvTime, tvPlace, tvFee;
        ImageView image;
        LinearLayout parentLayout, textLayout;


        public ViewHolder(@NonNull View itemView, com.example.match_app.adapter.PostOnClickListener listener) {
            super(itemView);

            Log.d(TAG, "onGenerateViewHolder: " );
            parentLayout = itemView.findViewById(R.id.parentLayout);
            tvTitle = itemView.findViewById(R.id.tvTitle);
//            imageLayout = itemView.findViewById(R.id.imageLayout); /*이미지 들어가는*/
            image = itemView.findViewById(R.id.image);
            tvGame = itemView.findViewById(R.id.tvGame);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPlace = itemView.findViewById(R.id.tvPlace);
            tvFee = itemView.findViewById(R.id.tvFee);

            // 3. 클릭리스너를 달아준다
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this,
                                view, position);
                    }
                }
            });
        }

        public void setDto(PostDTO dto){
            //사진, 이름을 카드 모양에 넣는다

            tvTitle.setText(dto.getTitle());
            tvGame.setText(dto.getGame());
            tvFee.setText(dto.getFee());
            tvPlace.setText(dto.getPlace());
            tvTime.setText(dto.getTime());

            switch (dto.getGame()){
                case "축구" :
                    image.setImageResource(R.drawable.soccer_ball);
                    break;
                case "농구" :
                    image.setImageResource(R.drawable.soccer_ball);
                    break;
                default:    //기본 사진
                    image.setImageResource(R.drawable.soccer_ball);
            }


        }

    }

}
