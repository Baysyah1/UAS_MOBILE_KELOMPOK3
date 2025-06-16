package com.example.uas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ModulActivity extends AppCompatActivity {

    private String username;
    private String displayName;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modul);

        username = getIntent().getStringExtra("username");
        displayName = getIntent().getStringExtra("displayName");
        dbHelper = new DBHelper(this);

        LinearLayout dashboardTab = findViewById(R.id.tabDashboard);
        LinearLayout mentorTab = findViewById(R.id.tabMentor);
        LinearLayout premiumTab = findViewById(R.id.tabPremium);

        CardView modulTps = findViewById(R.id.modulTps);
        CardView cardIndonesia = findViewById(R.id.cardIndonesia);
        CardView cardEnglish = findViewById(R.id.cardEnglish);
        CardView cardMath = findViewById(R.id.cardMatematika);

        modulTps.setOnClickListener(v -> {
            dbHelper.addHistory(username, "Modul TPS");
            Intent intent = new Intent(ModulActivity.this, MateriTPSActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        cardIndonesia.setOnClickListener(v -> {
            dbHelper.addHistory(username, "Modul Bahasa Indonesia");
            Intent intent = new Intent(ModulActivity.this, MateriActivity.class);
            intent.putExtra("judul", "Bahasa Indonesia");
            intent.putExtra("files", new String[]{"Paragraf.pdf", "Morfonologi.pdf", "Sintaksis.pdf", "PUEBI.pdf"});
            startActivity(intent);
        });

        cardEnglish.setOnClickListener(v -> {
            dbHelper.addHistory(username, "Modul Bahasa Inggris");
            Intent intent = new Intent(ModulActivity.this, MateriActivity.class);
            intent.putExtra("judul", "Bahasa Inggris");
            intent.putExtra("files", new String[]{"Tenses.pdf", "Passive Voice.pdf", "Derivative.pdf", "Concordance.pdf"});
            startActivity(intent);
        });

        cardMath.setOnClickListener(v -> {
            dbHelper.addHistory(username, "Modul Penalaran Matematika");
            Intent intent = new Intent(ModulActivity.this, MateriActivity.class);
            intent.putExtra("judul", "Penalaran Matematika");
            intent.putExtra("files", new String[]{"Penalaran Matematika.pdf"});
            startActivity(intent);
        });

        dashboardTab.setOnClickListener(v -> {
            Intent intent = new Intent(ModulActivity.this, DashboardActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("displayName", displayName);
            startActivity(intent);
            finish();
        });

        mentorTab.setOnClickListener(v -> {
            Intent intent = new Intent(ModulActivity.this, MentorActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("displayName", displayName);
            startActivity(intent);
            finish();
        });

        premiumTab.setOnClickListener(v -> {
            Intent intent = new Intent(ModulActivity.this, PremiumActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("displayName", displayName);
            startActivity(intent);
            finish();
        });
    }
}
