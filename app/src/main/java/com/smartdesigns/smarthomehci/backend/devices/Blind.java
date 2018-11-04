package com.smartdesigns.smarthomehci.backend.devices;

import com.smartdesigns.smarthomehci.backend.Action;
import com.smartdesigns.smarthomehci.backend.Device;

public class Blind extends Device {

    private static final String typeId = "blind";

    public Blind(String name){
        super(name, typeId);

    }

    public Action getUp(){
        return new Action(id, "up", "{}");
    }

    public Action getDown(){
        return new Action(id, "down", "{}");

    }
}
