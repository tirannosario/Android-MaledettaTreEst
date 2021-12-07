package com.example.maledetta_treest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    private ImageView biopic;
    private Button btnChangePic;
    private Button btnSave;
    private TextView txtChangeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        biopic = findViewById(R.id.biopic);
        btnChangePic = findViewById(R.id.btn_change_pic);
        btnSave = findViewById(R.id.btn_save);
        txtChangeName = findViewById(R.id.txt_change_name);

        //richiedo e mostro il mio profilo
        InternetCommunication internetCommunication = new InternetCommunication(this);
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "db_users").build();
/*        internetCommunication.getProfile(
                responseProfile -> new Thread(()->{db.userDAO().insertUsers(User.parseFromJSON((JSONObject) responseProfile));}).start(),
                error -> Log.d("Debug", "Error: " + error.toString())
        );*/
        internetCommunication.getProfile(
                responseProfile -> {
                    User user = User.parseFromJSON((JSONObject)responseProfile);
                    new Thread(()-> {
                            User foundUser = db.userDAO().findByUid(user.getUid());
                            if(foundUser == null)
                                db.userDAO().insertUsers(user);
                            else
                                Log.d("Debug", "preso quello salvato: " + foundUser.toString());
                        }
                    ).start();
                    txtChangeName.setHint(user.getUname());
                    biopic.setImageBitmap(decodeBase64(user.getUpicture()));
                },
                error -> Log.d("Debug", "Error: " + error.toString())
        );
    }

    public void dismissBtnClicked(View view){
        this.finish();
    }

    public Bitmap decodeBase64(String base64Text){
        byte[] bytes = Base64.decode(base64Text, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap; //TODO gestire errori presenti nel testo base64
    }
}