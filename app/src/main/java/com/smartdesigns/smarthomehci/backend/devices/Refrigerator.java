package com.smartdesigns.smarthomehci.backend.devices;

import com.smartdesigns.smarthomehci.backend.Action;
import com.smartdesigns.smarthomehci.backend.Device;

public class Refrigerator extends Device {
    private static final String typeId = "refrig";

    public Refrigerator(String name){
        super(name, typeId);
    }

    public Action getSetFreezerTemperature(int temp){
        return new Action(id, "setFreezerTemperature", "{["+temp+"]}");
    }

    public Action getSetTemperature(int temp){
        return new Action(id, "setTemperature", "{["+temp+"]}");
    }

    public Action getSetMode(String mode){
        return new Action(id, "setMode", "{["+mode+"]}");
    }


}
