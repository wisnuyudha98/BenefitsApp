package com.wisnuyudha.benefits_app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.wisnuyudha.benefits_app.Adapter.UMKMAdapter;
import com.wisnuyudha.benefits_app.Model.UMKM;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchResultActivity extends AppCompatActivity {

    public static final String EXTRA_CARI = "extra_cari";
    public static final String EXTRA_KATEGORI = "kategori";
    private RecyclerView rvUMKM;
    private ArrayList<UMKM> list = new ArrayList<>();
    private TextView searchHighlight, emptyResult;
    ApiInterface mApiInterface;
    SharedPreferences sp;
    Toolbar toolbar;
    private DataLoading dataLoading;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<UMKM> dataUMKM = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        searchHighlight = findViewById(R.id.search_highlight);
        emptyResult = findViewById(R.id.empty_result);
        rvUMKM = findViewById(R.id.rv_umkm);
        toolbar = findViewById(R.id.toolbar);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        emptyResult.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Hasil Pencarian");
        }

        dataLoading = new DataLoading();
        dataLoading.execute();
    }

    public void getSearchResult() {
        db.collection("umkm").
                get().
                addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        dataUMKM = queryDocumentSnapshots.toObjects(UMKM.class);
                        if (sp.contains("Search") && sp.getString("Search", "").equals("kategori")) {
                            List<UMKM> searchResult = new ArrayList<>();
                            for (int i = 0; i < dataUMKM.size(); i++) {
                                if (dataUMKM.get(i).getKategori().toLowerCase(Locale.ROOT).contains(getIntent().getStringExtra(EXTRA_KATEGORI).toLowerCase(Locale.ROOT))) {
                                    searchResult.add(dataUMKM.get(i));
                                }
                            }
                            UMKMAdapter listUMKMAdapter = new UMKMAdapter(searchResult, SearchResultActivity.this);
                            rvUMKM.setAdapter(listUMKMAdapter);
                            rvUMKM.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this));

                            listUMKMAdapter.setOnItemClickCallback(new UMKMAdapter.OnItemClickCallback() {
                                @Override
                                public void onItemClicked(UMKM umkm) {
                                    showSelectedUMKM(umkm);
                                }
                            });
                        }
                        else {
                            List<UMKM> searchResult = new ArrayList<>();
                            for (int i = 0; i < dataUMKM.size(); i++) {
                                if (dataUMKM.get(i).getNama_umkm().toLowerCase(Locale.ROOT).contains(getIntent().getStringExtra(EXTRA_CARI).toLowerCase(Locale.ROOT))) {
                                    searchResult.add(dataUMKM.get(i));
                                }
                            }
                            UMKMAdapter listUMKMAdapter = new UMKMAdapter(searchResult, SearchResultActivity.this);
                            rvUMKM.setAdapter(listUMKMAdapter);
                            rvUMKM.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this));

                            listUMKMAdapter.setOnItemClickCallback(new UMKMAdapter.OnItemClickCallback() {
                                @Override
                                public void onItemClicked(UMKM umkm) {
                                    showSelectedUMKM(umkm);
                                }
                            });
                        }
                    }
                }).
                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        emptyResult.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showSelectedUMKM(UMKM umkm) {
        Intent intent = new Intent(SearchResultActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_NAMA_UMKM, umkm.getNama_umkm());
        intent.putExtra(DetailActivity.EXTRA_DESKRIPSI_UMKM, umkm.getDeskripsi_umkm());
        intent.putExtra(DetailActivity.EXTRA_KONTAK_UMKM, umkm.getKontak_umkm());
        intent.putExtra(DetailActivity.EXTRA_KATEGORI, umkm.getKategori());
        intent.putExtra(DetailActivity.EXTRA_PENGELOLA_UMKM, umkm.getPengelola_umkm());
        intent.putExtra(DetailActivity.EXTRA_FOTO_UMKM, umkm.getFoto_umkm());
        startActivity(intent);
    }

    private class DataLoading extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        SharedPreferences sp;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SearchResultActivity.this, "Mengambil data", "Mohon menunggu");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
            if (sp.contains("Search") && sp.getString("Search", "").equals("kategori")) {
                searchHighlight.setText("Kategori: " + getIntent().getStringExtra(EXTRA_KATEGORI));
            }
            else {
                searchHighlight.setText("Anda mencari: " + getIntent().getStringExtra(EXTRA_CARI));
            }
            getSearchResult();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
        }
    }
}