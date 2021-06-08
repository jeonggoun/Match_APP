package com.example.match_app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.match_app.MainActivity;
import com.example.match_app.R;
import com.example.match_app.adapter.IconViewer;
import com.example.match_app.dto.IconDTO;

import java.util.ArrayList;

public class EtcFragment extends Fragment {

    GridView gv;
    Context context;
    IconAdapter iconAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_etc, container, false);
        TextView tv_nick = viewGroup.findViewById(R.id.tv_nick);
        TextView tv_local = viewGroup.findViewById(R.id.tv_local);
        Button btn_logout = viewGroup.findViewById(R.id.btn_logout);
        Button btn_profile = viewGroup.findViewById(R.id.btn_profile);
        gv = viewGroup.findViewById(R.id.gridView);

        iconAdapter = new IconAdapter();
        iconAdapter.addItem(new IconDTO("모임목록", R.drawable.etcf1));
        iconAdapter.addItem(new IconDTO("동네인증", R.drawable.etcf2));
        iconAdapter.addItem(new IconDTO("선호목록", R.drawable.etcf3));
        gv.setAdapter(iconAdapter);
        return  viewGroup;
    }

    private class IconAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        ArrayList<IconDTO> items = new ArrayList<IconDTO>();


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            context = parent.getContext();
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.icon_item, parent, false);
            }

            ImageView imageView = convertView.findViewById(R.id.iv_icon);
            TextView textView = convertView.findViewById(R.id.tv_icon);

            IconDTO dto = items.get(i);
            imageView.setImageResource(dto.getIconImage());
            textView.setText(dto.getIconId());

            return convertView;
        }

        public void addItem(IconDTO dto) {
            items.add(dto);
        }
    }
}