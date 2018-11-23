package com.smartdesigns.smarthomehci.repository.getStateReturn;

public class GetStateAlarm implements GetState {
    private String status;

    public GetStateAlarm(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
