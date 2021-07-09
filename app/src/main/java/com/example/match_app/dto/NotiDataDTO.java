package com.example.match_app.dto;public class NotiDataDTO {
    boolean read, like;
    String idToken, postToken;

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getPostToken() {
        return postToken;
    }

    public void setPostToken(String postToken) {
        this.postToken = postToken;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
