package com.smartdesigns.smarthomehci.repository.getStateReturn;

import com.smartdesigns.smarthomehci.backend.Device;

public class DeviceWrapper {
    private Device device;
    private GetState state;

    public DeviceWrapper(Device device, GetState state){
        this.device = device;
        this.state = state;
    }

    public Device getDevice() {
        return device;
    }

    public GetState getState() {
        return state;
    }
}
