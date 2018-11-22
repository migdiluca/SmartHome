package com.smartdesigns.smarthomehci.repository.getStateReturn;

public class GetStateAc {
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
}
