package com.wisnuyudha.benefits_app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wisnuyudha.benefits_app.Model.GetPriceRange;
import com.wisnuyudha.benefits_app.Model.GetUlasan;
import com.wisnuyudha.benefits_app.Model.Ulasan;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private TextView namaUMKM, deskripsiUMKM, alamatUMKM, priceRangeUMKM, textRerataUlasanUMKM, kontakUMKM;
    private ImageView fotoUMKM;
    private RatingBar rerataUlasanUMKM;
    private LinearLayout priceRangeLayout, ulasanLayout;
    private Button buttonEditHapusUMKM;
    private String nama_umkm, deskripsi_umkm, alamat_umkm, kontak_umkm, pengelola_umkm, foto_umkm, kategori;
    public static final String EXTRA_NAMA_UMKM = "nama_umkm";
    public static final String EXTRA_DESKRIPSI_UMKM = "deskripsi_umkm";
    public static final String EXTRA_ALAMAT_UMKM = "alamat_umkm";
    public static final String EXTRA_KONTAK_UMKM = "kontak_umkm";
    public static final String EXTRA_PENGELOLA_UMKM = "pengelola_umkm";
    public static final String EXTRA_KATEGORI = "kategori";
    public static final String EXTRA_FOTO_UMKM = "foto_umkm";
    ApiInterface mApiInterface;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        namaUMKM = findViewById(R.id.nama_detail_umkm);
        deskripsiUMKM = findViewById(R.id.deskripsi_detail_umkm);
        alamatUMKM = findViewById(R.id.alamat_detail_umkm);
        kontakUMKM = findViewById(R.id.kontak_umkm);
        priceRangeUMKM = findViewById(R.id.price_range_detail);
        textRerataUlasanUMKM = findViewById(R.id.rerata_ulasan_text);
        fotoUMKM = findViewById(R.id.foto_detail_umkm);
        rerataUlasanUMKM = findViewById(R.id.rerata_ulasan);
        priceRangeLayout = findViewById(R.id.price_range);
        ulasanLayout = findViewById(R.id.ulasan);
        buttonEditHapusUMKM = findViewById(R.id.button_edit_hapus_umkm);

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        sp = getSharedPreferences("LOGIN", MODE_PRIVATE);

        nama_umkm = getIntent().getStringExtra(EXTRA_NAMA_UMKM);
        deskripsi_umkm = getIntent().getStringExtra(EXTRA_DESKRIPSI_UMKM);
        alamat_umkm = getIntent().getStringExtra(EXTRA_ALAMAT_UMKM);
        kontak_umkm = getIntent().getStringExtra(EXTRA_KONTAK_UMKM);
        kategori = getIntent().getStringExtra(EXTRA_KATEGORI);
        pengelola_umkm = getIntent().getStringExtra(EXTRA_PENGELOLA_UMKM);
        foto_umkm = getIntent().getStringExtra(EXTRA_FOTO_UMKM);

        Toast.makeText(this, "Menampilkan data " + nama_umkm, Toast.LENGTH_LONG).show();

        namaUMKM.setText(nama_umkm);
        deskripsiUMKM.setText(deskripsi_umkm);
        alamatUMKM.setText(alamat_umkm);
        kontakUMKM.setText(kontak_umkm);

        getPriceRange();
        getRatingUMKM();

        if (sp.getString("USER_ROLE", "").equals("admin") || (sp.getString("USER_NAME", "").equals(pengelola_umkm) && sp.getString("USER_ROLE", "").equals("pengusaha"))) {
            buttonEditHapusUMKM.setVisibility(View.VISIBLE);
            buttonEditHapusUMKM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DetailActivity.this, DaftarEditUMKMActivity.class);
                    intent.putExtra(DaftarEditUMKMActivity.EXTRA_NAMA_UMKM, nama_umkm);
                    intent.putExtra(DaftarEditUMKMActivity.EXTRA_PENGELOLA_UMKM, pengelola_umkm);
                    intent.putExtra(DaftarEditUMKMActivity.EXTRA_DESKRIPSI_UMKM, deskripsi_umkm);
                    intent.putExtra(DaftarEditUMKMActivity.EXTRA_ALAMAT_UMKM, alamat_umkm);
                    intent.putExtra(DaftarEditUMKMActivity.EXTRA_KONTAK_UMKM, kontak_umkm);
                    intent.putExtra(DaftarEditUMKMActivity.EXTRA_KATEGORI, kategori);
                    intent.putExtra(DaftarEditUMKMActivity.EXTRA_FOTO_UMKM, foto_umkm);
                    sp.edit().putString("Status", "Edit").apply();
                    startActivity(intent);
                }
            });
        }
        else {
            buttonEditHapusUMKM.setVisibility(View.GONE);
        }

        priceRangeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, ListProdukActivity.class);
                intent.putExtra(ListProdukActivity.EXTRA_NAMA_UMKM, nama_umkm);
                intent.putExtra(ListProdukActivity.EXTRA_PENGELOLA_UMKM, pengelola_umkm);
                startActivity(intent);
            }
        });

        ulasanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, ListUlasanActivity.class);
                intent.putExtra(ListUlasanActivity.EXTRA_NAMA_UMKM, nama_umkm);
                intent.putExtra(ListUlasanActivity.EXTRA_PENGELOLA_UMKM, pengelola_umkm);
                startActivity(intent);
            }
        });
    }

    public void getPriceRange() {
        Call<GetPriceRange> priceRange = mApiInterface.getPriceRange("get_price_range", nama_umkm);
        priceRange.enqueue(new Callback<GetPriceRange>() {
            @Override
            public void onResponse(Call<GetPriceRange> call, Response<GetPriceRange> response) {
                String harga_terrendah = String.valueOf(response.body().getHargaTerrendah().getHargaProduk());
                String harga_tertinggi = String.valueOf(response.body().getHargaTertinggi().getHargaProduk());
                String textPriceRange = harga_terrendah + " - " + harga_tertinggi;
                priceRangeUMKM.setText(textPriceRange);
            }

            @Override
            public void onFailure(Call<GetPriceRange> call, Throwable t) {

            }
        });
    }

    public void getRatingUMKM() {
        String nama_umkm = namaUMKM.getText().toString();
        Call<GetUlasan> RatingAvg = mApiInterface.getUlasan("get_rating_umkm", nama_umkm);
        RatingAvg.enqueue(new Callback<GetUlasan>() {
            @Override
            public void onResponse(Call<GetUlasan> call, Response<GetUlasan> response) {
                float rerata = 0;
                List<Ulasan> listUlasan = response.body().getListDataUlasan();
                for (int i = 0; i < listUlasan.size(); i++) {
                    Ulasan ulasan = listUlasan.get(i);
                    rerata += ulasan.getNilaiUlasan();
                }
                rerata /= listUlasan.size();

                rerataUlasanUMKM.setRating(rerata);
                String textRerata = String.valueOf(rerata) + " / 5";
                textRerataUlasanUMKM.setText(textRerata);
            }

            @Override
            public void onFailure(Call<GetUlasan> call, Throwable t) {

            }
        });
    }
}