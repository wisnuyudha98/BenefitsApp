package com.wisnuyudha.benefits_app.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.wisnuyudha.benefits_app.Model.Produk;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahProdukActivity extends AppCompatActivity {

    private EditText inputNamaProduk, inputDeskripsiProduk, inputHargaProduk;
    private Button buttonInputProduk;
    private ApiInterface mApiInterface;
    public static final String EXTRA_NAMA_UMKM = "nama_umkm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk);

        inputNamaProduk = findViewById(R.id.input_nama_produk);
        inputDeskripsiProduk = findViewById(R.id.input_deskripsi_produk);
        inputHargaProduk = findViewById(R.id.input_harga_produk);
        buttonInputProduk = findViewById(R.id.button_input_produk);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

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

                    mApiInterface.addUpdateProduk(namaProduk, deskripsiProduk, hargaProduk, namaUMKM).enqueue(new Callback<Produk>() {
                        @Override
                        public void onResponse(Call<Produk> call, Response<Produk> response) {
                            Log.i(TAG, "Berhasil menambahkan produk" + response.body().toString());
                        }

                        @Override
                        public void onFailure(Call<Produk> call, Throwable t) {
                            Log.e(TAG, "Gagal menambahkan Produk");
                        }
                    });
                }
            }
        });
    }
}