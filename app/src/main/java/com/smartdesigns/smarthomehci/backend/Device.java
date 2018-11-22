package com.smartdesigns.smarthomehci.backend;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.smartdesigns.smarthomehci.Devices;
import com.smartdesigns.smarthomehci.FavouritesFragment;
import com.smartdesigns.smarthomehci.Home;
import com.smartdesigns.smarthomehci.R;
import com.smartdesigns.smarthomehci.RoutinesFragment;
import com.smartdesigns.smarthomehci.Utils.FavouritesList;
import com.smartdesigns.smarthomehci.Utils.RecyclerViewAdapter;
import com.smartdesigns.smarthomehci.repository.ApiConnection;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public class Device implements RecyclerInterface, Serializable {
    protected String id;
    private String name;
    private String typeId;
    private String meta;
    //private int background = -1;

    private Device thisDevice;


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

    public String getImg(){
        Gson gson = new Gson();
        Meta aux = gson.fromJson(this.meta, Meta.class);
        String img = aux.getImg();
        if(img == null) {
            return TypeId.getImg(typeId);
        }else{
            return img;
        }
    }

    public void onClickAction(Serializable arg, final Context context) {

        thisDevice = this;
        ApiConnection api = ApiConnection.getInstance(context);
        api.getDevice(this.id,new Response.Listener<Device>() {
            @Override
            public void onResponse(Device response) {
                FavouritesFragment.access(thisDevice);
                Home.getInstance().setDeviceFragment(thisDevice);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, context.getResources().getString(R.string.device_not_exist), Toast.LENGTH_LONG).show();

            }
        });
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
