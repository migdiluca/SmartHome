package com.smartdesigns.smarthomehci.backend;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.smartdesigns.smarthomehci.DevicesFragment;
import com.smartdesigns.smarthomehci.Home;

import java.io.Serializable;
import java.util.List;

public class Room implements RecyclerInterface, Serializable {
    private String id;
    private String name;
    private List<String> meta;
    //private int background = -1;

    public Room(String name, List<String> meta) {
        this.name = name;
        this.meta = meta;
    }

    public Room(String id, String name, List<String> meta) {
        this.id = id;
        this.name = name;
        this.meta = meta;
    }

    @Override
    public void setBackground(int background) {
        this.meta.add(Integer.toString(background));
    }

    @Override
    public int getBackground() {
        return Integer.parseInt(this.meta.get(1));
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setMeta(List<String> meta) {
        this.meta = meta;
    }

    public List<String> getMeta() {
        return this.meta;
    }

    public void onClickAction(Serializable arg, Context context) {
        Fragment fragment = new DevicesFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable("Object", this);
        fragment.setArguments(arguments);

        Home home = Home.getInstance();
        home.setFragment(fragment);
    }

    @Override
    public String toString() {
        return (this.id == null) ?
                String.format("%s - %s", this.getName(), this.getMeta()) :
                String.format("%s - %s - %s", this.getId(), this.getName(), this.getMeta());
    }

    @Override
    public boolean equals(Object o){
        if(o == null || !(o instanceof Room)) {
            return false;
        }

        Room d = (Room)o;
        return this.name.equals(d.name)  && this.meta.equals(d.meta);
    }
}