package com.example.maledetta_treest;

import java.util.List;

public class Line {
    private List<Station> stationList;
    private String name; // prima e ultima stazione
    private String direction1, direction2;
    private String did1, did2;

    public Line(String direction1, String direction2, String did1, String did2) {
        this.direction1 = direction1;
        this.direction2 = direction2;
        this.did1 = did1;
        this.did2 = did2;
        this.name = direction1 + " - " + direction2;
    }

    public String getName() {
        return name;
    }

    public String getDirection1(){
        return this.direction1;
    }

    public String getDirection2(){
        return this.direction2;
    }

    public String getDid1() {
        return did1;
    }

    public String getDid2() {
        return did2;
    }
}
