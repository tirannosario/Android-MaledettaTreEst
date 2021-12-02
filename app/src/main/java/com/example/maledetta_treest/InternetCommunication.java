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

//    public void getPosts
}
