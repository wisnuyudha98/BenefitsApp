package com.wisnuyudha.benefits_app.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wisnuyudha.benefits_app.Model.Ulasan;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahUlasanActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText inputUlasan, referralLain;
    private RadioButton radioApp, radioKerabat, radioLain;
    private Button buttonInputUlasan;
    private Spinner nilaiUlasan;
    private ApiInterface mApiInterface;
    public static final String EXTRA_NAMA_PRODUK = "nama_produk";
    public static final String EXTRA_PENULIS_ULASAN = "penulis_ulasan";
    String[] nilai = {"1", "2", "3", "4", "5"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_ulasan);

        inputUlasan = findViewById(R.id.input_ulasan);
        referralLain = findViewById(R.id.referral_lain_input);
        radioApp = findViewById(R.id.referral_app);
        radioKerabat = findViewById(R.id.referral_kerabat);
        radioLain = findViewById(R.id.referral_lain);
        buttonInputUlasan = findViewById(R.id.button_input_ulasan);
        nilaiUlasan = findViewById(R.id.nilai_ulasan);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        if (radioLain.isChecked()){
            referralLain.setVisibility(View.VISIBLE);
        }
        else {
            referralLain.setVisibility(View.INVISIBLE);
        }

        nilaiUlasan.setOnItemSelectedListener(this);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, nilai);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nilaiUlasan.setAdapter(arrayAdapter);

        buttonInputUlasan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String isiUlasan = "";
                String referral = "";
                String penulisUlasan = "";
                String namaProduk = "";
                float nilai = 1;

                if (TextUtils.isEmpty(inputUlasan.getText().toString())){
                    inputUlasan.setError("Ulasan tidak boleh kosong");
                }
                else if (radioLain.isChecked() && TextUtils.isEmpty(referralLain.getText().toString())){
                    referralLain.setError("Field tidak boleh kosong");
                }
                else{
                    isiUlasan = inputUlasan.getText().toString();
                    nilai = Float.parseFloat(nilaiUlasan.getSelectedItem().toString());
                    penulisUlasan = getIntent().getStringExtra(EXTRA_PENULIS_ULASAN);
                    namaProduk = getIntent().getStringExtra(EXTRA_NAMA_PRODUK);
                    if (radioApp.isChecked()){
                        referral = "Aplikasi";
                    }
                    else if (radioKerabat.isChecked()){
                        referral = "Teman/kerabat";
                    }
                    else {
                        referral = referralLain.getText().toString();
                    }
                }
                mApiInterface.addUlasan(penulisUlasan, namaProduk, isiUlasan, nilai, referral).enqueue(new Callback<Ulasan>() {
                    @Override
                    public void onResponse(Call<Ulasan> call, Response<Ulasan> response) {
                        if (response.isSuccessful()) {
                            Log.i(TAG, "Berhasil memposting ulasan." + response.body().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<Ulasan> call, Throwable t) {
                        Log.e(TAG, "Gagal memposting ulasan.");
                    }
                });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(TambahUlasanActivity.this, nilai[i], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}