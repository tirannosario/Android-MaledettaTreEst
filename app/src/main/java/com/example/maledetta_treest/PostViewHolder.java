package com.example.maledetta_treest;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostViewHolder extends RecyclerView.ViewHolder {
    private int[] POST_DELAY = {R.string.delay_0_string, R.string.delay_1_string, R.string.delay_2_string, R.string.delay_3_string};
    private int[] POST_STATUS = {R.string.status_0_string, R.string.status_1_string, R.string.status_2_string};

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
        final String emptyPlaceholder = "//";

        txtUsername.setText(post.getAuthorName());
        if(post.getDelay()!=-1 && post.getDelay() >= 0 && post.getDelay() <= 3)
            txtDelay.setText(txtDelay.getResources().getString(POST_DELAY[post.getDelay()]));
        else
            txtDelay.setText(emptyPlaceholder);
        if(post.getStatus()!=-1 && post.getStatus() >= 0 && post.getStatus() <= 2)
            txtState.setText(txtState.getResources().getString(POST_STATUS[post.getStatus()]));
        else
            txtState.setText(emptyPlaceholder);
        if(!post.getComment().equals(""))
            txtComment.setText(post.getComment());
        else
            txtComment.setText(emptyPlaceholder);
        String datetime = post.getDatetime();
        txtDate.setText(datetime.replace(datetime.substring(datetime.indexOf(".")), "")); //poichè non serve mostrare all'utente i millisecondi
        if(post.getFollowingAuthor().equals("true")) {
            btnFollowing.setText(R.string.dont_follow_string);
            btnFollowing.setBackgroundTintList(btnFollowing.getResources().getColorStateList(R.color.myOrange));
        }
        else
            btnFollowing.setText(R.string.follow_string);
        //TODO aggiungere img
    }



}
