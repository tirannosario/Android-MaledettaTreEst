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
    private List<Station> stationList; // verranno sovrascritte ad ogni cambio di direzione
    private List<Post> postList; // verranno sovrascritti ad ogni cambio di bacheca (direzione)

    public MyModel() {
        this.linesList = new ArrayList<>();
//        createFakeLines();
    }

    public static synchronized MyModel getSingleton() {
        if(instance == null)
            instance = new MyModel();
        return instance;
    }

    public synchronized void initLinesFromJSON(JSONObject lines){
        try {
            // ripulisco tutta la lista prima
            this.linesList = new ArrayList<>();
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

    public synchronized void initStationsFromJSON(JSONObject stations){
        try {
            this.stationList = new ArrayList<>();
            JSONArray stationsJSON = stations.getJSONArray("stations");
            for(int i=0; i<stationsJSON.length(); i++){
                JSONObject station = (JSONObject) stationsJSON.get(i);
                this.stationList.add(new Station(
                        station.getString("sname"),
                        station.getString("lat"),
                        station.getString("lon")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
/*        for (Station s: this.stationList) {
            Log.d("Debug", "Station: " + s.getSname() + ", " + s.getLat() + ", " + s.getLon());
        }*/
    }

    //TODO refactor di tutte le init from JSON
    public synchronized void initPostsFromJSON(JSONObject stations){
        try {
            this.postList = new ArrayList<>();
            JSONArray postsJSON = stations.getJSONArray("posts");
            for(int i=0; i<postsJSON.length(); i++){
                JSONObject post = (JSONObject) postsJSON.get(i);
                this.postList.add(new Post(
                        Integer.parseInt(post.getString("delay")),
                        Integer.parseInt(post.getString("status")),
                        post.getString("comment"),
                        post.getString("followingAuthor"),
                        post.getString("datetime"),
                        post.getString("authorName"),
                        post.getString("author"),
                        Integer.parseInt(post.getString("pversion"))
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (Post s: this.postList) {
            Log.d("Debug", s.toString());
        }
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

    public synchronized String getFirstStation(){
        if(this.stationList.size() >= 2)
            return this.stationList.get(0).getSname();
        return "";
    }

    public synchronized String getLastStation(){
        if(this.stationList.size() >= 2)
            return this.stationList.get(this.stationList.size()-1).getSname();
        return "";
    }
}
