package com.example.rootine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Kategori extends AppCompatActivity {
    TextView languange;
    String lang, urlEndpoint;
    Context context = this;
    private List<Tanaman> tanamanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);

        languange = findViewById(R.id.lang);
        lang = languange.getText().toString();
        getAllTanaman(lang);
    }

    private void getAllTanaman(String lang) {
        if (lang.equals("Id")) {
            urlEndpoint = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-rjiyx/endpoint/getAllTanaman";
        }else if (lang.equals("Eng")) {
            urlEndpoint = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-rjiyx/endpoint/getTanamanEnglish";
        }else {
            urlEndpoint = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-rjiyx/endpoint/getTanamanJapan";
        }
        Log.d("endpoint", "getAllTanaman: "+urlEndpoint);
        StringRequest sr = new StringRequest(Request.Method.GET, urlEndpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responsTanaman", "onResponse: "+response);
                populateData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
            tanamanList = new ArrayList<>();

            for (int i = 0; i< jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String namaTanaman = jsonObject.getString("nama_tanaman");
                String latinTanaman= jsonObject.getString("latin");
                String kesulitanTmn= jsonObject.getString("kesulitan");
                String descTanaman = jsonObject.getString("deskripsi");
                String tipsTanaman = jsonObject.getString("tips");
                String hamaTanaman = jsonObject.getString("hama");
                String penyakitTmn = jsonObject.getString("penyakit");
                String kategoriTmn = jsonObject.getString("kategori");
                String detailImage = jsonObject.getString("d_img");
                String imageTanaman= jsonObject.getString("imgID");
                String idTanaman   = jsonObject.getString("_id");

                int resId = context.getResources().getIdentifier(imageTanaman,"drawable", context.getPackageName());

                Tanaman tanaman = new Tanaman(idTanaman, namaTanaman, latinTanaman, kesulitanTmn, descTanaman, tipsTanaman, hamaTanaman, penyakitTmn, kategoriTmn, detailImage, resId);
                tanamanList.add(tanaman);
            }
            updateUI();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updateUI() {
        List<Tanaman> keringList = filterByKategori("xerofit");
        List<Tanaman> lembabList = filterByKategori("higrofit");
        List<Tanaman> basahList = filterByKategori("hydrofit");

        GridView kategoriKeringGrid = findViewById(R.id.katalogkering);
        GridView kategoriLembabGrid = findViewById(R.id.kataloglembab);
        GridView kategoriBasahGrid  = findViewById(R.id.katalogbasah);

        TanamanAdapter keringAdapter = new TanamanAdapter(this,keringList);
        TanamanAdapter lembabAdapter = new TanamanAdapter(this,lembabList);
        TanamanAdapter basahAdapter  = new TanamanAdapter(this,basahList);

        kategoriKeringGrid.setAdapter(keringAdapter);
        kategoriLembabGrid.setAdapter(lembabAdapter);
        kategoriBasahGrid.setAdapter(basahAdapter);

        kategoriKeringGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Tanaman selectedTanaman = keringList.get(position);
                goToDetailPage(selectedTanaman);
            }
        });

        kategoriLembabGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Tanaman selectedTanaman = lembabList.get(position);
                goToDetailPage(selectedTanaman);
            }
        });

        kategoriBasahGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Tanaman selectedTanaman = basahList.get(position);
                goToDetailPage(selectedTanaman);
            }
        });

    }

    private List<Tanaman> filterByKategori(String kategori) {
        List<Tanaman> filteredList = new ArrayList<>();
        for (Tanaman tanaman : tanamanList){
            if (tanaman.getKategori().equalsIgnoreCase(kategori)){
                filteredList.add(tanaman);
            }
        }
        return filteredList;
    }

    private void goToDetailPage(Tanaman tanaman) {
        UserModel userModel = (UserModel) getIntent().getSerializableExtra("userModel");
        Intent intent = new Intent(this, Pertanaman.class);
        intent.putExtra("TANAMAN_ID", tanaman.getId());
        intent.putExtra("TANAMAN_NM", tanaman.getNama());
        intent.putExtra("TANAMAN_LATIN", tanaman.getLatin());
        intent.putExtra("KESULITAN", tanaman.getKesulitan());
        intent.putExtra("DESKRIPSI", tanaman.getDeskripsi());
        intent.putExtra("TIPS", tanaman.getTips());
        intent.putExtra("HAMA", tanaman.getHama());
        intent.putExtra("PENYAKIT", tanaman.getPenyakit());
        intent.putExtra("IMAGE", tanaman.getD_img());
        intent.putExtra("userModel", userModel);
        startActivity(intent);
    }


    public void backToHome(View view){
        UserModel userModel = (UserModel) getIntent().getSerializableExtra("userModel");
        Intent i = new Intent(this, Beranda.class);
        i.putExtra("userModel", userModel);
        startActivity(i);
    }
}