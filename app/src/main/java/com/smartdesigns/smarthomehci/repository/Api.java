package com.smartdesigns.smarthomehci.repository;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;
import com.smartdesigns.smarthomehci.backend.Device;
import com.smartdesigns.smarthomehci.backend.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Api {
    private static Api instance;
    private static RequestQueue requestQueue;
    private final String URL = "http://192.168.1.137:8080/api/";

    private Api(Context context) {
        this.requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
    }

    public static synchronized Api getInstance(Context context) {
        if (instance == null) {
            instance = new Api(context);
        }
        return instance;
    }

    public String addRoom(Room room, Response.Listener<Room> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Room, Room> request =
                new GsonRequest<Room, Room>(Request.Method.POST, url, room, "room", new TypeToken<Room>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String modifyRoom(Room room, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms/" + room.getId();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Room, Boolean> request =
                new GsonRequest<Room, Boolean>(Request.Method.PUT, url, room, "result", new TypeToken<Boolean>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String deleteRoom(String id, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms/" + id;
        GsonRequest<Object, Boolean> request =
                new GsonRequest<Object, Boolean>(Request.Method.DELETE, url, null, "result", new TypeToken<Boolean>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String getRoom(String id, Response.Listener<Room> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms/" + id;
        GsonRequest<Object, Room> request =
                new GsonRequest<Object, Room>(Request.Method.GET, url, null, "room", new TypeToken<Room>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String getRooms(Response.Listener<ArrayList<Room>> listener, Response.ErrorListener errorListener) {
        String url = URL + "rooms/";
        GsonRequest<Object, ArrayList<Room>> request =
                new GsonRequest<Object, ArrayList<Room>>(Request.Method.GET, url, null, "rooms", new TypeToken<ArrayList<Room>>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String getDevices(Response.Listener<ArrayList<Device>> listener, Response.ErrorListener errorListener) {
        String url = URL + "devices/";
        GsonRequest<Object, ArrayList<Device>> request =
                new GsonRequest<Object, ArrayList<Device>>(Request.Method.GET, url, null, "devices", new TypeToken<ArrayList<Device>>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public void cancelRequest(String uuid) {
        if ((uuid != null) && (requestQueue != null)) {
            requestQueue.cancelAll(uuid);
        }
    }
}