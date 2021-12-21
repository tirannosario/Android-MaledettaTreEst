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
    private List<Post> allPostList; // verranno sovrascritti ad ogni cambio di bacheca (direzione)
    private List<Post> followPostList; // verranno sovrascritti ad ogni cambio di bacheca (direzione)
    private String did = "null";
    private String inverseDid = "null";
    private List<User> userList;
    private int uid = -1;

    public MyModel() {
        this.linesList = new ArrayList<>();
        this.stationList = new ArrayList<>();
        this.allPostList = new ArrayList<>();
        this.followPostList = new ArrayList<>();
        this.userList = new ArrayList<>();
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
    public synchronized void initPostsFromJSON(JSONObject posts){
        try {
            this.allPostList = new ArrayList<>();
            this.followPostList = new ArrayList<>();
            JSONArray postsJSON = posts.getJSONArray("posts");
            for(int i=0; i<postsJSON.length(); i++){
                JSONObject post = (JSONObject) postsJSON.get(i);
//                Log.d("Debug", "post{"+i+"}:" + post);
                Post p = new Post(
                        // con i metodi optX riesco a definire un valore nel caso quell'attributo non è presente
                        post.optInt("delay", -1),
                        post.optInt("status", -1),
                        post.optString("comment", ""),
                        post.getString("followingAuthor"),
                        post.getString("datetime"),
                        post.getString("authorName"),
                        post.getString("author"),
                        Integer.parseInt(post.getString("pversion"))
                );
                if(p.getFollowingAuthor().equals("true"))
                    this.followPostList.add(p);
                else
                    this.allPostList.add(p);
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

    public synchronized Post getFollowPost(int i){
        return this.followPostList.get(i);
    }

    public synchronized int getFollowPostsSize(){
        return this.followPostList.size();
    }

    public synchronized Post getAllPost(int i){
        return this.allPostList.get(i);
    }

    public synchronized int getAllPostsSize(){
        return this.allPostList.size();
    }

    public synchronized String getDid() {
        return did;
    }

    public synchronized void setDid(String did) {
        this.did = did;
    }

    public synchronized String getInverseDid() {
        return inverseDid;
    }

    public synchronized void setInverseDid(String inverseDid) {
        this.inverseDid = inverseDid;
    }

    public synchronized void addUser(User u){
        this.userList.add(u);
    }

    public synchronized void updateUserPicVersion(int uid, int pversion){
        for(User u: this.userList){
            if(u.getUid() == uid)
                u.setPversion(pversion);
        }
    }

    public synchronized int getUserPicVersion(int uid){
        for(User u: this.userList){
            if(u.getUid() == uid)
                return u.getPversion();
        }
        return -1;
    }

    public synchronized List<Station> getStationList() {
        return stationList;
    }

    public synchronized int getUid() {
        return uid;
    }

    public synchronized void setUid(int uid) {
        this.uid = uid;
    }
}
