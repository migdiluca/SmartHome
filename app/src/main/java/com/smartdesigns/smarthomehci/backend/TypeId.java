package com.smartdesigns.smarthomehci.backend;

public enum TypeId {
    Ac("li6cbv5sdlatti0j"), Blind("eu0v2xgprrhhg41g"), Door("lsf78ly0eqrjbz91"), Lamp("go46xmbqeomjrsjr"),
        Oven("im77xxyulpegfmv8"), Refrigerator("rnizejqr2di0okho"), Alarm("mxztsyjzsrq7iaqc"), Timer("ofglvd9gqX8yfl3l");

    private String typeId;
    private TypeId(String typeId){
        this.typeId = typeId;
    }

    public String getTypeId() {
        return typeId;
    }
}
