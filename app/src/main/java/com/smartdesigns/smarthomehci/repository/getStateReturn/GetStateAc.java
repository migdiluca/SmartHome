package com.smartdesigns.smarthomehci.repository.getStateReturn;

public class GetStateAc implements GetState{
    private String status;
    private int temperature;
    private String verticalSwing;
    private String horizontalSwing;
    private String fanSpeed;
    private String mode;

    public GetStateAc(String status, int temperature, String verticalSwing, String horizontalSwing,
                      String fanSpeed, String mode){
        this.status = status;
        this.temperature = temperature;
        this.verticalSwing = verticalSwing;
        this.horizontalSwing = horizontalSwing;
        this.fanSpeed = fanSpeed;
        this.mode = mode;
    }

    public GetStateAc(){

    }

    public String getStatus() {
        return status;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getFanSpeed() {
        return fanSpeed;
    }

    public String getVerticalSwing() {
        return verticalSwing;
    }

    public String getHorizontalSwing() {
        return horizontalSwing;
    }

    public String getMode() {
        return mode;
    }

    public void setFanSpeed(String fanSpeed) {
        this.fanSpeed = fanSpeed;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setHorizontalSwing(String horizontalSwing) {
        this.horizontalSwing = horizontalSwing;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setVerticalSwing(String verticalSwing) {
        this.verticalSwing = verticalSwing;
    }
}
