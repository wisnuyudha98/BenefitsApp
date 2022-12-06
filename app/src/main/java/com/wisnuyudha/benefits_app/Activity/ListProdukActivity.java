package com.wisnuyudha.benefits_app.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    SharedPreferences sp;
    private Button buttonTambahProduk;
    public String nama_user, nama_umkm, pengelola_umkm;
    public static final String EXTRA_NAMA_UMKM = "nama_umkm";
    public static final String EXTRA_PENGELOLA_UMKM = "pengelola_umkm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_produk);

        rvProduk = findViewById(R.id.rv_produk);
        mLayoutManager = new LinearLayoutManager(this);
        rvProduk.setLayoutManager(mLayoutManager);
        buttonTambahProduk = findViewById(R.id.button_tambah_produk);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        sp = getSharedPreferences("LOGIN", MODE_PRIVATE);

        nama_user = sp.getString("USER_NAME", "");
        nama_umkm = getIntent().getStringExtra(EXTRA_NAMA_UMKM);
        pengelola_umkm = getIntent().getStringExtra(EXTRA_PENGELOLA_UMKM);

        data();

        if (nama_user.equals(pengelola_umkm)) {
            buttonTambahProduk.setVisibility(View.VISIBLE);
        }
        else {
            buttonTambahProduk.setVisibility(View.GONE);
        }

        buttonTambahProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListProdukActivity.this, TambahEditProdukActivity.class);
                intent.putExtra(TambahEditProdukActivity.EXTRA_NAMA_UMKM, nama_umkm);
                startActivity(intent);
            }
        });
    }

    public void data() {
        Call<GetProduk> ProdukCall = mApiInterface.getProduk("get_produk", nama_umkm);
        ProdukCall.enqueue(new Callback<GetProduk>() {
            @Override
            public void onResponse(Call<GetProduk> call, Response<GetProduk> response) {
                List<Produk> produkList = response.body().getListDataProduk();
                ProdukAdapter listProdukAdapter = new ProdukAdapter(produkList);
                rvProduk.setAdapter(listProdukAdapter);
                rvProduk.setLayoutManager(new LinearLayoutManager(ListProdukActivity.this));

                listProdukAdapter.setOnItemClickCallback(new ProdukAdapter.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(Produk produk) {
                        showSelectedProduk(produk);
                    }
                });
            }

            @Override
            public void onFailure(Call<GetProduk> call, Throwable t) {
                Log.e(TAG, "Terjadi kesalahan");
            }
        });
    }

    private void showSelectedProduk(Produk produk) {
        Intent intent = new Intent(ListProdukActivity.this, TambahEditProdukActivity.class);
        intent.putExtra(TambahEditProdukActivity.EXTRA_NAMA_UMKM, produk.getNamaUMKMProduk());
        intent.putExtra(TambahEditProdukActivity.EXTRA_NAMA_PRODUK, produk.getNamaProduk());
        intent.putExtra(TambahEditProdukActivity.EXTRA_DESKRIPSI_PRODUK, produk.getDeskripsiProduk());
        intent.putExtra(TambahEditProdukActivity.EXTRA_HARGA_PRODUK, produk.getHargaProduk());
        intent.putExtra(TambahEditProdukActivity.EXTRA_FOTO_PRODUK, produk.getFotoProduk());
        sp.edit().putString("Status", "Edit").apply();
        startActivity(intent);
    }
}