package com.example.rootine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class Beranda extends AppCompatActivity {
    TextView languange;
    String lang;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_beranda);

        //maps
        Fragment fragment = new Map_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mapView,fragment).commit();

        //data user
        userModel = (UserModel) getIntent().getSerializableExtra("userModel");


            //bahasa
        languange = findViewById(R.id.lang);
        lang = languange.getText().toString();

        if (userModel != null){
            String username = userModel.getUsername();
            TextView greeting = findViewById(R.id.greeting);
            if (lang.equals("言語")) {
                greeting.setText("こんにちは, " + username);
//                greeting.setText(Html.);
            }else {
                greeting.setText("Hallo " + username +"!");
            };
        }
    }
    private void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //Simpen data pilihannya ke SharedPreference
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }
    //Load pilihan bahasanya
    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        setLocale(language);
    }
    public void aksiKlik(View view){
        Intent i;

        if (view.getId() == R.id.cuaca) {
            i = new Intent(this, Cuaca.class);
        } else if (view.getId() == R.id.toKategori) {
            i = new Intent(this, Kategori.class);
        } else if (view.getId() == R.id.profile) {
            i = new Intent(this, Profile.class);
        } else if (view.getId() == R.id.produk) {
            i = new Intent(this, HalamanProduk.class);
        } else if (view.getId() == R.id.home) {
            i = new Intent(this, Beranda.class);
        } else if (view.getId() == R.id.rootbot || view.getId() == R.id.diskusi) {
            i = new Intent(this, Rootbot.class);
        } else {
            return;
        }

        // Menambahkan userModel ke Intent
        if (userModel != null) {
            i.putExtra("userModel", userModel);
        }
        startActivity(i);
    }}