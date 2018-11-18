package com.smartdesigns.smarthomehci;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.smartdesigns.smarthomehci.backend.Action;
import com.smartdesigns.smarthomehci.backend.Device;
import com.smartdesigns.smarthomehci.backend.Room;
import com.smartdesigns.smarthomehci.backend.Routine;
import com.smartdesigns.smarthomehci.backend.TypeId;
import com.smartdesigns.smarthomehci.repository.ApiConnection;

import java.util.LinkedList;

public class Devices extends AppCompatActivity {

    private Room room = null;
    private Routine routine = null;
    private Device device = null;

    private ApiConnection api = ApiConnection.getInstance(this);

    /**
     * Buttons
     */

    Button onOffLights = (Button) findViewById(R.id.OnOff);
    Switch onOffAc = (Switch) findViewById(R.id.OnOffAc);
    SeekBar temperatureAc = (SeekBar) findViewById(R.id.AcTempSeekBar);
    TextView vSwing = (TextView) findViewById(R.id.VSwing);
    TextView hSwing = (TextView) findViewById(R.id.HSwing);
    TextView fanSpeed = (TextView) findViewById(R.id.FanSpeed);
    TextView acMode = (TextView) findViewById(R.id.AcMode);


    RadioButton up = (RadioButton) findViewById(R.id.UpBut);
    RadioButton down = (RadioButton) findViewById(R.id.DownBut);


    Button lampColorPicker = (Button) findViewById(R.id.colorPickerView);
    View thumbView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        thumbView = LayoutInflater.from(this).inflate(R.layout.layout_seekbar_thumb, null, false);

        final Intent i = getIntent();

        if (i.getExtras() != null) {

            device = (Device) i.getSerializableExtra("device");
            setTitle(device.getName());


            if (Home.getInstance().getCurrentMode() == 0) {
                room = (Room) i.getSerializableExtra("Object");
            } else if (Home.getInstance().getCurrentMode() == 1) {
                routine = (Routine) i.getSerializableExtra("Object");
            }

            if (device.getTypeId().equals(TypeId.Ac.getTypeId())) {

                setTheme(R.style.acStyle);
                this.setContentView(R.layout.ac);

                if (Home.getInstance().getCurrentMode() == 0) {

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
                                    Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.SuccessMsgOnOff) + sPrint
                                            , Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.ActionFail)
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
                                    Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.SuccessMsgTemp)
                                            , Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.ActionFail)
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

                setTheme(R.style.blindsStyle);
                this.setContentView(R.layout.blinds);

                if (Home.getInstance().getCurrentMode() == 0) {

                    up.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Action action = new Action(device.getId(), "up", null);
                            api.runAction(action, new Response.Listener<Boolean>() {
                                @Override
                                public void onResponse(Boolean response) {
                                    Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.SuccessBlindsDown)
                                            , Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.ActionFail)
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
                                    Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.SuccessBlindsDown)
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
                setTheme(R.style.doorStyle);
                this.setContentView(R.layout.door);

            } else if (device.getTypeId().equals(TypeId.Lamp.getTypeId())) {

                setTheme(R.style.lampStyle);
                this.setContentView(R.layout.lamp);

                lampColorPicker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openColorPicker();
                    }
                });

            } else if (device.getTypeId().equals(TypeId.Oven.getTypeId())) {

                setTheme(R.style.ovenStyle);
                this.setContentView(R.layout.oven);

            } else if (device.getTypeId().equals(TypeId.Refrigerator.getTypeId())) {

                setTheme(R.style.refrigeratorStyle);
                this.setContentView(R.layout.refrigerator);

            } else if (device.getTypeId().equals(TypeId.Alarm.getTypeId())) {

                setTheme(R.style.alarmStyle);
                this.setContentView(R.layout.alarm);

            } else if (device.getTypeId().equals(TypeId.Timer.getTypeId())) {
                setTheme(R.style.timerStyle);
                this.setContentView(R.layout.timer);

            } else {

                Toast.makeText(getApplicationContext(), "Not valid deviceType", Toast.LENGTH_LONG).show();

            }


        }
    }

    private void openColorPicker() {
        //final ColorPicker colorPicker = new ColorPicker(this);

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
                                Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.SuccessCMode)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.ActionFail)
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
                                Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.SuccessHMode)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.ActionFail)
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
                                Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.SuccessGMode)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.ActionFail)
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
                                Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.SuccessGMode)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.ActionFail)
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
                                Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.SuccessGMode)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.ActionFail)
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
                                Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.SuccessGMode)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.ActionFail)
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
                                Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.SuccessGMode)
                                        , Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(Devices.this, getResources().getString(R.string.ActionFail)
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
        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText(progress + "");

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }


}
