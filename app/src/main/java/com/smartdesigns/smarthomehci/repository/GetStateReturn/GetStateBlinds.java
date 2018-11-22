package com.smartdesigns.smarthomehci.repository.GetStateReturn;

public class GetStateBlinds {
    private String status;
    private int level;

    public GetStateBlinds(String status, int level){
        this.status = status;
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public int getLevel() {
        return level;
    }
}
