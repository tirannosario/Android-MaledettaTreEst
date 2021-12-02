package com.example.maledetta_treest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAllAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private LayoutInflater inflater;
    private MainActivity mainActivity;

    public PostAllAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.inflater = LayoutInflater.from(this.mainActivity);
    }

    //TODO da IMPLEMENTARE
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
