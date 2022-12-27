package com.wisnuyudha.benefits_app.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.wisnuyudha.benefits_app.Model.Produk;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TambahEditProdukActivity extends AppCompatActivity {

    private EditText inputNamaProduk, inputDeskripsiProduk, inputHargaProduk;
    private Button buttonInputProduk, buttonDeleteProduk;
    ApiInterface mApiInterface;
    SharedPreferences sp;
    Toolbar toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String namaOriginal;

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

        sp = getSharedPreferences("LOGIN", MODE_PRIVATE);

        if (sp.contains("Status") && sp.getString("Status", "").equals("Edit")) {
            inputNamaProduk.setText(getIntent().getStringExtra(EXTRA_NAMA_PRODUK));
            inputDeskripsiProduk.setText(getIntent().getStringExtra(EXTRA_DESKRIPSI_PRODUK));
            inputHargaProduk.setText(getIntent().getStringExtra(EXTRA_HARGA_PRODUK));
            buttonDeleteProduk.setVisibility(View.VISIBLE);
            namaOriginal = inputNamaProduk.getText().toString();
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Edit Produk");
            }
        }
        else {
            buttonDeleteProduk.setVisibility(View.GONE);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Tambah Produk");
            }
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

                    Map<String, Object> produk = new HashMap<>();
                    produk.put("nama_produk", namaProduk);
                    produk.put("deskripsi_produk", deskripsiProduk);
                    produk.put("harga_produk", hargaProduk);
                    produk.put("nama_umkm", namaUMKM);

                    if (sp.contains("Status") && sp.getString("Status", "").equals("Edit")) {
                        db.collection("produk")
                                .whereEqualTo("nama_produk", namaOriginal)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                            String id = documentSnapshot.getId();
                                            db.collection("produk").document(id).set(produk, SetOptions.merge())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Log.d(TAG, "Berhasil mengubah produk");
                                                            sp.edit().remove("Status").apply();
                                                            finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(TambahEditProdukActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TambahEditProdukActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else {
                        db.collection("produk")
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        List<Produk> listProduk = queryDocumentSnapshots.toObjects(Produk.class);
                                        String lastItem = listProduk.get(listProduk.size() - 1).getNama_produk();
                                        db.collection("produk")
                                                .whereEqualTo("nama_produk", lastItem)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                                            int produkNum = Integer.parseInt(documentSnapshot.getId()) + 1;
                                                            String produkId = "";

                                                            if (String.valueOf(produkNum).length() == 1) {
                                                                produkId = "0000" + produkNum;
                                                            }
                                                            if (String.valueOf(produkNum).length() == 2) {
                                                                produkId = "000" + produkNum;
                                                            }
                                                            if (String.valueOf(produkNum).length() == 3) {
                                                                produkId = "00" + produkNum;
                                                            }
                                                            if (String.valueOf(produkNum).length() == 4) {
                                                                produkId = "0" + produkNum;
                                                            }
                                                            if (String.valueOf(produkNum).length() == 5) {
                                                                produkId = String.valueOf(produkNum);
                                                            }

                                                            db.collection("produk").document(produkId).set(produk)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            Log.d(TAG, "Berhasil menambahkan data");
                                                                            finish();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(TambahEditProdukActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(TambahEditProdukActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(TambahEditProdukActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }
        });

        buttonDeleteProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("produk")
                        .whereEqualTo("nama_produk", namaOriginal)
                        .limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                    String id = documentSnapshot.getId();
                                    db.collection("produk").document(id).delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d(TAG, "Berhasil menghapus produk");
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(TambahEditProdukActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });
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