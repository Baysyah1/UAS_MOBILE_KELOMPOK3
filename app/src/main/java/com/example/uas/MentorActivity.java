package com.example.uas;

// Import library yang diperlukan untuk UI, database, dan navigasi
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MentorActivity extends AppCompatActivity {

    // Variabel untuk menyimpan data user yang login
    private String username;        // Username user yang sedang login
    private String displayName;     // Nama tampilan user
    private DBHelper dbHelper;      // Helper untuk mengakses database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Menghubungkan activity dengan layout XML
        setContentView(R.layout.activity_mentor);

        // Mengambil data yang dikirim dari activity sebelumnya (misal: LoginActivity)
        username = getIntent().getStringExtra("username");
        displayName = getIntent().getStringExtra("displayName");
        // Membuat instance database helper untuk operasi database
        dbHelper = new DBHelper(this);

        // Memanggil method untuk setup navigasi dan kartu mentor
        setupBottomNavigation();    // Setup tab navigasi bawah
        setupMentorCards();        // Setup kartu-kartu mentor
    }

    // Method untuk mengatur fungsi navigasi tab bawah
    private void setupBottomNavigation() {
        // Mendapatkan referensi ke setiap tab dari layout
        LinearLayout tabDashboard = findViewById(R.id.tabDashboard);
        LinearLayout tabModul = findViewById(R.id.tabModul);
        LinearLayout tabMentor = findViewById(R.id.tabMentor);
        LinearLayout tabPremium = findViewById(R.id.tabPremium);

        // Tab Dashboard - pindah ke DashboardActivity
        tabDashboard.setOnClickListener(v -> {
            Intent intent = new Intent(MentorActivity.this, DashboardActivity.class);
            // Membawa data login ke activity tujuan
            intent.putExtra("username", username);
            intent.putExtra("displayName", displayName);
            startActivity(intent);
            finish(); // Menutup activity saat ini
        });

        // Tab Modul - pindah ke ModulActivity
        tabModul.setOnClickListener(v -> {
            Intent intent = new Intent(MentorActivity.this, ModulActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("displayName", displayName);
            startActivity(intent);
            finish();
        });

        // Tab Premium - pindah ke PremiumActivity
        tabPremium.setOnClickListener(v -> {
            Intent intent = new Intent(MentorActivity.this, PremiumActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("displayName", displayName);
            startActivity(intent);
            finish();
        });

        // Tab Mentor - dinonaktifkan karena sedang berada di halaman ini
        tabMentor.setClickable(false);
    }

    // Method untuk mengatur fungsi setiap kartu mentor
    private void setupMentorCards() {
        // Mendapatkan referensi ke setiap CardView mentor dari layout
        CardView cardSulastri = findViewById(R.id.cardSulastri);
        CardView cardRatu = findViewById(R.id.cardRatu);
        CardView cardSarah = findViewById(R.id.cardSarah);
        CardView cardSiti = findViewById(R.id.cardSiti);
        CardView cardRachel = findViewById(R.id.cardRachel);

        // Set listener untuk setiap kartu mentor
        // Parameter: gambar, nama, rating, spesialisasi, nomor telepon
        cardSulastri.setOnClickListener(v -> showMentorPopup(R.drawable.sulastri, "Sulastri", "★★★★★", "TPS", "0812-3456-7890"));
        cardRatu.setOnClickListener(v -> showMentorPopup(R.drawable.ratu, "Ratu", "★★★★★", "BAHASA INGGRIS", "0813-5678-1234"));
        cardSarah.setOnClickListener(v -> showMentorPopup(R.drawable.sarah, "Sarah", "★★★★★", "TPS", "0821-9876-5432"));
        cardSiti.setOnClickListener(v -> showMentorPopup(R.drawable.siti, "Siti", "★★★★★", "BAHASA INDONESIA", "0821-9876-5432"));
        cardRachel.setOnClickListener(v -> showMentorPopup(R.drawable.rachel, "Rachel", "★★★★★", "BAHASA INGGRIS", "0821-9876-5432"));
    }

    // Method untuk menampilkan pop-up detail mentor
    private void showMentorPopup(int imageRes, String nama, String rating, String spesialis, String phone) {
        // Menyimpan history bahwa user telah melihat mentor tertentu ke database
        dbHelper.addHistory(username, "Mentor " + nama);

        // Membuat dialog custom
        Dialog dialog = new Dialog(MentorActivity.this);
        dialog.setContentView(R.layout.popup_mentor_detail); // Layout untuk pop-up
        // Membuat background transparan agar terlihat modern
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Mendapatkan referensi komponen UI dalam pop-up
        ImageView image = dialog.findViewById(R.id.imageMentor);
        TextView tvNama = dialog.findViewById(R.id.tvNamaMentor);
        TextView tvRating = dialog.findViewById(R.id.tvRatingMentor);
        TextView tvSpesialis = dialog.findViewById(R.id.tvSpesialisMentor);
        TextView tvPhone = dialog.findViewById(R.id.tvPhoneMentor);

        // Mengisi data mentor ke komponen UI
        image.setImageResource(imageRes);                    // Set gambar mentor
        tvNama.setText(nama);                               // Set nama mentor
        tvRating.setText("Rating: " + rating);              // Set rating mentor
        tvSpesialis.setText("Spesialis: " + spesialis);     // Set spesialisasi
        tvPhone.setText("Telp: " + phone);                  // Set nomor telepon

        // Menampilkan pop-up
        dialog.show();
    }
}