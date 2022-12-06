package com.wisnuyudha.benefits_app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
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
    private TextView searchHighlight;
    ApiInterface mApiInterface;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        searchHighlight = findViewById(R.id.search_highlight);

        rvUMKM = findViewById(R.id.rv_umkm);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        sp = getSharedPreferences("LOGIN", MODE_PRIVATE);

        if (sp.contains("Search") && sp.getString("Search", "").equals("kategori")) {
            searchHighlight.setText("Kategori: " + getIntent().getStringExtra(EXTRA_KATEGORI));
        }
        else {
            searchHighlight.setText("Anda mencari: " + getIntent().getStringExtra(EXTRA_CARI));
        }
        dataSearch();
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

                }
            });
        }
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
}