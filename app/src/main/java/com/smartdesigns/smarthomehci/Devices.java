package com.smartdesigns.smarthomehci;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class Devices extends Fragment {

    private Device device = null;
    private View view;
    private Context context;

    private ApiConnection api = ApiConnection.getInstance(getActivity());

    /**
     * Buttons
     * <p>
     * Ac
     */
    Switch onOffAc = (Switch) view.findViewById(R.id.OnOffAc);
    SeekBar temperatureAc = (SeekBar) view.findViewById(R.id.AcTempSeekBar);
    TextView vSwing = (TextView) view.findViewById(R.id.VSwing);
    TextView hSwing = (TextView) view.findViewById(R.id.HSwing);
    TextView fanSpeed = (TextView) view.findViewById(R.id.FanSpeed);
    TextView acMode = (TextView) view.findViewById(R.id.AcMode);

    /**
     * Blinds
     */
    RadioButton up = (RadioButton) view.findViewById(R.id.UpBut);
    RadioButton down = (RadioButton) view.findViewById(R.id.DownBut);

    /**
     * Oven
     */
    Switch onOffOven = (Switch) view.findViewById(R.id.OnOffOven);
    SeekBar temperatureOven = (SeekBar) view.findViewById(R.id.OvenTempSeekBar);
    TextView grillMode = (TextView) view.findViewById(R.id.GrillMode);
    TextView heatMode = (TextView) view.findViewById(R.id.HeatMode);
    TextView convectionMode = (TextView) view.findViewById(R.id.ConvectionMode);

    /**
     * Door
     */
    CheckBox openBut = (CheckBox) view.findViewById(R.id.OpenButton);
    CheckBox lockBut = (CheckBox) view.findViewById(R.id.LockButton);

    /**
     * Lamp
     */
    Button onOffLights = (Button) view.findViewById(R.id.OnOff);
    Button lampColorPicker = (Button) view.findViewById(R.id.colorPickerView);

    /**
     * Refrigerator
     */
    SeekBar freezerTemperature = (SeekBar) view.findViewById(R.id.FreezerTempSeekBar);
    SeekBar fridgeTemperature = (SeekBar) view.findViewById(R.id.FridgeTempSeekBar);
    TextView fridgeMode = (TextView) view.findViewById(R.id.FridgeMode);

    /**
     * Timer
     */
    NumberPicker hour = (NumberPicker) view.findViewById(R.id.Hour);
    NumberPicker minute = (NumberPicker) view.findViewById(R.id.Minute);
    NumberPicker second = (NumberPicker) view.findViewById(R.id.Second);
    Button startButton = (Button) view.findViewById(R.id.StartTimer);
    Button stopButton = (Button) view.findViewById(R.id.StopTimer);
    Button setButton = (Button) view.findViewById(R.id.SetTimer);
    TextView timer = (TextView) view.findViewById(R.id.countdownText);


    View thumbView;

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

        setBackgroundColor(view);
        Home.getInstance().getSupportActionBar().setHomeButtonEnabled(true);

        //Empiezo con los devices

        if (device.getTypeId().equals(TypeId.Ac.getTypeId())) {

            getActivity().setTheme(R.style.acStyle);
            v = inflater.inflate(R.layout.ac, container, false);
            view = v;
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
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.SuccessMsgOnOff) + sPrint
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
                //t 18 38
                temperatureAc.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        // You can have your own calculation for progress

                        int aux = 18 + progress;

                        seekBar.setThumb(getThumb(aux));
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
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
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

            if (Home.getInstance().getCurrentMode() != 1) {

                up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Action action = new Action(device.getId(), "up", null);
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.SuccessBlindsDown)
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

                down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Action action = new Action(device.getId(), "down", null);
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
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
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
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
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
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

            lampColorPicker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openColorPicker();
                }
            });

        } else if (device.getTypeId().equals(TypeId.Oven.getTypeId())) {

            getActivity().setTheme(R.style.ovenStyle);
            v = inflater.inflate(R.layout.oven, container, false);
            view = v;

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
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
                                Toast toast = Toast.makeText(context, getResources().getString(R.string.SuccessMsgOnOff) + sPrint
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

                        seekBar.setThumb(getThumb(aux));
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
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
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

            if (Home.getInstance().getCurrentMode() != 1) {

                fridgeTemperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        // You can have your own calculation for progress

                        int aux = 2 + progress;

                        seekBar.setThumb(getThumb(aux));
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
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
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

                        seekBar.setThumb(getThumb(aux));
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
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
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
            hour.setMinValue(0);
            minute.setMinValue(0);
            second.setMinValue(0);
            hour.setMaxValue(99);
            minute.setMaxValue(60);
            second.setMaxValue(60);
            hour.setWrapSelectorWheel(true);
            minute.setWrapSelectorWheel(true);
            second.setWrapSelectorWheel(true);


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
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
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

                    }
                });

                setButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int time = hour.getValue()*3600 + minute.getValue()*60+second.getValue();
                        LinkedList<String> ll = new LinkedList<>();
                        ll.add(Integer.toString(time));
                        Action action = new Action(device.getId(), "setInterval", ll);
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
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
        CountDownTimer countDownTimer = new CountDownTimer(totalTime, 1000) {

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

    public void showDialogueConvectionMode(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.ConvectionMode);

        //list of items
        String[] items = getResources().getStringArray(R.array.ConvectionMode);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic

                        LinkedList<String> l = new LinkedList<>();
                        l.add(getResources().getStringArray(R.array.ConvectionMode)[which]);

                        Action action = new Action(device.getId(), "setConvection", l);
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
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
        builder.setTitle(R.string.ConvectionMode);

        //list of items
        String[] items = getResources().getStringArray(R.array.HeatMode);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic

                        LinkedList<String> l = new LinkedList<>();
                        l.add(getResources().getStringArray(R.array.ConvectionMode)[which]);

                        Action action = new Action(device.getId(), "setHeat", l);
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
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
        builder.setTitle(R.string.ConvectionMode);

        //list of items
        String[] items = getResources().getStringArray(R.array.GrillMode);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic

                        LinkedList<String> l = new LinkedList<>();
                        l.add(getResources().getStringArray(R.array.ConvectionMode)[which]);

                        Action action = new Action(device.getId(), "setGrill", l);
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
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
        builder.setTitle(R.string.ConvectionMode);

        //list of items
        String[] items = getResources().getStringArray(R.array.VSwing);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic

                        LinkedList<String> l = new LinkedList<>();
                        l.add(getResources().getStringArray(R.array.VSwing)[which]);

                        Action action = new Action(device.getId(), "setVerticalSwing", l);
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
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

    public void showDialogueHSwing(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.ConvectionMode);

        //list of items
        String[] items = getResources().getStringArray(R.array.HSwing);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic

                        LinkedList<String> l = new LinkedList<>();
                        l.add(getResources().getStringArray(R.array.HSwing)[which]);

                        Action action = new Action(device.getId(), "setHorizontalSwing", l);
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
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

    public void showDialogueFanSpeed(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.ConvectionMode);

        //list of items
        String[] items = getResources().getStringArray(R.array.FanSpeed);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic

                        LinkedList<String> l = new LinkedList<>();
                        l.add(getResources().getStringArray(R.array.FanSpeed)[which]);

                        Action action = new Action(device.getId(), "setFanSpeed", l);
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
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

    public void showDialogueAcMode(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(R.string.ConvectionMode);

        //list of items
        String[] items = getResources().getStringArray(R.array.AcMode);
        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // item selected logic

                        LinkedList<String> l = new LinkedList<>();
                        l.add(getResources().getStringArray(R.array.AcMode)[which]);

                        Action action = new Action(device.getId(), "setMode", l);
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
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

                        LinkedList<String> l = new LinkedList<>();
                        l.add(getResources().getStringArray(R.array.AcMode)[which]);

                        Action action = new Action(device.getId(), "setMode", l);
                        api.runAction(action, new Response.Listener<Boolean>() {
                            @Override
                            public void onResponse(Boolean response) {
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

    public Drawable getThumb(int progress) {
        ((TextView) view.findViewById(R.id.tvProgress)).setText(progress + "");

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }

    private void setBackgroundColor(View view) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean darkTheme = preferences.getBoolean("dark_theme_checkbox", false);
        if (darkTheme == true) {
            Home.getInstance().setTheme(AppCompatDelegate.MODE_NIGHT_YES);
            view.setBackgroundColor(getResources().getColor(R.color.black));
            Home.setNavColor(R.color.dark_grey);
            Home.getInstance().getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dark_grey)));
            Home.getInstance().getWindow().setStatusBarColor(getResources().getColor(R.color.dark_grey_navbar));
        } else if (getView() != null) {
            Home.getInstance().setTheme(AppCompatDelegate.MODE_NIGHT_NO);
            view.setBackgroundColor(getResources().getColor(R.color.white));
            Home.setNavColor(R.color.colorPrimary);
            Home.getInstance().getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
            Home.getInstance().getWindow().setStatusBarColor(getResources().getColor(R.color.dark_grey));
            Home.getInstance().getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
            Home.getInstance().getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }


}