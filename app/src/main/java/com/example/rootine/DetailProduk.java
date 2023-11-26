package com.example.rootine;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DetailProduk extends AppCompatActivity {
    Context context = this;
    Button addPesanan;
    TextView harga,nama,berat,stok,kategori,deskripsi,getimg;
    ImageView imgDetail;
    String n,h,g,b,un,al,st,jm;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        //data user
        userModel = (UserModel) getIntent().getSerializableExtra("userModel");

        Intent intent = getIntent();
        String orderId = intent.getStringExtra("PRODUK_ID");
        String detailIMG = intent.getStringExtra("GAMBAR_ID");
        double detailHarga = Double.parseDouble(intent.getStringExtra("HARGA"));
        String detailNama = intent.getStringExtra("NAMA");
        double detailBerat = Double.parseDouble(intent.getStringExtra("BERAT"));
        String detailStok = intent.getStringExtra("STOK");
        String detailKategori = intent.getStringExtra("KATEGORI");
        String detailDesc = intent.getStringExtra("DESKRIPSI");

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.applyPattern("###,###");
        String hargaFormatted = decimalFormat.format(detailHarga);

        double beratToKG = detailBerat / 1000;
        DecimalFormat decimalFormat2 = new DecimalFormat("#.#");
        String formattedBerat = decimalFormat2.format(beratToKG);

        int resImg = context.getResources().getIdentifier(detailIMG,"drawable",context.getPackageName());

        imgDetail = findViewById(R.id.detail_img);
        harga = findViewById(R.id.detail_harga);
        nama = findViewById(R.id.detail_nama);
        berat = findViewById(R.id.detail_brt);
        stok = findViewById(R.id.detail_stok);
        kategori = findViewById(R.id.detail_ktgr);
        deskripsi = findViewById(R.id.detail_desc);
        getimg = findViewById(R.id.addimg);

        imgDetail.setImageResource(resImg);
        harga.setText("Rp"+hargaFormatted);
        nama.setText(detailNama);
        berat.setText(formattedBerat+"kg");
        stok.setText(detailStok);
        if (detailKategori.equals("Hama")){
            kategori.setText("Pembasmi "+detailKategori);
        } else if (detailKategori.equals("Nutrition")) {
            kategori.setText("Plant "+detailKategori);
        } else if (detailKategori.equals("Pesticide")) {
            kategori.setText(detailKategori);
        } else if (detailKategori.equals("Nutrisi")){
            kategori.setText(detailKategori + " Tanaman");
        } else {
            kategori.setText(detailKategori);
        };
        deskripsi.setText(detailDesc);
        getimg.setText(detailIMG);

        n = nama.getText().toString();
        h = harga.getText().toString();
        g = detailIMG;
        b = berat.getText().toString();
        un = userModel.getUsername();
        jm = "1";
        al = userModel.getAlamat();
        st = "dikeranjang";

        addPesanan = findViewById(R.id.btn_tambah);
        addPesanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel userModel = (UserModel) getIntent().getSerializableExtra("userModel");
                tambahPesanan();
                Intent intent = new Intent(DetailProduk.this, HalamanProduk.class);
                intent.putExtra("userModel", userModel);
                startActivity(intent);
            }
        });
    }

    public void tambahPesanan() {
        String urlEndpoint = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-rjiyx/endpoint/tambahPesanan";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlEndpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(DetailProduk.this, "Pesanan Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailProduk.this, "Gagal Membuat Pesanan", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                return super.getParams();
                Map map = new HashMap();
                map.put("username",un);
                map.put("gambar",g);
                map.put("nama",n);
                map.put("varian",b);
                map.put("harga",h);
                map.put("jumlah",jm);
                map.put("alamat",al);
                map.put("status",st);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void backToHalaman(View view){
        UserModel userModel = (UserModel) getIntent().getSerializableExtra("userModel");
        Intent i = new Intent(DetailProduk.this, HalamanProduk.class);
        i.putExtra("userModel", userModel);
        startActivity(i);
    }
}