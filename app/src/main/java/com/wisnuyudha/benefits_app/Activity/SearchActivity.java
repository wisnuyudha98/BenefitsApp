package com.wisnuyudha.benefits_app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.wisnuyudha.benefits_app.R;

public class SearchActivity extends AppCompatActivity {

    private EditText search;
    private Button searchButton;
    SharedPreferences sp;
    LinearLayout kategoriMakanan, kategoriMinuman, kategoriToko, kategoriJasa;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search = findViewById(R.id.search_box);
        searchButton = findViewById(R.id.search_button);
        kategoriMakanan = findViewById(R.id.pick_kategori_makanan);
        kategoriMinuman = findViewById(R.id.pick_kategori_minuman);
        kategoriToko = findViewById(R.id.pick_kategori_toko);
        kategoriJasa = findViewById(R.id.pick_kategori_jasa);
        toolbar = findViewById(R.id.toolbar);

        sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        if (sp.contains("Search")) {
            sp.edit().remove("Search").apply();
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pencarian");
        }

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH && !TextUtils.isEmpty(search.toString())) {
                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                    intent.putExtra(SearchResultActivity.EXTRA_CARI, search.getText().toString());
                    startActivity(intent);
                }
                return false;
            }
        });

        kategoriMakanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString("Search", "kategori").apply();
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra(SearchResultActivity.EXTRA_KATEGORI, "Makanan");
                startActivity(intent);
            }
        });

        kategoriMinuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString("Search", "kategori").apply();
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra(SearchResultActivity.EXTRA_KATEGORI, "Minuman");
                startActivity(intent);
            }
        });

        kategoriToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString("Search", "kategori").apply();
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra(SearchResultActivity.EXTRA_KATEGORI, "Toko/Apotek");
                startActivity(intent);
            }
        });

        kategoriJasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString("Search", "kategori").apply();
                Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                intent.putExtra(SearchResultActivity.EXTRA_KATEGORI, "Jasa/Lainnya");
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

    @Override
    protected void onResume() {
        super.onResume();
        if (sp.contains("Search")) {
            sp.edit().remove("Search").apply();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}