package com.example.match_app.dto;

public class MetaDTO {
    String title, game, date;
    ChattingDTO recent;

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
