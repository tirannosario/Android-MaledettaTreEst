package com.example.maledetta_treest;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class InternetCommunication {
    RequestQueue queue;
    final String baseUrl = "https://ewserver.di.unimi.it/mobicomp/treest/";

    public InternetCommunication(Context context) {
        queue = Volley.newRequestQueue(context);
    }

    public void register(Response.Listener listener, Response.ErrorListener errorListener){
        final JSONObject jsonBody = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(
                baseUrl+"register.php",
                jsonBody,
                listener,
                errorListener
        );
        Log.d("Debug", "Faccio la Register");
        queue.add(request);
    }

    public void getLines(Response.Listener listener, Response.ErrorListener errorListener){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", MyModel.getSingleton().getSid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                baseUrl+"getLines.php",
                jsonBody,
                listener,
                errorListener
        );
        Log.d("Debug", "Faccio la getLines");
        queue.add(request);
    }

    public void getStations(Response.Listener listener, Response.ErrorListener errorListener, String did){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", MyModel.getSingleton().getSid());
            jsonBody.put("did", did);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                baseUrl+"getStations.php",
                jsonBody,
                listener,
                errorListener
        );
        Log.d("Debug", "Faccio la getStations di " + did);
        queue.add(request);
    }

    //TODO refactor di tutte le getQualcosa
    public void getPosts(Response.Listener listener, Response.ErrorListener errorListener, String did){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", MyModel.getSingleton().getSid());
            jsonBody.put("did", did);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                baseUrl+"getPosts.php",
                jsonBody,
                listener,
                errorListener
        );
        Log.d("Debug", "Faccio la getPosts di " + did);
        queue.add(request);
    }

    public void followUser(Response.Listener listener, Response.ErrorListener errorListener, String uid){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", MyModel.getSingleton().getSid());
            jsonBody.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                baseUrl+"follow.php",
                jsonBody,
                listener,
                errorListener
        );
        Log.d("Debug", "Faccio la follow di " + uid);
        queue.add(request);
    }

    public void unfollowUser(Response.Listener listener, Response.ErrorListener errorListener, String uid){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", MyModel.getSingleton().getSid());
            jsonBody.put("uid", uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                baseUrl+"unfollow.php",
                jsonBody,
                listener,
                errorListener
        );
        Log.d("Debug", "Faccio la unfollow di " + uid);
        queue.add(request);
    }

    public void createPost(Response.Listener listener, Response.ErrorListener errorListener, String did, int delay, int status, String comment){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", MyModel.getSingleton().getSid());
            jsonBody.put("did", did);
            if(delay != -1)
                jsonBody.put("delay", delay);
            if(status != -1)
                jsonBody.put("status", status);
            if(!comment.equals(""))
                jsonBody.put("comment", comment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                baseUrl+"addPost.php",
                jsonBody,
                listener,
                errorListener
        );
        Log.d("Debug", "Creo il post su " + did + " {" + delay + ", " + status + ", " + comment + "}");
        queue.add(request);
    }

    public void getProfile(Response.Listener listener, Response.ErrorListener errorListener){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", MyModel.getSingleton().getSid());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                baseUrl+"getProfile.php",
                jsonBody,
                listener,
                errorListener
        );
        Log.d("Debug", "Faccio la getProfile");
        queue.add(request);
    }

    public void setProfile(Response.Listener listener, Response.ErrorListener errorListener, String name, String picture){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", MyModel.getSingleton().getSid());
            if(!name.equals(""))
                jsonBody.put("name", name);
            if(!picture.equals(""))
                jsonBody.put("picture", picture);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                baseUrl+"setProfile.php",
                jsonBody,
                listener,
                errorListener
        );
        Log.d("Debug", "Faccio la setProfile");
        queue.add(request);
    }
}
