package com.example.rootine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class Cuaca extends AppCompatActivity {
    EditText etCity;
    TextView tvsuhu,tvkelembaban,tvawan,tvangin,tvtips,tvnamakota, tvfeelslike, tvtekanan, tvdesc, tvmaksmin, languange;
    ImageView ivtanaman,ivtanaman1,ivtanaman2,ivtanaman3, backToHome, btnSearch;
    String Lang;
    private CuacaModel cuacaModel;
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "88878a287df2d36415c6c17943c03679";
    private final String lang = "id";
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityName;
    private String temperatur;
    DecimalFormat df = new DecimalFormat("#");
    private ImageView btnGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuaca);

        etCity = findViewById(R.id.etCity);
        tvnamakota = findViewById(R.id.tvNamaKota);
        tvsuhu = findViewById(R.id.tvsuhu);
        tvkelembaban = findViewById(R.id.tvkelembaban);
        tvawan = findViewById(R.id.tvawan);
        tvangin = findViewById(R.id.tvangin);
        tvtips = findViewById(R.id.tvtips);
        tvfeelslike = findViewById(R.id.tvfeelslike);
        tvtekanan = findViewById(R.id.tvtekanan);
        tvdesc = findViewById(R.id.tvdesc);
        tvmaksmin = findViewById(R.id.tvmaksmin);
        ivtanaman1 = findViewById(R.id.ivtanaman1);
        ivtanaman2 = findViewById(R.id.ivtanaman2);
        ivtanaman = findViewById(R.id.ivtanaman);
        ivtanaman3 = findViewById(R.id.ivtanaman3);
        btnGet = findViewById(R.id.btnGet);
        backToHome = findViewById(R.id.backToHome);
        btnSearch = findViewById(R.id.btn_search);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Cuaca.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_CODE);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        cityName = getCityName(location.getLongitude(),location.getLatitude());
        getWeatherDetails(cityName);

        languange = findViewById(R.id.lang);
        Lang = languange.getText().toString();

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = etCity.getText().toString();
                if (city.isEmpty()){
                    Toast.makeText(Cuaca.this,"Nama Kota Harus Diisi", Toast.LENGTH_SHORT).show();
                }else {
                    tvnamakota.setText(cityName);
                    getWeatherDetails(city);
                }
            }
        });

        ImageView btnSearch = findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etCity.setVisibility(etCity.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                btnGet.setVisibility(btnGet.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                btnSearch.setVisibility(View.GONE);
                backToHome.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Please provide the permissions", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getCityName(double longitude, double latitude){
        String cityName = "Note Found";
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 10);
            for (Address adr : addresses) {
                String city = adr.getLocality();
                if(city != null && !city.equals("")){
                    cityName = city;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

    private void getWeatherDetails(String cityName) {
        String tempUrl = url + "?q=" + cityName + "&lang=" + lang + "&appid=" + appid;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String temper = "";
                String temper2 = "";
                String lembab = "";
                String berawan = "";
                String berangin = "";
                String tekanan = "";
                String deskripsi = "";
                String tips = "";
                String temper_min = "";
                String temper_max = "";
                int gamtanaman;
                int gamtanaman1;
                int gamtanaman2;
                int gamtanaman3;
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String main = jsonObjectWeather.getString("main");
                    String iconCode = jsonObjectWeather.getString("icon");
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                    double temp_min = jsonObjectMain.getDouble("temp_min") - 273.15;
                    double temp_max = jsonObjectMain.getDouble("temp_max") - 273.15;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");
                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");
                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");
                    String cityName = jsonResponse.getString("name");

                    tvnamakota.setText(cityName);

                    Log.d("Cuaca Model", "onResponse: "+cuacaModel);

                    temperatur = String.valueOf(temp);

                    temper = df.format(temp);
                    int temperInteger = (int) Math.round(temp);
                    tvsuhu.setText(temper);

                    temper2 = df.format(feelsLike);
                    int temper2Integer = (int) Math.round(feelsLike);
                    tvfeelslike.setText(temper2 + "°");

                    temper_min = df.format(temp_min);
                    int temper_minInteger = (int) Math.round(temp_min);

                    temper_max = df.format(temp_max);
                    int temper_maxInteger = (int) Math.round(temp_max);
                    tvmaksmin.setText(temper_min + "°/" + temper_max +"°");

                    lembab = humidity + "%";
                    tvkelembaban.setText(lembab);

                    berawan = clouds + "%";
                    tvawan.setText(berawan);

                    berangin = wind + "m/s";
                    tvangin.setText(berangin);

                    tekanan = df.format(pressure);
                    int presureInteger = (int) Math.round(pressure);
                    tvtekanan.setText(tekanan + "mbar");

                    deskripsi = main;
                    tvdesc.setText(deskripsi);

                    if (temperInteger < 10) {
                        gamtanaman1 = R.drawable.contoh_bputih;
                        gamtanaman2 = R.drawable.contoh_brokoli;
                        gamtanaman = R.drawable.contoh_bmerah;
                        gamtanaman3 = R.drawable.contoh_wortel;
                        if (Lang.equals("Id")){
                            tips = "1. Pastikan tanaman mendapatkan cukup cahaya matahari, bahkan jika suhu rendah. \n\n2. Pindahkan tanaman ke dalam ruangan jika suhu terlalu dingin atau gunakan penutup tanaman jika memungkinkan. \n\n3. Kurangi frekuensi penyiraman karena tanaman cenderung membutuhkan air lebih sedikit dalam suhu rendah.";
                        } else if (Lang.equals("Eng")) {
                            tips = "1. Ensure the plants receive enough sunlight, even in cold temperatures.\n\n2. Move the plants indoors if it's too cold or use plant covers if possible.\n\n3. Reduce watering frequency as plants tend to need less water in low temperatures.";
                        } else {
                            tips = "1. 植物が十分な日光を受けるようにしてください、寒い温度でも。\n\n2. 温度が低すぎる場合は、植物を室内に移動するか、可能であれば植物カバーを使用してください。\n\n3. 寒い温度では植物は水を少なく必要とする傾向があるため、散水頻度を減らしてください。";
                        }
                    } else if (temperInteger >= 10 && temperInteger < 20) {
                        gamtanaman1 = R.drawable.contoh_cabai;
                        gamtanaman2 = R.drawable.contoh_kacanghijau;
                        gamtanaman = R.drawable.contoh_jahe;
                        gamtanaman3 = R.drawable.contoh_pokcoy;
                        if (Lang.equals("Id")){
                            tips = "1. Pastikan tanaman tetap terkena cahaya matahari yang cukup. \n\n2. Lakukan penyiraman secara teratur, tetapi biarkan tanah mengering di antara penyiraman. \n\n3. Pertimbangkan untuk memberikan nutrisi tambahan dengan pupuk yang sesuai.";
                        } else if (Lang.equals("Eng")) {
                            tips = "1. Ensure the plants get enough light, but avoid direct sunlight that can burn leaves.\n\n2. Water regularly and consider spraying the leaves to maintain humidity.\n\n3. Provide periodic fertilization according to the type of plant and season.";
                        } else {
                            tips = "1. 植物が引き続き十分な日光を受けるようにしてください。\n\n2. 定期的に水を与えますが、散水の間に土が乾燥するのを許してください。\n\n3. 適切な肥料で追加の栄養を提供することを検討してください。";
                        }
                    } else if (temperInteger >= 20 && temperInteger < 30) {
                        gamtanaman1 = R.drawable.contoh_tomat;
                        gamtanaman2 = R.drawable.contoh_kangkung;
                        gamtanaman = R.drawable.contoh_ubikayu;
                        gamtanaman3 = R.drawable.contoh_kentang;
                        if (Lang.equals("Id")){
                            tips = "1. Pastikan tanaman mendapatkan cahaya yang cukup, tetapi hindari sinar matahari langsung yang bisa membakar daun. \n\n2. Lakukan penyiraman secara teratur dan pertimbangkan untuk menyemprotkan daun untuk menjaga kelembaban. \n\n3. Berikan pupuk secara berkala sesuai dengan jenis tanaman dan musim.";
                        } else if (Lang.equals("Eng")) {
                            tips = "1. Ensure the plants get enough light, but avoid direct sunlight that can burn leaves.\n\n2. Water regularly and consider spraying the leaves to maintain humidity.\n\n3. Provide periodic fertilization according to the type of plant and season.";
                        } else {
                            tips = "1. 植物が十分な光を受けるようにしてくださいが、葉が焼ける可能性のある直射日光を避けてください。\n\n2. 定期的に水をやり、湿度を維持するために葉に霧吹きをすることを検討してください。\n\n3. 植物の種類と季節に応じて定期的に肥料を与えてください。";
                        }

                    } else {
                        gamtanaman1 = R.drawable.contoh_gandum;
                        gamtanaman2 = R.drawable.contoh_paprika;
                        gamtanaman = R.drawable.contoh_jagung;
                        gamtanaman3 = R.drawable.contoh_padi;
                        if (Lang.equals("Id")){
                            tips = "1. Lindungi tanaman dari sinar matahari langsung selama periode panas yang ekstrem. \n\n2. Pastikan penyiraman cukup untuk menjaga tanah tetap lembab, tetapi hindari overwatering. \n\n3. Jika memungkinkan, gunakan penutup bayangan atau tirai peneduh untuk mengurangi suhu di sekitar tanaman.";
                        } else if (Lang.equals("Eng")) {
                            tips = "1. Ensure the plants get enough light, but avoid direct sunlight that can burn leaves.\n\n2. Water regularly and consider spraying the leaves to maintain humidity.\n\n3. Provide periodic fertilization according to the type of plant and season.";
                        } else {
                            tips = "1. 植物を極端な暑さの期間から直射日光から保護してください。\n\n2. 土を湿らせたままにするために十分な散水を確保してくださいが、過敏な散水は避けてください。\n\n3. 可能であれば、遮光カバーやカーテンを使用して植物周りの温度を下げてください。";
                        }
                    }

                    tvtips.setText(tips);
                    ivtanaman.setImageResource(gamtanaman);
                    ivtanaman1.setImageResource(gamtanaman1);
                    ivtanaman2.setImageResource(gamtanaman2);
                    ivtanaman3.setImageResource(gamtanaman3);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

        etCity.setVisibility(View.GONE);
        btnGet.setVisibility(View.GONE);
        btnSearch.setVisibility(View.VISIBLE);
        backToHome.setVisibility(View.VISIBLE);

    }
    public void goToHome(View view){
        UserModel userModel = (UserModel) getIntent().getSerializableExtra("userModel");
        Intent i = new Intent(this, Beranda.class);
        i.putExtra("userModel", userModel);
        startActivity(i);
    }
}