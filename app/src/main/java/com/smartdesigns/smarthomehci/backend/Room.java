package com.smartdesigns.smarthomehci.backend;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.smartdesigns.smarthomehci.DevicesFragment;
import com.smartdesigns.smarthomehci.Home;
import com.smartdesigns.smarthomehci.Utils.RecyclerViewAdapter;
import com.smartdesigns.smarthomehci.Utils.RefreshFragment;
import com.smartdesigns.smarthomehci.repository.ApiConnection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room implements RecyclerInterface, Serializable {
    private String id;
    private String name;
    private String meta;
    //private int background = -1;

    public Room(String name, String meta) {
        this.name = name;
        this.meta = meta;
    }

    public Room(String id, String name,String meta) {
        this.id = id;
        this.name = name;
        this.meta = meta;
    }

    @Override
    public void setBackground(int background) {
        Gson gson = new Gson();
        Meta aux = gson.fromJson(this.meta, Meta.class);
        aux.setBackground(String.valueOf(background));
        this.meta = gson.toJson(aux, Meta.class);
    }

    @Override
    public int getBackground() {
        Gson gson = new Gson();
        Meta aux = gson.fromJson(this.meta, Meta.class);
        String bg = aux.getBackground();
        if(bg == null){

            Random rand = new Random();

            List<Integer> colors = RecyclerViewAdapter.getColors();
            this.setBackground(colors.get(rand.nextInt(colors.size())));
            return getBackground();
        }
        return Integer.parseInt(aux.getBackground());
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

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getMeta() {
        return this.meta;
    }

    public void onClickAction(Serializable arg, Context context) {
        RefreshFragment fragment = new DevicesFragment();
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