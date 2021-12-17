package com.example.maledetta_treest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class DirectionDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }

    public void btnBackDirectionDetailClicked(View view){
        this.finish();
    }
}