package com.example.maledetta_treest;

import java.util.List;

public class Line {
    private List<Station> stationList;
    private String name; // prima e ultima stazione
    private String did1, did2;

    public Line(List<Station> stationList) {
        this.stationList = stationList;
        //crea il nome della linea, con prima e ultima fermata
        //TODO fare controllo se l'array ho tot array, per evitare IndexBoundException
        this.name = this.stationList.get(0).sname + " - " + this.stationList.get(this.stationList.size()-1).sname;
    }

    public String getName() {
        return name;
    }

    public String getDirection1(){
        return this.stationList.get(0).sname;
    }

    public String getDirection2(){
        return this.stationList.get(this.stationList.size()-1).sname;
    }


}
