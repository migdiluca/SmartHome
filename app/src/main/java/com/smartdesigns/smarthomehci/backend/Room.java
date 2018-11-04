package com.smartdesigns.smarthomehci.backend;

import java.util.List;

public class Room {
    private String name;
    private List<Device> devices;

    public Room(String name){
        this.name = name;
    }

    public void addDevice(Device device){
        this.devices.add(device);
    }

    public void removeDevice(Device device){
        this.devices.remove(device);
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Device> getDevices() {
        return devices;
    }
}
