package com.example.match_app.dto;

import java.io.Serializable;

public class MetaDTO implements Serializable {
    String title, game, date, chatToken, postToken;
    ChattingDTO recent;

    public String getPostToken() {
        return postToken;
    }

    public void setPostToken(String postToken) {
        this.postToken = postToken;
    }

    public String getChatToken() {
        return chatToken;
    }

    public void setChatToken(String chatToken) {
        this.chatToken = chatToken;
    }

    public ChattingDTO getRecent() {
        return recent;
    }

    public void setRecent(ChattingDTO recent) {
        this.recent = recent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
