package com.wisnuyudha.benefits_app.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    ApiInterface mApiInterface;
    SharedPreferences sp;
    private Button buttonTambahUlasan;
    public static final String EXTRA_NAMA_UMKM = "nama_umkm";
    public static final String EXTRA_PENGELOLA_UMKM = "pengelola_umkm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ulasan);

        rvUlasan = findViewById(R.id.rv_ulasan);
        mLayoutManager = new LinearLayoutManager(this);
        rvUlasan.setLayoutManager(mLayoutManager);
        buttonTambahUlasan = findViewById(R.id.button_tambah_ulasan);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        sp = getSharedPreferences("LOGIN", MODE_PRIVATE);

        Boolean login_status = sp.getBoolean("USER_LOGGED", false);
        String user_name = sp.getString("USER_NAME", "");
        String pengelola_umkm = getIntent().getStringExtra(EXTRA_PENGELOLA_UMKM);

        data();

        if (user_name.equals(pengelola_umkm)) {
            buttonTambahUlasan.setVisibility(View.INVISIBLE);
        }
        else {
            buttonTambahUlasan.setVisibility(View.VISIBLE);
        }

        buttonTambahUlasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login_status && !user_name.equals("")) {
                    Intent intent = new Intent(ListUlasanActivity.this, TambahUlasanActivity.class);
                    intent.putExtra(TambahUlasanActivity.EXTRA_PENULIS_ULASAN, user_name);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(ListUlasanActivity.this, "Anda harus login terlebih dahulu untuk menulis ulasan", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void data() {
        String nama_umkm = getIntent().getStringExtra(EXTRA_NAMA_UMKM);
        Call<GetUlasan> UlasanCall = mApiInterface.getUlasan("get_ulasan", nama_umkm);
        UlasanCall.enqueue(new Callback<GetUlasan>() {
            @Override
            public void onResponse(Call<GetUlasan> call, Response<GetUlasan> response) {
                List<Ulasan> ulasanList = response.body().getListDataUlasan();
                UlasanAdapter listUlasanAdapter = new UlasanAdapter(ulasanList);
                rvUlasan.setAdapter(listUlasanAdapter);
                rvUlasan.setLayoutManager(new LinearLayoutManager(ListUlasanActivity.this));
            }

            @Override
            public void onFailure(Call<GetUlasan> call, Throwable t) {
                Log.e(TAG, "Terjadi kesalahan");
            }
        });
    }
}