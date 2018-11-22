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
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.smartdesigns.smarthomehci.backend.Device;
import com.smartdesigns.smarthomehci.repository.ApiConnection;
import com.smartdesigns.smarthomehci.repository.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationBroadcastReceiver extends BroadcastReceiver {
    private List<String> deviceTypes;
    private Context context;
    private Map<String, Integer> existingIds;
    private int NOTIFICATION_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Im In", "Entered Broadcast Reciever");
        existingIds = new HashMap<>();
        this.context = context;
        getAllowedDevices();
        getEvents();
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

        if (ac) {
            auxList.add("li6cbv5sdlatti0j");
        }
        if (blind) {
            auxList.add("eu0v2xgprrhhg41g");
        }
        if (door) {
            auxList.add("lsf78ly0eqrjbz91");
        }
        if (lamp) {
            auxList.add("go46xmbqeomjrsjr");
        }
        if (oven) {
            auxList.add("im77xxyulpegfmv8");
        }
        if (refrigerator) {
            auxList.add("rnizejqr2di0okho");
        }
        if (alarm) {
            auxList.add("mxztsyjzsrq7iaqc");
        }
        if (timer) {
            auxList.add("ofglvd9gqX8yfl3l");
        }
        this.deviceTypes = auxList;
        Log.d("LOG","selecting desired devices");
    }

    private void getEvents() {
        ApiConnection api = ApiConnection.getInstance(context);
        Log.d("Talking to API", "hellp");
        api.getDevices(new Response.Listener<List<Device>>() {
            @Override
            public void onResponse(List<Device> response) {
                for (Device device : response) {
                    boolean aux = true;
                    for (int i = 0; i < deviceTypes.size() && aux; i++) {
                        if (deviceTypes.get(i).equals(device.getTypeId())) {
                            ApiConnection api = ApiConnection.getInstance(context);
                            class EventsResponse implements Response.Listener<String> {
                                private Device device;

                                public EventsResponse(Device device) {
                                    this.device = device;
                                }

                                @Override
                                public void onResponse(String response) {
                                    Log.d("NOTIF", response);
                                    sendNotification(response, device.getName(), device.getTypeId());
                                }
                            }
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, api.getApiUrl() + "devices/" + device.getId() + "/events",
                                    new EventsResponse(device), new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("NOTIFERROR2", error.toString());
                                }
                            });
                            VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("NOTIFERROR1", error.toString());
            }
        });
    }

    private void sendNotification(String response, String name, String id) {
        String[] events = response.split("event");

        if (events.length > 1) {
            String[] aux;
            String event = "";
            for (int i = 1; i < events.length; i++) {
                aux = events[i].split(",");
                aux[0] = aux[0].replace(":", "");
                aux[0] = aux[0].replace(" ", "");
                aux[0] = aux[0].replace("\"", "");
                event = event + aux[0] + " ";
            }

            if (Home.isActivityVisible() && !Home.changedHere) {
                if(Home.currentClass.equals(Home.class) || Home.currentClass.equals(SettingsActivity.class)) {
                    Toast.makeText(context, name + " has changed its state: " + event, Toast.LENGTH_LONG).show();
                }
            } else if (!Home.changedHere){
                Home.changedHere = false;
                Intent notificationIntent = new Intent(this.context, Home.class);


                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.context);
                stackBuilder.addParentStack(Home.class);
                stackBuilder.addNextIntent(notificationIntent);

                final PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                @SuppressLint("ResourceAsColor") Notification notification = new Notification.Builder(context)
                        .setContentTitle(context.getResources().getString(R.string.ChangeMessage))
                        .setContentText(name + " " + event)
                        .setSmallIcon(R.drawable.ic_smarthome).setColor(ContextCompat.getColor(context, R.color.blue))
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), getDrawable(id)))
                        .setSound(defaultSoundUri)
                        .setContentIntent(contentIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .build();

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                Integer currId = existingIds.get(name);
                if (currId == null) {
                    currId = NOTIFICATION_ID++;
                }
                notificationManager.notify(currId, notification);
            }
        }
    }

    private int getDrawable(String id) {
        if (id.equals("li6cbv5sdlatti0j")) {
            return R.drawable.ac;
        } else if (id.equals("eu0v2xgprrhhg41g")) {
            return R.drawable.blind;
        } else if (id.equals("lsf78ly0eqrjbz91")) {
            return R.drawable.door;
        } else if (id.equals("go46xmbqeomjrsjr")) {
            return R.drawable.lamp;
        } else if (id.equals("im77xxyulpegfmv8")) {
            return R.drawable.oven;
        } else if (id.equals("rnizejqr2di0okho")) {
            return R.drawable.refrigerator;
        } else if (id.equals("mxztsyjzsrq7iaqc")) {
            return R.drawable.alarm;
        } else if (id.equals("ofglvd9gqX8yfl3l")) {
            return R.drawable.timer;
        } else {
            return R.drawable.notfound;
        }
    }
}
