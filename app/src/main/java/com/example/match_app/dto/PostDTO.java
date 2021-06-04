package com.example.match_app.dto;

import java.io.Serializable;

//adapter에서 xml으로 이어주기 위해 만든 DTO
public class PostDTO implements Serializable {
    String postKey, matchConfirm;

    String game, title, time, place, fee, imgPath, content, writer, writerToken;


    public PostDTO() {
    }

    public PostDTO(String postKey, String game, String title, String time, String place, String fee, String imgPath) {
        this.game = game;
        this.title = title;
        this.time = time;
        this.place = place;
        this.fee = fee;
        this.imgPath = imgPath;
        this.postKey = postKey;
    }

    public String getMatchConfirm() {
        return matchConfirm;
    }

    public void setMatchConfirm(String matchConfirm) {
        this.matchConfirm = matchConfirm;
    }

    public String getWriterToken() {
        return writerToken;
    }

    public void setWriterToken(String writerToken) {
        this.writerToken = writerToken;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

}
