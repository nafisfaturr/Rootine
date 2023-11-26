package com.example.rootine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Keranjang extends AppCompatActivity implements ListAdapter.ListAdapterListener{
    ListView lv;
    Context context = this;
    private EditText jumlahPesananDialog;
    private EditText alamatUserDialog;
    private JSONArray jsonArrayRes;
    private String username, sPesanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        UserModel userModel = (UserModel) getIntent().getSerializableExtra("userModel");
        if (userModel != null){
            username = userModel.getUsername();
        }

        getAPIdata(username);
    }

    private void getAPIdata(String uname) {
        String urlEndpoint = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-rjiyx/endpoint/getPesananByAkun?username="+username;
        StringRequest sr = new StringRequest(Request.Method.GET, urlEndpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                // Panggil metode untuk mengisi ListView dengan data JSON
                populateListView(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Keranjang.this, "Gagal Memuat Data", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(sr);
    }

    private void populateListView(String jsonResponse) {
        try {
            lv = findViewById(R.id.lv_pesanan);
            jsonArrayRes = new JSONArray(jsonResponse);
            ArrayList<Produk2> produkList = new ArrayList<>();

            for (int i = 0; i < jsonArrayRes.length(); i++) {
                JSONObject jsonObject = jsonArrayRes.getJSONObject(i);
                String username = jsonObject.getString("username");
                String nama_produk = jsonObject.getString("nama");
                String gambar = jsonObject.getString("gambar");
                String date = jsonObject.getString("date");
                String varian = jsonObject.getString("varian");
                String harga = jsonObject.getString("harga");
                String jumlah = jsonObject.getString("jumlah");
                String alamat = jsonObject.getString("alamat");
                String status = jsonObject.getString("status");
                String id = jsonObject.getString("_id");

                sPesanan = status;
                if (sPesanan.equals("Pesanan Selesai")){
                    pesananSelesai(sPesanan);
                }
                int resID = context.getResources().getIdentifier(gambar, "drawable", context.getPackageName());

                Produk2 produk = new Produk2(id,nama_produk,date,jumlah,alamat,status, varian, harga, resID);
                produkList.add(produk);
            }

            ListAdapter adapter = new ListAdapter(this, produkList);
            adapter.setListener(this);
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("statusPesanan", "onCreate: "+sPesanan);
//        if (sPesanan.equals("Pesanan Selesai")){
//            pesananSelesai();
//        }
    }

    //FUNGSI UPDATE DATA
    @Override
    public void onUbahButtonClicked(String orderId, String nama_produk, String alamat, int gambar, String jumlah) {
        final Dialog dialog = new Dialog(Keranjang.this);
        dialog.setContentView(R.layout.dialog_update);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(64, 0, 0, 0)));
        Window window = dialog.getWindow();
        if (window != null){
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }

        View backgroundView = dialog.findViewById(android.R.id.content);
        backgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ImageView gambarP = dialog.findViewById(R.id.gambar_produk);
        gambarP.setImageResource(gambar);

        TextView judulP = dialog.findViewById(R.id.judulP);
        judulP.setText(nama_produk);

        alamatUserDialog = dialog.findViewById(R.id.alamat);
        alamatUserDialog.setText(alamat);

        jumlahPesananDialog = dialog.findViewById(R.id.jumlah);
        jumlahPesananDialog.setText(jumlah);

        Button closeButton = dialog.findViewById(R.id.tmbleditcancel);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button simpanButton = dialog.findViewById(R.id.tmbleditsave);
        simpanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePesanan(orderId);
                dialog.dismiss();
            }
        });
    }

    private void updatePesanan(String orderId) {
        String updateJml = jumlahPesananDialog.getText().toString();
        String updateAl = alamatUserDialog.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String urlEndpoint = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-rjiyx/endpoint/updatePesananMobile?id=" + orderId + "&jumlah=" +updateJml+ "&alamat=" +updateAl;

        StringRequest updateObject = new StringRequest(Request.Method.PUT, urlEndpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Pesanan Berhasil Diubah", Toast.LENGTH_SHORT).show();
                fetchDataAndPopulateListView(username);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Tidak Dapat Mengubah Pesanan", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(updateObject);
    }

    private void fetchDataAndPopulateListView(String username) {
        getAPIdata(username);
    }

    //FUNGSI HAPUS DATA
    @Override
    public void onHapusButtonClicked(String orderId) {
        final Dialog dialog = new Dialog(Keranjang.this);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(64, 0, 0, 0)));
        Window window = dialog.getWindow();
        if (window != null){
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }

        View backgroundView = dialog.findViewById(android.R.id.content);
        backgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button deleteButton = dialog.findViewById(R.id.tmbldelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePesanan(orderId);
                dialog.dismiss();
            }
        });
        Button closeButton = dialog.findViewById(R.id.tmblcancel);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void deletePesanan(String orderId) {
        String idToDelete = orderId;

        String urlEndpoint = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-rjiyx/endpoint/deletePesanan?id=" + idToDelete;

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, urlEndpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Keranjang.this, "Pesanan Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                fetchDataAndPopulateListView(username);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Keranjang.this, "Gagal Menghapus Data", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(deleteRequest);
    }

    //FUNGSI CHECKOUT
    @Override
    public void onCheckoutButtonClicked(String orderId, String nama_produk, String alamat, String varian, String jumlah, String hargaStr) {
        Log.d("CheckoutFunction", "Checkout button clicked for order ID: " + hargaStr);
        final Dialog dialog = new Dialog(Keranjang.this);
        dialog.setContentView(R.layout.dialog_checkout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(64, 0, 0, 0)));
        Window window = dialog.getWindow();
        if (window != null){
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }
        View backgroundView = dialog.findViewById(android.R.id.content);
        backgroundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView jdlPesanan = dialog.findViewById(R.id.judulPdk);
        jdlPesanan.setText(nama_produk);

        TextView alamatUsr = dialog.findViewById(R.id.Alamat);
        alamatUsr.setText(alamat);

        TextView varianP = dialog.findViewById(R.id.Berat);
        varianP.setText(varian);

        TextView jumlahP = dialog.findViewById(R.id.Jumlah);
        jumlahP.setText(jumlah);
        int jumlahInt = Integer.parseInt(jumlah);
        Log.d("jumlahInt", "onCheckoutButtonClicked: " + jumlahInt);

        hargaStr = hargaStr.replaceAll("[^0-9]", ""); // Menghapus semua karakter kecuali angka
        int hargaInteger = Integer.parseInt(hargaStr);

        int totalHarga = hargaInteger * jumlahInt;

        // Membuat objek NumberFormat untuk mata uang
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.applyPattern("###,###");
        String totalFormatted = numberFormat.format(totalHarga);
        TextView totalP = dialog.findViewById(R.id.Total);
        totalP.setText("Rp"+totalFormatted);

        Button closeButton = dialog.findViewById(R.id.tmblcancel);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button beliButton = dialog.findViewById(R.id.tmblbeli);
        beliButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOutPesanan(orderId);
                dialog.dismiss();
            }
        });
    }

    private void checkOutPesanan(String orderId) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String urlEndpoint = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-rjiyx/endpoint/checkoutPesanan?id=" + orderId;

        StringRequest updateObject = new StringRequest(Request.Method.PUT, urlEndpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context, "Berhasil Checkout Pesanan", Toast.LENGTH_SHORT).show();
                fetchDataAndPopulateListView(username);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Tidak Dapat Checkout Pesanan", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(updateObject);
    }
    private void pesananSelesai(String status) {
        String urlEndpoint = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-rjiyx/endpoint/pesananSelesai?status=" + status;
        StringRequest pesananSelsai = new StringRequest(Request.Method.DELETE, urlEndpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                fetchDataAndPopulateListView(username);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(pesananSelsai);
    }
    public void goBack(View v){
        UserModel userModel = (UserModel) getIntent().getSerializableExtra("userModel");
        Intent i = new Intent(Keranjang.this, HalamanProduk.class);
        i.putExtra("userModel", userModel);
        startActivity(i);
    }

}