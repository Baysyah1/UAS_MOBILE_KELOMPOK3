package com.example.uas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MateriTPSActivity extends AppCompatActivity {

    LinearLayout card1, card2, card3, card4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi_tps);

        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);

        card1.setOnClickListener(v -> openPDF("Penalaran Umum (TPS).pdf"));
        card2.setOnClickListener(v -> openPDF("Kemampuan Kuantitatif (TPS).pdf"));
        card3.setOnClickListener(v -> openPDF("Pengetahuan dan Pemahaman Umum (TPS).pdf"));
        card4.setOnClickListener(v -> openPDF("Kemampuan Memahami Bacaan dan Menulis (TPS).pdf"));
    }

    private void openPDF(String fileName) {
        try {
            // Salin PDF dari assets ke cache
            InputStream inputStream = getAssets().open(fileName);
            File outFile = new File(getCacheDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(outFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }

            outputStream.flush();
            inputStream.close();
            outputStream.close();

            // Dapatkan URI file
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", outFile);

            // Intent untuk buka PDF
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal membuka PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
