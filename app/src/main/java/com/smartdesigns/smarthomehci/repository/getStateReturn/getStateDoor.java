package com.smartdesigns.smarthomehci.repository.getStateReturn;

public class getStateDoor {
    private String status;
    private String lock;

    public getStateDoor(String status, String lock){
        this.status = status;
        this.lock = lock;
    }

    public String getStatus() {
        return status;
    }

    public String getLock() {
        return lock;
    }
}
