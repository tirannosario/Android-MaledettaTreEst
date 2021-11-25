package com.example.maledetta_treest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "MainActivity";

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
                            Log.d("Debug", "nuovo sid: " + newSid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Log.d("Volley", "Error: " + error.toString())
            );
            Intent intent = new Intent(this, ShowLines.class);
            startActivity(intent);
            this.finish();
        }
        else{
            Log.d("Debug", "sid salvato: " + sid);
            String direction = settings.getString("direction", "null");
            // controllo se ho già scelto una direzione l'ultima volta, altrimenti mostro la schermata delle Linee
            if(direction.equals("null")){
                Intent intent = new Intent(this, ShowLines.class);
                startActivity(intent);
                this.finish();
            }
            else{
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