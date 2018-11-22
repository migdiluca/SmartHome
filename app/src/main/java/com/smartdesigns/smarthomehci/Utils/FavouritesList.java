package com.smartdesigns.smarthomehci.Utils;

import android.util.Log;

import com.smartdesigns.smarthomehci.FavouritesFragment;
import com.smartdesigns.smarthomehci.backend.Device;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FavouritesList {

    private class DeviceFavorite{
        private Device device;
        private int access;

        public DeviceFavorite(Device device) {
            this.device = device;
            this.access = 1;
        }

        public void access() {
            access++;
        }

        public Device getDevice(){return device;}
    }

    private ArrayList<DeviceFavorite> list = new ArrayList<>();

    public boolean access(Device device) {
        DeviceFavorite df = contains(device);
        if(df != null){
            df.access();
            return false;
        }
        return list.add(new DeviceFavorite(device));
    }

    public DeviceFavorite contains(Device device) {
        for(int i = 0; i< list.size(); i++) {
            if(list.get(i).getDevice().equals(device))
                return list.get(i);
        }
        return null;
    }

    public ArrayList<Device> getFavouritesDevices(int amount) {
        Collections.sort(list,new Comparator<DeviceFavorite>(){
            @Override
            public int compare(DeviceFavorite d1,DeviceFavorite d2) {
                return d2.access - d1.access;
            }
        });
        ArrayList<Device> resp = new ArrayList<>();

        for(int i = 0; i < amount && i < list.size(); i++) {
            resp.add(list.get(i).getDevice());
            Log.d("FAVORITO", list.get(i).getDevice().getName());
            Log.d("VALOR:", Integer.toString(list.get(i).access));
        }
        return resp;
    }

}
