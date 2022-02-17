package com.example.maledetta_treest;

public class Station {
    private String sname, lat, lon;

    public Station(String sname, String lat, String lon) {
        this.sname = sname;
        this.lat = lat;
        this.lon = lon;
    }

    public String getSname() {
        return sname;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }
}
