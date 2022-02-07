package com.example.maledetta_treest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    public static final String NOT_SQUARE_ERROR = "NOT_SQUARE";
    public static final String TOO_BIG_ERROR = "TOO_BIG";
    private ImageView biopic;
    private String newPic = "";
    private Button btnChangePic;
    private Button btnSave;
    private TextView txtChangeName;
    private final static int CAMERA_PIC_REQUEST = 100;

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
        // salvo o aggiorno lo User nel db
        internetCommunication.getProfile(
                responseProfile -> {
                    User user = User.parseFromJSON((JSONObject)responseProfile);
                    new Thread(()-> {
                            User foundUser = db.userDAO().findByUid(user.getUid());
                            if(foundUser == null)
                                db.userDAO().insertUsers(user);
                            else
                                db.userDAO().updateUsers(user);
                        }
                    ).start();
                    txtChangeName.setHint(user.getUname());
                    if(!user.getUpicture().equals("null")) {
                        Bitmap bitmap = decodeBase64(user.getUpicture());
                        if(bitmap != null) // nel caso l'user abbia una pic codificata male (che comunque impossibile averla usando il caricamento da app)
                            biopic.setImageBitmap(bitmap);
                    }
                },
                error -> {
                    Log.d("Debug", "Error: " + error.toString());
                    InternetCommunication.showNetworkError(this, false);
                }
        );
    }

    public void dismissBtnClicked(View view){
        this.finish();
    }

    public void saveChanges(View view){
        String newName = txtChangeName.getText().toString();
        if(!newName.equals("") || !newPic.equals("")) {
            if(newName.length() <= 20) {
                InternetCommunication internetCommunication = new InternetCommunication(this);
                internetCommunication.setProfile(
                        response -> {
                            Toast toast = Toast.makeText(this, "Modifiche Salvate", Toast.LENGTH_SHORT);
                            toast.show();
                            this.finish();
                        },
                        error -> {
                            Log.d("Debug", "Error: " + error.toString());
                            InternetCommunication.showNetworkError(this, false);
                        },
                        newName,
                        newPic
                );
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Attenzione").setMessage("Inseirire un nome di max 20 caratteri");
                builder.create().show();
            }

        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Attenzione").setMessage("Fornire delle Informazioni");
            builder.create().show();
        }
    }

    public void selectPic(View view){
        // controllo se ho i permessi
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            // richiedo i permessi
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},CAMERA_PIC_REQUEST);
        }
        else
            choosePic();
    }

    // viene richiamato quando dò i permessi
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PIC_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            choosePic();
        else
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
    }

    public void choosePic() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Select Image"),CAMERA_PIC_REQUEST);
    }

    //  viene richiamato quando scegliamo una foto
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_PIC_REQUEST && resultCode == RESULT_OK) {
            String changedPic = toBase64(data); // salvo la nuova img base64
            if(!changedPic.equals("")){
                newPic = changedPic;
                biopic.setImageBitmap(decodeBase64(newPic)); // mostra la nuova img
            }
        }
    }

    public String toBase64(Intent data) {
        String imgString = "";
        Uri uri=data.getData();
        try {
            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            if(bitmap.getWidth() != bitmap.getHeight())
                throw new Exception(NOT_SQUARE_ERROR);
            ByteArrayOutputStream stream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
            byte[] bytes=stream.toByteArray();
            imgString =  Base64.encodeToString(bytes,Base64.DEFAULT);
            if(imgString.length() >= 137000)
                throw new Exception(TOO_BIG_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            if(e.getMessage().equals(NOT_SQUARE_ERROR))
                makeErrorToast("Caricare un Immagine Quadrata");
            else if(e.getMessage().equals(TOO_BIG_ERROR))
                makeErrorToast("Carica un Immagine più piccola di 100KB");
            imgString = "";
        }
        return imgString;
    }

    public Bitmap decodeBase64(String base64Text){
        byte[] bytes = Base64.decode(base64Text, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    private void makeErrorToast(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.show();
    }
}