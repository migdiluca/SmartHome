package com.smartdesigns.smarthomehci.backend;

public class Action {
    private String id;
    private String name;
    private String params;

    public Action(String id, String name, String params){
        this.id = id;
        this.name = name;
        this.params = params;
    }
}
