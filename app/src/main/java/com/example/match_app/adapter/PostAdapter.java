package com.example.match_app.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.example.match_app.MainActivity;
import com.example.match_app.R;
import com.example.match_app.dto.MemberDTO;
import com.example.match_app.dto.NotiDataDTO;
import com.example.match_app.dto.PostDTO;
import com.example.match_app.fragment.SearchFragment;
import com.example.match_app.post.PostDetailActivity;
import com.example.match_app.post.PostUpdateActivity;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;


import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.match_app.Common.CommonMethod.keywords;
import static com.example.match_app.Common.CommonMethod.memberDTO;
import static com.example.match_app.Common.CommonMethod.notiDataDTO;
import static com.example.match_app.Common.MyService.notiDTO;
import static com.example.match_app.MainActivity.user;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>
        implements com.example.match_app.adapter.PostOnClickListener {

    private static final String TAG = "ListItemAdapter ";
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    FirebaseUser user2 = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
    String uid = user2 != null ? user2.getUid() : null; // 로그인한 유저의 고유 uid 가져오기
    FirebaseDatabase database = FirebaseDatabase.getInstance();

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
        return new ViewHolder(itemView, this);
    }

    // 인플레이트 시킨 화면에 데이터를 셋팅시킨다
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PostDTO dto = dtos.get(position);
        // 뷰홀더에 만들어 놓은 setDto에 선택된 dto를 넘긴다
        holder.setDto(dto);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , PostDetailActivity.class);

                intent.putExtra("post", getItem(position));
                ((Activity)context).startActivityForResult(intent, 100); /* 글 삭제 후 새로고침 */
            }
        });

        //todo 카드뷰에서 수정 삭제 보이게 하는 부분
        Log.d(TAG, "user.getIdToken(): " + user.getIdToken());
        Log.d(TAG, "dto.getWriterToken(): " + dto.getWriterToken());
        if(user.getIdToken().equals(dto.getWriterToken())){  //Writer와 user의 token이 동일하다면 카드뷰에 있는 수정, 삭제 버튼을 보이게 한다
            holder.writerLayout.setVisibility(View.VISIBLE);
        }

        CheckBox like = holder.itemView.findViewById(R.id.like);

        for (int j = 0 ; j<notiDTO.size(); j++) {
            if(dtos.get(position).getPostKey().equals(notiDTO.get(j).getPostToken())){
                like.setChecked(notiDTO.get(j).isLike());
            }
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notiDataDTO.setIdToken(uid);
                notiDataDTO.setPostToken(dto.getPostKey());
                notiDataDTO.setLike(like.isChecked());
                firebaseDatabase.getReference("matchapp").child("NotiData").child(uid).child(dto.getPostKey()).setValue(notiDataDTO);
                firebaseDatabase.getReference("matchapp").child("Post").child(dto.getPostKey()).setValue(dto);
            }
        });


        //수정 버튼 누르는 경우
        holder.itemView.findViewById(R.id.btnModify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostUpdateActivity.class);
                intent.putExtra("post", dto);
                context.startActivity(intent);
            }
        });

        //삭제 버튼 누르는 경우
        holder.itemView.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("정말 삭제하시겠습니까?")        // 제목 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton){

//                                            Log.d(TAG, "onClick: " + firebaseDatabase.getReference().child(dto.getPostKey()));

                                //선생님1     databaseReference = firebaseDatabase.getReference("matchapp/Post");
                                //                                            databaseReference.child(dto.getPostKey()).removeValue().

                                //선생님2     firebaseDatabase.getReference("matchapp/Post/" + dto.getPostKey() ).removeValue().

                                firebaseDatabase.getReference("matchapp/Post/" + dto.getPostKey() ).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        // 삭제시 메인액티비티 재실행 : Adapter는 액티비티가 아니므로 반드시 FLAG_ACTIVITY_NEW_TASK 추가
                                        Intent mainIntent = new Intent(context, MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                                Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        mainIntent.putExtra("requestCode", 100);
                                        context.startActivity(mainIntent);

                                        Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton){

                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기
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
        ImageView image; CheckBox like;
        LinearLayout parentLayout, textLayout, writerLayout;


        public ViewHolder(@NonNull View itemView, com.example.match_app.adapter.PostOnClickListener listener) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.parentLayout);
            tvTitle = itemView.findViewById(R.id.tvTitle);
//            imageLayout = itemView.findViewById(R.id.imageLayout); /*이미지 들어가는*/
            image = itemView.findViewById(R.id.image);
            tvGame = itemView.findViewById(R.id.tvGame);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvPlace = itemView.findViewById(R.id.tvPlace);
            tvFee = itemView.findViewById(R.id.tvFee);
            like = itemView.findViewById(R.id.like);

            writerLayout = itemView.findViewById(R.id.writerLayout);

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

            if(dto.getFee().equals("0")) {
                tvFee.setText("참가비 없음");
            } else {
                tvFee.setText(dto.getFee() + "원");
            }
            tvPlace.setText(dto.getPlace());
            tvTime.setText(dto.getTime());





            if(dto.getMatchConfirm().equals("enable")){
                tvGame.setText(dto.getGame());
            }else if(dto.getMatchConfirm().equals("disable")){
                tvGame.setText("모집 완료");
                parentLayout.setBackgroundColor(R.drawable.button_background2);
           }

            switch (dto.getGame()){
                case "축구" :
                    image.setImageResource(R.drawable.soccer);
                    break;
                case "농구" :
                    image.setImageResource(R.drawable.basketball);
                    break;
                case "테니스" :
                    image.setImageResource(R.drawable.tennis);
                    break;
                case "야구" :
                    image.setImageResource(R.drawable.baseball);
                    break;
                case "배구" :
                    image.setImageResource(R.drawable.volleyball);
                    break;
                case "배드민턴" :
                    image.setImageResource(R.drawable.badminton);
                    break;
                case "볼링" :
                    image.setImageResource(R.drawable.bowling);
                    break;
                case "당구" :
                    image.setImageResource(R.drawable.snooker);
                    break;
                case "이스포츠" :
                    image.setImageResource(R.drawable.computer);
                    break;
                default:    //기본 사진
                    image.setImageResource(R.drawable.match);
            }



        }

    }

}
