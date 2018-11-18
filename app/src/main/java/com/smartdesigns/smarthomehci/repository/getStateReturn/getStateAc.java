package com.smartdesigns.smarthomehci.repository.getStateReturn;

public class getStateAc {
    private String status;
    private int temperature;
    private String verticalSwing;
    private String horizontalSwing;
    private String fanSpeed;

    public getStateAc(String status, int temperature, String verticalSwing, String horizontalSwing,
                      String fanSpeed){
        this.status = status;
        this.temperature = temperature;
        this.verticalSwing = verticalSwing;
        this.horizontalSwing = horizontalSwing;
        this.fanSpeed = fanSpeed;
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
}
