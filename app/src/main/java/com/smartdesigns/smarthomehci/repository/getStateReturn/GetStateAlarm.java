package com.smartdesigns.smarthomehci.repository.getStateReturn;

public class GetStateAlarm {
    private String status;

    public GetStateAlarm(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
