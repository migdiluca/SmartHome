package com.smartdesigns.smarthomehci.Utils;

import com.smartdesigns.smarthomehci.FavouritesFragment;
import com.smartdesigns.smarthomehci.backend.Device;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FavouritesList {

    private Node first = null;

    private class Node {
        private Device device;
        private int access;
        private Node next;

        public Node(Device device) {
            this.device = device;
            int access = 1;
            this.next = null;
        }

        public Node(Device device, Node next){
            this.device = device;
            int access = 1;
            this.next = next;
        }
    }


    public void add(Device device) {
        if(first == null) {
            Node node = new Node(device,null);
        }
    }



 /*

    private class DeviceFavorite{
        private Device device;
        private int access;

        public DeviceFavorite(Device device) {
            this.device = device;
        }

        public void access() {
            access++;
        }

        public Device getDevice(){return device;}
    }
    private ArrayList<DeviceFavorite> list = new ArrayList<>();

    public boolean add(Device device) {
        DeviceFavorite df = contains(device);
        if(df != null){
            df.access();
            return false;
        }
        return list.add(new DeviceFavorite(device));
    }

    public DeviceFavorite contains(Device device) {
        for(int i = 0; i< list.size(); i++) {
            if(list.get(i).getDevice() == device)
                return list.get(i);
        }
        return null;
    }

    public ArrayList<Device> getFavouritesDevices(int amount) {
        Collections.sort(list,new Comparator<DeviceFavorite>(){
            @Override
            public int compare(DeviceFavorite d1,DeviceFavorite d2) {
                return d1.access - d2.access;
            }
        });
        ArrayList<Device> resp = new ArrayList<>();
        for(int i = 0; i < amount && i < list.size(); i++) {
            resp.add(list.get(i).getDevice());
        }
        return resp;
    }*/

}