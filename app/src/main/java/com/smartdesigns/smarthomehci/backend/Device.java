package com.smartdesigns.smarthomehci.backend;



import java.util.List;

public abstract class Device {
    protected String id = null;
    private String name;
    private String typeId;

    public Device(String name, String typeId){
        this.name = name;
        this.typeId = typeId;
    }

    public Action getState(){
        return new Action(id, "getState", "{}");
    }

    public void setId(String id){
        this.id = id;
    }
}
