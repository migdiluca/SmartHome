package com.smartdesigns.smarthomehci;

import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.smartdesigns.smarthomehci.backend.Action;
import com.smartdesigns.smarthomehci.backend.Device;
import com.smartdesigns.smarthomehci.backend.TypeId;
import com.smartdesigns.smarthomehci.repository.ApiConnection;
import com.smartdesigns.smarthomehci.repository.getStateReturn.GetStateAc;
import com.smartdesigns.smarthomehci.repository.getStateReturn.GetStateBlinds;
import com.smartdesigns.smarthomehci.repository.getStateReturn.GetStateDoor;
import com.smartdesigns.smarthomehci.repository.getStateReturn.GetStateOven;
import com.smartdesigns.smarthomehci.repository.getStateReturn.GetStateRefrigerator;
import com.smartdesigns.smarthomehci.repository.getStateReturn.GetStateTimer;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class Devices extends Fragment {

    //TODO SEEKBAR arreglar, usar de ultima el import este que meti

    private Device device = null;
    private View view;
    private Context context;

    private ApiConnection api = ApiConnection.getInstance(getActivity());

    ImageView image;

    /**
     * Buttons
     *
     * Ac
     */
    Switch onOffAc;
    SeekBar temperatureAc;
    LinearLayout vSwing;
    LinearLayout hSwing;
    LinearLayout fanSpeed;
    LinearLayout acMode;
    TextView vSwingStat;
    TextView hSwingStat;
    TextView fanSpeedStat;
    TextView acModeStat;
    TextView acTempStats;


    /**
     * Blinds
     */
    RadioButton up;
    RadioButton down;

    /**
     * Oven
     */
    Switch onOffOven;
    SeekBar temperatureOven;
    LinearLayout grillMode;
    LinearLayout heatMode;
    LinearLayout convectionMode;
    TextView grillModeStats;
    TextView heatModeStats;
    TextView convectionModeStats;
    TextView ovenTempStats;

    /**
     * Door
     */
    CheckBox openBut;
    CheckBox lockBut;

    /**
     * Lamp
     */

    Switch onOffLamp;
    LinearLayout lampColor;
    SeekBar lampBrightness;
    TextView lampBrightnessStats;

    /**
     * Refrigerator
     */
    SeekBar freezerTemperature;
    SeekBar fridgeTemperature;
    LinearLayout fridgeMode;
    TextView fridgeModeStats;
    TextView freezerTempStats;
    TextView fridgeTempStats;


    /**
     * Timer
     */
    NumberPicker hour;
    NumberPicker minute;
    NumberPicker second;
    Button startButton;
    Button stopButton;
    Button setButton;
    TextView timer;
    CountDownTimer countDownTimer;


    View thumbView;
    Boolean ft = false;

    public Devices() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //getView().getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        device = (Device) getArguments().getSerializable("Device");
        getActivity().setTitle(device.getName());

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = null;
        thumbView = inflater.inflate(R.layout.layout_seekbar_thumb, null, false);
        Home.getInstance().getSupportActionBar().setHomeButtonEnabled(true);

        //Empiezo con los devices

        if (device.getTypeId().equals(TypeId.Ac.getTypeId())) {

            getActivity().setTheme(R.style.acStyle);

            v = inflater.inflate(R.layout.ac, container, false);
            view = v;

            onOffAc = view.findViewById(R.id.OnOffAc);
            temperatureAc = view.findViewById(R.id.AcTempSeekBar);
            vSwing = view.findViewById(R.id.VSwing);
            hSwing = view.findViewById(R.id.HSwing);
            fanSpeed = view.findViewById(R.id.FanSpeed);
            acMode = view.findViewById(R.id.AcMode);
            vSwingStat = view.findViewById(R.id.VSwingStatus);
            hSwingStat = view.findViewById(R.id.HSwingStatus);
            fanSpeedStat = view.findViewById(R.id.FanSpeedStatus);
            acModeStat = view.findViewById(R.id.AcModeStatus);
            acTempStats = view.findViewById(R.id.AcTempStat);

            api.getStateAc(device, new Response.Listener<GetStateAc>() {
                @Override
                public void onResponse(GetStateAc response) {
                    if(response.getStatus().equals("on"))
                        onOffAc.setChecked(true);
                    else
                        onOffAc.setChecked(false);
                    vSwingStat.setText(response.getVerticalSwing());
                    hSwingStat.setText(response.getHorizontalSwing());
                    fanSpeedStat.setText(response.getFanSpeed());
                    acTempStats.setText(Integer.toString(response.getTemperature()) + " C");

                    switch (response.getMode()) {
                        case "cool":
                            acModeStat.setText(R.string.Cool);
                            break;
                        case "heat":
                            acModeStat.setText(R.string.Heat);
                            break;
                        default:
                            acModeStat.setText(R.string.Fan);
                            break;
                    }
                    temperatureAc.setProgress(response.getTemperature() - 18);

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            if (Home.getInstance().getCurrentMode() != 1) {

                onOffAc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String s;
                        final String sPrint;
                        if (isChecked) {
                            s = "turnOn";
                            sPrint = getResources().getString(R.string.OnM);
                        } else {
                            s = "turnOff";
                            sPrint = getResources().getString(R.string.OffM);
                        }
                        Action action = new Action(device.getId(), s, null);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                if(ft) {
                                    Toast toast = Toast.makeText(context, getResources().getString(R.string.SuccessMsgOnOff) + " " + sPrint
                                            , Toast.LENGTH_LONG);
                                    toast.show();
                                }
                                ft = true;
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("ERROR", error.toString());
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
                    }
                });
                //t 18 38
                temperatureAc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // You can have your own calculation for progress

                        int aux = 18 + progress;
                        acTempStats.setText(Integer.toString(aux) + " C");

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        int value = seekBar.getProgress() + 18;
                        LinkedList<String> l = new LinkedList<>();
                        l.add(Integer.toString(value));
                        Action action = new Action(device.getId(), "setTemperature", l);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.SuccessMsgTemp)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
                    }
                });

                vSwing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogueVSwing(v);
                    }
                });

                hSwing.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogueHSwing(v);
                    }
                });

                fanSpeed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogueFanSpeed(v);
                    }
                });

                acMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogueAcMode(v);
                    }
                });

            } else {
                onOffAc.setEnabled(false);
                temperatureAc.setEnabled(false);
                vSwing.setClickable(false);
                hSwing.setClickable(false);
                fanSpeed.setClickable(false);
                acMode.setClickable(false);
            }

        } else if (device.getTypeId().equals(TypeId.Blind.getTypeId())) {

            getActivity().setTheme(R.style.blindsStyle);

            v = inflater.inflate(R.layout.blinds, container, false);
            view = v;

            up = view.findViewById(R.id.UpBut);
            down = view.findViewById(R.id.DownBut);

            api.getStateBlinds(device, new Response.Listener<GetStateBlinds>() {
                @Override
                public void onResponse(GetStateBlinds response) {
                    if(response.getStatus().equals("open") || response.getStatus().equals("opening")) {
                        up.toggle();
                    }
                    else {
                        down.toggle();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            if (Home.getInstance().getCurrentMode() != 1) {

                up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Action action = new Action(device.getId(), "up", null);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.SuccessBlindsUp)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("ERROR", error.toString());
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
                    }
                });

                down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Action action = new Action(device.getId(), "down", null);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.SuccessBlindsDown)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                    }
                });

            } else {
                up.setEnabled(false);
                down.setEnabled(false);
            }


        } else if (device.getTypeId().equals(TypeId.Door.getTypeId())) {

            getActivity().setTheme(R.style.doorStyle);
            v = inflater.inflate(R.layout.door, container, false);
            view = v;

            openBut = view.findViewById(R.id.OpenButton);
            lockBut = view.findViewById(R.id.LockButton);

            api.getStateDoor(device, new Response.Listener<GetStateDoor>() {
                @Override
                public void onResponse(GetStateDoor response) {
                    if(response.getStatus().equals("closed")) {
                        openBut.setChecked(false);
                    }
                    else {
                        openBut.setChecked(true);
                    }
                    if(response.getStatus().equals("locked")) {
                        lockBut.setChecked(true);
                    }
                    else {
                        lockBut.setChecked(false);
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            if (Home.getInstance().getCurrentMode() != 1) {

                openBut.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String s;
                        final String f;
                        if (openBut.isChecked()) {
                            s = "open";
                            f = getResources().getString(R.string.OpenMsg);
                        } else {
                            s = "close";
                            f = getResources().getString(R.string.CloseMsg);
                        }
                        Action action = new Action(device.getId(), s, null);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.SuccessMsgDoor) + f
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
                    }
                });

                lockBut.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        String s;
                        final String f;
                        if (lockBut.isChecked()) {
                            s = "lock";
                            f = getResources().getString(R.string.LockMsg);
                        } else {
                            s = "unlock";
                            f = getResources().getString(R.string.UnlockMsg);
                        }
                        Action action = new Action(device.getId(), s, null);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.SuccessMsgDoor) + f
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
                    }
                });

            } else {
                lockBut.setEnabled(false);
                openBut.setEnabled(false);
            }

        } else if (device.getTypeId().equals(TypeId.Lamp.getTypeId())) {

            getActivity().setTheme(R.style.lampStyle);
            v = inflater.inflate(R.layout.lamp, container, false);
            view = v;


        } else if (device.getTypeId().equals(TypeId.Oven.getTypeId())) {

            getActivity().setTheme(R.style.ovenStyle);
            v = inflater.inflate(R.layout.oven, container, false);
            view = v;

            onOffOven = view.findViewById(R.id.OnOffOven);
            temperatureOven = view.findViewById(R.id.OvenTempSeekBar);
            grillMode = view.findViewById(R.id.GrillMode);
            heatMode = view.findViewById(R.id.HeatMode);
            convectionMode = view.findViewById(R.id.ConvectionMode);
            grillModeStats = view.findViewById(R.id.GrillModeStats);
            heatModeStats = view.findViewById(R.id.HeatModeStats);
            convectionModeStats = view.findViewById(R.id.ConvectionModeStats);
            ovenTempStats = view.findViewById(R.id.OvenTempStat);

            api.getStateOven(device, new Response.Listener<GetStateOven>() {
                @Override
                public void onResponse(GetStateOven response) {
                    if(response.getStatus().equals("on"))
                        onOffOven.setChecked(true);
                    else
                        onOffOven.setChecked(false);

                    ovenTempStats.setText(Integer.toString(response.getTemperature()) + " C");

                    temperatureOven.setProgress(response.getTemperature() - 90);

                    switch (response.getGrill()) {
                        case "large":
                            grillModeStats.setText(R.string.Large);
                            break;
                        case "eco":
                            grillModeStats.setText(R.string.Eco);
                            break;
                        default:
                            grillModeStats.setText(R.string.Off);
                            break;
                    }

                    switch (response.getConvection()) {
                        case "normal":
                            convectionModeStats.setText(R.string.Large);
                            break;
                        case "Normal":
                            convectionModeStats.setText(R.string.Eco);
                            break;
                        default:
                            convectionModeStats.setText(R.string.Off);
                            break;
                    }

                    switch (response.getHeat()) {
                        case "conventional":
                            heatModeStats.setText(R.string.Conventional);
                            break;
                        case "bottom":
                            heatModeStats.setText(R.string.Bottom);
                            break;
                        default:
                            heatModeStats.setText(R.string.Top);
                            break;
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            if (Home.getInstance().getCurrentMode() != 1) {

                onOffOven.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String s;
                        final String sPrint;
                        if (isChecked) {
                            s = "turnOn";
                            sPrint = getResources().getString(R.string.OnM);
                        } else {
                            s = "turnOff";
                            sPrint = getResources().getString(R.string.OffM);
                        }
                        Action action = new Action(device.getId(), s, null);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.SuccessMsgOnOff) +" "+ sPrint
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
                    }
                });

                temperatureOven.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        // You can have your own calculation for progress

                        int aux = 90 + progress;

                        ovenTempStats.setText(Integer.toString(aux) + " C");

                        //seekBar.setThumb(getThumb(aux));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        int value = seekBar.getProgress() + 90;
                        LinkedList<String> l = new LinkedList<>();
                        l.add(Integer.toString(value));
                        Action action = new Action(device.getId(), "setTemperature", l);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.SuccessMsgTemp)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
                    }
                });

                grillMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogueGrillMode(v);
                    }
                });
                convectionMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogueConvectionMode(v);
                    }
                });

                heatMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogueHeatMode(v);
                    }
                });


            } else {
                onOffOven.setEnabled(false);
                temperatureOven.setEnabled(false);
                grillMode.setClickable(false);
                heatMode.setClickable(false);
                convectionMode.setClickable(false);
            }

        } else if (device.getTypeId().equals(TypeId.Refrigerator.getTypeId())) {

            getActivity().setTheme(R.style.refrigeratorStyle);
            v = inflater.inflate(R.layout.refrigerator, container, false);
            view = v;

            freezerTemperature = view.findViewById(R.id.FreezerTempSeekBar);
            fridgeTemperature = view.findViewById(R.id.FridgeTempSeekBar);
            fridgeMode = view.findViewById(R.id.FridgeMode);
            fridgeModeStats = view.findViewById(R.id.FridgeModeStatus);
            fridgeTempStats = view.findViewById(R.id.FridgeTempStat);
            freezerTempStats = view.findViewById(R.id.FreezerTempStat);


            api.getStateRefrigerator(device, new Response.Listener<GetStateRefrigerator>() {
                @Override
                public void onResponse(GetStateRefrigerator response) {
                    
                    fridgeTempStats.setText(Integer.toString(response.getTemperature()) + " C");
                    freezerTempStats.setText(Integer.toString(response.getTemperature()) + " C");


                    fridgeTemperature.setProgress(response.getTemperature() - 2);
                    freezerTemperature.setProgress(response.getFreezerTemperature() + 20);



                    switch (response.getMode()) {
                        case "default":
                            fridgeModeStats.setText(R.string.Default);
                            break;
                        case "vacation":
                            fridgeModeStats.setText(R.string.Vacations);
                            break;
                        default:
                            fridgeModeStats.setText(R.string.Party);
                            break;
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            if (Home.getInstance().getCurrentMode() != 1) {

                fridgeTemperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        // You can have your own calculation for progress

                        int aux = 2 + progress;
                        fridgeTempStats.setText(Integer.toString(aux) + " C");
                        //seekBar.setThumb(getThumb(aux));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        int value = seekBar.getProgress() + 2;
                        LinkedList<String> l = new LinkedList<>();
                        l.add(Integer.toString(value));
                        Action action = new Action(device.getId(), "setTemperature", l);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.SuccessMsgTemp)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
                    }
                });

                freezerTemperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        // You can have your own calculation for progress

                        int aux = -20 + progress;

                        freezerTempStats.setText(Integer.toString(aux) + " C");

                        //seekBar.setThumb(getThumb(aux));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        int value = seekBar.getProgress() - 20;
                        LinkedList<String> l = new LinkedList<>();
                        l.add(Integer.toString(value));
                        Action action = new Action(device.getId(), "setFreezerTemperature", l);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.SuccessMsgTemp)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
                    }
                });

                fridgeMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialogueFridgeMode(v);
                    }
                });

            } else {
                fridgeTemperature.setEnabled(false);
                freezerTemperature.setEnabled(false);
                fridgeMode.setClickable(false);
            }

        } else if (device.getTypeId().equals(TypeId.Alarm.getTypeId())) {

            getActivity().setTheme(R.style.alarmStyle);
            v = inflater.inflate(R.layout.alarm, container, false);
            view = v;

        } else if (device.getTypeId().equals(TypeId.Timer.getTypeId())) {
            getActivity().setTheme(R.style.timerStyle);
            v = inflater.inflate(R.layout.timer, container, false);
            view = v;

            hour = view.findViewById(R.id.Hour);
            minute = view.findViewById(R.id.Minute);
            second = view.findViewById(R.id.Second);
            startButton = view.findViewById(R.id.StartTimer);
            stopButton = view.findViewById(R.id.StopTimer);
            setButton = view.findViewById(R.id.SetTimer);
            timer = view.findViewById(R.id.countdownText);


            hour.setMinValue(0);
            minute.setMinValue(0);
            second.setMinValue(0);
            hour.setMaxValue(99);
            minute.setMaxValue(60);
            second.setMaxValue(60);
            hour.setWrapSelectorWheel(true);
            minute.setWrapSelectorWheel(true);
            second.setWrapSelectorWheel(true);

            api.getStateTimer(device, new Response.Listener<GetStateTimer>() {
                @Override
                public void onResponse(GetStateTimer response) {

                    int value = response.getInterval();
                    hour.setValue(value/3600);
                    minute.setValue((value%3600)/60);
                    second.setValue(((value%3600)%60)/60);

                    String hms = String.format("%02d:%02d:%02d", hour.getValue(), minute.getValue(), second.getValue());
                    timer.setText(hms);//set text


                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            if (Home.getInstance().getCurrentMode() != 1) {

                hour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //Display the newly selected number from picker
                    }
                });
                minute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //Display the newly selected number from picker
                    }
                });
                second.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //Display the newly selected number from picker
                    }
                });

                startButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Action action = new Action(device.getId(), "start", null);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                startTimer(hour.getValue(), minute.getValue(), second.getValue());
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });

                    }
                });

                //TODO stop!!
                stopButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Action action = new Action(device.getId(), "stop", null);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                countDownTimer.cancel();
                                String hms = String.format("%02d:%02d:%02d", hour.getValue(), minute.getValue(), second.getValue());
                                timer.setText(hms);//set text
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
                    }
                });

                setButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int time = hour.getValue()*3600 + minute.getValue()*60+second.getValue();
                        LinkedList<String> ll = new LinkedList<>();
                        ll.add(Integer.toString(time));
                        Action action = new Action(device.getId(), "setInterval", ll);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.SetDone)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
                    }
                });

            } else {
                hour.setEnabled(false);
                minute.setEnabled(false);
                second.setEnabled(false);
                startButton.setClickable(false);
                stopButton.setClickable(false);
            }

        } else {
            Toast.makeText(context, "Not valid deviceType", Toast.LENGTH_LONG).show();
        }
        if (view != null) {
            image = view.findViewById(R.id.DeviceLogo);
            image.setImageResource(context.getResources().getIdentifier(device.getImg().split(".png$")[0], "drawable", context.getPackageName()));
        }
        return v;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void openColorPicker() {
        //final ColorPicker colorPicker = new ColorPicker(this);

    }

    private void startTimer(int h, int m, int s) {
        int totalTime = 1000*(h*3600 + m*60+s);
        countDownTimer = new CountDownTimer(totalTime, 1000) {

            public void onTick(long millisUntilFinished) {
                //Convert milliseconds into hour,minute and seconds
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished), TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)), TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                timer.setText(hms);//set text
            }

            public void onFinish() {
                Toast.makeText(context, getResources().getString(R.string.TimerFinished), Toast.LENGTH_LONG).show();
            }
        }.start();
    }

    public int numberDialogue;

    public void showDialogueConvectionMode(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.ConvectionMode);

        //list of items
        String[] items = getResources().getStringArray(R.array.ConvectionMode2);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        numberDialogue = which;
                        LinkedList<String> l = new LinkedList<>();
                        l.add(getResources().getStringArray(R.array.ConvectionMode2)[which]);

                        Action action = new Action(device.getId(), "setConvection", l);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                switch (getResources().getStringArray(R.array.ConvectionMode)[numberDialogue])
                                {
                                    case "normal":
                                        convectionModeStats.setText(R.string.Normal);
                                        break;
                                    case "eco":
                                        convectionModeStats.setText(R.string.Eco);
                                        break;
                                    case "off":
                                        convectionModeStats.setText(R.string.Off);
                                        break;
                                }
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.SuccessCMode)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
                        dialog.dismiss();
                    }
                });


        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogueHeatMode(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.HeatMode);

        //list of items
        String[] items = getResources().getStringArray(R.array.HeatMode);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        numberDialogue = which;
                        LinkedList<String> l = new LinkedList<>();
                        l.add(getResources().getStringArray(R.array.HeatMode2)[which]);

                        Action action = new Action(device.getId(), "setHeat", l);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                switch (getResources().getStringArray(R.array.HeatMode2)[numberDialogue])
                                {
                                    case "conventional":
                                        heatModeStats.setText(R.string.Conventional);
                                        break;
                                    case "bottom":
                                        heatModeStats.setText(R.string.Bottom);
                                        break;
                                    case "top":
                                        heatModeStats.setText(R.string.Top);
                                        break;
                                }
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.SuccessHMode)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });

                        dialog.dismiss();
                    }
                });


        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogueGrillMode(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.GrillMode);

        //list of items
        String[] items = getResources().getStringArray(R.array.GrillMode);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        numberDialogue = which;
                        LinkedList<String> l = new LinkedList<>();
                        l.add(getResources().getStringArray(R.array.GrillMode2)[which]);

                        Action action = new Action(device.getId(), "setGrill", l);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {

                                switch (getResources().getStringArray(R.array.GrillMode2)[numberDialogue])
                                {
                                    case "large":
                                        grillModeStats.setText(R.string.Large);
                                        break;
                                    case "eco":
                                        grillModeStats.setText(R.string.Eco);
                                        break;
                                    case "off":
                                        grillModeStats.setText(R.string.Off);
                                        break;
                                }

                                Toast toast = Toast.makeText(context, getResources().getString(R.string.SuccessGMode)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });

                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogueVSwing(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.VerticalSwing);

        //list of items
        String[] items = getResources().getStringArray(R.array.VSwing);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        numberDialogue = which;
                        LinkedList<String> l = new LinkedList<>();
                        l.add(getResources().getStringArray(R.array.VSwing)[which]);

                        Action action = new Action(device.getId(), "setVerticalSwing", l);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                vSwingStat.setText(getResources().getStringArray(R.array.VSwing)[numberDialogue]);
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.VSwingSuccess)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("ERROR", error.toString());
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });

                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogueHSwing(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.HSwingSuccess);

        //list of items
        String[] items = getResources().getStringArray(R.array.HSwing);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        numberDialogue = which;
                        LinkedList<String> l = new LinkedList<>();
                        l.add(getResources().getStringArray(R.array.HSwing)[which]);

                        Action action = new Action(device.getId(), "setHorizontalSwing", l);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                hSwingStat.setText(getResources().getStringArray(R.array.HSwing)[numberDialogue]);
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.HSwingSuccess)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });

                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogueFanSpeed(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.FanSpeed);

        //list of items
        String[] items = getResources().getStringArray(R.array.FanSpeed);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        numberDialogue = which;
                        LinkedList<String> l = new LinkedList<>();
                        l.add(getResources().getStringArray(R.array.FanSpeed)[which]);

                        Action action = new Action(device.getId(), "setFanSpeed", l);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                fanSpeedStat.setText(getResources().getStringArray(R.array.FanSpeed)[numberDialogue]);
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.FanSpeedSuccess)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });

                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogueAcMode(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.AcMode);

        //list of items
        String[] items = getResources().getStringArray(R.array.AcMode);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        numberDialogue = which;

                        LinkedList<String> l = new LinkedList<>();
                        l.add(getResources().getStringArray(R.array.AcMode2)[which]);

                        Action action = new Action(device.getId(), "setMode", l);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {

                                switch (getResources().getStringArray(R.array.AcMode2)[numberDialogue])
                                {
                                    case "cool":
                                        acModeStat.setText(R.string.Cool);
                                        break;
                                    case "heat":
                                        acModeStat.setText(R.string.Heat);
                                        break;
                                    case "fan":
                                        acModeStat.setText(R.string.Fan);
                                        break;
                                }


                                Toast toast = Toast.makeText(context, getResources().getString(R.string.AcModeSuccess)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });

                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogueFridgeMode(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.AcMode);

        //list of items
        String[] items = getResources().getStringArray(R.array.FridgeMode);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic
                        numberDialogue = which;

                        LinkedList<String> l = new LinkedList<>();
                        l.add(getResources().getStringArray(R.array.FridgeMode2)[which]);

                        Action action = new Action(device.getId(), "setMode", l);
                        api.runAction(action, new Response.Listener<Object>() {
                            @Override
                            public void onResponse(Object response) {
                                switch (getResources().getStringArray(R.array.FridgeMode2)[numberDialogue])
                                {
                                    case "default":
                                        fridgeModeStats.setText(R.string.Default);
                                        break;
                                    case "vacation":
                                        fridgeModeStats.setText(R.string.Vacations);
                                        break;
                                    case "party":
                                        fridgeModeStats.setText(R.string.Party);
                                        break;
                                }
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.SuccessFMode)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.ActionFail)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });

                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    //public void showDialogueLampColor(View view) {}

    public Drawable getThumb(int progress) {
        String str = progress + "";
        ((TextView) view.findViewById(R.id.tvProgress)).setText(str);

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }

}