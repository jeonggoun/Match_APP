package com.example.match_app.dto;

import android.widget.CheckBox;

import java.util.ArrayList;

public class SportsDTO {

    String Sports;
    boolean Checked;


    public SportsDTO() {
    }



    public SportsDTO(String sports, boolean checked) {
        Checked = checked;
        Sports = sports;
    }

    public String getSports() {
        return Sports;
    }

    public void setSports(String sports) {
        Sports = sports;
    }

    public boolean isChecked() {
        return Checked;
    }

    public void setChecked(boolean checked) {
        this.Checked = checked;
    }
}
