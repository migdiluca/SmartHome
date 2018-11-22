package com.smartdesigns.smarthomehci.Utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.smartdesigns.smarthomehci.FavouritesFragment;
import com.smartdesigns.smarthomehci.Home;
import com.smartdesigns.smarthomehci.R;
import com.smartdesigns.smarthomehci.backend.Device;
import com.smartdesigns.smarthomehci.backend.Room;
import com.smartdesigns.smarthomehci.repository.ApiConnection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FavouritesList implements Serializable {

    private class DeviceFavorite implements Serializable {
        private Device device;
        private Integer access;

        public DeviceFavorite(Device device) {
            this.device = device;
            this.access = 1;
        }

        public void access() {
            access++;
        }

        public Device getDevice() {
            return device;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || !(o instanceof DeviceFavorite)) {
                return false;
            }

            DeviceFavorite d = (DeviceFavorite) o;
            return this.device.equals(d.device);
        }
    }

    private ArrayList<DeviceFavorite> list = new ArrayList<>();

    List<Device> devicesList = new ArrayList<>();

    int errorFlag = 0;

    public boolean access(Device device) {
        DeviceFavorite df = contains(device);
        if (df != null) {
            df.access();
            return false;
        }
        return list.add(new DeviceFavorite(device));
    }

    public DeviceFavorite contains(Device device) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDevice().equals(device))
                return list.get(i);
        }
        return null;
    }

    private void remove(Device device) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getDevice().equals(device))
                list.remove(i);
        }
    }


    public ArrayList<Device> getFavouritesDevices(int amount, final Context context) {
        Collections.sort(list, new Comparator<DeviceFavorite>() {
            @Override
            public int compare(DeviceFavorite d1, DeviceFavorite d2) {
                return d2.access - d1.access;
            }
        });


        devicesList = new ArrayList<>();

        ApiConnection api = ApiConnection.getInstance(context);
        api.getDevices(new Response.Listener<List<Device>>() {
            @Override
            public void onResponse(List<Device> response) {
                for (Device device : response) {
                    devicesList.add(device);
                }
                for(int i = 0; i < list.size(); i++) {
                    if(!devicesList.contains(list.get(i).getDevice()))
                        list.remove(i);
                }
                Log.d("FINISH:", "onResponse");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        Log.d("FINISH:", "creating resp");
        ArrayList<Device> resp = new ArrayList<>();
        for (int i = 0; i < amount && i < list.size(); i++) {
            resp.add(list.get(i).getDevice());
        }

        return resp;
    }

}
