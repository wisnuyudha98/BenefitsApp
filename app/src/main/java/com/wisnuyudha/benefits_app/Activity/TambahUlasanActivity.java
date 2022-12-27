package com.wisnuyudha.benefits_app.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.wisnuyudha.benefits_app.Model.Ulasan;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahUlasanActivity extends AppCompatActivity {

    private EditText inputUlasan, referralLain;
    private RadioGroup radioGroup;
    private RadioButton radioApp, radioKerabat, radioLain;
    private TextView headerReferral;
    private Button buttonInputUlasan;
    private RatingBar nilaiUlasan;
    private ApiInterface mApiInterface;
    public static final String EXTRA_NAMA_UMKM = "nama_produk";
    public static final String EXTRA_PENULIS_ULASAN = "penulis_ulasan";
    Toolbar toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_ulasan);

        inputUlasan = findViewById(R.id.input_ulasan);
        referralLain = findViewById(R.id.referral_lain_input);
        radioApp = findViewById(R.id.referral_app);
        radioKerabat = findViewById(R.id.referral_kerabat);
        radioLain = findViewById(R.id.referral_lain);
        headerReferral = findViewById(R.id.header_referral);
        buttonInputUlasan = findViewById(R.id.button_input_ulasan);
        nilaiUlasan = findViewById(R.id.nilai_ulasan);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Tambah Ulasan");
        }

        radioLain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    referralLain.setVisibility(View.VISIBLE);
                }
                else {
                    referralLain.setVisibility(View.GONE);
                }
            }
        });

        buttonInputUlasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String isiUlasan = "";
                String referral = "";
                String penulisUlasan = "";
                String namaUMKM = "";
                float nilai = 1;

                if (TextUtils.isEmpty(inputUlasan.getText().toString())){
                    inputUlasan.setError("Ulasan tidak boleh kosong");
                }
                else if (radioGroup.getCheckedRadioButtonId() == -1) {
                    headerReferral.setError("Pilih salah satu");
                }
                else if (radioLain.isChecked() && TextUtils.isEmpty(referralLain.getText().toString())){
                    referralLain.setError("Field tidak boleh kosong");
                }
                else {
                    isiUlasan = inputUlasan.getText().toString();
                    nilai = nilaiUlasan.getRating();
                    penulisUlasan = getIntent().getStringExtra(EXTRA_PENULIS_ULASAN);
                    namaUMKM = getIntent().getStringExtra(EXTRA_NAMA_UMKM);
                    if (radioApp.isChecked()) {
                        referral = "Aplikasi";
                    } else if (radioKerabat.isChecked()) {
                        referral = "Teman/kerabat";
                    } else {
                        referral = referralLain.getText().toString();
                    }

                    Map<String, Object> ulasan = new HashMap<>();
                    ulasan.put("penulis_ulasan", penulisUlasan);
                    ulasan.put("nama_umkm", namaUMKM);
                    ulasan.put("isi_ulasan", isiUlasan);
                    ulasan.put("nilai_ulasan", nilai);
                    ulasan.put("referral", referral);

                    db.collection("ulasan")
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<Ulasan> listUlasan = queryDocumentSnapshots.toObjects(Ulasan.class);
                                    int ulasanNum = listUlasan.size() + 1;
                                    String ulasanId = "";
                                    if (String.valueOf(ulasanNum).length() == 1) {
                                        ulasanId = "0000" + ulasanNum;
                                    }
                                    if (String.valueOf(ulasanNum).length() == 2) {
                                        ulasanId = "000" + ulasanNum;
                                    }
                                    if (String.valueOf(ulasanNum).length() == 3) {
                                        ulasanId = "00" + ulasanNum;
                                    }
                                    if (String.valueOf(ulasanNum).length() == 4) {
                                        ulasanId = "0" + ulasanNum;
                                    }
                                    if (String.valueOf(ulasanNum).length() == 5) {
                                        ulasanId = String.valueOf(ulasanNum);
                                    }

                                    db.collection("ulasan").document(ulasanId)
                                            .set(ulasan)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.i(TAG, "Berhasil memposting ulasan");
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e(TAG, "Gagal memposting ulasan.");                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Gagal memposting ulasan.");
                                }
                            });

                    mApiInterface.addUlasan("add_ulasan", penulisUlasan, namaUMKM, isiUlasan, nilai, referral).enqueue(new Callback<Ulasan>() {
                        @Override
                        public void onResponse(Call<Ulasan> call, Response<Ulasan> response) {
                            if (response.isSuccessful()) {
                                Log.i(TAG, "Berhasil memposting ulasan." + response.body().toString());
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Ulasan> call, Throwable t) {
                            Log.e(TAG, "Gagal memposting ulasan.");
                        }
                    });

                    Toast.makeText(TambahUlasanActivity.this, "Activity menambahkan ulasan user ke database", Toast.LENGTH_LONG).show();
                    finish();
                }
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