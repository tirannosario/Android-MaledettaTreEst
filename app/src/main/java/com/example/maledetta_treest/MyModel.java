package com.example.maledetta_treest;

import java.util.ArrayList;
import java.util.List;

public class MyModel {
    private static MyModel instance = null;
    private List<Line> linesList;

    public MyModel() {
        this.linesList = new ArrayList<>();
        createFakeLines();
    }

    public static synchronized MyModel getSingleton() {
        if(instance == null)
            instance = new MyModel();
        return instance;
    }

    private void createFakeLines(){
        List<Station> stationList1 = new ArrayList<>();
        stationList1.add(new Station("Milano Celoria"));
        stationList1.add(new Station("Milano Rogoredo"));
        List<Station> stationList2 = new ArrayList<>();
        stationList2.add(new Station("Milano Lambrate"));
        stationList2.add(new Station("Hogwarts"));
        stationList2.add(new Station("Sesto San Giovanni"));
        this.linesList.add(new Line(stationList1));
        this.linesList.add(new Line(stationList2));
    }

    public Line getLine(int i){
        return this.linesList.get(i);
    }

    public int getSize(){
        return this.linesList.size();
    }
}
