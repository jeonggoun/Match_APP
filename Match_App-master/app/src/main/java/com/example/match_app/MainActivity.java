package com.example.match_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.match_app.post.Fragment1;
import com.example.match_app.post.Fragment2;
import com.example.match_app.post.Fragment3;
import com.example.match_app.post.Fragment4;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
//홈화면
    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;
    Fragment4 fragment4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contain, fragment1).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.tab1:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contain, fragment1).commit();
                        break;

                    case R.id.tab2:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contain, fragment2).commit();
                        break;

                    case R.id.tab3:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contain, fragment3).commit();
                        break;

                    case R.id.tab4:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contain, fragment4).commit();
                        break;
                }

                return true;
            }
        });

    }
}