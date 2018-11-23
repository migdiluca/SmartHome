package com.smartdesigns.smarthomehci.repository.getStateReturn;

public class GetStateBlinds implements GetState {
    private String status;
    private int level;

    public GetStateBlinds(String status, int level){
        this.status = status;
        this.level = level;
    }

    public GetStateBlinds(){

    }

    public String getStatus() {
        return status;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
