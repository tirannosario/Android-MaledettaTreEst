package com.example.maledetta_treest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LineViewHolder extends RecyclerView.ViewHolder {
    private ShowLines showLinesActivity;
    private TextView txtLine;
    private Button btnDirection1;
    private Button btnDirection2;
    private Context context;
    private static final String PREFS_NAME = "SharedPreferences";


    public LineViewHolder(@NonNull View itemView, ShowLines showLinesActivity) {
        super(itemView);
        this.showLinesActivity = showLinesActivity;
        txtLine = itemView.findViewById(R.id.txtLine);
        btnDirection1 = itemView.findViewById(R.id.btnDirection1);
        btnDirection2 = itemView.findViewById(R.id.btnDirection2);
        context = itemView.getContext();
    }

    public void updateContent(Line line){
        txtLine.setText(line.getName());
        btnDirection1.setText(line.getDirection1());
        btnDirection2.setText(line.getDirection2());

        btnDirection1.setOnClickListener(view -> {
            Log.d("Debug", "Andrò alla bacheca con DID :" + line.getDid1());
            openBoard(line.getDid1());

        });
        btnDirection2.setOnClickListener(view -> {
            Log.d("Debug", "Andrò alla bacheca con DID :" + line.getDid2());
            openBoard(line.getDid2());
        });
    }

    private void openBoard(String did){
        // non passo il DID tramite intent, poichè uso le SharedPreferences, visto che il DID sarà un dato che manterrò in memoria
        Intent intent = new Intent(context, MainActivity.class);
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("direction", did);
        editor.commit();
        context.startActivity(intent);
        ((Activity)context).finish();
    }
}
