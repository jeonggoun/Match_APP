package com.example.match_app.dto;

import android.net.Uri;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;

// 사용자 계정 정보 모델 클래스
public class MemberDTO {
    private String idToken;     // Firebase Uid (고유 토큰 정보)
    private String emailId;
    private String phoneNumber;
    private String nickName;
    private double latitude, longitude;
    private String address;
    private String fileName, filePath;
    private String sports;
    private int changed;
    private String checked1;
    private String checked2;
    private String checked3;

    public String getChecked1() { return checked1; }
    public void setChecked1(String checked1) {
        this.checked1 = checked1;
    }
    public String getChecked2() {
        return checked2;
    }
    public void setChecked2(String checked2) {
        this.checked2 = checked2;
    }
    public String getChecked3() {
        return checked3;
    }
    public void setChecked3(String checked3) {
        this.checked3 = checked3;
    }

    public MemberDTO() {
    }

    public String getSports() {
        return sports;
    }

    public void setSports(String sports) {
        this.sports = sports;
    }

    public int getChanged() {
        return changed;
    }

    public void setChanged(int changed) {
        this.changed = changed;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getNickName() {
        return nickName;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
