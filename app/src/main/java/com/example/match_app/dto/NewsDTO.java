package com.example.match_app.dto;

public class NewsDTO {

    private String img;
    private String title;
    private String content;
    private String league;

    private String match_title;
    private String match_time;
    private String match_date;
    private String match_content;



    public NewsDTO(String img, String title, String content, String league, String match_title, String match_date, String match_content, String match_time) {
        this.img = img;
        this.title = title;
        this.content = content;
        this.league = league;
        ////
        this.match_title = match_title;
        this.match_time = match_time;
        this.match_date = match_date;
        this.match_content = match_content;
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

    /////
    public String getMatch_title() { return match_title; }

    public void setMatch_title(String match_title) { this.match_title = match_title; }

    public String getMatch_time() { return match_time; }

    public void setMatch_time(String match_time) { this.match_time = match_time; }

    public String getMatch_date() { return match_date;  }

    public void setMatch_date(String match_date) { this.match_date = match_date;  }

    public String getMatch_content() { return match_content; }

    public void setMatch_content(String match_content) { this.match_content = match_content; }

}
