package com.smartdesigns.smarthomehci.repository.getStateReturn;

public class GetStateDoor implements GetState{
    private String status;
    private String lock;

    public GetStateDoor(String status, String lock){
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
