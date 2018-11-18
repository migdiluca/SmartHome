package com.smartdesigns.smarthomehci.backend;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.smartdesigns.smarthomehci.DevicesFragment;
import com.smartdesigns.smarthomehci.Home;
import com.smartdesigns.smarthomehci.RoutinesFragment;

import java.io.Serializable;
import java.util.List;

public class Routine implements RecyclerInterface, Serializable{
    private String id;
    private String name;
    private List<Action> actions;
    private List<String> meta;
    //private int background = -1;

    public Routine(String id, List<Action> actions, List<String> meta){
        this(actions, meta);
        this.id = id;
        //TODO:BORRAR
        this.name = "hola";
    }

    public Routine(List<Action> actions, List<String> meta){
        this.meta = meta;
        this.actions = actions;
    }

    public String getName(){return name;}
    public List<String> getMeta(){return meta;}
    public String getId(){return id;}
    @Override
    public void setBackground(int background) {
        this.meta.add(Integer.toString(background));
    }

    @Override
    public int getBackground() {
        return Integer.parseInt(meta.get(1));
    }

    public void onClickAction(Serializable arg, Context context){
        RoutinesFragment.setCurrentRoutine(this);

        Fragment fragment = new DevicesFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable("Object", this);
        fragment.setArguments(arguments);

        Home home = Home.getInstance();
        home.setFragment(fragment);
    };
}
