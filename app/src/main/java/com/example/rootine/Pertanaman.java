package com.example.rootine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Pertanaman extends AppCompatActivity {
    Context context = this;
    TextView nama, nm_latin,desc, tips, hama, penyakit;
    ImageView imageTanaman, imageKesulitan;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pertanaman);

        //data user
        userModel = (UserModel) getIntent().getSerializableExtra("userModel");

        Intent intent = getIntent();
        String nm_tanaman = intent.getStringExtra("TANAMAN_NM");
        String nama_latin = intent.getStringExtra("TANAMAN_LATIN");
        String tkesulitan = intent.getStringExtra("KESULITAN");
        String deskripsi = intent.getStringExtra("DESKRIPSI");
        String tips_tanaman = intent.getStringExtra("TIPS");
        String hama_tanaman = intent.getStringExtra("HAMA");
        String penyakit_tanaman = intent.getStringExtra("PENYAKIT");
        String dataImage= intent.getStringExtra("IMAGE");

        int resImg = context.getResources().getIdentifier(dataImage,"drawable",context.getPackageName());
        nama = findViewById(R.id.nm_tanaman);
        nm_latin = findViewById(R.id.latin_tanaman);
        imageKesulitan = findViewById(R.id.kesulitan);
        imageTanaman = findViewById(R.id.dImg_tanaman);
        desc = findViewById(R.id.desc_tanaman);
        tips = findViewById(R.id.tips);
        hama = findViewById(R.id.hama);
        penyakit = findViewById(R.id.penyakit);

        //mengubah data
        nama.setText(nm_tanaman);
        nm_latin.setText(nama_latin);
        imageTanaman.setImageResource(resImg);
        desc.setText(deskripsi);
        tips.setText(tips_tanaman);
        hama.setText(hama_tanaman);
        penyakit.setText(penyakit_tanaman);

        if (tkesulitan.equals("satu")||tkesulitan.equals("one")||tkesulitan.equals("一")){
            imageKesulitan.setImageResource(R.drawable.tingkat1);
        } else if (tkesulitan.equals("dua")||tkesulitan.equals("two")||tkesulitan.equals("二")) {
            imageKesulitan.setImageResource(R.drawable.tingkat2);
        } else if (tkesulitan.equals("tiga")||tkesulitan.equals("three")||tkesulitan.equals("三つ")) {
            imageKesulitan.setImageResource(R.drawable.tingkat3);
        }
    }
    public void goToProduk(View view){
        Intent i = new Intent(this, HalamanProduk.class);
        i.putExtra("userModel", userModel);
        startActivity(i);
    }
    public void backToTanaman(View view){
        Intent i = new Intent(this, Kategori.class);
        i.putExtra("userModel", userModel);
        startActivity(i);
    }
}