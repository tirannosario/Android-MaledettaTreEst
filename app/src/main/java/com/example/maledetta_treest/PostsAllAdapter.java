package com.example.maledetta_treest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostsAllAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private LayoutInflater inflater;
    private MainActivity mainActivity;

    public PostsAllAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.inflater = LayoutInflater.from(this.mainActivity);
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_post, parent, false);
        return new PostViewHolder(view, this.mainActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = MyModel.getSingleton().getAllPost(position);
        holder.updateContent(post);
    }

    @Override
    public int getItemCount() {
        return MyModel.getSingleton().getAllPostsSize();
    }
}
