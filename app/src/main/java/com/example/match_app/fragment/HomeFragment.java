package com.example.match_app.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.match_app.Common.CommonMethod;
import com.example.match_app.R;
import com.example.match_app.dto.NewsDTO;
import com.example.match_app.post.PostWriteActivity;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.match_app.MainActivity.user;
import static com.example.match_app.MainActivity.ImageUri;

public class HomeFragment extends Fragment {
    private static final String TAG = "Main:HomeFragment";

    ImageView imageView, news_image[] = new ImageView[3];
    TextView textView, news_content_text[] = new TextView[3], news_title_text[] = new TextView[3],
            match_title[] = new TextView[3], match_time[] = new TextView[3], match_date[] = new TextView[3],
            match_content[] = new TextView[3], time[] = new TextView[3];
    Context context;

    private String href[] = new String[3];





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        time[0] = viewGroup.findViewById(R.id.time01);
        time[1] = viewGroup.findViewById(R.id.time02);
        time[2] = viewGroup.findViewById(R.id.time03);

        time[0].setText(getTime());
        time[1].setText(getTime());
        time[2].setText(getTime());

        context = container.getContext();

        textView = viewGroup.findViewById(R.id.home_user_email);

        news_image[0] = viewGroup.findViewById(R.id.news_image1);
        news_image[1] = viewGroup.findViewById(R.id.news_image2);
        news_image[2] = viewGroup.findViewById(R.id.news_image3);
        textView.setText(CommonMethod.memberDTO.getNickName() + "님 반갑습니다!");

        news_content_text[0] = viewGroup.findViewById(R.id.news_content_text1);
        news_content_text[1] = viewGroup.findViewById(R.id.news_content_text2);
        news_content_text[2] = viewGroup.findViewById(R.id.news_content_text3);

        news_title_text[0] = viewGroup.findViewById(R.id.news_title_text1);
        news_title_text[1] = viewGroup.findViewById(R.id.news_title_text2);
        news_title_text[2] = viewGroup.findViewById(R.id.news_title_text3);

        ///
        match_title[0] = viewGroup.findViewById(R.id.match_title1);
        match_title[1] = viewGroup.findViewById(R.id.match_title2);
        match_title[2] = viewGroup.findViewById(R.id.match_title3);

        match_time[0] = viewGroup.findViewById(R.id.match_time1);
        match_time[1] = viewGroup.findViewById(R.id.match_time2);
        match_time[2] = viewGroup.findViewById(R.id.match_time3);

        match_date[0] = viewGroup.findViewById(R.id.match_date1);
        match_date[1] = viewGroup.findViewById(R.id.match_date2);
        match_date[2] = viewGroup.findViewById(R.id.match_date3);

        match_content[0] = viewGroup.findViewById(R.id.match_content1);
        match_content[1] = viewGroup.findViewById(R.id.match_content2);
        match_content[2] = viewGroup.findViewById(R.id.match_content3);


        //뉴스크롤링
        newsData task = new newsData();
        task.execute();


