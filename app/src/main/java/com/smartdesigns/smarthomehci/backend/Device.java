package com.smartdesigns.smarthomehci.backend;
import android.content.Context;
import android.content.Intent;

import com.smartdesigns.smarthomehci.Devices;
import com.smartdesigns.smarthomehci.Home;
import com.smartdesigns.smarthomehci.RoutinesFragment;

import java.io.Serializable;

public class Device implements RecyclerInterface, Serializable {
    protected String id;
    private String name;
    private String typeId;
    private String meta;


    public Device(String name, String typeId,String meta){
        this.name = name;
        this.typeId = typeId;
        this.meta = meta;
    }

    public Device(String name, String typeId, String meta, String id){
        this(name, typeId, meta);
        this.id=id;
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
