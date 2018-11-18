package com.smartdesigns.smarthomehci.repository.getStateReturn;

public class getStateRefrigerator {
    private int freezerTemperature;
    private int temperature;
    private String mode;

    public getStateRefrigerator(int freezerTemperature, int temperature, String mode){
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
