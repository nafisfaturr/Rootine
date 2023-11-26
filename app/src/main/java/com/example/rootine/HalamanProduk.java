package com.example.rootine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HalamanProduk extends AppCompatActivity {
    TextView languange, nutrisi, hama;
    String lang, nut, ham, urlEndpoint;
    ViewPager2 viewPager2;
    private Handler slidehandler = new Handler(); //Autoslidenya
    Context context = this;
    private List<Produk> produkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_produk);

        viewPager2 = findViewById(R.id.viewPager);

        List<SlideItem> sliderItem = new ArrayList<>();
        sliderItem.add(new SlideItem((R.drawable.slide1)));
        sliderItem.add(new SlideItem((R.drawable.slide2)));
        sliderItem.add(new SlideItem((R.drawable.slide3)));
        sliderItem.add(new SlideItem((R.drawable.slide4)));
        sliderItem.add(new SlideItem((R.drawable.slide5)));
        sliderItem.add(new SlideItem((R.drawable.slide6)));

        viewPager2.setAdapter(new SlideAdapter(sliderItem, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(5);

        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);

        CompositePageTransformer compositionTransform = new CompositePageTransformer();
        compositionTransform.addTransformer(new MarginPageTransformer(0));
        compositionTransform.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });
        viewPager2.setPageTransformer(compositionTransform);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slidehandler.removeCallbacks(sliderRunnable);
                slidehandler.postDelayed(sliderRunnable,3500);
            }
        });

        languange = findViewById(R.id.lang_produk);
        lang = languange.getText().toString();

        nutrisi = findViewById(R.id.lang_kategori_nutrisi);
        hama = findViewById(R.id.lang_kategori_hama);
        nut = nutrisi.getText().toString();
        ham = hama.getText().toString();
        getAllProduk(lang);
    }
    private void getAllProduk(String lang) {
        if (lang.equals("Id")){
            urlEndpoint = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-rjiyx/endpoint/getallrootineproduct";
        }else if (lang.equals("Eng")){
            urlEndpoint = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-rjiyx/endpoint/getProdukEnglish";
        }else {
            urlEndpoint = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-rjiyx/endpoint/getProdukJapan";
        }
        Log.d("endpoint", "HalamanProduk: "+urlEndpoint);
        StringRequest sr = new StringRequest(Request.Method.GET, urlEndpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responsse", "onResponse: "+response);
                populateData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Produkt", "onErrorResponse: "+ error);
                Toast.makeText(HalamanProduk.this, "Gagal Memuat Data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String utf8String = null;
                try {
                    utf8String = new String(response.data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return Response.success(utf8String, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(sr);
    }

    private void populateData(String response) {
        try{
            JSONArray jsonArray = new JSONArray(response);
            produkList = new ArrayList<>();

            for (int i = 0; i< jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String namaProduk =jsonObject.getString("nama");
                String descProduk =jsonObject.getString("desc_s");
                String descDetailProduk =jsonObject.getString("Desc");
                String hargaProduk =jsonObject.getString("Harga");
                String imgProduk =jsonObject.getString("imgID");
                String detailImg =jsonObject.getString("d_img");
                String stokProduk =jsonObject.getString("stock");
                String beratProduk =jsonObject.getString("mass");
                String kategoriProduk =jsonObject.getString("kategori");
                String id = jsonObject.getString("_id");

                Log.d("DESK", "populateData: "+ kategoriProduk);
                int imgID = context.getResources().getIdentifier(imgProduk, "drawable", context.getPackageName());
//                int detailIMG = context.getResources().getIdentifier(detailImg,"drawable",context.getPackageName());

                Produk produk = new Produk(id,namaProduk, descProduk, descDetailProduk, hargaProduk, stokProduk, beratProduk,kategoriProduk, detailImg, imgID);
                produkList.add(produk);
            }
            updateUI();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updateUI() {
        List<Produk> nutrisiList = filterByKategori(nut);
        List<Produk> hamaList = filterByKategori(ham);

        GridView katalogNutrisiGridView = findViewById(R.id.katalognutrisi);
        GridView katalogHamaGridView = findViewById(R.id.kataloghama);

        ProdukAdapter nutrisiAdapter = new ProdukAdapter(this, nutrisiList);
        ProdukAdapter hamaAdapter = new ProdukAdapter(this, hamaList);

        katalogNutrisiGridView.setAdapter(nutrisiAdapter);
        katalogHamaGridView.setAdapter(hamaAdapter);

        katalogNutrisiGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Produk selectedProduk = nutrisiList.get(position);
                Log.d("position", "onItemClick: " + selectedProduk);
                goToDetailPage(selectedProduk);
            }
        });

        katalogHamaGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Produk selectedProduk = hamaList.get(position);
                Log.d("position", "onItemClick: " + selectedProduk);
                goToDetailPage(selectedProduk);
            }
        });
    }

    private void goToDetailPage(Produk produk) {
        UserModel userModel = (UserModel) getIntent().getSerializableExtra("userModel");
        Intent intent = new Intent(this, DetailProduk.class);
        intent.putExtra("PRODUK_ID", produk.getId());
        intent.putExtra("GAMBAR_ID", produk.getDetail_img());
        intent.putExtra("HARGA", produk.getHrg_produk());
        intent.putExtra("NAMA",produk.getNm_produk());
        intent.putExtra("BERAT", produk.getBrt_produk());
        intent.putExtra("STOK", produk.getStk_produk());
        intent.putExtra("KATEGORI",produk.getKtg_produk());
        intent.putExtra("DESKRIPSI", produk.getDetail_produk());
        intent.putExtra("userModel", userModel);
        startActivity(intent);
    }

    private List<Produk> filterByKategori(String kategori) {
        List<Produk> filteredList = new ArrayList<>();
        for (Produk produk : produkList) {
            if (produk.getKtg_produk().equalsIgnoreCase(kategori)) {
                filteredList.add(produk);
            }
        }
        return filteredList;
    }
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

    public void goToKeranjang(View view){
        UserModel userModel = (UserModel) getIntent().getSerializableExtra("userModel");
        Intent i = new Intent(HalamanProduk.this, Keranjang.class);
        i.putExtra("userModel", userModel);
        startActivity(i);
    }
    public void aksiKlik(View view){
        UserModel userModel = (UserModel) getIntent().getSerializableExtra("userModel");
        Intent i = new Intent(HalamanProduk.this, Rootbot.class);
        i.putExtra("userModel", userModel);
        startActivity(i);
    }
    public void backTo(View view){
        UserModel userModel = (UserModel) getIntent().getSerializableExtra("userModel");
        Intent i = new Intent(HalamanProduk.this, Beranda.class);
        i.putExtra("userModel", userModel);
        startActivity(i);
    }
}