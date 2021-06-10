package com.example.match_app.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.match_app.R;
import com.example.match_app.dto.NewsDTO;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import static com.example.match_app.MainActivity.user;
import static com.example.match_app.MainActivity.ImageUri;

public class HomeFragment extends Fragment {

    ImageView imageView, news_image[] = new ImageView[3];
    TextView textView, news_content_text[] = new TextView[3], news_title_text[] = new TextView[3];
    Context context;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        context = container.getContext();

        textView = viewGroup.findViewById(R.id.home_user_email);
        imageView = viewGroup.findViewById(R.id.homeImage);
        news_image[0] = viewGroup.findViewById(R.id.news_image1);
        news_image[1] = viewGroup.findViewById(R.id.news_image2);
        news_image[2] = viewGroup.findViewById(R.id.news_image3);
        if (ImageUri != null)
            imageView.setImageURI(ImageUri);
        textView.setText(user.getEmailId());

        news_content_text[0] = viewGroup.findViewById(R.id.news_content_text1);
        news_content_text[1] = viewGroup.findViewById(R.id.news_content_text2);
        news_content_text[2] = viewGroup.findViewById(R.id.news_content_text3);

        news_title_text[0] = viewGroup.findViewById(R.id.news_title_text1);
        news_title_text[1] = viewGroup.findViewById(R.id.news_title_text2);
        news_title_text[2] = viewGroup.findViewById(R.id.news_title_text3);

        //뉴스크롤링
        newsData task = new newsData();
        task.execute();

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
                Elements doc = document.select("a.link_today");

                int region_num = 0;
                String img = "";
                String  title= "";
                String content = "";
                String league = "";
                Log.d(TAG, "doInBackground: arrayListSize => " + doc.size());
                Log.d(TAG, "doInBackground: arrayListSize => " + doc.toString());


                ArrayList<Integer> index = new ArrayList<>();
                for(int j=0;j<doc.size();j++ ){
                    index.add(j);
                }
                int[] index_list = {0, 0, 0};
                for(int j=0;j<3;j++ ){
                    int k = (int) Math.floor(Math.random()*index.size());
                    index_list[j] = index.get(k);
                    index.remove(k);
                }
//                int i = (int) Math.floor(Math.random()*5);
                for(int j=0; j<3; j++) {
                    int i = index_list[j];
                    img = doc.get(i).select("img").attr("src");
                    title = doc.get(i).select("strong").text();
                    content = doc.get(i).select("p").text();
                    league = doc.get(i).select("span").text();

                    Log.d(TAG, "doInBackground: arrayListSize => " + img + "\n"  + title + "\n" + content + "\n" + league + "\n");

                    arrayList.add(new NewsDTO(img, title, content, league ));

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<NewsDTO> arrayList) {

            // doInBackground 완료 후 실행시킬 코드
            for(int i=0; i<3; i++){
                Glide.with(context).load(arrayList.get(i).getImg()).into(news_image[i]);
                news_title_text[i].setText(arrayList.get(i).getTitle() + "\n"); ;
                news_content_text[i].append(arrayList.get(i).getContent()+ "\n") ;
                news_content_text[i].append(arrayList.get(i).getLeague() + "\n") ;


            }



        }
    }


    }

