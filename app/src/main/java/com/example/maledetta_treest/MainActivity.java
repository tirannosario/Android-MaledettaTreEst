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
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "SharedPreferences";
    private PostsFollowAdapter postsFollowAdapter;
    private PostsAllAdapter postsAllAdapter;
    private String did;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // controllo se è la prima volta che avvio l'app (ovvero se non ho il SID)
        //TODO forse anche il sid devo prenderlo dal model? ha senso?
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
            // controllo se ho il did salvato nel Model
            String modelDid = MyModel.getSingleton().getDid();
            if(modelDid.equals("null")) {
                did = settings.getString("direction", "null");
                MyModel.getSingleton().setDid(did);
            }
            else
                did = modelDid;
            // controllo se ho già scelto una direzione l'ultima volta, altrimenti mostro la schermata delle Linee
            if(did.equals("null")){
                Intent intent = new Intent(this, ShowLines.class);
                startActivity(intent);
                this.finish();
            }
            else{
                RecyclerView recyclerViewFollow = findViewById(R.id.recycleViewPostFollow);
                recyclerViewFollow.setLayoutManager(new LinearLayoutManager(this));
                postsFollowAdapter = new PostsFollowAdapter(this);
                recyclerViewFollow.setAdapter(postsFollowAdapter);

                RecyclerView recyclerViewAll = findViewById(R.id.recycleViewPostAll);
                recyclerViewAll.setLayoutManager(new LinearLayoutManager(this));
                postsAllAdapter = new PostsAllAdapter(this);
                recyclerViewAll.setAdapter(postsAllAdapter);


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
                            postsAllAdapter.notifyDataSetChanged();
                        },
                        error -> Log.d("Debug", "Error: " + error.toString()),
                        did
                );
            }
        }
    }

    public void refreshPostLists(){
        Log.d("Debug", "Refresh Liste di Post");
        InternetCommunication internetCommunication = new InternetCommunication(this);
        internetCommunication.getPosts(
                response -> {
                    MyModel.getSingleton().initPostsFromJSON((JSONObject) response);
                    postsFollowAdapter.notifyDataSetChanged();
                    postsAllAdapter.notifyDataSetChanged();
                },
                error -> Log.d("Debug", "Error: " + error.toString()),
                did // non sarà mai Null poichè questo metodo viene richiamato solo da btn dentro la bacheca (quindi abbiamo già un DID)
        );
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

    public void btnCreatePostClicked(View view){
        TextView txtDeparture = findViewById(R.id.txt_departure_station);
        TextView txtArrival = findViewById(R.id.txt_arrival_station);
        Intent intent = new Intent(this, CreatePost.class);
        intent.putExtra("departure", txtDeparture.getText().toString());
        intent.putExtra("arrival", txtArrival.getText().toString());
        // il did della bacheca lo prenderemo dal Model
        startActivity(intent);
    }

    public void btnSwitchDirectionClicked(View view){
        Log.d("Debug", "Switch");
        // controllo se ho l'inverseDid nel Model
        String inverseDid;
        if(MyModel.getSingleton().getInverseDid().equals("null")) {
            SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, 0);
            inverseDid = settings.getString("inverseDirection", "null");
        }
        else
            inverseDid = MyModel.getSingleton().getInverseDid();

        if(!inverseDid.equals("null"))
        {
            SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("direction", inverseDid);
            editor.putString("inverseDirection", did);
            editor.commit();
            MyModel.getSingleton().setDid(inverseDid);
            MyModel.getSingleton().setInverseDid(did);
            // refresh dell'activity (richiama l'OnCreate)
            this.finish();
            startActivity(getIntent());
        }
    }

    @Override
    protected void onRestart() { // così quando ritorniamo dalla creazione di un post posso vedere anche il mio post nella lista di post
        super.onRestart();
        refreshPostLists();
    }
}