package com.wisnuyudha.benefits_app.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wisnuyudha.benefits_app.Adapter.ProdukAdapter;
import com.wisnuyudha.benefits_app.Model.GetProduk;
import com.wisnuyudha.benefits_app.Model.Produk;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListProdukActivity extends AppCompatActivity {

    private RecyclerView rvProduk;
    private RecyclerView.LayoutManager mLayoutManager;
    ApiInterface mApiInterface;
    public static final String EXTRA_NAMA_UMKM = "nama_umkm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_produk);

        rvProduk = findViewById(R.id.rv_produk);
        mLayoutManager = new LinearLayoutManager(this);
        rvProduk.setLayoutManager(mLayoutManager);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        data();
    }

    public void data() {
        String nama_umkm = getIntent().getStringExtra(EXTRA_NAMA_UMKM).toString();
        Call<GetProduk> ProdukCall = mApiInterface.getProduk("get_produk", nama_umkm);
        ProdukCall.enqueue(new Callback<GetProduk>() {
            @Override
            public void onResponse(Call<GetProduk> call, Response<GetProduk> response) {
                List<Produk> produkList = response.body().getListDataProduk();
                ProdukAdapter listProdukAdapter = new ProdukAdapter(produkList);
                rvProduk.setAdapter(listProdukAdapter);
            }

            @Override
            public void onFailure(Call<GetProduk> call, Throwable t) {
                Log.e(TAG, "Terjadi kesalahan");
            }
        });
    }
}