package com.smartdesigns.smarthomehci.repository.getStateReturn;

public class GetStateTimer implements GetState{
    private String status;
    private String interval;
    private int remaining;

    public GetStateTimer(String newStatus, String interval, int remaining){
        this.status = newStatus;
        this.interval = interval;
        this.remaining = remaining;
    }

    public GetStateTimer(){

    }

    public String getInterval() {
        return interval;
    }

    public int getRemaining() {
        return remaining;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }
}
