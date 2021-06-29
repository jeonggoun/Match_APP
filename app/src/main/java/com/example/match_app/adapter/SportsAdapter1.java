package com.example.match_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.match_app.R;
import com.example.match_app.dto.FavoriteDTO;
import com.example.match_app.dto.SportsDTO;

import java.util.ArrayList;

import static com.example.match_app.Common.CommonMethod.favoriteDTO;

public class SportsAdapter1 extends BaseAdapter {

    ArrayList<FavoriteDTO> dtos;
    ArrayList<SportsDTO> items;
    Context context;
    FavoriteDTO favoriteDTO;

    public SportsAdapter1(ArrayList<SportsDTO> items, Context context){
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.grid_sports1, parent, false);

        ConstraintLayout cblay_01 = convertView.findViewById(R.id.gridcblay_01);
        CheckBox cb01 = convertView.findViewById(R.id.gridcb01);

        TextView tv01 = convertView.findViewById(R.id.gridtv01);
        tv01.setText(items.get(i).getSports());

        if (items.get(i).isChecked() == true) {
            cb01.setChecked(true);
        } else {
            cb01.setChecked(false);
        }

        cblay_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb01.isChecked() == false) cb01.setChecked(true);
                else cb01.setChecked(false);

                items.get(i).setChecked(cb01.isChecked());
            }
        });

        cb01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.get(i).setChecked(cb01.isChecked());
            }
        });

        return convertView;
    }
}
