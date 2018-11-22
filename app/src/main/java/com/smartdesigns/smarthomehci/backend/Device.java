package com.smartdesigns.smarthomehci.backend;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import com.google.gson.Gson;
import com.smartdesigns.smarthomehci.Devices;
import com.smartdesigns.smarthomehci.FavouritesFragment;
import com.smartdesigns.smarthomehci.Home;
import com.smartdesigns.smarthomehci.RoutinesFragment;
import com.smartdesigns.smarthomehci.Utils.FavouritesList;
import com.smartdesigns.smarthomehci.Utils.RecyclerViewAdapter;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class Device implements RecyclerInterface, Serializable {
    protected String id;
    private String name;
    private String typeId;
    private String meta;
    //private int background = -1;


    public Device(String name, String typeId,String meta){
        this.name = name;
        this.typeId = typeId;
        this.meta = meta;
    }

    public Device(String name, String typeId, String meta, String id){
        this(name, typeId, meta);
        this.id=id;
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

    public void setId(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getTypeId() { return typeId; }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getMeta() {
        return this.meta;
    }

    public void onClickAction(Serializable arg, Context context) {

        FavouritesFragment.access(this);

        Intent device = new Intent(context, Devices.class);
        if(Home.getInstance().getCurrentMode() == 0)
            device.putExtra("mode", 0);
        else {
            device.putExtra("mode", 1);
            device.putExtra("routine",RoutinesFragment.getCurrentRoutine());
        }

        device.putExtra("device", this);
        context.startActivity(device);
    }

    @Override
    public String toString() {
        return "Id: " + this.id + "; typeId: "+ this.typeId + "; name: "+ this.name+"; meta: "+ this.meta;
    }

    @Override
    public boolean equals(Object o){
        if(o == null || !(o instanceof Device)) {
            return false;
        }

        Device d = (Device)o;
        return this.name.equals(d.name) && this.typeId.equals(d.typeId) && this.meta.equals(d.meta);
    }
}
