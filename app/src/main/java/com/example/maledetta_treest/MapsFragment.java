package com.example.maledetta_treest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.pm.verify.domain.DomainVerificationUserState;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsFragment extends Fragment {
    public MapsFragment() {
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            // mostro la posizione dell'utente
            if(getArguments() != null){
                Double userLat = getArguments().getDouble("userLat");
                Double userLong = getArguments().getDouble("userLong");
                Log.d("Debug", "L'utente è a: " + userLat + " " + userLong);
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(userLat, userLong))
                        .title("Posizione Corrente")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }

            List<Station> stationList = MyModel.getSingleton().getStationList();
            for (Station s : stationList) {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(s.getLat()), Double.parseDouble(s.getLon())))
                        .title(s.getSname()));
            }
            if (stationList.size() > 0) {
                LatLng firstStation = new LatLng(Double.parseDouble(stationList.get(0).getLat()), Double.parseDouble(stationList.get(0).getLon()));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstStation, 12.0f));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}