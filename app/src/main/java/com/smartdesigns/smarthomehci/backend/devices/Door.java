package com.smartdesigns.smarthomehci.backend.devices;

import com.smartdesigns.smarthomehci.backend.Action;
import com.smartdesigns.smarthomehci.backend.Device;

public class Door extends Device {
    private static String typeId = "door";

    public Door(String name){
        super(name, typeId);
    }

    public Action getOpen(){
        return new Action(id, "open", "{[]}");
    }

    public Action getClose(){
        return new Action(id, "close", "{[]}");
    }

    public Action getLock(){
        return new Action(id, "lock", "{[]}");
    }

    public Action getUnlock(){
        return new Action(id, "unlock", "{[]}");
    }
}
