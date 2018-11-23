package com.smartdesigns.smarthomehci.repository.getStateReturn;

import java.io.Serializable;

public class GetStateAlarm implements GetState, Serializable {
    private String status;

    public GetStateAlarm(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
