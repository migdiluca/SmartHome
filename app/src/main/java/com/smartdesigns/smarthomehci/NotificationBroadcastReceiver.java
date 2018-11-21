package com.smartdesigns.smarthomehci;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.smartdesigns.smarthomehci.repository.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private List<String> deviceTypes;
    private Context context;
    private Map<String,Integer> idMap;
    private String api = "http://192.168.1.17:8080/api/";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Im In", "Entered Broadcast Reciever");
        idMap = new HashMap<>();
        this.context= context;
        getAllowedDevices();
        for (int i = 0; i < deviceTypes.size(); i++) {
            getDevicesForType(deviceTypes.get(i));
        }

    }

    private void getAllowedDevices() {
        List<String> auxList = new ArrayList<>();
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);

        boolean ac = sharedPreferences.getBoolean("ac", true);
        boolean blind = sharedPreferences.getBoolean("blind", true);
        boolean door = sharedPreferences.getBoolean("door", true);
        boolean lamp = sharedPreferences.getBoolean("lamp", true);
        boolean oven = sharedPreferences.getBoolean("oven", true);
        boolean refrigerator = sharedPreferences.getBoolean("refrigerator", true);

        if(ac) {
            auxList.add("li6cbv5sdlatti0j");
            idMap.put("ac", 0);
        }
        if(blind){
            auxList.add("eu0v2xgprrhhg41g");
            idMap.put("blind",1);
        }
        if(door){
            auxList.add("lsf78ly0eqrjbz91");
            idMap.put("door",2);
        }
        if(lamp){
            auxList.add("go46xmbqeomjrsjr");
            idMap.put("lamp",3);
        }
        if(oven){
            auxList.add("im77xxyulpegfmv8");
            idMap.put("oven",4);
        }
        if(refrigerator){
            auxList.add("rnizejqr2di0okho");
            idMap.put("refrigerator",5);
        }
        this.deviceTypes = auxList;
    }

    private void getDevicesForType(String type) {
        String url = api +"devices/devicetypes/" + type;
        Log.d("GET DEVICES FROM", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    parseResponse(response);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSON ERROR ", String.valueOf(R.string.error_request));
                error.printStackTrace();
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private void parseResponse(JSONObject response) throws JSONException{
        JSONArray jsonArray = response.getJSONArray("devices");
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject device = jsonArray.getJSONObject(i);
            final String name = device.getString("name");
            final String id = device.getString("id");
            Log.d("Response ", name + " " + id);
            StringRequest request = new StringRequest(Request.Method.GET, api + "devices/" + id + "/events",  new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("OnResponse parser", "Parser says:" + response);
                    sendNotification(response, name, id);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("JSON ERROR", String.valueOf(R.string.error_request));
                    error.printStackTrace();
                }
            });
            VolleySingleton.getInstance(context).addToRequestQueue(request);
        }
    }

    private void sendNotification(String response, String name, String id) {
        Log.d("Send Notification", "Events response: " + response + " name: " + name+ " id: " + id);
        Log.d("events length", Integer.toString(response.length()));
        String[] events = response.split(",");

        if(events.length > 1){
            String[] event1 = events[2].split(":");
            String event = event1[2].replace("\"", "");
            String[] args1 = events[3].split(":");
            String arg = args1[3].replace("\"", "").replace("}", "").replace("data", "");
            String devName = name.substring(0, name.indexOf('_'));

            Intent notificationIntent = new Intent(context, Devices.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
             stackBuilder.addParentStack(Devices.class);
            stackBuilder.addNextIntent(notificationIntent);

            final PendingIntent contentIntent =
            stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            @SuppressLint("ResourceAsColor") Notification notification = new Notification.Builder(context)
                    .setContentTitle("A device has changed its state")
                    .setContentText(devName + " " + arg + " " + event)
                    .setSmallIcon(R.drawable.ic_smarthome).setColor(ContextCompat.getColor(context,R.color.blue))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(contentIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(idMap.get(name), notification);
        }
    }
}


//    int messageId = intent.getIntExtra("id",0);
//
//    Intent notificationIntent = new Intent(context, Devices.class);
//
//    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addParentStack(Devices.class);
//        stackBuilder.addNextIntent(notificationIntent);
//
//final PendingIntent contentIntent =
//        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//@SuppressLint("ResourceAsColor") Notification notification = new Notification.Builder(context)
//        .setContentTitle(intent.getStringExtra("title"))
//        .setContentText(intent.getStringExtra("subtitle"))
//        .setSmallIcon(R.drawable.ic_smarthome)
//        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), intent.getIntExtra("image", R.drawable.notfound)))
//        .setColor(ContextCompat.getColor(context,R.color.blue))
//        .setAutoCancel(true)
//        .setSound(defaultSoundUri)
//        .setContentIntent(contentIntent)
//        .build();
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(messageId, notification);