package com.example.maledetta_treest;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyModel {
    private static MyModel instance = null;
    private List<Line> linesList;
    private String sid;

    public MyModel() {
        this.linesList = new ArrayList<>();
//        createFakeLines();
    }

    public static synchronized MyModel getSingleton() {
        if(instance == null)
            instance = new MyModel();
        return instance;
    }

    public synchronized void addLinesFromJSON(JSONObject lines){
        try {
            JSONArray linesJSON = lines.getJSONArray("lines");
            for(int i=0; i<linesJSON.length(); i++){
                JSONObject line = (JSONObject) linesJSON.get(i);
                JSONObject terminus1 = line.getJSONObject("terminus1");
                JSONObject terminus2 = line.getJSONObject("terminus2");
                this.linesList.add(new Line(
                        terminus1.getString("sname"),
                        terminus2.getString("sname"),
                        terminus1.getString("did"),
                        terminus2.getString("did")));

            }
        } catch (JSONException e) {
            e.getStackTrace();
        }
    }

/*    private void createFakeLines(){
        List<Station> stationList1 = new ArrayList<>();
        stationList1.add(new Station("Milano Celoria"));
        stationList1.add(new Station("Milano Rogoredo"));
        List<Station> stationList2 = new ArrayList<>();
        stationList2.add(new Station("Milano Lambrate"));
        stationList2.add(new Station("Hogwarts"));
        stationList2.add(new Station("Sesto San Giovanni"));
        this.linesList.add(new Line(stationList1));
        this.linesList.add(new Line(stationList2));
    }*/

    public synchronized Line getLine(int i){
        return this.linesList.get(i);
    }

    public synchronized int getLinesSize(){
        return this.linesList.size();
    }

    public synchronized void setSid(String sid) {
        this.sid = sid;
    }
    public synchronized String getSid() {
        return sid;
    }
}
