package com.example.rootine;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class faq extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        // Pasangan pertama untuk ques1 dan ans1
        TextView ques1 = findViewById(R.id.ques1);
        final TextView ans1 = findViewById(R.id.ans1);
        setupToggle(ques1, ans1);

        // Pasangan kedua untuk ques2 dan ans2
        TextView ques2 = findViewById(R.id.ques2);
        final TextView ans2 = findViewById(R.id.ans2);
        setupToggle(ques2, ans2);

        // Pasangan ketiga untuk ques2 dan ans2
        TextView ques3 = findViewById(R.id.ques3);
        final TextView ans3 = findViewById(R.id.ans3);
        setupToggle(ques3, ans3);

        // Anda dapat menambahkan pasangan berikutnya dengan cara yang sama jika diperlukan.
    }

    private void setupToggle(final TextView question, final TextView answer) {
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer.getVisibility() == View.VISIBLE) {
                    answer.setVisibility(View.GONE);
                } else {
                    answer.setVisibility(View.VISIBLE);
                }
            }
        });

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel userModel = (UserModel) getIntent().getSerializableExtra("userModel");
                Intent intent = new Intent(faq.this, Profile.class);
                intent.putExtra("userModel",userModel);
                startActivity(intent);
            }
        });
    }
}
