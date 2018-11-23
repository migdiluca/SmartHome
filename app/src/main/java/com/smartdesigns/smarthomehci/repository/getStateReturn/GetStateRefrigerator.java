package com.smartdesigns.smarthomehci.repository.getStateReturn;

public class GetStateRefrigerator implements GetState {
    private int freezerTemperature;
    private int temperature;
    private String mode;

    public GetStateRefrigerator(int freezerTemperature, int temperature, String mode){
        this.freezerTemperature = freezerTemperature;
        this.temperature = temperature;
        this.mode = mode;
    }

    public GetStateRefrigerator(){

    }

    public int getTemperature() {
        return temperature;
    }

    public int getFreezerTemperature() {
        return freezerTemperature;
    }

    public String getMode() {
        return mode;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setFreezerTemperature(int freezerTemperature) {
        this.freezerTemperature = freezerTemperature;
    }
}
