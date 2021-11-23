package com.example.maledetta_treest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ShowLines extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lines);
    }

    public void userBtnClicked(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}