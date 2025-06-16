package com.example.uas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MateriActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi);

        // Ambil data dari Intent
        String judul = getIntent().getStringExtra("judul");
        String[] fileNames = getIntent().getStringArrayExtra("files");

        // Set judul utama dan subjudul
        TextView title = findViewById(R.id.textJudulUtama);
        TextView subtitle = findViewById(R.id.textSubjudul);

        title.setText(judul);

        // Set subjudul sesuai modul
        switch (judul) {
            case "Bahasa Indonesia":
                subtitle.setText("(Materi Bahasa Indonesia)");
                break;
            case "Bahasa Inggris":
                subtitle.setText("(Materi Bahasa Inggris)");
                break;
            case "Penalaran Matematika":
                subtitle.setText("(Materi Penalaran Matematika)");
                break;
            default:
                subtitle.setText("(Materi Umum)");
                break;
        }

        // Container utama
        LinearLayout container = findViewById(R.id.containerMateri);

        // Loop semua file PDF
        for (String file : fileNames) {
            // Block: subjudul + card
            LinearLayout block = new LinearLayout(this);
            block.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams blockParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            blockParams.setMargins(0, 0, 0, 32);
            block.setLayoutParams(blockParams);

            // Subjudul di atas kotak
            TextView subjudul = new TextView(this);
            subjudul.setText(file.replace(".pdf", ""));
            subjudul.setTextSize(16);
            subjudul.setTypeface(null, android.graphics.Typeface.BOLD);
            subjudul.setTextColor(getResources().getColor(android.R.color.black));
            subjudul.setPadding(0, 0, 0, 8);

            // Kotak (card)
            LinearLayout card = new LinearLayout(this);
            card.setBackgroundResource(R.drawable.card_modul);
            card.setPadding(24, 24, 24, 24);
            card.setGravity(android.view.Gravity.CENTER);
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    200
            );
            card.setLayoutParams(cardParams);

            // Isi dalam card
            TextView label = new TextView(this);
            label.setText("Materi " + file.replace(".pdf", "") + " (PDF)");
            label.setTextSize(14);
            label.setTypeface(null, android.graphics.Typeface.BOLD);
            label.setTextColor(getResources().getColor(android.R.color.black));
            card.addView(label);

            // Gabungkan semua ke block
            block.addView(subjudul);
            block.addView(card);
            container.addView(block);

            // Event klik buka PDF
            card.setOnClickListener(v -> openPDF(file));
        }
    }

    private void openPDF(String fileName) {
        try {
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

            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", outFile);

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
