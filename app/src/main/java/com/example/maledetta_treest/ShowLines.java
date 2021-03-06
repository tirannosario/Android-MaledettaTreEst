package com.example.maledetta_treest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

public class ShowLines extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lines);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        RecyclerView recyclerView = findViewById(R.id.recycleViewLines);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LinesAdapter myAdapter = new LinesAdapter(this);
        recyclerView.setAdapter(myAdapter);

        InternetCommunication internetCommunication = new InternetCommunication(this);
        ProgressDialog p = InternetCommunication.showProgressSpinner(this);
        internetCommunication.getLines(
                response -> {
                    MyModel.getSingleton().initLinesFromJSON((JSONObject) response);
                    myAdapter.notifyDataSetChanged();
                    p.dismiss();
                },
                error ->  {
                    Log.d("Debug", "Error: " + error.toString());
                    InternetCommunication.showNetworkError(this, true);
                }
        );
    }

    public void userBtnClicked(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}