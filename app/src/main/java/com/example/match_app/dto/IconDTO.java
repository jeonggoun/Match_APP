package com.example.match_app.dto;

public class IconDTO {
    String iconId;
    int iconImage;

    public IconDTO(String iconId, int iconImage) {
        this.iconId = iconId;
        this.iconImage = iconImage;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public int getIconImage() {
        return iconImage;
    }

    public void setIconImage(int iconImage) {
        this.iconImage = iconImage;
    }
}


