package com.smartdesigns.smarthomehci.repository.GetStateReturn;

public class GetStateOven {
    private String status;
    private int temperature;
    private String heat;
    private String grill;
    private String convection;

    public GetStateOven(String status, int temperature, String heat, String grill, String convection){
        this.status = status;
        this.temperature = temperature;
        this.heat = heat;
        this.grill = grill;
        this.convection = convection;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getHeat() {
        return heat;
    }

    public String getGrill() {
        return grill;
    }

    public String getStatus() {
        return status;
    }

    public String getConvection() {
        return convection;
    }
}
