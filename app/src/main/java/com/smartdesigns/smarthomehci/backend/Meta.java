package com.smartdesigns.smarthomehci.backend;

public class Meta {
    String img;
    String background;

    public Meta(String img, String background){
        this(img);
        this.background = background;
    }

    public Meta(String img){
        this.img = img;
    }

    public String getBackground() {
        return background;
    }

    public String getImg() {
        return img;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
