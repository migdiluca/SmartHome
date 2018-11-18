package com.smartdesigns.smarthomehci;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.smartdesigns.smarthomehci.backend.Action;
import com.smartdesigns.smarthomehci.backend.Device;
import com.smartdesigns.smarthomehci.backend.Room;
import com.smartdesigns.smarthomehci.backend.TypeId;
import com.smartdesigns.smarthomehci.repository.ApiConnection;
import com.smartdesigns.smarthomehci.repository.getStateReturn.getStateLamp;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private List<Device> devices;
    private List<Room> rooms;

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.smartdesigns.smarthomehci", appContext.getPackageName());
    }

    /*Test con la base de datos inicialmente vacia*/
    @Test
    public void apiTest(){
       /* DevicesApiTest();
        RoomsApiTest();
        DeviceInRoomTest();*/
        ActionTest();
    }

    public void DevicesApiTest(){
        devices = new ArrayList<>();

        Context appContext = InstrumentationRegistry.getTargetContext();

        ApiConnection api = ApiConnection.getInstance(appContext);

        //creating devices
        Device device1 = new Device("Lamparita", TypeId.Lamp.getTypeId(), "[]");
        Device device2 = new Device("Horno", TypeId.Oven.getTypeId(), "[]");
        Device device3 = new Device("Heladera", TypeId.Refrigerator.getTypeId(), "[]");

        devices.add(device1);
        devices.add(device2);
        devices.add(device3);


        for(Device device: devices) {
            api.createDevice(device, new Response.Listener<Device>() {
                @Override
                public void onResponse(Device response) {
                    devices.get(devices.indexOf(response)).setId(response.getId());
                    assertTrue(devices.contains(response));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    assertTrue(false);
                }
            });
        }


        try {
            Thread.sleep(3000);
        }catch (Exception e){

        }

        //retrieving devices
        api.getDevices(new Response.Listener<List<Device>>() {
            @Override
            public void onResponse(List<Device> response) {
                for (Device device : response) {
                    assertTrue(devices.contains(device));
                }
                assertTrue(devices.size() == response.size());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                assertTrue(false);
            }
        });

        try {
            Thread.sleep(3000);
        }catch (Exception e){

        }

        //updating devices
        devices.get(0).setName("Cortinita");
        api.updateDevice(devices.get(0), new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                assertTrue(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                assertTrue(false);
            }
        });

        try {
            Thread.sleep(3000);
        }catch (Exception e){

        }



        //deleting devices
        for(Device device: devices){
            api.deleteDevice(device, new Response.Listener<Boolean>() {
                @Override
                public void onResponse(Boolean response) {
                    assertTrue(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    assertTrue(false);
                }
            });
        }

        try {
            Thread.sleep(3000);
        }catch (Exception e){

        }

    }


    public void RoomsApiTest(){
        Context appContext = InstrumentationRegistry.getTargetContext();

        ApiConnection api = ApiConnection.getInstance(appContext);

        rooms = new ArrayList<>();

        //creating rooms
        Room room1 = new Room("Cocina", "[]");
        Room room2 = new Room("Dormitorio", "[]");
        Room room3 = new Room("Banio", "[]");


        rooms.add(room1);
        rooms.add(room2);
        rooms.add(room3);


        for(Room room: rooms) {
            api.createRoom(room, new Response.Listener<Room>() {
                @Override
                public void onResponse(Room response) {
                    rooms.get(rooms.indexOf(response)).setId(response.getId());
                    assertTrue(rooms.contains(response));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    assertTrue(false);
                }
            });
        }

        try {
            Thread.sleep(3000);
        }catch (Exception e){

        }

        //retrieving rooms
        api.getRooms(new Response.Listener<List<Room>>() {
            @Override
            public void onResponse(List<Room> response) {
                for (Room room : response) {
                    assertTrue(rooms.contains(room));
                }
                assertTrue(rooms.size() == response.size());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                assertTrue(false);
            }
        });

        try {
            Thread.sleep(3000);
        }catch (Exception e){

        }

        //updating rooms
        rooms.get(0).setName("Cocinita");
        api.updateRoom(rooms.get(0), new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                assertTrue(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                assertTrue(false);
            }
        });

        try {
            Thread.sleep(3000);
        }catch (Exception e){

        }

        //deleting rooms
        for(Room room: rooms){
            api.deleteRoom(room, new Response.Listener<Boolean>() {
                @Override
                public void onResponse(Boolean response) {
                    assertTrue(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    assertTrue(false);
                }
            });
        }

        try {
            Thread.sleep(3000);
        }catch (Exception e){

        }
    }

    public void DeviceInRoomTest(){
        devices = new ArrayList<>();
        Device device1 = new Device("Lampara", TypeId.Lamp.getTypeId(), "[]");
        Device device2 = new Device("Cosos", TypeId.Alarm.getTypeId(), "[]");
        devices.add(device1);
        devices.add(device2);
        Room room = new Room("Cuarto", "[]");

        Context appContext = InstrumentationRegistry.getTargetContext();
        ApiConnection api = ApiConnection.getInstance(appContext);

        //create device
        for(Device device : devices) {
            api.createDevice(device, new Response.Listener<Device>() {
                @Override
                public void onResponse(Device response) {
                    devices.get(devices.indexOf(response)).setId(response.getId());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    assertTrue(false);
                }
            });
        }

        //create room
        api.createRoom(room, new Response.Listener<Room>() {
            @Override
            public void onResponse(Room response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                assertTrue(false);
            }
        });

        try {
            Thread.sleep(3000);
        }catch (Exception e){

        }

        //assign device to room
        for(Device device: devices){
            api.assignDeviceToRoom(device, room, new Response.Listener<Boolean>(){
                @Override
                public void onResponse(Boolean response){

                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    assertTrue(false);
                }
            });
        }

        try {
            Thread.sleep(3000);
        }catch (Exception e){

        }

        //list all room devices
        api.getRoomDevices(room, new Response.Listener<List<Device>>() {
            @Override
            public void onResponse(List<Device> response) {
                assertTrue(response.size() == devices.size());
                for (Device device : response) {
                    assertTrue(devices.contains(device));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                assertTrue(false);
            }
        });

        try {
            Thread.sleep(3000);
        }catch (Exception e){

        }

        //delete room and devices

        api.deleteRoom(room, new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                assertTrue(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                assertTrue(false);
            }
        });


        try {
            Thread.sleep(3000);
        }catch (Exception e){

        }

        //deleting rooms
        for(Device device: devices){
            api.deleteDevice(device, new Response.Listener<Boolean>() {
                @Override
                public void onResponse(Boolean response) {
                    assertTrue(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    assertTrue(false);
                }
            });
        }

        try {
            Thread.sleep(3000);
        }catch (Exception e){

        }
    }

    public void ActionTest(){
    devices = new ArrayList<>();

    Context appContext = InstrumentationRegistry.getTargetContext();

    ApiConnection api = ApiConnection.getInstance(appContext);


        //retrieving devices
        api.getDevices(new Response.Listener<List<Device>>() {
            @Override
            public void onResponse(List<Device> response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                assertTrue(false);
            }
        });

        try {
            Thread.sleep(3000);
        }catch (Exception e){

        }

    //creating devices
    Device device1 = new Device("Lamparita", TypeId.Lamp.getTypeId(), "[]");

        api.createDevice(device1, new Response.Listener<Device>() {
            @Override
            public void onResponse(Device response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                assertTrue(false);
            }
        });

        try {
            Thread.sleep(3000);
        }catch (Exception e){

        }

    //executing actions
    Action action = new Action(device1.getId(), "down", new ArrayList<String>());
    api.runAction(action, new Response.Listener<Boolean>() {
        @Override
        public void onResponse(Boolean response) {
            assertTrue(response);
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            assertTrue(false);
        }
    });

    try {
        Thread.sleep(3000);
    }catch (Exception e){

    }

    api.getStateLamp(device1, new Response.Listener<getStateLamp>() {
        @Override
        public void onResponse(getStateLamp response) {
            assertTrue(response.getStatus().equals("closing") || response.getStatus().equals("closed"));
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            assertTrue(false);
        }
    });

    try {
        Thread.sleep(3000);
    }catch (Exception e){

    }

        api.deleteDevice(device1, new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                assertTrue(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                assertTrue(false);
            }
        });

        try {
            Thread.sleep(3000);
        }catch (Exception e){

        }
    }
}
