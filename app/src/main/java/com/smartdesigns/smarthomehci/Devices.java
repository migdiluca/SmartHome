package com.smartdesigns.smarthomehci;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;


import com.smartdesigns.smarthomehci.backend.Device;
import com.smartdesigns.smarthomehci.backend.Room;
import com.smartdesigns.smarthomehci.backend.Routine;
import com.smartdesigns.smarthomehci.backend.TypeId;

public class Devices extends AppCompatActivity {

    private Room room = null;
    private Routine routine = null;
    private Device device = null;

    /**
     * Buttons
     */

    Button onOff = (Button) findViewById(R.id.OnOff);

    RadioButton up = (RadioButton) findViewById(R.id.UpBut);
    RadioButton down = (RadioButton) findViewById(R.id.DownBut);


    Button lampColorPicker = (Button) findViewById(R.id.colorPickerView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent i = getIntent();

        if (i.getExtras() != null) {

            device = (Device) i.getSerializableExtra("Object");
            setTitle(device.getName());


            if(Home.getInstance().getCurrentMode() == 0){
                room =  (Room) i.getSerializableExtra("Object");
            }
            else if(Home.getInstance().getCurrentMode() == 1) {
                routine =  (Routine) i.getSerializableExtra("Object");
            }

            if(device.getTypeId().equals(TypeId.Ac.getTypeId())) {

                setTheme(R.style.acStyle);
                this.setContentView(R.layout.ac);

            }else if(device.getTypeId().equals(TypeId.Blind.getTypeId())) {

                setTheme(R.style.blindsStyle);
                this.setContentView(R.layout.blinds);

                up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //ApiConnection instance = new ApiConnection(this);
                        //Action action = new Action();
                        //instance.runAction();
                    }
                });


            }else if(device.getTypeId().equals(TypeId.Door.getTypeId())) {
                setTheme(R.style.doorStyle);
                this.setContentView(R.layout.door);

            }else if(device.getTypeId().equals(TypeId.Lamp.getTypeId())) {

                setTheme(R.style.lampStyle);
                this.setContentView(R.layout.lamp);

                lampColorPicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openColorPicker();
                    }
                });

            }else if(device.getTypeId().equals(TypeId.Oven.getTypeId())) {

                setTheme(R.style.ovenStyle);
                this.setContentView(R.layout.oven);

            }else if(device.getTypeId().equals(TypeId.Refrigerator.getTypeId())) {

                setTheme(R.style.refrigeratorStyle);
                this.setContentView(R.layout.refrigerator);

            }else if(device.getTypeId().equals(TypeId.Alarm.getTypeId())) {

                setTheme(R.style.alarmStyle);
                this.setContentView(R.layout.alarm);

            }else if(device.getTypeId().equals(TypeId.Timer.getTypeId())) {
                setTheme(R.style.timerStyle);
                this.setContentView(R.layout.timer);

            }else {

                Toast.makeText(getApplicationContext(), "Not valid deviceType", Toast.LENGTH_LONG).show();

            }



        }
    }

    private void openColorPicker() {
        //final ColorPicker colorPicker = new ColorPicker(this);

    }

    public void showDialogueConvectionMode(View view){
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle(R.string.ConvectionMode);

            //list of items
            String[] items = getResources().getStringArray(R.array.ConvectionMode);
            builder.setSingleChoiceItems(items, 0,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // item selected logic
                            dialog.dismiss();
                        }
                    });


            AlertDialog dialog = builder.create();
            // display dialog
            dialog.show();
    }

    public void showDialogueHeatMode(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.ConvectionMode);

        //list of items
        String[] items = getResources().getStringArray(R.array.HeatMode);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        dialog.dismiss();
                    }
                });


        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogueGrillMode(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.ConvectionMode);

        //list of items
        String[] items = getResources().getStringArray(R.array.GrillMode);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        dialog.dismiss();
                    }
                });


        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }




}
