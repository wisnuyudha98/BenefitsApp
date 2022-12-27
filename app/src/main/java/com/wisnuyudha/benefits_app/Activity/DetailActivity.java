package com.wisnuyudha.benefits_app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wisnuyudha.benefits_app.Model.Produk;
import com.wisnuyudha.benefits_app.Model.Ulasan;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private TextView namaUMKM, deskripsiUMKM, alamatUMKM, priceRangeUMKM, textRerataUlasanUMKM, kontakUMKM, hintPriceRange, hintRerataUlasan;
    private ImageView fotoUMKM;
    private RatingBar rerataUlasanUMKM;
    private LinearLayout priceRangeLayout, ulasanLayout;
    private String nama_umkm, deskripsi_umkm, alamat_umkm, kontak_umkm, pengelola_umkm, foto_umkm, kategori;
    private FloatingActionButton fabEdit;
    public static final String EXTRA_NAMA_UMKM = "nama_umkm";
    public static final String EXTRA_DESKRIPSI_UMKM = "deskripsi_umkm";
    public static final String EXTRA_KONTAK_UMKM = "kontak_umkm";
    public static final String EXTRA_PENGELOLA_UMKM = "pengelola_umkm";
    public static final String EXTRA_KATEGORI = "kategori";
    public static final String EXTRA_FOTO_UMKM = "foto_umkm";
    ApiInterface mApiInterface;
    SharedPreferences sp;
    Toolbar toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        namaUMKM = findViewById(R.id.nama_detail_umkm);
        deskripsiUMKM = findViewById(R.id.deskripsi_detail_umkm);
        alamatUMKM = findViewById(R.id.alamat_detail_umkm);
        kontakUMKM = findViewById(R.id.kontak_umkm);
        priceRangeUMKM = findViewById(R.id.price_range_detail);
        hintPriceRange = findViewById(R.id.price_range_hint);
        textRerataUlasanUMKM = findViewById(R.id.rerata_ulasan_text);
        hintRerataUlasan = findViewById(R.id.rerata_ulasan_hint);
        fotoUMKM = findViewById(R.id.foto_detail_umkm);
        rerataUlasanUMKM = findViewById(R.id.rerata_ulasan);
        priceRangeLayout = findViewById(R.id.price_range);
        ulasanLayout = findViewById(R.id.ulasan);
        fabEdit = findViewById(R.id.fab_edit);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Informasi UMKM");
        }

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        sp = getSharedPreferences("LOGIN", MODE_PRIVATE);

        String username = sp.getString("USER_NAME", "");
        String role = sp.getString("USER_ROLE", "");

        nama_umkm = getIntent().getStringExtra(EXTRA_NAMA_UMKM);
        deskripsi_umkm = getIntent().getStringExtra(EXTRA_DESKRIPSI_UMKM);
        kontak_umkm = getIntent().getStringExtra(EXTRA_KONTAK_UMKM);
        kategori = getIntent().getStringExtra(EXTRA_KATEGORI);
        pengelola_umkm = getIntent().getStringExtra(EXTRA_PENGELOLA_UMKM);
        foto_umkm = getIntent().getStringExtra(EXTRA_FOTO_UMKM);

        if (role.equals("admin") || (role.equals("pengusaha") && username.equals(pengelola_umkm))) {
            fabEdit.setVisibility(View.VISIBLE);
            fabEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp.edit().putString("Status", "Edit").apply();
                    Intent intent = new Intent(DetailActivity.this, DaftarEditUMKMActivity.class);
                    intent.putExtra(DaftarEditUMKMActivity.EXTRA_NAMA_UMKM, nama_umkm);
                    intent.putExtra(DaftarEditUMKMActivity.EXTRA_DESKRIPSI_UMKM, deskripsi_umkm);
                    intent.putExtra(DaftarEditUMKMActivity.EXTRA_KONTAK_UMKM, kontak_umkm);
                    intent.putExtra(DaftarEditUMKMActivity.EXTRA_KATEGORI, kategori);
                    intent.putExtra(DaftarEditUMKMActivity.EXTRA_PENGELOLA_UMKM, pengelola_umkm);
                    intent.putExtra(DaftarEditUMKMActivity.EXTRA_FOTO_UMKM, foto_umkm);
                    startActivity(intent);
                }
            });
        }
        else {
            fabEdit.setVisibility(View.GONE);
        }

        namaUMKM.setText(nama_umkm);
        deskripsiUMKM.setText(deskripsi_umkm);
        kontakUMKM.setText(kontak_umkm);

        hintPriceRange.setVisibility(View.VISIBLE);
        hintRerataUlasan.setVisibility(View.VISIBLE);

        StorageReference photo = FirebaseStorage.getInstance().getReference().child(foto_umkm);

        final long ONE_MEGABYTE = 1024 * 1024 * 100;
        photo.getBytes(ONE_MEGABYTE)
                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                fotoUMKM.setImageBitmap(bmp);
                            }
                        });

        getPriceRange();
        getRatingUMKM();
    }

    @Override
    protected void onResume() {
        getPriceRange();
        getRatingUMKM();
        super.onResume();
    }

    public void getPriceRange() {
        db.collection("produk")
                .whereEqualTo("nama_umkm", nama_umkm)
                .orderBy("harga_produk")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<Produk> listProduk = queryDocumentSnapshots.toObjects(Produk.class);
                            String textPriceRange = "Rp. " + listProduk.get(0).getHarga_produk() + " - Rp. " + listProduk.get(listProduk.size() - 1).getHarga_produk();

                            priceRangeUMKM.setText(textPriceRange);

                            priceRangeLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(DetailActivity.this, ListProdukActivity.class);
                                    intent.putExtra(ListProdukActivity.EXTRA_NAMA_UMKM, nama_umkm);
                                    intent.putExtra(ListProdukActivity.EXTRA_PENGELOLA_UMKM, pengelola_umkm);
                                    startActivity(intent);
                                }
                            });
                        }
                        else {
                            priceRangeUMKM.setText("Produk belum tersedia");
                            hintPriceRange.setVisibility(View.GONE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailActivity.this, "Gagal mengambil data produk", Toast.LENGTH_SHORT).show();
                        priceRangeUMKM.setText("Produk belum tersedia");
                        hintPriceRange.setVisibility(View.GONE);
                    }
                });
    }

    public void getRatingUMKM() {
        db.collection("ulasan")
                .whereEqualTo("nama_umkm", nama_umkm)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<Ulasan> listUlasan = queryDocumentSnapshots.toObjects(Ulasan.class);
                            float rerata = 0;
                            for (int i = 0; i < listUlasan.size(); i++) {
                                Ulasan ulasan = listUlasan.get(i);
                                rerata += ulasan.getNilai_ulasan();
                            }

                            rerata /= listUlasan.size();

                            rerataUlasanUMKM.setRating(rerata);
                            String textRerata = rerata + " / 5";
                            textRerataUlasanUMKM.setText(textRerata);

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
                        else {
                            hintRerataUlasan.setVisibility(View.GONE);
                            rerataUlasanUMKM.setRating(0);
                            textRerataUlasanUMKM.setText("Belum ada ulasan");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hintRerataUlasan.setVisibility(View.GONE);
                        rerataUlasanUMKM.setRating(0);
                        textRerataUlasanUMKM.setText("Belum ada ulasan");
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}