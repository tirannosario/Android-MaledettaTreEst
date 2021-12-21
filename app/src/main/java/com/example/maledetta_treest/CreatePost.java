package com.example.maledetta_treest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.stream.Stream;

public class CreatePost extends AppCompatActivity {
    private TextView txtComment;
    private Spinner spinnerDelay, spinnerStatus;
    private static final String PREFS_NAME = "SharedPreferences";


    private int[] POST_DELAY = {R.string.empty_thing, R.string.delay_0_string, R.string.delay_1_string, R.string.delay_2_string, R.string.delay_3_string};
    private int[] POST_STATUS = {R.string.empty_thing, R.string.status_0_string, R.string.status_1_string, R.string.status_2_string};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        TextView txtDeparture = findViewById(R.id.txt_departure_station_create);
        TextView txtArrival = findViewById(R.id.txt_arrival_station_create);
        Log.d("Debug", "Ricreo activity con " + intent.getStringExtra("departure") + " " + intent.getStringExtra("arrival"));
        txtDeparture.setText(intent.getStringExtra("departure"));
        txtArrival.setText(intent.getStringExtra("arrival"));
        txtComment = findViewById(R.id.txtCommentCreate);
        spinnerDelay = findViewById(R.id.spinnerDelay);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        String[] mappedDelays = Arrays.stream(POST_DELAY).mapToObj(v -> this.getResources().getString(v)).toArray(String[]::new);

        String[] mappedStatus = Arrays.stream(POST_STATUS).mapToObj(v -> this.getResources().getString(v)).toArray(String[]::new);

        ArrayAdapter<String> adapterDelays = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mappedDelays);
        adapterDelays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDelay.setAdapter(adapterDelays);

        ArrayAdapter<String> adapterStatus = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mappedStatus);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapterStatus);
    }

    public void getBackFromCreate(View view){
        this.finish();
    }

    public void createPostClicked(View view){
        String delaySelected =  spinnerDelay.getSelectedItem().toString();
        String statusSelected = spinnerStatus.getSelectedItem().toString();
        int delayInt = parseValueToIntResource(delaySelected, POST_DELAY);
        int statusInt = parseValueToIntResource(statusSelected, POST_STATUS);
        String comment = txtComment.getText().toString();

        if(delayInt != -1 || statusInt != -1 || !comment.equals(""))
        {
            if(comment.length() > 100){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Attenzione").setMessage("Commento troppo lungo, fallo stare nei 100 caratteri!");
                builder.create().show();
            }
            else {
                //controllo se ho il did nel Model
                String did;
                if(MyModel.getSingleton().getDid().equals("null")) {
                    SharedPreferences settings = this.getSharedPreferences(PREFS_NAME, 0);
                    did = settings.getString("direction", "null");
                }
                else
                    did = MyModel.getSingleton().getDid();

                if(!did.equals("null")) { // comunque non dovrebbe mai succedere, se sono arrivato a questa activity
                    InternetCommunication internetCommunication = new InternetCommunication(this);
                    internetCommunication.createPost(
                            response -> {
                                Log.d("Debug", "Post inserito correttamente");
                                Toast toast = Toast.makeText(this, "Post Aggiunto", Toast.LENGTH_SHORT);
                                toast.show();
                                this.finish();
                            },
                            error -> {
                                Log.d("Debug", "Error: " + error.toString());
                                InternetCommunication.showNetworkError(this, false);
                            },
                            did,
                            delayInt,
                            statusInt,
                            comment
                            );
                }
            }
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Attenzione").setMessage("Fornire delle Informazioni");
            builder.create().show();
        }
    }

    private int parseValueToIntResource(String valueToParse, int[] resourceArray){
        for(int i=0; i<resourceArray.length; i++){
            if(getResources().getString(resourceArray[i]).equals(valueToParse)){
                if(i==0)
                    return -1; // quello in prima posizione è la scelta vuota
                else
                    return i - 1; // -1 così da rispettare il coding tra delay/status e significato del numero
            }
        }
        return -1;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        Log.d("Debug", "Sto uscendo");

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}