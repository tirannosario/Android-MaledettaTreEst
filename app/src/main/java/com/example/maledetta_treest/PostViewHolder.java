package com.example.maledetta_treest;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import org.json.JSONException;
import org.json.JSONObject;

public class PostViewHolder extends RecyclerView.ViewHolder {
    private int[] POST_DELAY = {R.string.delay_0_string, R.string.delay_1_string, R.string.delay_2_string, R.string.delay_3_string};
    private int[] POST_STATUS = {R.string.status_0_string, R.string.status_1_string, R.string.status_2_string};

    private TextView txtUsername, txtDelay, txtState, txtComment, txtDate;
    private ImageView userpic;
    private Button btnFollowing;
    private MainActivity activity;

    public PostViewHolder(@NonNull View itemView, MainActivity activity) {
        super(itemView);
        txtUsername = itemView.findViewById(R.id.txt_username_post);
        txtDelay = itemView.findViewById(R.id.txtLatePost);
        txtState = itemView.findViewById(R.id.txtStatePost);
        txtComment = itemView.findViewById(R.id.txtCommentPost);
        userpic = itemView.findViewById(R.id.img_userpic_post);
        btnFollowing = itemView.findViewById(R.id.btn_follow);
        txtDate = itemView.findViewById(R.id.txtDatePost);
        this.activity = activity;
    }

    public void updateContent(Post post, AppDatabase db) {
        final String emptyPlaceholder = "//";
        userpic.setImageResource(R.drawable.placeholder_user_pic);

        txtUsername.setText(post.getAuthorName());
        if (post.getDelay() != -1 && post.getDelay() >= 0 && post.getDelay() <= 3)
            txtDelay.setText(activity.getResources().getString(POST_DELAY[post.getDelay()]));
        else
            txtDelay.setText(emptyPlaceholder);
        if (post.getStatus() != -1 && post.getStatus() >= 0 && post.getStatus() <= 2)
            txtState.setText(activity.getResources().getString(POST_STATUS[post.getStatus()]));
        else
            txtState.setText(emptyPlaceholder);
        if (!post.getComment().equals(""))
            txtComment.setText(post.getComment());
        else
            txtComment.setText(emptyPlaceholder);
        String datetime = post.getDatetime();
        txtDate.setText(datetime.replace(datetime.substring(datetime.indexOf(".")), "")); //poichè non serve mostrare all'utente i millisecondi

        //gestione comportamento button follow/unfollow
        InternetCommunication internetCommunication = new InternetCommunication(activity);
        if (post.getFollowingAuthor().equals("true")) {
            btnFollowing.setText(R.string.dont_follow_string);
            btnFollowing.setBackgroundTintList(activity.getResources().getColorStateList(R.color.myOrange));
            btnFollowing.setOnClickListener(view -> {
                Log.d("Debug", "Smetto di seguire il DID " + post.getAuthor());
                internetCommunication.unfollowUser(response -> activity.refreshPostLists(),
                        error -> {
                            Log.d("Debug", error.toString());
                            InternetCommunication.showNetworkError(this.activity, false);
                        },
                        post.getAuthor());
            });
        } else {
            btnFollowing.setText(R.string.follow_string);
            btnFollowing.setOnClickListener(view -> {
                Log.d("Debug", "Inizio a seguire il DID " + post.getAuthor());
                internetCommunication.followUser(response -> activity.refreshPostLists(),
                        error -> {
                            Log.d("Debug", error.toString());
                            InternetCommunication.showNetworkError(this.activity, false);
                        },
                        post.getAuthor());

            });
        }
/*        Nel Model ho una lista aggiornata di User (senza upicture per motivi di spazio),
        che mi servono solo per sapere se ho la pversiona aggiornata senza dover interrogare il DB */
//        final AppDatabase db = Room.databaseBuilder(activity, AppDatabase.class, "db_users").build();
        int myPicVersion = MyModel.getSingleton().getUserPicVersion(Integer.parseInt(post.getAuthor()));
        //non ho lo User nel Model o che ne ho una version vecchia
        if (myPicVersion == -1 || myPicVersion < post.getPversion()) {
            new Thread(() -> {
                User foundUser = db.userDAO().findByUid(Integer.parseInt(post.getAuthor()));
                if (foundUser == null) {
                    db.userDAO().insertUsers(new User(Integer.parseInt(post.getAuthor()), post.getAuthorName(), null, post.getPversion()));
                    this.saveUPicFromInternetAndShow(db, post.getAuthor(), post.getAuthorName());
                }
                else {
                    if (foundUser.getPversion() < post.getPversion()) // ho lo user in DB ma non è aggiornato
                        this.saveUPicFromInternetAndShow(db, post.getAuthor(), post.getAuthorName());
                    else // ho lo user aggiornato nel DB (foundUser)
                        userpic.post(() -> setUserPic(foundUser.getUpicture()));
                }
                // salvo lo User aggiornato anche nel Model (la upicture a null, per non riempire la memoria principale con le immagini -> le prenderemo ogni volta dal DB
                if(myPicVersion==-1)
                    MyModel.getSingleton().addUser(new User(Integer.parseInt(post.getAuthor()), post.getAuthorName(), null, post.getPversion()));
                else
                    MyModel.getSingleton().updateUserPicVersion(Integer.parseInt(post.getAuthor()), post.getPversion());
            }
            ).start();
        }
        else {
            new Thread(()->{
                User foundUser = db.userDAO().findByUid(Integer.parseInt(post.getAuthor()));
                userpic.post(() -> setUserPic(foundUser.getUpicture()));
            }).start();
        }
    }

        // gli viene passato anche il Nome, così da poter aggiornare completamente lo User (visto che la getUserPicture non lo ritorna
        private void saveUPicFromInternetAndShow(AppDatabase db, String userUid, String name){
            InternetCommunication internetCommunication = new InternetCommunication(activity);
            internetCommunication.getUserPicture(
                    response -> {
                        JSONObject jsonObject = (JSONObject) response;
                        try {
                            int uid = jsonObject.getInt("uid");
                            String picture = jsonObject.getString("picture");
                            int pversion = jsonObject.getInt("pversion");
                            new Thread(()-> db.userDAO().updateUsers(new User(uid, name, picture, pversion))).start();
                            setUserPic(picture);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    },
                    error -> Log.d("Debug", error.toString()),
                    userUid
            );
        }

    private void setUserPic(String img){
        try {
            if (!img.equals("null")) {
                Bitmap bitmap = decodeBase64(img);
                if(bitmap != null) // nel caso in cui l'utente abbia una pic codificata male
                    userpic.setImageBitmap(bitmap);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public Bitmap decodeBase64(String base64Text){
//        Log.d("Debug", "Mosto l'immagine " + base64Text);
        byte[] bytes = Base64.decode(base64Text, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap; //TODO gestire errori presenti nel testo base64
    }

}
