package com.example.maledetta_treest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "SharedPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // controllo se è la prima volta che avvio l'app (ovvero se non ho il SID)
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String sid = settings.getString("sid", "null");
        if(sid.equals("null")) {
            //la prima volta che entro nell'app, mi registro al sistema e mostro la schermata delle Linee
            InternetCommunication internetCommunication = new InternetCommunication(this);
            internetCommunication.register(
                    response -> {
                        try {
                            String newSid = ((JSONObject)response).getString("sid");
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("sid", newSid);
                            editor.commit();
                            // lo salvo anche nel model, così da poterlo prendere in maniera semplice
                            MyModel.getSingleton().setSid(newSid);
                            Log.d("Debug", "nuovo sid: " + newSid);
                            // una volta che ho il SID, passo all'altra activity
                            Intent intent = new Intent(this, ShowLines.class);
                            startActivity(intent);
                            this.finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Log.d("Debug", "Error: " + error.toString())
            );
        }
        else{
            Log.d("Debug", "sid salvato: " + sid);
            MyModel.getSingleton().setSid(sid);
            String did = settings.getString("direction", "null");
            // controllo se ho già scelto una direzione l'ultima volta, altrimenti mostro la schermata delle Linee
            if(did.equals("null")){
                Intent intent = new Intent(this, ShowLines.class);
                startActivity(intent);
                this.finish();
            }
            else{
                RecyclerView recyclerViewFollow = findViewById(R.id.recycleViewPostFollow);
                recyclerViewFollow.setLayoutManager(new LinearLayoutManager(this));
                PostsFollowAdapter postsFollowAdapter = new PostsFollowAdapter(this);
                recyclerViewFollow.setAdapter(postsFollowAdapter);

                //TODO fare lo stesso per i post di tutti gli utenti


                Log.d("Debug", "Questa è la bacheca con did: " + did);
                InternetCommunication internetCommunication = new InternetCommunication(this);
                // richiedo le stazione così da poter sapere partenza-arrivo della direzione
                internetCommunication.getStations(
                        response -> {
                            MyModel.getSingleton().initStationsFromJSON((JSONObject) response);
                            TextView txtDeparture = findViewById(R.id.txt_departure_station);
                            TextView txtArrival = findViewById(R.id.txt_arrival_station);
                            txtDeparture.setText(MyModel.getSingleton().getFirstStation());
                            txtArrival.setText(MyModel.getSingleton().getLastStation());
                        },
                        error -> Log.d("Debug", "Error: " + error.toString()),
                        did
                );

                internetCommunication.getPosts(
                        response -> {
                            MyModel.getSingleton().initPostsFromJSON((JSONObject) response);
                            postsFollowAdapter.notifyDataSetChanged();
                        },
                        error -> Log.d("Debug", "Error: " + error.toString()),
                        did
                );
            }
        }
    }

    public void userBtnClicked(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void btnBackClicked(View view){
        Intent intent = new Intent(this, ShowLines.class);
        startActivity(intent);
        this.finish();
    }
}