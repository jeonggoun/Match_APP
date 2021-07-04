package com.example.match_app.dto;

public class OptionDTO {
    boolean vib;
    boolean sound;
    boolean public_post;
boolean chat;

    public boolean isChat() {
        return chat;
    }

    public void setChat(boolean chat) {
        this.chat = chat;
    }

    public boolean isPublic_post() {
        return public_post;
    }

    public void setPublic_post(boolean public_post) {
        this.public_post = public_post;
    }

    public boolean isKeyword() {
        return keyword;
    }
    public void setKeyword(boolean keyword) {
        this.keyword = keyword;
    }

    boolean keyword;

    public boolean isVib() {
        return vib;
    }

    public void setVib(boolean vib) {
        this.vib = vib;
    }

    public boolean isSound() {
        return sound;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }
}
