package com.smartdesigns.smarthomehci.backend;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.smartdesigns.smarthomehci.DevicesFragment;
import com.smartdesigns.smarthomehci.Home;
import com.smartdesigns.smarthomehci.RoutinesFragment;
import com.smartdesigns.smarthomehci.Utils.RecyclerViewAdapter;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class Routine implements RecyclerInterface, Serializable{
    private String id;
    private String name;
    private List<Action> actions;
    private String meta;
    //private int background = -1;

    public Routine(String id, List<Action> actions, String meta){
        this(actions, meta);
        this.id = id;
        //TODO:BORRAR
        this.name = "hola";
    }

    public Routine(List<Action> actions, String meta){
        this.meta = meta;
        this.actions = actions;
    }

    public String getName(){return name;}
    public String getMeta(){return meta;}
    public String getId(){return id;}

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

    public void onClickAction(Serializable arg, Context context){
        RoutinesFragment.setCurrentRoutine(this);

        Fragment fragment = new DevicesFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable("Object", this);
        fragment.setArguments(arguments);

        Home home = Home.getInstance();
        home.setFragment(fragment);
    };

    public boolean containsDevice(Device device){
        for(Action action: actions){
            if(action.getDeviceId().equals(device.getId()))
                return true;
        }
        return false;
    }
}
