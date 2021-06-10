package com.example.match_app.dto;

public class NewsDTO {

    private String img;
    private String title;
    private String content;
    private String league;


    public NewsDTO(String img, String title, String content, String league) {
        this.img = img;
        this.title = title;
        this.content = content;
        this.league = league;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

}
