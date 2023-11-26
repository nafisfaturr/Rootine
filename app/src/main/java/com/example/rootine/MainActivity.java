package com.example.rootine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class MainActivity extends AppCompatActivity {
    EditText username,password;
    Button login;
    String user, pass;
    UserDataListener listener;
    UserModel userModel;
    public interface UserDataListener {
        void onUserDataReceived(UserModel userModel);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.l_username);
        password = findViewById(R.id.l_password);
        login = findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getuser();
            }
        });
        listener = new UserDataListener() {
            @Override
            public void onUserDataReceived(UserModel userModel) {
                // Callback saat data pengguna diterima
                MainActivity.this.userModel = userModel;
            }
        };
    }

    private void getuser() {
        user = username.getText().toString();
        pass = password.getText().toString();

        cekUser();
    }

    private void cekUser() {
        String urlCekUser = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-rjiyx/endpoint/getPenggunaRootine?username="+user+"&password="+pass;
        StringRequest sr = new StringRequest(Request.Method.GET, urlCekUser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    JSONObject jsonResponse = jsonArray.getJSONObject(0);

                    String userId = jsonResponse.getString("_id");
                    String email = jsonResponse.getString("email");
                    String nama = jsonResponse.getString("username");
                    String password = jsonResponse.getString("password");
                    String role = jsonResponse.getString("role");
                    String alamat = jsonResponse.getString("alamat");

                    userModel = new UserModel(userId,email, nama, password, role, alamat);

                    Log.d("User Model", "onResponse: "+ userModel);

                    listener.onUserDataReceived(userModel);

                    dataDiterima();
                    if (dataDiterima()) {
                        Intent intent = new Intent(MainActivity.this, Beranda.class);
                        intent.putExtra("userModel", userModel);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Akun belum terdaftar", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Terdapat kesalahan server", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(sr);
    }

    private boolean dataDiterima() {
        return userModel != null;
    }

    public void goToRegist(View v){
        Intent i = new Intent(MainActivity.this, signup.class);
        startActivity(i);
    }
}