        news_image[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sports.news.naver.com" + href[0]));
                startActivity(intent);
            }
        });

        news_image[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sports.news.naver.com" + href[1]));
                startActivity(intent);
            }
        });

        news_image[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sports.news.naver.com" + href[2]));
                startActivity(intent);
            }
        });
        return viewGroup;
    }


    private class newsData extends AsyncTask<Void, Void, ArrayList<NewsDTO>> {
        private static final String TAG = "main:newsData";

        @Override
        protected ArrayList<NewsDTO> doInBackground(Void... voids) {

            ArrayList<NewsDTO> arrayList = new ArrayList<NewsDTO>();

            try {
                /* Jsoup을 이용해 데이터 가져오기 */
                Document document = Jsoup.connect("https://sports.news.naver.com/index.nhn").get();
                Document document2 = Jsoup.connect("https://sports.news.naver.com/scoreboard/index.nhn").get();
                Elements doc = document.select("a.link_today");
                Elements doc2 = document2.select("tbody .start");

                //Elements live_slider = document.select("div.live_slider");
                // Elements live_card_list = document.select("div.live_card_list div.scroller");
                //Elements live_box_card = live_card_list.select("div.type_live");

                // Log.d(TAG, "doInBackground: doc => " + doc);
                // Log.d(TAG, "doInBackground: doc2 => " + doc2);
                String img = "";
                String title = "";
                String content = "";
                String league = "";

                String match_title = "";
                String match_time = "";
                String match_date = "";
                String match_content = "";

                //Log.d(TAG, "doInBackground: arrayListSize => " + doc.size());
                //Log.d(TAG, "doInBackground: arrayList.toString => " + doc.toString());


                ArrayList<Integer> index = new ArrayList<>();
                for (int j = 0; j < doc.size(); j++) {
                    index.add(j);
                }


                int[] index_list = {0, 0, 0};
                for (int j = 0; j < 3; j++) {
                    int k = (int) Math.floor(Math.random() * index.size());
                    index_list[j] = index.get(k);
                    index.remove(k);
                }
//                int i = (int) Math.floor(Math.random()*5);
                for (int j = 0; j < 3; j++) {
                    int i = index_list[j];
                    img = doc.get(i).select("img").attr("src");
                    title = doc.get(i).select("strong").text();
                    content = doc.get(i).select("p").text();
                    league = doc.get(i).select("span").text();

                    match_title = doc2.get(i).select("td.game").text();
                    match_time = doc2.get(i).select("td.time").text();
                    match_date = doc2.get(i).select("td.state").text();
                    match_content = doc2.get(i).select("td.place").text();


                    href[j] = doc.get(i).select("a").attr("href");

                    String st[] = league.split(" ");
                    // Log.d(TAG, "doInBackground: img => " + img );
                    // Log.d(TAG, "doInBackground: title => " + title );
                    //Log.d(TAG, "doInBackground: content => " + content);
                    //Log.d(TAG, "doInBackground: league => " + league);
                    //Log.d(TAG, "doInBackground: a => " + a);
                    //Log.d(TAG, "doInBackground: href => " + href);

                    //Log.d(TAG, "doInBackground: match_title => " + match_title );
                    // Log.d(TAG, "doInBackground: match_date => " + match_date );
                    //Log.d(TAG, "doInBackground: match_content => " + match_content);

                    //Log.d(TAG, "doInBackground: doc => " + doc);
                    // Log.d(TAG, "doInBackground: doc2 => " + doc2);
                    //Log.d(TAG, "doInBackground: arrayList.toString => " + doc2.toString());


                    arrayList.add(new NewsDTO(img, title, content, league, match_title, match_time, match_date, match_content));

                }

                //여기서부터 데일리매치



                /*for(int r=0; r< doc2.size(); r++){
                    index.add(r);
                }
                for(int r=0;r<3;r++ ){
                    int k = (int) Math.floor(Math.random()*index.size());
                    index_list[r] = index.get(k);
                    index.remove(k);
                }

                for(int r=0; r<3; r++){
                    int i = index_list[r];
                    *//*match_title = doc2.get(i).select("th").text();
                    match_date = doc2.get(i).select("td.state").text();
                    match_content = doc2.get(i).select("td.place").text();
*//*
                    Log.d(TAG, "doInBackground: doc2 => " + doc2 );
                    Log.d(TAG, "doInBackground: match_title => " + match_title );
                    Log.d(TAG, "doInBackground: match_date => " + match_date );
                    Log.d(TAG, "doInBackground: match_content => " + match_content);

                   // Log.d(TAG, "doInBackground: doc => " + doc);
                   // Log.d(TAG, "doInBackground: doc2 => " + doc2);

                    arrayList.add(new NewsDTO(img, title, content, league, match_title, match_date, match_title));
                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }


            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsDTO> arrayList) {

            // doInBackground 완료 후 실행시킬 코드
            for (int i = 0; i < 3; i++) {
                Glide.with(context).load(arrayList.get(i).getImg()).into(news_image[i]);
                news_title_text[i].setText(arrayList.get(i).getTitle() + "\n");
                ;
                news_content_text[i].append(arrayList.get(i).getContent() + "\n");
                news_content_text[i].append(arrayList.get(i).getLeague() + "\n");

                match_title[i].setText(arrayList.get(i).getMatch_title() + " 리그\n");
                match_time[i].setText(arrayList.get(i).getMatch_time() + "\n");
                match_date[i].setText(arrayList.get(i).getMatch_date() + "\n");
                match_content[i].setText(arrayList.get(i).getMatch_content() + "\n");


            }


        }


    }

    private String getTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy" + "년 " + "MM" + "월 " + "dd"+"일 " +
                                                                    "hh:mm" + "\n★오늘의 경기★");
        String getTime = dateFormat.format(date);

        //Log.d(TAG, "getTime: " + getTime);
        return getTime;

    }

}

