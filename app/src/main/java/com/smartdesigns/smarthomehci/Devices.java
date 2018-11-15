package com.smartdesigns.smarthomehci;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;


import com.smartdesigns.smarthomehci.backend.Device;
import com.smartdesigns.smarthomehci.backend.Room;
import com.smartdesigns.smarthomehci.backend.Routine;
import com.smartdesigns.smarthomehci.backend.TypeId;
import com.smartdesigns.smarthomehci.repository.ApiConnection;

public class Devices extends AppCompatActivity {

    private Room room = null;
    private Routine routine = null;
    private Device device = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent i = getIntent();

        if (i.getExtras() != null) {

            device = (Device) i.getSerializableExtra("Object");
            setTitle(device.getName());


            if(Home.getInstance().getCurrentMode() == 0){
                room =  (Room) i.getSerializableExtra("Object");
                setTitle(room.getName());
            }
            else if(Home.getInstance().getCurrentMode() == 1) {
                routine =  (Routine) i.getSerializableExtra("Object");
                setTitle(routine.getName());
            }

            if(device.getTypeId().equals(TypeId.Ac.getTypeId())) {

                this.setContentView(R.layout.ac);

            }else if(device.getTypeId().equals(TypeId.Blind.getTypeId())) {

                this.setContentView(R.layout.blinds);

            }else if(device.getTypeId().equals(TypeId.Door.getTypeId())) {

                this.setContentView(R.layout.door);

            }else if(device.getTypeId().equals(TypeId.Lamp.getTypeId())) {

                this.setContentView(R.layout.lamp);

            }else if(device.getTypeId().equals(TypeId.Oven.getTypeId())) {

                this.setContentView(R.layout.oven);

            }else if(device.getTypeId().equals(TypeId.Refrigerator.getTypeId())) {

                this.setContentView(R.layout.refrigerator);

            }else if(device.getTypeId().equals(TypeId.Alarm.getTypeId())) {

                this.setContentView(R.layout.alarm);

            }else if(device.getTypeId().equals(TypeId.Timer.getTypeId())) {

                this.setContentView(R.layout.timer);

            }else {

                Toast.makeText(getApplicationContext(), "Not valid deviceType", Toast.LENGTH_LONG).show();

            }



        }
    }



}
