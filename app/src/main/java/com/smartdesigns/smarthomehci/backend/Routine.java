package com.smartdesigns.smarthomehci.backend;

import java.util.List;

public class Routine {
    private String id;
    private List<Action> actions;
    private String meta;

    public Routine(String id, List<Action> actions, String meta){
        this(actions, meta);
        this.id = id;
    }

    public Routine(List<Action> actions, String meta){
        this.meta = meta;
        this.actions = actions;
    }

    public String getId() {
        return id;
    }
}
