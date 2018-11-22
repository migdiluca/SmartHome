package com.smartdesigns.smarthomehci.repository.GetStateReturn;

public class GetStateLamp {
    private String status;
    private String color;
    private int brightness;

    public GetStateLamp(String status, String color, int brightness){
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
