package com.example.rootine;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText email,username,password,alamat;
    Spinner role;
    Button signup;
    String em,nm,ps,al,rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        alamat = findViewById(R.id.alamat);

        role = findViewById(R.id.role);
        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(this,R.array.role, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role.setAdapter(roleAdapter);
        role.setOnItemSelectedListener(this);

        signup = findViewById(R.id.btn_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ambilDataUser();
                clearInputFields();
            }
        });


    }
    private void ambilDataUser() {
        em = email.getText().toString();
        nm = username.getText().toString();
        ps = password.getText().toString();
        rl = role.getSelectedItem().toString();
        al = alamat.getText().toString();
        Log.d("ambilDataUser", "ambilDataUser: "+em);
        tambahUser();
    }

    private void tambahUser() {
        String urlEndpoint = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-rjiyx/endpoint/createUserRootineMobile";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlEndpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(signup.this, "Berhasil Membuat Akun", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(signup.this, "Gagal Membuat Akun", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map map = new HashMap<>();
                map.put("email",em);
                map.put("username",nm);
                map.put("password",ps);
                map.put("role",rl);
                map.put("alamat",al);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void clearInputFields() {
        email.setText("");
        username.setText("");
        password.setText("");
        alamat.setText("");
    }

    public void aksiBack(View v){
        Intent i = new Intent(signup.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}