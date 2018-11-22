package com.smartdesigns.smarthomehci.Utils;

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
    }

    private ArrayList<DeviceFavorite> list = new ArrayList<>();

    List<Room> roomList = new ArrayList<>();
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

    private void getRooms() {
        roomList = new ArrayList<>();

        ApiConnection api = ApiConnection.getInstance(Home.getInstance());
        api.getRooms(new Response.Listener<List<Room>>() {
            @Override
            public void onResponse(List<Room> response) {

                for (Room room : response) {

                    roomList.add(room);

                    if (room.getMeta().matches("\"background\"") == false) {
                        int aux = room.getBackground();
                        ApiConnection.getInstance(Home.getInstance()).updateRoom(room, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    private void getDevices() {
        devicesList = new ArrayList<>();
        ApiConnection api = ApiConnection.getInstance(Home.getInstance());
        for (Room room : roomList) {
            api.getRoomDevices(room, new Response.Listener<List<Device>>() {
                @Override
                public void onResponse(List<Device> response) {
                    for (Device device : response) {
                        devicesList.add(device);
                        if (device.getMeta().matches("\"background\"") == false) {
                            int aux = device.getBackground();
                            ApiConnection.getInstance(Home.getInstance()).updateDevice(device, new Response.Listener<Boolean>() {
                                @Override
                                public void onResponse(Boolean response) {

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    errorFlag = 1;
                                }
                            });
                        }

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    errorFlag = 1;
                }
            });
        }
    }

    private void eraseNotExistingDevices() {
        if (devicesList.size() == list.size())
            return;

        List<DeviceFavorite> auxDeviceList = new ArrayList<>();

        for (Device device : devicesList) {
            auxDeviceList.add(new DeviceFavorite(device));
        }

        Log.d("AUXLISTSIZE", Integer.toString(roomList.size()));
        for (int i = 0; i < list.size(); i++) {
            if (!auxDeviceList.contains(list.get(i)))
                list.remove(i);
        }
    }

    public ArrayList<Device> getFavouritesDevices(int amount) {
        Collections.sort(list, new Comparator<DeviceFavorite>() {
            @Override
            public int compare(DeviceFavorite d1, DeviceFavorite d2) {
                return d2.access - d1.access;
            }
        });


        getRooms();
        Log.d("ROOMSIZE", Integer.toString(roomList.size()));
        getDevices();

        if (errorFlag == 1) {
            errorFlag = 0;
            return null;
        }

        eraseNotExistingDevices();

        ArrayList<Device> resp = new ArrayList<>();
        for (int i = 0; i < amount && i < list.size(); i++) {
            resp.add(list.get(i).getDevice());
        }
        return resp;
    }

}
