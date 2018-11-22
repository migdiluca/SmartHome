package com.smartdesigns.smarthomehci.repository.GetStateReturn;

public class GetStateRefrigerator {
    private int freezerTemperature;
    private int temperature;
    private String mode;

    public GetStateRefrigerator(int freezerTemperature, int temperature, String mode){
        this.freezerTemperature = freezerTemperature;
        this.temperature = temperature;
        this.mode = mode;
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
}
