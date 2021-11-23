package com.example.maledetta_treest;

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

    public LineViewHolder(@NonNull View itemView, ShowLines showLinesActivity) {
        super(itemView);
        this.showLinesActivity = showLinesActivity;
        txtLine = itemView.findViewById(R.id.txtLine);
        btnDirection1 = itemView.findViewById(R.id.btnDirection1);
        btnDirection2 = itemView.findViewById(R.id.btnDirection2);
    }

    public void updateContent(Line line){
        txtLine.setText(line.getName());
        btnDirection1.setText(line.getDirection1());
        btnDirection2.setText(line.getDirection2());
    }
}
