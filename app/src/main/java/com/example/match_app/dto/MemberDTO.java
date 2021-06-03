package com.example.match_app.dto;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

// 사용자 계정 정보 모델 클래스
public class MemberDTO implements Serializable {
    private String idToken;     // Firebase Uid (고유 토큰 정보)
    private String emailId;
    private String password;
    private String phoneNumber;
    // FireBase에서는 빈 생성자 필수
    private String nickName;
    private double latitude, longitude;
    private String address;

    public MemberDTO() {
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MemberDTO(String emailId) {
        this.emailId = emailId;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
