package com.smartdesigns.smarthomehci.repository.getStateReturn;

public class getStateLamp {
    private String status;
    private String color;
    private int brightness;

    public getStateLamp(String status, String color, int brightness){
        this.status = status;
        this.color = color;
        this.brightness = brightness;
    }

    public int getBrightness() {
        return brightness;
    }

    public String getColor() {
        return color;
    }

    public String getStatus(){
        return status;
    }
}