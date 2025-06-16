package com.example.uas;

// Import library yang diperlukan untuk UI, validasi, dan navigasi
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    // Deklarasi komponen UI untuk form profile
    private EditText etName, etEmail, etPhone, etPassword;  // Input field untuk data user
    private Button btnSave, btnLogout;                      // Tombol simpan dan logout
    private ImageView imgProfile;                           // Gambar profile user
    private TextView tvChangePhoto;                         // Text untuk ganti foto

    // Variabel untuk mengelola data dan state
    private String username;                                // Username user yang sedang login
    private DBHelper dbHelper;                             // Helper untuk operasi database
    private boolean hasUnsavedChanges = false;            // Flag untuk tracking perubahan yang belum disimpan

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile); // Menghubungkan dengan layout profile.xml

        // Menghubungkan variabel dengan komponen UI di layout
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        btnSave = findViewById(R.id.btnSave);
        btnLogout = findViewById(R.id.btnLogout);
        imgProfile = findViewById(R.id.imgProfile);
        tvChangePhoto = findViewById(R.id.tvChangePhoto);

        // Membuat instance database helper untuk operasi CRUD
        dbHelper = new DBHelper(this);

        // Mengambil username yang dikirim dari activity sebelumnya
        username = getIntent().getStringExtra("username");

        // Memanggil method-method untuk setup awal
        loadProfileData();      // Load data profile user dari database
        setupTextWatchers();    // Setup auto-save ketika user mengetik

        // Event listener untuk tombol Save - menyimpan perubahan profile
        btnSave.setOnClickListener(view -> {
            saveProfile();
        });

        // Event listener untuk tombol Logout - keluar dari aplikasi
        btnLogout.setOnClickListener(view -> {
            logout();
        });

        // Event listener untuk text "Change Photo" - ganti foto profile
        tvChangePhoto.setOnClickListener(view -> {
            changeProfilePhoto();
        });

        // Event listener untuk gambar profile - alternatif cara ganti foto
        imgProfile.setOnClickListener(view -> {
            changeProfilePhoto();
        });
    }

    // Method untuk setup auto-save ketika user mengetik di input field
    private void setupTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Dipanggil sebelum text berubah - tidak digunakan
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Dipanggil saat text sedang berubah
                hasUnsavedChanges = true;   // Tandai ada perubahan yang belum disimpan
                autoSaveProfile();          // Mulai timer auto-save
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Dipanggil setelah text berubah - tidak digunakan
            }
        };

        // Menambahkan TextWatcher ke semua input field
        etName.addTextChangedListener(textWatcher);
        etEmail.addTextChangedListener(textWatcher);
        etPhone.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
    }

    // Method untuk mengatur auto-save dengan delay
    private void autoSaveProfile() {
        // Menghapus callback sebelumnya untuk mencegah multiple timer
        etName.removeCallbacks(autoSaveRunnable);
        // Menjalankan auto-save setelah 1.5 detik user berhenti mengetik
        etName.postDelayed(autoSaveRunnable, 1500);
    }

    // Runnable yang akan dijalankan untuk auto-save
    private final Runnable autoSaveRunnable = new Runnable() {
        @Override
        public void run() {
            if (hasUnsavedChanges) {
                saveProfileSilently(); // Simpan tanpa menampilkan toast
            }
        }
    };

    // Method untuk load data profile user dari database
    private void loadProfileData() {
        if (username != null) {
            if (username.equals("admin")) {
                // Khusus untuk admin - set data default hardcoded
                etName.setText("Administrator");
                etEmail.setText("admin@gmail.com");
                etPhone.setText("081234567890");
                etPassword.setText("1234");
            } else {
                // Untuk user biasa - ambil data dari database
                DBHelper.UserProfile profile = dbHelper.getUserProfile(username);
                if (profile != null) {
                    // Jika data ditemukan, isi ke form
                    etName.setText(profile.name);
                    etEmail.setText(profile.email);
                    etPhone.setText(profile.phone);
                    etPassword.setText(profile.password);
                } else {
                    // Jika tidak ada data, set username sebagai nama default
                    etName.setText(username);
                    etEmail.setText("");
                    etPhone.setText("");
                    etPassword.setText("");
                }
            }
        }
        hasUnsavedChanges = false; // Reset flag perubahan
    }

    // Method untuk save profile dengan feedback toast
    private void saveProfile() {
        saveProfileWithFeedback(true);
    }

    // Method untuk save profile tanpa feedback (untuk auto-save)
    private void saveProfileSilently() {
        saveProfileWithFeedback(false);
    }

    // Method utama untuk menyimpan profile dengan opsi menampilkan toast
    private void saveProfileWithFeedback(boolean showToast) {
        // Mengambil dan membersihkan input dari user
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validasi: semua field harus diisi
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            if (showToast) {
                Toast.makeText(this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        // Validasi format email menggunakan Android Patterns
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (showToast) {
                Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if (username.equals("admin")) {
            // Untuk admin - hanya update UI, tidak simpan ke database
            if (showToast) {
                Toast.makeText(this, "Profile admin berhasil diupdate", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Untuk user biasa - simpan ke database
            boolean success = dbHelper.updateUserProfile(username, name, email, phone, password);
            if (success) {
                if (showToast) {
                    Toast.makeText(this, "Profile berhasil disimpan", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (showToast) {
                    Toast.makeText(this, "Gagal menyimpan profile", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }

        hasUnsavedChanges = false; // Reset flag perubahan

        // Jika dipanggil dari tombol Save, kembali ke dashboard
        if (showToast) {
            returnToDashboardWithUpdatedName(name);
        }
    }

    // Method untuk kembali ke dashboard dengan nama yang sudah diupdate
    private void returnToDashboardWithUpdatedName(String updatedName) {
        Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("displayName", updatedName); // Kirim nama yang baru
        // Flag untuk clear stack dan membuat single instance
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    // Method untuk logout user
    private void logout() {
        Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        // Flag untuk membersihkan semua activity dan membuat task baru
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Method untuk mengubah foto profile (belum diimplementasi)
    private void changeProfilePhoto() {
        Toast.makeText(this, "Fitur ganti foto akan segera tersedia", Toast.LENGTH_SHORT).show();
        // TODO: Implementasi untuk membuka gallery atau camera
        // Intent intent = new Intent(Intent.ACTION_PICK);
        // intent.setType("image/*");
        // startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    // Override method ketika user menekan tombol back
    @Override
    public void onBackPressed() {
        // Auto-save sebelum kembali jika ada perubahan
        if (hasUnsavedChanges) {
            saveProfileSilently();
        }

        // Ambil nama yang mungkin sudah diupdate
        String displayName = etName.getText().toString().trim();
        if (displayName.isEmpty()) {
            displayName = username; // Fallback ke username jika nama kosong
        }

        // Kembali ke dashboard dengan nama terbaru
        returnToDashboardWithUpdatedName(displayName);
        super.onBackPressed();
    }

    // Override method ketika activity di-pause (minimize, phone call, dll)
    @Override
    protected void onPause() {
        super.onPause();
        // Auto-save ketika activity tidak aktif
        if (hasUnsavedChanges) {
            saveProfileSilently();
        }
    }

    // Override method ketika activity dihancurkan
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hapus callback untuk mencegah memory leak
        if (etName != null) {
            etName.removeCallbacks(autoSaveRunnable);
        }
    }
}