package com.smartdesigns.smarthomehci.repository.getStateReturn;

import java.io.Serializable;

public class GetStateDoor implements GetState, Serializable {
    private String status;
    private String lock;

    public GetStateDoor(String status, String lock){
        this.status = status;
        this.lock = lock;
    }

    public GetStateDoor(){

    }

    public String getStatus() {
        return status;
    }

    public String getLock() {
        return lock;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }
}
