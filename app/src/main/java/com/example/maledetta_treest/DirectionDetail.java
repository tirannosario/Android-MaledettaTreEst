package com.example.maledetta_treest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class DirectionDetail extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 42;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Debug", "NON Ho i permessi per la posizione!");
            // richiedo i permessi
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            );
        } else {
            Log.d("Debug", "Ho i permessi per la posizione!");
            requestCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // perchè mi sta obbligando
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Debug", "Ora ho i permessi per la posizione!");
                requestCurrentLocation();

            } else {
                // Android chiederà all'utente altre due volte i permessi, dopo di che sarà necessario andare sulle impotazioni
                Log.d("Debug", "Ancora non da i permessi!");
                Toast toast = Toast.makeText(this, "La posizione è necessaria per offrire un servizio migliore. Se vuoi fornirla vai su:\n Impostazioni -> Posizione -> Maledetta TreEst", Toast.LENGTH_LONG);
                toast.show();

            }
            return;
        }
    }

    @SuppressLint("MissingPermission") // altrimenti diceva che mancavano i permessi sul manifest (anche se ci sono)
    private void requestCurrentLocation(){
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(
                        location -> {
                            if (location != null && !getSupportFragmentManager().isDestroyed()) { // controllo il fragmentManager, poichè se torno all'activity precedente prima che questo metodo sia richiamato, il fragmentManager non esiste più e darà errori
                                Log.d("Debug", "Current Location:" + location.toString());
                                MapsFragment mapFragment = new MapsFragment();
                                Bundle bundle = new Bundle();
                                bundle.putDouble("userLat", location.getLatitude());
                                bundle.putDouble("userLong", location.getLongitude());
                                mapFragment.setArguments(bundle);
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerViewMaps, mapFragment).commit();
                            }
                        });
    }

    public void btnBackDirectionDetailClicked(View view){
        this.finish();
    }
}