package com.smartdesigns.smarthomehci.backend.devices;

import com.smartdesigns.smarthomehci.backend.Action;
import com.smartdesigns.smarthomehci.backend.Device;


public class Oven extends Device {

    private static final String typeId = "oven";

    public Oven(String name){
        super(name, typeId);
    }

    public Action getTurnOn(){
        return new Action(id, "turnOn", "{}");
    }

    public Action getTurnOff(){
        return new Action(id, "turnOff", "{}");
    }

    public Action getSetTemperature(int value){
        return new Action(id, "setTemperature", "{["+value+"]}");
    }

    public Action getSetHeat(String mode){
        return new Action(id, "setHeat", "{["+mode+"]}");
    }

    public Action getSetGrill(String mode){
        return new Action(id, "setGrill", "{["+mode+"]}");
    }

    public Action getSetConvection(String mode){
        return new Action(id, "setConvection", "{["+mode+"]}");
    }
}
