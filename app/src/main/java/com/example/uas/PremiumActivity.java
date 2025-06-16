package com.example.uas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class PremiumActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        // Ambil username dari Intent
        username = getIntent().getStringExtra("username");

        // Inisialisasi tombol navigasi bawah
        LinearLayout tabDashboard = findViewById(R.id.tabDashboard);
        LinearLayout tabModul = findViewById(R.id.tabModul);
        LinearLayout tabMentor = findViewById(R.id.tabMentor);
        LinearLayout tabPremium = findViewById(R.id.tabPremium);

        // Navigasi ke Dashboard
        tabDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PremiumActivity.this, DashboardActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }
        });

        // Navigasi ke Modul
        tabModul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PremiumActivity.this, ModulActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }
        });

        // Navigasi ke Mentor
        tabMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PremiumActivity.this, MentorActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                finish();
            }
        });
    }
}