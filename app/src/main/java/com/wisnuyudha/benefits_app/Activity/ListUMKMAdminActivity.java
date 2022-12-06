package com.wisnuyudha.benefits_app.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.wisnuyudha.benefits_app.Adapter.UMKMAdapter;
import com.wisnuyudha.benefits_app.Model.GetListUMKM;
import com.wisnuyudha.benefits_app.Model.UMKM;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListUMKMAdminActivity extends AppCompatActivity {

    private RecyclerView rvadmin;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_umkmadmin);

        rvadmin = findViewById(R.id.rv_umkm_admin);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        data();
    }

    public void data() {
        Call<GetListUMKM> listUMKMCall = mApiInterface.getAllUMKM("get_umkm_all");
        listUMKMCall.enqueue(new Callback<GetListUMKM>() {
            @Override
            public void onResponse(Call<GetListUMKM> call, Response<GetListUMKM> response) {
                List<UMKM> listUMKM = response.body().getListDataUMKM();
                UMKMAdapter listUMKMAdapter = new UMKMAdapter(listUMKM);

                rvadmin.setAdapter(listUMKMAdapter);

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

    public void showSelectedUMKM (UMKM umkm) {
        Intent intent = new Intent(ListUMKMAdminActivity.this, DetailActivity.class);
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