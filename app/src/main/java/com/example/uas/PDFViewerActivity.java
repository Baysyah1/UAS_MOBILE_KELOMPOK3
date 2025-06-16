package com.example.uas;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class PDFViewerActivity extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        webView = findViewById(R.id.webView);

        String fileName = getIntent().getStringExtra("pdf_file");

        if (fileName != null) {
            // Asumsikan file disimpan di folder assets
            // kita aksesnya via file:///android_asset/
            String url = "https://drive.google.com/viewerng/viewer?embedded=true&url=" +
                    "file:///android_asset/" + fileName;

            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);
        }
    }
}
