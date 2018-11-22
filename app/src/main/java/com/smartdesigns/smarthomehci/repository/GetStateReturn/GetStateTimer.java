package com.smartdesigns.smarthomehci.repository.GetStateReturn;

public class GetStateTimer {
    private String newStatus;
    private int interval;
    private int remaining;

    public GetStateTimer(String newStatus, int interval, int remaining){
        this.newStatus = newStatus;
        this.interval = interval;
        this.remaining = remaining;
    }

    public int getInterval() {
        return interval;
    }

    public int getRemaining() {
        return remaining;
    }

    public String getNewStatus() {
        return newStatus;
    }
}
