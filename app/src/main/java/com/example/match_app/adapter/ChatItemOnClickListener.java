package com.example.match_app.adapter;

import android.view.View;

public interface ChatItemOnClickListener {
    public void onItemClick(ChatListAdapter.ViewHolder holderm,
                            View view, int position);
}
