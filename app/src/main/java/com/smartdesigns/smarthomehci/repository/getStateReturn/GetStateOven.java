package com.smartdesigns.smarthomehci.repository.getStateReturn;

import java.io.Serializable;

public class GetStateOven implements GetState, Serializable {
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

    public GetStateOven(){

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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setConvection(String convection) {
        this.convection = convection;
    }

    public void setGrill(String grill) {
        this.grill = grill;
    }

    public void setHeat(String heat) {
        this.heat = heat;
    }
}
