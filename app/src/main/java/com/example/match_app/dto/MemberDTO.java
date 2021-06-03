package com.example.match_app.dto;

// 사용자 계정 정보 모델 클래스
public class MemberDTO {
    private String idToken;     // Firebase Uid (고유 토큰 정보)
    private String emailId;
    private String password;
    private String phoneNumber;
    // FireBase에서는 빈 생성자 필수


    public MemberDTO() {
    }

    public MemberDTO( String emailId) {
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
