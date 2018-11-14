package com.smartdesigns.smarthomehci.backend;

public class Room {
    private String id;
    private String name;
    private String meta;

    public Room(String name, String meta) {
        this.name = name;
        this.meta = meta;
    }

    public Room(String id, String name, String meta) {
        this.id = id;
        this.name = name;
        this.meta = meta;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getMeta() {
        return this.meta;
    }

    @Override
    public String toString() {
        return (this.id == null) ?
                String.format("%s - %s", this.getName(), this.getMeta()) :
                String.format("%s - %s - %s", this.getId(), this.getName(), this.getMeta());
    }

    @Override
    public boolean equals(Object o){
        if(o == null || !(o instanceof Room)) {
            return false;
        }

        Room d = (Room)o;
        return this.name.equals(d.name)  && this.meta.equals(d.meta);
    }
}