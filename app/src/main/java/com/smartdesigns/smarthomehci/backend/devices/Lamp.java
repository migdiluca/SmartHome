package com.smartdesigns.smarthomehci.backend.devices;

import com.smartdesigns.smarthomehci.backend.Action;
import com.smartdesigns.smarthomehci.backend.Device;

public class Lamp extends Device {
    private static String typeId = "lampara";

    public Lamp(String name){
        super(name, typeId);
    }

    public Action getTurnOn(){
        return new Action(id, "turnOn", "{}");
    }

    public Action getTurnOff(){
        return new Action(id, "turnOff", "{}");
    }

    public Action getSetColor(int value){
        return new Action(id, "setColor", "{["+value+"]}");
    }

    public Action getSetBrightness(int value){
        return new Action(id, "setBrightness", "{["+value+"]}");
    }
}
