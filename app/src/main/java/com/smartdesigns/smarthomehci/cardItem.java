package com.smartdesigns.smarthomehci;

public abstract class cardItem {
    private String name;
    private int thumbnail;

    public void editName(String name){
        if(name != null)
            this.name = name;
    }

    public void editImage(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName(){
        return name;
    }

    public int getThumbnail(){
        return thumbnail;
    }
}
