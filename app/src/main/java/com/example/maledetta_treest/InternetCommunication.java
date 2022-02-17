package com.example.maledetta_treest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

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

    private void genericRequest(String endpoint, JSONObject param, Response.Listener listener, Response.ErrorListener errorListener){
        JsonObjectRequest request = new JsonObjectRequest(
                baseUrl + endpoint + ".php",
                param,
                listener,
                errorListener
        );
        queue.add(request);
    }

    public void register(Response.Listener listener, Response.ErrorListener errorListener){
        final JSONObject jsonBody = new JSONObject();
        genericRequest("register", jsonBody, listener, errorListener);
        Log.d("Debug", "Faccio la Register");
    }

    public void getLines(Response.Listener listener, Response.ErrorListener errorListener){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", MyModel.getSingleton().getSid());
            genericRequest("getLines", jsonBody, listener, errorListener);
            Log.d("Debug", "Faccio la getLines");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getStations(Response.Listener listener, Response.ErrorListener errorListener, String did){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", MyModel.getSingleton().getSid());
            jsonBody.put("did", did);
            genericRequest("getStations", jsonBody, listener, errorListener);
            Log.d("Debug", "Faccio la getStations di " + did);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getPosts(Response.Listener listener, Response.ErrorListener errorListener, String did){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", MyModel.getSingleton().getSid());
            jsonBody.put("did", did);
            genericRequest("getPosts", jsonBody, listener, errorListener);
            Log.d("Debug", "Faccio la getPosts di " + did);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void followUser(Response.Listener listener, Response.ErrorListener errorListener, String uid){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", MyModel.getSingleton().getSid());
            jsonBody.put("uid", uid);
            genericRequest("follow", jsonBody, listener, errorListener);
            Log.d("Debug", "Faccio la follow di " + uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void unfollowUser(Response.Listener listener, Response.ErrorListener errorListener, String uid){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", MyModel.getSingleton().getSid());
            jsonBody.put("uid", uid);
            genericRequest("unfollow", jsonBody, listener, errorListener);
            Log.d("Debug", "Faccio la unfollow di " + uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            genericRequest("addPost", jsonBody, listener, errorListener);
            Log.d("Debug", "Creo il post su " + did + " {" + delay + ", " + status + ", " + comment + "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getProfile(Response.Listener listener, Response.ErrorListener errorListener){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", MyModel.getSingleton().getSid());
            genericRequest("getProfile", jsonBody, listener, errorListener);
            Log.d("Debug", "Faccio la getProfile");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setProfile(Response.Listener listener, Response.ErrorListener errorListener, String name, String picture){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", MyModel.getSingleton().getSid());
            if(!name.equals(""))
                jsonBody.put("name", name);
            if(!picture.equals(""))
                jsonBody.put("picture", picture);
            genericRequest("setProfile", jsonBody, listener, errorListener);
            Log.d("Debug", "Faccio la setProfile");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getUserPicture(Response.Listener listener, Response.ErrorListener errorListener, String uid){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", MyModel.getSingleton().getSid());
            jsonBody.put("uid", uid);
            genericRequest("getUserPicture", jsonBody, listener, errorListener);
            Log.d("Debug", "Faccio la getUserPicture di " + uid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getSponsors(Response.Listener listener, Response.ErrorListener errorListener){
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("sid", MyModel.getSingleton().getSid());
            genericRequest("locspons", jsonBody, listener, errorListener);
            Log.d("Debug", "Faccio la getSponsors");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // per mostrare Alert di errore di Network
    public static void showNetworkError(Activity activity, boolean closeApp){
        String textClose = "";
        if(closeApp)
            textClose = "L'App verrà chiusa";
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Errore di Collegamento!").setMessage("Controlla la tua connessione o riprova tra qualche minuto...\n" + textClose)
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    if(closeApp)
                        activity.finish();
                });
        builder.setCancelable(false);
        builder.create().show();
    }

    public static ProgressDialog showProgressSpinner(Activity activity){
        ProgressDialog pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading...");
        pDialog.show();
        return  pDialog;
    }

    public static void stopProgressSpinner(Activity activity, ProgressDialog pDialog){
        pDialog.dismiss();
    }
}
