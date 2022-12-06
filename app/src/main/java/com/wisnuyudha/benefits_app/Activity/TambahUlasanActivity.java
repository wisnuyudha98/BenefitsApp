package com.wisnuyudha.benefits_app.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wisnuyudha.benefits_app.Model.Ulasan;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

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
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

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
}