package com.example.rootine;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Locale;

public class Profile extends AppCompatActivity {
    TextView bahasa,logout,tv_faq,tv_masukan, email, username, password, alamat, role,ubah,selesai;
    EditText et_email, et_uname, et_pass, et_alamat;
    Dialog dialogLogout;
    ImageView show,hide;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_profile);

        userModel = (UserModel) getIntent().getSerializableExtra("userModel");
        Log.d("usermodel", "onCreate: "+userModel);

        bahasa = findViewById(R.id.setting_bahasa);
        logout = findViewById(R.id.logout);
        tv_faq = findViewById(R.id.faq);
        tv_masukan = findViewById(R.id.masukan);
        email = findViewById(R.id.email_pengguna);
        username = findViewById(R.id.username_pengguna);
        password = findViewById(R.id.password_pengguna);
        alamat = findViewById(R.id.alamat_pengguna);
        role = findViewById(R.id.role_pengguna);
        ubah = findViewById(R.id.edit);
        selesai = findViewById(R.id.selesai);

        //editText
        et_email = findViewById(R.id.e_email);
        et_uname = findViewById(R.id.e_username);
        et_pass = findViewById(R.id.e_password);
        et_alamat = findViewById(R.id.e_alamat);

        //ImageView
        show = findViewById(R.id.eyes);
        hide = findViewById(R.id.hide);

        et_email.setText(userModel.getEmail());
        et_uname.setText(userModel.getUsername());
        et_pass.setText(userModel.getPassword());
        et_alamat.setText(userModel.getAlamat());

        //mengisi data user
        email.setText(userModel.getEmail());
        username.setText(userModel.getUsername());
        password.setText(userModel.getPassword());
        alamat.setText(userModel.getAlamat());
        role.setText(userModel.getRole());

        dialogLogout = new Dialog(this);
        onCreateLogoutDialog();

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    // Jika tersembunyi, ubah menjadi terlihat
                    show.setVisibility(View.GONE);
                    hide.setVisibility(View.VISIBLE);
                    password.setTransformationMethod(null);
                } else {
                    // Jika terlihat, ubah menjadi tersembunyi
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    // Jika tersembunyi, ubah menjadi terlihat
                    password.setTransformationMethod(null);
                } else {
                    // Jika terlihat, ubah menjadi tersembunyi
                    show.setVisibility(View.VISIBLE);
                    hide.setVisibility(View.GONE);
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        ubah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ubah.setVisibility(View.GONE);
                email.setVisibility(View.GONE);
                username.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
                alamat.setVisibility(View.GONE);
                show.setVisibility(View.GONE);

                selesai.setVisibility(View.VISIBLE);
                et_email.setVisibility(View.VISIBLE);
                et_uname.setVisibility(View.VISIBLE);
                et_pass.setVisibility(View.VISIBLE);
                et_alamat.setVisibility(View.VISIBLE);
            }
        });
        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDataBaru(userModel.getId());

                ubah.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                username.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                alamat.setVisibility(View.VISIBLE);
                show.setVisibility(View.VISIBLE);

                selesai.setVisibility(View.GONE);
                et_email.setVisibility(View.GONE);
                et_uname.setVisibility(View.GONE);
                et_pass.setVisibility(View.GONE);
                et_alamat.setVisibility(View.GONE);
            }
        });

        bahasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Profile.this);
                dialog.setContentView(R.layout.dialog_bahasa);
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
                RadioButton btnindo = dialog.findViewById(R.id.lang_indo);
                RadioButton btneng = dialog.findViewById(R.id.lang_eng);
                RadioButton btnjap = dialog.findViewById(R.id.lang_jap);

                btnindo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setLocale("id");
                        recreate();
                        dialog.dismiss();
                    }
                });

                btneng.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setLocale("en");
                        recreate();
                        dialog.dismiss();
                    }
                });

                btnjap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setLocale("ja");
                        recreate();
                        dialog.dismiss();
                    }
                });
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialogLogout = new Dialog(Profile.this);
                dialogLogout.setContentView(R.layout.dialog_logout);
                dialogLogout.setCanceledOnTouchOutside(true);
                dialogLogout.show();

                dialogLogout.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(64, 0, 0, 0)));
                Window window = dialogLogout.getWindow();
                if (window != null){
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                }

                View backgroundView = dialogLogout.findViewById(android.R.id.content);
                backgroundView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogLogout.dismiss();
                    }
                });

                Button batal = dialogLogout.findViewById(R.id.batallogout);
                Button doLogout = dialogLogout.findViewById(R.id.btn_logout);

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogLogout.dismiss(); // Menutup dialog logout
                    }
                });

                doLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Profile.this, MainActivity.class);
                        startActivity(i);
                        dialogLogout.dismiss();
                    }
                });
            }
        });


        tv_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel userModel = (UserModel) getIntent().getSerializableExtra("userModel");
                Intent i = new Intent(Profile.this, faq.class);
                i.putExtra("userModel", userModel);
                startActivity(i);
            }
        });

        tv_masukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel userModel = (UserModel) getIntent().getSerializableExtra("userModel");
                Intent i = new Intent(Profile.this, masukan.class);
                i.putExtra("userModel", userModel);
                startActivity(i);
            }
        });

        dialogLogout.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
    private void updateDataBaru(String id) {
        String u_email = et_email.getText().toString();
        String u_uname = et_uname.getText().toString();
        String u_pass  = et_pass.getText().toString();
        String u_alamat= et_alamat.getText().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String urlEndpoint = "https://us-east-1.aws.data.mongodb-api.com/app/application-0-rjiyx/endpoint/editPenggunaRootine?id="+id+"&email="+u_email+"&username="+u_uname+"&password="+u_pass+"&alamat="+u_alamat;

        StringRequest updateData = new StringRequest(Request.Method.PUT, urlEndpoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                userModel.setEmail(u_email);
                userModel.setUsername(u_uname);
                userModel.setPassword(u_pass);
                userModel.setAlamat(u_alamat);

                //Perbarui tampilan data dgn nilai baru
                updateViewWithData(userModel);
                Toast.makeText(Profile.this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profile.this, "Gagal mengubah data", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(updateData);
    }

    private void updateViewWithData(UserModel userModel) {
        et_email.setText(userModel.getEmail());
        et_uname.setText(userModel.getUsername());
        et_pass.setText(userModel.getPassword());
        et_alamat.setText(userModel.getAlamat());

        email.setText(userModel.getEmail());
        username.setText(userModel.getUsername());
        password.setText(userModel.getPassword());
        alamat.setText(userModel.getAlamat());
        role.setText(userModel.getRole());
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
    private void onCreateLogoutDialog() {
        View viewLogout = getLayoutInflater().inflate(R.layout.dialog_logout, null, false);
        dialogLogout.setContentView(viewLogout);

        // Tambahkan properti dialog untuk menampilkan dialog di tengah layar
        dialogLogout.getWindow().setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );
    }

    public void gotoHome(View view){
        UserModel userModel = (UserModel) getIntent().getSerializableExtra("userModel");
        Intent i = new Intent(Profile.this, Beranda.class);
        i.putExtra("userModel", userModel);
        startActivity(i);
    }
}