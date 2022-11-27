package com.wisnuyudha.benefits_app.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wisnuyudha.benefits_app.Adapter.UlasanAdapter;
import com.wisnuyudha.benefits_app.Model.GetUlasan;
import com.wisnuyudha.benefits_app.Model.Ulasan;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListUlasanActivity extends AppCompatActivity {

    private RecyclerView rvUlasan;
    private RecyclerView.LayoutManager mLayoutManager;
    private ApiInterface mApiInterface;
    public static final String EXTRA_NAMA_UMKM = "nama_produk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ulasan);

        rvUlasan = findViewById(R.id.rv_ulasan);
        mLayoutManager = new LinearLayoutManager(this);
        rvUlasan.setLayoutManager(mLayoutManager);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        data();
    }

    public void data() {
        String nama_umkm = getIntent().getStringExtra(EXTRA_NAMA_UMKM).toString();
        Call<GetUlasan> UlasanCall = mApiInterface.getUlasan("get_ulasan", nama_umkm);
        UlasanCall.enqueue(new Callback<GetUlasan>() {
            @Override
            public void onResponse(Call<GetUlasan> call, Response<GetUlasan> response) {
                List<Ulasan> ulasanList = response.body().getListDataUlasan();
                UlasanAdapter listUlasanAdapter = new UlasanAdapter(ulasanList);
                rvUlasan.setAdapter(listUlasanAdapter);
            }

            @Override
            public void onFailure(Call<GetUlasan> call, Throwable t) {
                Log.e(TAG, "Terjadi kesalahan");
            }
        });
    }
}