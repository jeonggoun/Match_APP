package com.example.match_app.dto;

import java.util.ArrayList;

public class FavoriteDTO {
    private ArrayList<Boolean> chked1;
    private ArrayList<Boolean> chked2;
    private ArrayList<Boolean> chked3;

    public FavoriteDTO() {
    }

    public ArrayList<Boolean> getChked1() {
        return chked1;
    }

    public void setChked1(ArrayList<Boolean> chked1) {
        this.chked1 = chked1;
    }

    public ArrayList<Boolean> getChked2() {
        return chked2;
    }

    public void setChked2(ArrayList<Boolean> chked2) {
        this.chked2 = chked2;
    }

    public ArrayList<Boolean> getChked3() {
        return chked3;
    }

    public void setChked3(ArrayList<Boolean> chked3) {
        this.chked3 = chked3;
    }
}