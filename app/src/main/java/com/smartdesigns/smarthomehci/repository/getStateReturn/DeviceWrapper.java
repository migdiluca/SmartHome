package com.smartdesigns.smarthomehci.repository.getStateReturn;

import com.smartdesigns.smarthomehci.backend.Action;
import com.smartdesigns.smarthomehci.backend.Device;
import com.smartdesigns.smarthomehci.backend.Routine;
import com.smartdesigns.smarthomehci.backend.TypeId;

import java.io.Serializable;

public class DeviceWrapper implements Serializable {
    private Device device;
    private GetState state;

    public DeviceWrapper(Device device, Routine routine) {
        this.device = device;
        generateState(routine);
    }

    public Device getDevice() {
        return device;
    }

    public GetState getGetState() {
        return state;
    }

    private void generateState(Routine routine){
        if(device.getTypeId().equals(TypeId.Ac.getTypeId())){
            state = new GetStateAc();
        }else if(device.getTypeId().equals(TypeId.Blind.getTypeId())){
            state = new GetStateBlinds();
        }else if(device.getTypeId().equals(TypeId.Door.getTypeId())){
            state = new GetStateDoor();
        }else if(device.getTypeId().equals(TypeId.Lamp.getTypeId())){
            state = new GetStateLamp();
        }else if(device.getTypeId().equals(TypeId.Oven.getTypeId())){
            state = new GetStateOven();
        }else if(device.getTypeId().equals(TypeId.Timer.getTypeId())){
            state = new GetStateTimer();
        }else if(device.getTypeId().equals(TypeId.Refrigerator.getTypeId())){
            state = new GetStateRefrigerator();
        }

        for(Action action: routine.getActions()){
            if(action.getDeviceId().equals(device.getId())){
                if(device.getTypeId().equals(TypeId.Ac.getTypeId())) {
                    if (action.getActionName().equals("turnOn")) {
                        ((GetStateAc) state).setStatus("on");
                    } else if (action.getActionName().equals("turnOff")) {
                        ((GetStateAc) state).setStatus("off");
                    }else if (action.getActionName().equals("setTemperature")) {
                        ((GetStateAc) state).setTemperature(Integer.parseInt(action.getParams().get(0)));
                    } else if (action.getActionName().equals("setMode")) {
                        ((GetStateAc) state).setMode(action.getParams().get(0));
                    } else if (action.getActionName().equals("setVerticalSwing")) {
                        ((GetStateAc) state).setVerticalSwing(action.getParams().get(0));
                    } else if (action.getActionName().equals("setHorizontalSwing")) {
                        ((GetStateAc) state).setHorizontalSwing(action.getParams().get(0));
                    } else if (action.getActionName().equals("setFanSpeed")) {
                        ((GetStateAc) state).setFanSpeed(action.getParams().get(0));
                    }
                }else if(device.getTypeId().equals(TypeId.Blind.getTypeId())){
                    if (action.getActionName().equals("up")) {
                        ((GetStateBlinds)state).setStatus("opened");
                    }else if (action.getActionName().equals("down")) {
                        ((GetStateBlinds)state).setStatus("closed");
                    }
                }else if(device.getTypeId().equals(TypeId.Door.getTypeId())){
                    if (action.getActionName().equals("open")) {
                        ((GetStateDoor)state).setStatus("opened");
                    }else if (action.getActionName().equals("close")) {
                        ((GetStateDoor)state).setStatus("closed");
                    }else if (action.getActionName().equals("lock")) {
                        ((GetStateDoor)state).setLock("locked");
                    }else if (action.getActionName().equals("unlock")) {
                        ((GetStateDoor)state).setLock("unlocked");
                    }
                }else if(device.getTypeId().equals(TypeId.Lamp.getTypeId())){
                    if (action.getActionName().equals("turnOn")) {
                        ((GetStateLamp)state).setStatus("on");
                    }else if (action.getActionName().equals("turnOff")) {
                        ((GetStateLamp)state).setStatus("off");
                    }else if (action.getActionName().equals("setColor")) {
                        ((GetStateLamp)state).setColor(action.getParams().get(0));
                    }else if (action.getActionName().equals("setBrightness")) {
                        ((GetStateLamp)state).setBrightness(Integer.parseInt(action.getParams().get(0)));
                    }
                }else if(device.getTypeId().equals(TypeId.Oven.getTypeId())){
                    if (action.getActionName().equals("turnOn")) {
                        ((GetStateOven)state).setStatus("on");
                    }else if (action.getActionName().equals("turnOff")) {
                        ((GetStateOven)state).setStatus("off");
                    }else if (action.getActionName().equals("setTemperature")) {
                        ((GetStateOven)state).setTemperature(Integer.parseInt(action.getParams().get(0)));
                    }else if (action.getActionName().equals("setHeat")) {
                        ((GetStateOven) state).setHeat(action.getParams().get(0));
                    }else if (action.getActionName().equals("setGrill")) {
                        ((GetStateOven)state).setGrill(action.getParams().get(0));
                    }else if (action.getActionName().equals("setConvection")) {
                        ((GetStateOven)state).setConvection(action.getParams().get(0));
                    }
                }else if(device.getTypeId().equals(TypeId.Timer.getTypeId())){
                    if (action.getActionName().equals("setInterval")) {
                        ((GetStateTimer)state).setInterval(action.getParams().get(0));
                    }else if (action.getActionName().equals("start")) {
                        ((GetStateTimer)state).setStatus("active");
                    }else if (action.getActionName().equals("stop")) {
                        ((GetStateTimer)state).setStatus("inactive");
                    }
                }else if(device.getTypeId().equals(TypeId.Refrigerator.getTypeId())){
                    if (action.getActionName().equals("setFreezerTemperature")) {
                        ((GetStateRefrigerator)state).setFreezerTemperature(Integer.parseInt(action.getParams().get(0)));
                    }else if (action.getActionName().equals("setTemperature")) {
                        ((GetStateRefrigerator)state).setTemperature(Integer.parseInt(action.getParams().get(0)));
                    }else if (action.getActionName().equals("setMode")) {
                        ((GetStateRefrigerator)state).setMode(action.getParams().get(0));
                    }
                }
            }
        }
    }


}
