package com.example.maledetta_treest;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class User {
    @PrimaryKey @NonNull
    private int uid;
    @ColumnInfo(name="UNAME")
    private String uname;
    @ColumnInfo(name="UPICTURE")
    private String upicture;
    @ColumnInfo(name="PVERSION")
    private int pversion;

    public User(int uid, String uname, String upicture, int pversion) {
        this.uid = uid;
        this.uname = uname;
        this.upicture = upicture;
        this.pversion = pversion;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpicture() {
        return upicture;
    }

    public void setUpicture(String upicture) {
        this.upicture = upicture;
    }

    public int getPversion() {
        return pversion;
    }

    public void setPversion(int pversion) {
        this.pversion = pversion;
    }

    public static User parseFromJSON(JSONObject jsonObject){
        try {
            int uid = jsonObject.getInt("uid");
            String name = jsonObject.getString("name");
            String picture = jsonObject.getString("picture");
            int pversion = jsonObject.getInt("pversion");
//            Log.d("Debug", uid + " " + name + " " + picture + " " + pversion);
            return new User(uid, name, picture, pversion);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", uname='" + uname + '\'' +
                ", upicture='" + upicture + '\'' +
                ", pversion=" + pversion +
                '}';
    }
}
