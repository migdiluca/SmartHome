package com.smartdesigns.smarthomehci.repository.getStateReturn;

public class getStateBlinds {
    private String status;
    private int level;

    public getStateBlinds(String status, int level){
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
