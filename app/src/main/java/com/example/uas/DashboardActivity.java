package com.example.uas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DashboardActivity extends AppCompatActivity {


    private String username;                    // Username dari login
    private String displayName;                 // Nama tampilan user (opsional)
    private TextView userNameText;              // TextView untuk menampilkan greeting
    private DBHelper dbHelper;                  // Helper untuk akses database

    private LinearLayout containerRecentMentor; // Container untuk kartu mentor
    private LinearLayout containerRecentModul;  // Container untuk kartu modul

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        dbHelper = new DBHelper(this);
        username = getIntent().getStringExtra("username");     // Dari activity sebelumnya
        displayName = getIntent().getStringExtra("displayName"); // Nama custom user


        userNameText = findViewById(R.id.userNameText);
        containerRecentMentor = findViewById(R.id.containerRecentMentor);
        containerRecentModul = findViewById(R.id.containerRecentModul);

        CardView profileCardView = findViewById(R.id.profileCardView);
        ImageView profileImage = findViewById(R.id.profileImage);


        profileCardView.setOnClickListener(v -> openProfileActivity());
        profileImage.setOnClickListener(v -> openProfileActivity());


        updateDisplayName();        // Set nama user di greeting
        setupBottomNavigation();    // Setup bottom tabs
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateDisplayName();        // Update nama (mungkin berubah di profil)
        loadRecentHistory();        // Load ulang riwayat pembelajaran
    }


    private void openProfileActivity() {
        Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
        intent.putExtra("username", username);  // Kirim username ke ProfileActivity
        startActivity(intent);
    }


    private void updateDisplayName() {
        if (username != null) {
            String nameToDisplay;


            if (displayName != null && !displayName.trim().isEmpty()) {
                nameToDisplay = displayName;
            } else {

                DBHelper.UserProfile profile = dbHelper.getUserProfile(username);

                nameToDisplay = (profile != null && profile.name != null) ? profile.name : username;
            }


            userNameText.setText(nameToDisplay + "!");
        }
    }


    private void setupBottomNavigation() {

        LinearLayout tabDashboard = findViewById(R.id.tabDashboard);
        LinearLayout tabMentor = findViewById(R.id.tabMentor);
        LinearLayout tabModul = findViewById(R.id.tabModul);
        LinearLayout tabPremium = findViewById(R.id.tabPremium);


        tabDashboard.setClickable(false);


        tabMentor.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, MentorActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        });


        tabModul.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ModulActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        });


        tabPremium.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, PremiumActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        });
    }


    private void loadRecentHistory() {

        containerRecentMentor.removeAllViews();
        containerRecentModul.removeAllViews();


        List<String> history = dbHelper.getRecentSubjects(username);


        Set<String> addedMentors = new LinkedHashSet<>();
        Set<String> addedModules = new LinkedHashSet<>();


        for (String item : history) {


            if (item.startsWith("Mentor ")) {
                String name = item.replace("Mentor ", "");


                if (!addedMentors.contains(name)) {
                    addedMentors.add(name);


                    View card = CardHelper.createMentorCard(this, name);

                    containerRecentMentor.addView(card);
                }
            }

            else if (item.startsWith("Modul ")) {
                String modul = item.replace("Modul ", "");


                if (!addedModules.contains(modul)) {
                    addedModules.add(modul);


                    View card = CardHelper.createModulCard(this, modul);

                    containerRecentModul.addView(card);
                }
            }
        }
    }
}