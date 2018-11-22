package com.smartdesigns.smarthomehci.repository;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;
import com.smartdesigns.smarthomehci.backend.Action;
import com.smartdesigns.smarthomehci.backend.Device;
import com.smartdesigns.smarthomehci.backend.Room;
import com.smartdesigns.smarthomehci.backend.Routine;
import com.smartdesigns.smarthomehci.repository.getStateReturn.GetStateAc;
import com.smartdesigns.smarthomehci.repository.getStateReturn.GetStateAlarm;
import com.smartdesigns.smarthomehci.repository.getStateReturn.GetStateBlinds;
import com.smartdesigns.smarthomehci.repository.getStateReturn.GetStateDoor;
import com.smartdesigns.smarthomehci.repository.getStateReturn.GetStateLamp;
import com.smartdesigns.smarthomehci.repository.getStateReturn.GetStateOven;
import com.smartdesigns.smarthomehci.repository.getStateReturn.GetStateRefrigerator;
import com.smartdesigns.smarthomehci.repository.getStateReturn.GetStateTimer;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class ApiConnection {

    private static final String apiUrl = "http://192.168.1.137:8080/api/";
    //private static final String apiUrl = "http://192.168.1.17:8080/api/";


    private static ApiConnection instance;
    private static RequestQueue requestQueue;

    private ApiConnection(Context context) {
        this.requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
    }

    public static synchronized ApiConnection getInstance(Context context) {
        if (instance == null) {
            instance = new ApiConnection(context);
        }
        return instance;
    }

    public static String getApiUrl() {
        return apiUrl;
    }

    public String getDevices(Response.Listener<List<Device>> listener, Response.ErrorListener errorListener){
        String url = apiUrl+"devices";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Object, List<Device>> request =
                new GsonRequest<Object, List<Device>>(Request.Method.GET, url, null, "devices", new TypeToken<List<Device>>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String getDevice(String id, Response.Listener<Device> listener, Response.ErrorListener errorListener){
        String url = apiUrl+"devices/"+id;
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Object, Device> request =
                new GsonRequest<>(Request.Method.GET, url, null, "device", new TypeToken<Device>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String getRooms(Response.Listener<List<Room>> listener, Response.ErrorListener errorListener) {
        String url = apiUrl + "rooms/";
        GsonRequest<Object, List<Room>> request =
                new GsonRequest<Object, List<Room>>(Request.Method.GET, url, null, "rooms", new TypeToken<List<Room>>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String getRoutines(Response.Listener<List<Routine>> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "routines/";
        GsonRequest<Object, List<Routine>> request =
                new GsonRequest<>(Request.Method.GET, url, null, "routines", new TypeToken<List<Routine>>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String getRoomDevices(Room room, Response.Listener<List<Device>> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "rooms/" + room.getId() + "/devices/";
        GsonRequest<Object, List<Device>> request =
                new GsonRequest<Object, List<Device>>(Request.Method.GET, url, null, "devices", new TypeToken<List<Device>>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);
        return uuid;
    }

    public String createDevice(Device device, Response.Listener<Device> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "devices/";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Device, Device> request =
                new GsonRequest<Device, Device>(Request.Method.POST, url, device, "device", new TypeToken<Device>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String deleteDevice(Device device, Response.Listener<Boolean> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "devices/" + device.getId();
        GsonRequest<Object, Boolean> request =
                new GsonRequest<Object, Boolean>(Request.Method.DELETE, url, null, "result", new TypeToken<Boolean>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String updateDevice(Device device, Response.Listener<Boolean> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "devices/" + device.getId();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Device, Boolean> request =
                new GsonRequest<Device, Boolean>(Request.Method.PUT, url, device, "result", new TypeToken<Boolean>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }


    public String runAction(Action action, Response.Listener<Object> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "devices/" + action.getDeviceId() +"/" + action.getActionName();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<List<String>, Object> request =
                new GsonRequest<>(Request.Method.PUT, url, action.getParams(), null, new TypeToken<Object>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String getStateAc(Device device, Response.Listener<GetStateAc> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "devices/" + device.getId() +"/" + "getState";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Object, GetStateAc> request =
                new GsonRequest<>(Request.Method.PUT, url, null , "result", new TypeToken<GetStateAc>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String getStateAlarm(Device device, Response.Listener<GetStateAlarm> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "devices/" + device.getId() +"/" + "getState";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Object, GetStateAlarm> request =
                new GsonRequest<>(Request.Method.PUT, url, null , "result", new TypeToken<GetStateAlarm>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String getStateBlinds(Device device, Response.Listener<GetStateBlinds> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "devices/" + device.getId() +"/" + "getState";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Object, GetStateBlinds> request =
                new GsonRequest<>(Request.Method.PUT, url, null , "result", new TypeToken<GetStateBlinds>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String getStateDoor(Device device, Response.Listener<GetStateDoor> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "devices/" + device.getId() +"/" + "getState";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Object, GetStateDoor> request =
                new GsonRequest<>(Request.Method.PUT, url, null , "result", new TypeToken<GetStateDoor>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }


    public String getStateLamp(Device device, Response.Listener<GetStateLamp> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "devices/" + device.getId() +"/" + "getState";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Object, GetStateLamp> request =
                new GsonRequest<>(Request.Method.PUT, url, null , "result", new TypeToken<GetStateLamp>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String getStateOven(Device device, Response.Listener<GetStateOven> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "devices/" + device.getId() +"/" + "getState";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Object, GetStateOven> request =
                new GsonRequest<>(Request.Method.PUT, url, null , "result", new TypeToken<GetStateOven>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String getStateRefrigerator(Device device, Response.Listener<GetStateRefrigerator> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "devices/" + device.getId() +"/" + "getState";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Object, GetStateRefrigerator> request =
                new GsonRequest<>(Request.Method.PUT, url, null , "result", new TypeToken<GetStateRefrigerator>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String getStateTimer(Device device, Response.Listener<GetStateTimer> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "devices/" + device.getId() +"/" + "getState";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Object, GetStateTimer> request =
                new GsonRequest<>(Request.Method.PUT, url, null , "result", new TypeToken<GetStateTimer>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }



    public String createRoom(Room room, Response.Listener<Room> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "rooms";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Room, Room> request =
                new GsonRequest<Room, Room>(Request.Method.POST, url, room, "room", new TypeToken<Room>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String updateRoom(Room room, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        String url = apiUrl + "rooms/" + room.getId();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Room, Boolean> request =
                new GsonRequest<Room, Boolean>(Request.Method.PUT, url, room, "result", new TypeToken<Boolean>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String deleteRoom(Room room, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
        String url = apiUrl + "rooms/" + room.getId();
        GsonRequest<Object, Boolean> request =
                new GsonRequest<Object, Boolean>(Request.Method.DELETE, url, null, "result", new TypeToken<Boolean>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String createRoutine(Routine routine,  Response.Listener<Routine> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "routines";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Routine, Routine> request =
                new GsonRequest<Routine, Routine>(Request.Method.POST, url, routine, "routine", new TypeToken<Routine>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String updateRoutine(Routine routine, Response.Listener<Boolean> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "routines/" + routine.getId();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Routine, Boolean> request =
                new GsonRequest<Routine, Boolean>(Request.Method.PUT, url, routine, "result", new TypeToken<Boolean>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String deleteRoutine(Routine routine, Response.Listener<Boolean> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "routines/" + routine.getId();
        GsonRequest<Object, Boolean> request =
                new GsonRequest<Object, Boolean>(Request.Method.DELETE, url, null, "result", new TypeToken<Boolean>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String getDeviceEvents(Device device, Response.Listener<String> listener, Response.ErrorListener errorListener){
        String url = apiUrl+"devices/"+device.getId()+"/events";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Object, String> request =
                new GsonRequest<>(Request.Method.GET, url, null, "String", new TypeToken<String>(){}, null, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String assignDeviceToRoom(Device device, Room room, Response.Listener<Boolean> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "devices/"+ device.getId() +"/rooms/"+ room.getId();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Object, Boolean> request =
                new GsonRequest<>(Request.Method.POST, url, null, "result", new TypeToken<Boolean>(){}, headers, listener, errorListener);
        String uuid = UUID.randomUUID().toString();
        request.setTag(uuid);
        requestQueue.add(request);

        return uuid;
    }

    public String executeRoutine(Routine routine, Response.Listener<Object> listener, Response.ErrorListener errorListener){
        String url = apiUrl + "routines/" + routine.getId() + "/execute";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        GsonRequest<Object, Object> request =
                new GsonRequest<>(Request.Method.PUT, url, null, null, new TypeToken<Object>(){}, headers, listener, errorListener);
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
