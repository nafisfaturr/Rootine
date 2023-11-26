package com.example.rootine;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


public class masukan extends AppCompatActivity {
    EditText etMasukan;

    Button btnSimpan;

    ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masukan);
        etMasukan = findViewById(R.id.et_masukan);
        btnSimpan = findViewById(R.id.btn_simpanmasukan);
        btnBack = findViewById(R.id.backButton);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedback = etMasukan.getText().toString();

                if (!feedback.isEmpty()) {
                    // Replace the endpointFeedback with your actual endpoint
                    String endpointFeedback = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-rjiyx/endpoint/postFeedback";
                    RequestQueue requestQueue = Volley.newRequestQueue(masukan.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, endpointFeedback, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(masukan.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(masukan.this, "Error mas", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("feedback", feedback); // Use feedback variable here
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);

                    Toast.makeText(masukan.this, "Feedback telah dikirimkan!", Toast.LENGTH_SHORT).show();
                    etMasukan.setText("");
                } else {
                    Toast.makeText(masukan.this, "Silakan isi feedback terlebih dahulu!", Toast.LENGTH_SHORT).show();
                }
            }

        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel userModel = (UserModel) getIntent().getSerializableExtra("userModel");
                Intent i = new Intent(masukan.this,Profile.class);
                i.putExtra("userModel",userModel);
                startActivity(i);
            }
        });
    }
}