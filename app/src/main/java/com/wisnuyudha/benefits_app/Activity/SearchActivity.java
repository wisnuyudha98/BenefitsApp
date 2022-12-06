package com.wisnuyudha.benefits_app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wisnuyudha.benefits_app.R;

public class SearchActivity extends AppCompatActivity {

    private EditText search;
    private Button searchButton;
    SharedPreferences sp;
    LinearLayout kategoriMakananBerat, kategoriMakananRingan, kategoriMinuman, kategoriKerajinan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search = findViewById(R.id.search_box);
        searchButton = findViewById(R.id.search_button);
        kategoriMakananBerat = findViewById(R.id.pick_kategori_makanan_berat);
        kategoriMakananRingan = findViewById(R.id.pick_kategori_makanan_ringan);
        kategoriMinuman = findViewById(R.id.pick_kategori_minuman);
        kategoriKerajinan = findViewById(R.id.pick_kategori_kerajinan);

        sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        if (sp.contains("Search")) {
            sp.edit().remove("Search").apply();
        }

        kategoriMakananBerat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString("Search", "kategori").apply();
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra(SearchResultActivity.EXTRA_KATEGORI, "makanan berat");
                startActivity(intent);
            }
        });

        kategoriMakananRingan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString("Search", "kategori").apply();
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra(SearchResultActivity.EXTRA_KATEGORI, "makanan ringan");
                startActivity(intent);
            }
        });

        kategoriMinuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString("Search", "kategori").apply();
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra(SearchResultActivity.EXTRA_KATEGORI, "minuman");
                startActivity(intent);
            }
        });

        kategoriKerajinan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString("Search", "kategori").apply();
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra(SearchResultActivity.EXTRA_KATEGORI, "kerajinan");
                startActivity(intent);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(search.getText().toString())){
                    Toast.makeText(SearchActivity.this, "Kotak pencarian tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                    intent.putExtra(SearchResultActivity.EXTRA_CARI, search.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }
}