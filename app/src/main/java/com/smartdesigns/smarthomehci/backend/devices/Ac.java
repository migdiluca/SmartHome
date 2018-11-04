package com.smartdesigns.smarthomehci.backend.devices;

import com.smartdesigns.smarthomehci.backend.Action;
import com.smartdesigns.smarthomehci.backend.Device;

public class Ac extends Device {
    private static final String typeId="ac";

    public Ac(String name){
        super(name, typeId);
    }

    public Action getTurnOn(){
        return new Action(id, "turnOn", "{}");
    }

    public Action getTurnOff(){
        return new Action(id, "turnOff", "{}");
    }

    public Action getSetTemperature(int temp){
        return new Action(id, "setTemperature", "{["+temp+"]}");
    }

    public Action getSetMode(String mode){
        return new Action(id, "setMode", "{["+mode+"]}");
    }

    public Action getSetVerticalSwing(int value){
        return new Action(id, "setVerticalSwing", "{["+value+"]}");
    }

    public Action getSetHorizontalSwing(int value){
        return new Action(id, "setHorizontalSwing", "{["+value+"]}");
    }

    public Action getSetFanSpeed(int value){
        return new Action(id, "setFanSpeed", "{["+value+"]}");
    }

}
