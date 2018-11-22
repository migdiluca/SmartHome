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
    private String api = "http://181.28.198.15:8080/api/";
    private Map<String, Integer> existingIds;
    private int NOTIFICATION_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Im In", "Entered Broadcast Reciever");
        existingIds = new HashMap<>();
        this.context= context;
        getAllowedDevices();
        for (int i = 0; i < deviceTypes.size(); i++) {
            getDevicesForType(deviceTypes.get(i));
        }

    }

    private void getAllowedDevices() {
        List<String> auxList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        boolean ac = sharedPreferences.getBoolean("ac_preference", false);
        boolean blind = sharedPreferences.getBoolean("blinds_preference", false);
        boolean door = sharedPreferences.getBoolean("door_preference", false);
        boolean lamp = sharedPreferences.getBoolean("lamp_preference", false);
        boolean oven = sharedPreferences.getBoolean("oven_preference", false);
        boolean refrigerator = sharedPreferences.getBoolean("refrigerator_preference", false);
        boolean alarm = sharedPreferences.getBoolean("alarm_preference", false);
        boolean timer = sharedPreferences.getBoolean("timer_preference", false);

        if(ac) {
            auxList.add("li6cbv5sdlatti0j");
        }
        if(blind){
            auxList.add("eu0v2xgprrhhg41g");
        }
        if(door){
            auxList.add("lsf78ly0eqrjbz91");
        }
        if(lamp){
            auxList.add("go46xmbqeomjrsjr");
        }
        if(oven){
            auxList.add("im77xxyulpegfmv8");
        }
        if(refrigerator){
            auxList.add("rnizejqr2di0okho");
        }
        if(alarm) {
            auxList.add("mxztsyjzsrq7iaqc");
        }
        if(timer){
            auxList.add("ofglvd9gqX8yfl3l");
        }
        this.deviceTypes = auxList;
    }

    private void getDevicesForType(String type) {
        String url = api +"devices/devicetypes/" + type;
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
            StringRequest request = new StringRequest(Request.Method.GET, api + "devices/" + id + "/events",  new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                        Log.d("Response", response);
                        sendNotification(response, name, id);
                    }
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
        String[] events = response.split("event");

        if(events.length > 1){
            String[] aux;
            String event = "";
            for (int i = 1; i < events.length; i++) {
                aux = events[i].split(",");
                aux[0] = aux[0].replace(":", "");
                aux[0] = aux[0].replace(" ", "");
                aux[0] = aux[0].replace("\"", "");
                event = event + aux[0] + " ";
            }

            Log.d("STATUS", event);
            Intent notificationIntent = new Intent(context, Devices.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
             stackBuilder.addParentStack(Devices.class);
            stackBuilder.addNextIntent(notificationIntent);

            final PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            @SuppressLint("ResourceAsColor") Notification notification = new Notification.Builder(context)
                    .setContentTitle("A device has been changed!")
                    .setContentText(name + " " + event )
                    .setSmallIcon(R.drawable.ic_smarthome).setColor(ContextCompat.getColor(context,R.color.blue))

                    .setSound(defaultSoundUri)
                    .setContentIntent(contentIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Integer currId = existingIds.get(name);
            if(currId == null){
                currId = NOTIFICATION_ID++;
            }
            notificationManager.notify(currId, notification);
        }
    }
}
