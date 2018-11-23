package com.smartdesigns.smarthomehci.repository.getStateReturn;

public class GetStateLamp implements GetState{
    private String status;
    private String color;
    private int brightness;

    public GetStateLamp(String status, String color, int brightness){
        this.status = status;
        this.color = color;
        this.brightness = brightness;
    }

    public GetStateLamp(){

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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
