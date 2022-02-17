package com.example.maledetta_treest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LinesAdapter extends RecyclerView.Adapter<LineViewHolder> {
    private LayoutInflater inflater;
    private ShowLines showLinesActivity;

    public LinesAdapter(ShowLines showLinesActivity) {
        this.showLinesActivity = showLinesActivity;
        inflater = LayoutInflater.from(this.showLinesActivity);
    }

    @NonNull
    @Override
    public LineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_line, parent, false);
        return new LineViewHolder(view, this.showLinesActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull LineViewHolder holder, int position) {
        Line line = MyModel.getSingleton().getLine(position);
        holder.updateContent(line);
    }


    @Override
    public int getItemCount() {
        return MyModel.getSingleton().getLinesSize();
    }
}
