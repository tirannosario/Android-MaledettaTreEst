package com.example.maledetta_treest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SponsorPage extends AppCompatActivity {
    ImageView sponsorLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_page);

        sponsorLogo = findViewById(R.id.sponsorLogo);
        TextView sponsorName = findViewById(R.id.sponsorName);
        TextView sponsorUrl = findViewById(R.id.sponsorUrl);
        TextView sponsorMessage = findViewById(R.id.sponsorMessage);

        Intent intent = getIntent();
        int index = intent.getIntExtra("index", -1);
        Sponsor sponsor = MyModel.getSingleton().getSponsor(index);
        if(sponsor != null){
            Log.d("Debug", "esamefebbraio: tap su pin: " + sponsor.getNome() + " con messaggio pubblicitario: " + sponsor.getMessage());
            sponsorName.setText(sponsor.getNome());
            sponsorUrl.setText(sponsor.getUrl());
            sponsorMessage.setText(sponsor.getMessage());
            setUserPic(sponsor.getLogo());
        }
    }

    private void setUserPic(String img){
        try {
            if (!img.equals("null")) {
                Bitmap bitmap = decodeBase64(img);
                if(bitmap != null) // nel caso in cui lo sponsor non abbia una pic codificata male
                    sponsorLogo.setImageBitmap(bitmap);
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
        return bitmap;
    }
}