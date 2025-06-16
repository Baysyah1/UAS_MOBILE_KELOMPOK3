package com.example.uas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CardHelper {

    public static View createMentorCard(Context context, String name) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mentor, null, false);

        // Set width untuk horizontal scroll
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                dpToPx(context, 180), // Width: 180dp
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMarginEnd(dpToPx(context, 12)); // Margin kanan 12dp
        view.setLayoutParams(params);

        ImageView imageView = view.findViewById(R.id.imageMentor);
        TextView nameView = view.findViewById(R.id.textMentorName);
        TextView badgeView = view.findViewById(R.id.textMentorBadge);
        LinearLayout starContainer = view.findViewById(R.id.starContainer);

        // Set gambar dan badge berdasarkan nama
        switch (name) {
            case "Sulastri":
                imageView.setImageResource(R.drawable.sulastri);
                badgeView.setText("Certified Mentor");
                break;
            case "Siti":
                imageView.setImageResource(R.drawable.siti);
                badgeView.setText("Certified Mentor");
                break;
            case "Rachel":
                imageView.setImageResource(R.drawable.rachel);
                badgeView.setText("Certified Mentor");
                break;
            case "Ratu":
                imageView.setImageResource(R.drawable.ratu);
                badgeView.setText("Certified Mentor");
                break;
            case "Sarah":
                imageView.setImageResource(R.drawable.sarah);
                badgeView.setText("Certified Mentor");
                break;
            default:
                imageView.setImageResource(R.drawable.profile_image);
                badgeView.setText("Mentor");
                break;
        }

        nameView.setText(name);

        // Tambahkan 5 bintang
        starContainer.removeAllViews();
        for (int i = 0; i < 5; i++) {
            ImageView star = new ImageView(context);
            star.setImageResource(R.drawable.ic_star_filled);
            LinearLayout.LayoutParams starParams = new LinearLayout.LayoutParams(
                    dpToPx(context, 14),
                    dpToPx(context, 14)
            );
            if (i < 4) starParams.setMarginEnd(dpToPx(context, 2));
            star.setLayoutParams(starParams);
            starContainer.addView(star);
        }

        return view;
    }

    public static View createModulCard(Context context, String modul) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_modul, null, false);

        // Set width untuk horizontal scroll
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(context, 180), // Width: 180dp
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMarginEnd(dpToPx(context, 12)); // Margin kanan 12dp
        view.setLayoutParams(params);

        ImageView imageView = view.findViewById(R.id.imageModul);
        TextView titleView = view.findViewById(R.id.textModulTitle);
        TextView infoView = view.findViewById(R.id.textModulInfo);
        TextView certView = view.findViewById(R.id.textCertificate);

        switch (modul) {
            case "TPS":
                imageView.setImageResource(R.drawable.ic_tps);
                titleView.setText("TPS");
                infoView.setText("25 Quizzes\n15 Assignments");
                certView.setText("Certificate Available");
                break;
            case "Penalaran Matematika":
                imageView.setImageResource(R.drawable.ic_math);
                titleView.setText("Penalaran Matematika");
                infoView.setText("30 Quizzes\n10 Assignments");
                certView.setText("Certificate Available");
                break;
            case "Bahasa Inggris":
                imageView.setImageResource(R.drawable.ic_english);
                titleView.setText("Bahasa Inggris");
                infoView.setText("30 Quizzes\n10 Assignments");
                certView.setText("Certificate Available");
                break;
            case "Bahasa Indonesia":
                imageView.setImageResource(R.drawable.ic_indonesia);
                titleView.setText("Bahasa Indonesia");
                infoView.setText("30 Quizzes\n10 Assignments");
                certView.setText("Certificate Available");
                break;
            default:
                imageView.setImageResource(R.drawable.ic_module);
                titleView.setText(modul);
                infoView.setText("Content not available");
                certView.setText("No Certificate");
                break;
        }

        return view;
    }

    // Helper method untuk konversi dp ke px
    private static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}