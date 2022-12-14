package com.wisnuyudha.benefits_app.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.wisnuyudha.benefits_app.Model.Produk;
import com.wisnuyudha.benefits_app.Model.UMKM;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahEditProdukActivity extends AppCompatActivity {

    private EditText inputNamaProduk, inputDeskripsiProduk, inputHargaProduk;
    private Button buttonInputProduk, buttonDeleteProduk;
    ApiInterface mApiInterface;
    SharedPreferences sp;
    Toolbar toolbar;

    public static final String EXTRA_NAMA_UMKM = "nama_umkm";
    public static final String EXTRA_NAMA_PRODUK = "nama_produk";
    public static final String EXTRA_DESKRIPSI_PRODUK = "deksripsi_produk";
    public static final String EXTRA_HARGA_PRODUK = "harga_produk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_edit_produk);

        inputNamaProduk = findViewById(R.id.input_nama_produk);
        inputDeskripsiProduk = findViewById(R.id.input_deskripsi_produk);
        inputHargaProduk = findViewById(R.id.input_harga_produk);
        buttonInputProduk = findViewById(R.id.button_input_produk);
        buttonDeleteProduk = findViewById(R.id.button_delete_produk);
        toolbar = findViewById(R.id.toolbar);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sp = getSharedPreferences("LOGIN", MODE_PRIVATE);

        if (sp.contains("Status") && sp.getString("Status", "").equals("Edit")) {
            inputNamaProduk.setText(getIntent().getStringExtra(EXTRA_NAMA_PRODUK));
            inputDeskripsiProduk.setText(getIntent().getStringExtra(EXTRA_DESKRIPSI_PRODUK));
            inputHargaProduk.setText(getIntent().getStringExtra(EXTRA_HARGA_PRODUK));
            buttonDeleteProduk.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("Edit Produk");
        }
        else {
            buttonDeleteProduk.setVisibility(View.GONE);
            getSupportActionBar().setTitle("Tambah Produk");
        }

        buttonInputProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String error = "Field tidak boleh kosong";
                String namaProduk = "";
                String deskripsiProduk = "";
                String namaUMKM = "";
                int hargaProduk = 0;

                if (TextUtils.isEmpty(inputNamaProduk.getText().toString())){
                    inputNamaProduk.setError(error);
                }
                else if (TextUtils.isEmpty(inputDeskripsiProduk.getText().toString())){
                    inputDeskripsiProduk.setError(error);
                }
                else if (TextUtils.isEmpty(inputHargaProduk.getText().toString())){
                    inputHargaProduk.setError(error);
                }
                else {
                    namaProduk = inputNamaProduk.getText().toString();
                    deskripsiProduk = inputDeskripsiProduk.getText().toString();
                    hargaProduk = Integer.parseInt(inputHargaProduk.getText().toString());
                    namaUMKM = getIntent().getStringExtra(EXTRA_NAMA_UMKM);
                    if (sp.contains("Status") && sp.getString("Status", "").equals("Edit")) {
                        mApiInterface.addUpdateDeleteProduk("update_produk", namaProduk, deskripsiProduk, hargaProduk, namaUMKM).enqueue(new Callback<Produk>() {
                            @Override
                            public void onResponse(Call<Produk> call, Response<Produk> response) {
                                Log.i(TAG, "Berhasil mengubah produk" + response.body().toString());
                                sp.edit().remove("Status").apply();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Produk> call, Throwable t) {
                                Log.e(TAG, "Gagal mengubah Produk");
                            }
                        });
                    }
                    else {
                        mApiInterface.addUpdateDeleteProduk("add_produk", namaProduk, deskripsiProduk, hargaProduk, namaUMKM).enqueue(new Callback<Produk>() {
                            @Override
                            public void onResponse(Call<Produk> call, Response<Produk> response) {
                                Log.i(TAG, "Berhasil menambahkan produk" + response.body().toString());
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Produk> call, Throwable t) {
                                Log.e(TAG, "Gagal menambahkan Produk");
                            }
                        });
                    }
                }
            }
        });

        buttonDeleteProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama_umkm = inputNamaProduk.getText().toString();
                mApiInterface.addUpdateDeleteUMKM("delete_produk", nama_umkm, "", "", "", "", "", "").enqueue(new Callback<UMKM>() {
                    @Override
                    public void onResponse(Call<UMKM> call, Response<UMKM> response) {
                        Log.i(TAG, "Barhasil menghapus produk");
                        finish();
                    }

                    @Override
                    public void onFailure(Call<UMKM> call, Throwable t) {

                    }
                });
            }
        });
    }
}