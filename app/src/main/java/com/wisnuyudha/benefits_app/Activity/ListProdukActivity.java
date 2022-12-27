package com.wisnuyudha.benefits_app.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.wisnuyudha.benefits_app.Adapter.ProdukAdapter;
import com.wisnuyudha.benefits_app.Model.Produk;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import java.util.List;

public class ListProdukActivity extends AppCompatActivity {

    private RecyclerView rvProduk;
    private RecyclerView.LayoutManager mLayoutManager;
    ApiInterface mApiInterface;
    SharedPreferences sp;
    private Button buttonTambahProduk;
    public String nama_user, nama_umkm, pengelola_umkm;
    public static final String EXTRA_NAMA_UMKM = "nama_umkm";
    public static final String EXTRA_PENGELOLA_UMKM = "pengelola_umkm";
    Toolbar toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_produk);

        rvProduk = findViewById(R.id.rv_produk);
        mLayoutManager = new LinearLayoutManager(this);
        rvProduk.setLayoutManager(mLayoutManager);
        buttonTambahProduk = findViewById(R.id.button_tambah_produk);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        toolbar = findViewById(R.id.toolbar);

        sp = getSharedPreferences("LOGIN", MODE_PRIVATE);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Daftar Produk");
        }

        sp.edit().remove("Status").apply();
        nama_user = sp.getString("USER_NAME", "");
        nama_umkm = getIntent().getStringExtra(EXTRA_NAMA_UMKM);
        pengelola_umkm = getIntent().getStringExtra(EXTRA_PENGELOLA_UMKM);

        data();

        if (nama_user.equals(pengelola_umkm) || nama_user.equals("Admin Benefits")) {
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

    @Override
    protected void onResume() {
        data();
        super.onResume();
    }

    public void data() {
        db.collection("produk")
                .whereEqualTo("nama_umkm", nama_umkm)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Produk> listProduk = queryDocumentSnapshots.toObjects(Produk.class);
                        ProdukAdapter listProdukAdapter = new ProdukAdapter(listProduk, ListProdukActivity.this, getIntent().getStringExtra(EXTRA_PENGELOLA_UMKM));
                        rvProduk.setAdapter(listProdukAdapter);
                        rvProduk.setLayoutManager(new GridLayoutManager(ListProdukActivity.this, 2));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Terjadi kesalahan");
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