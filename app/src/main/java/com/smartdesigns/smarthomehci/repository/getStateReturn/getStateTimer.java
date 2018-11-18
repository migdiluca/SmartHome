package com.smartdesigns.smarthomehci.repository.getStateReturn;

public class getStateTimer {
    private String newStatus;
    private int interval;
    private int remaining;

    public getStateTimer(String newStatus, int interval, int remaining){
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