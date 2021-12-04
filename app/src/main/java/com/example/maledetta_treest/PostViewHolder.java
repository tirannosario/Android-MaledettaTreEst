package com.example.maledetta_treest;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostViewHolder extends RecyclerView.ViewHolder {
    private TextView txtUsername, txtDelay, txtState, txtComment, txtDate;
    private ImageView userpic;
    private Button btnFollowing;

    public PostViewHolder(@NonNull View itemView, MainActivity activity) {
        super(itemView);
        txtUsername = itemView.findViewById(R.id.txt_username_post);
        txtDelay = itemView.findViewById(R.id.txtLatePost);
        txtState = itemView.findViewById(R.id.txtStatePost);
        txtComment = itemView.findViewById(R.id.txtCommentPost);
        userpic = itemView.findViewById(R.id.img_userpic_post);
        btnFollowing = itemView.findViewById(R.id.btn_follow);
        txtDate = itemView.findViewById(R.id.txtDatePost);
    }

    public void updateContent(Post post){
        txtUsername.setText(post.getAuthorName());
        txtDelay.setText(String.valueOf(post.getDelay()));
        txtState.setText(String.valueOf(post.getStatus()));
        txtComment.setText(post.getComment());
        txtDate.setText(post.getDatetime());
        //TODO aggiungere img e situazione btn
    }



}
