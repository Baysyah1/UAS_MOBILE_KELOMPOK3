package com.example.uas;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private int waktu_loading = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // tampilan splash screen

        new Handler().postDelayed(() -> {
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);
            finish(); // agar tidak bisa kembali ke splash
        }, waktu_loading);
    }
}
