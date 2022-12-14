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

import com.wisnuyudha.benefits_app.Adapter.UMKMAdapter;
import com.wisnuyudha.benefits_app.Model.GetListUMKM;
import com.wisnuyudha.benefits_app.Model.UMKM;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void dataSearch() {
        if (sp.contains("Search") && sp.getString("Search", "").equals("kategori")) {
            Call<GetListUMKM> getSearchList = mApiInterface.getUMKMFromKategori("get_umkm_from_kategori", getIntent().getStringExtra(EXTRA_KATEGORI));
            getSearchList.enqueue(new Callback<GetListUMKM>() {
                @Override
                public void onResponse(Call<GetListUMKM> call, Response<GetListUMKM> response) {
                    sp.edit().remove("Search").apply();
                    List<UMKM> listUMKM = response.body().getListDataUMKM();
                    UMKMAdapter listUMKMAdapter = new UMKMAdapter(listUMKM);
                    rvUMKM.setAdapter(listUMKMAdapter);
                    rvUMKM.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this));

                    listUMKMAdapter.setOnItemClickCallback(new UMKMAdapter.OnItemClickCallback() {
                        @Override
                        public void onItemClicked(UMKM umkm) {
                            showSelectedUMKM(umkm);
                        }
                    });
                }

                @Override
                public void onFailure(Call<GetListUMKM> call, Throwable t) {
                    emptyResult.setVisibility(View.VISIBLE);
                }
            });
        }
        else {
            Call<GetListUMKM> getSearchList = mApiInterface.getUMKM("get_umkm", getIntent().getStringExtra(EXTRA_CARI));
            getSearchList.enqueue(new Callback<GetListUMKM>() {
                @Override
                public void onResponse(Call<GetListUMKM> call, Response<GetListUMKM> response) {
                    sp.edit().remove("Search").apply();
                    List<UMKM> listUMKM = response.body().getListDataUMKM();
                    UMKMAdapter listUMKMAdapter = new UMKMAdapter(listUMKM);
                    rvUMKM.setAdapter(listUMKMAdapter);
                    rvUMKM.setLayoutManager(new LinearLayoutManager(SearchResultActivity.this));

                    listUMKMAdapter.setOnItemClickCallback(new UMKMAdapter.OnItemClickCallback() {
                        @Override
                        public void onItemClicked(UMKM umkm) {
                            showSelectedUMKM(umkm);
                        }
                    });
                }

                @Override
                public void onFailure(Call<GetListUMKM> call, Throwable t) {
                    emptyResult.setVisibility(View.VISIBLE);
                }
            });
        }
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
        intent.putExtra(DetailActivity.EXTRA_NAMA_UMKM, umkm.getNamaUMKM());
        intent.putExtra(DetailActivity.EXTRA_DESKRIPSI_UMKM, umkm.getDeskripsiUMKM());
        intent.putExtra(DetailActivity.EXTRA_ALAMAT_UMKM, umkm.getAlamatUMKM());
        intent.putExtra(DetailActivity.EXTRA_KONTAK_UMKM, umkm.getKontakUMKM());
        intent.putExtra(DetailActivity.EXTRA_KATEGORI, umkm.getKategori());
        intent.putExtra(DetailActivity.EXTRA_PENGELOLA_UMKM, umkm.getPengelolaUMKM());
        intent.putExtra(DetailActivity.EXTRA_FOTO_UMKM, umkm.getFotoUMKM());
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
            dataSearch();
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