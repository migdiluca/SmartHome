package com.smartdesigns.smarthomehci.backend;

public enum TypeId {
    Ac("li6cbv5sdlatti0j", "ac2.png"), Blind("eu0v2xgprrhhg41g", "blind.png"), Door("lsf78ly0eqrjbz91", "door.png"), Lamp("go46xmbqeomjrsjr", "lamp3.png"),
        Oven("im77xxyulpegfmv8", "oven.png"), Refrigerator("rnizejqr2di0okho", "refrigerator.png"), Alarm("mxztsyjzsrq7iaqc", "alarm3.png"), Timer("ofglvd9gqX8yfl3l", "timer.png");

    private String typeId;
    private String defaultImg;
    private TypeId(String typeId, String defaultImg){
        this.typeId = typeId;
        this.defaultImg = defaultImg;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getDefaultImg(){
        return defaultImg;
    }

    public static String getImg(String typeId){
        for (TypeId dir : TypeId.values()) {
            if(dir.typeId.equals(typeId)){
                return dir.defaultImg;
            }
        }
        return null;
    }
}
