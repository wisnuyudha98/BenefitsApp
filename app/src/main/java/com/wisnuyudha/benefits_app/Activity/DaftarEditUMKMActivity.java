package com.wisnuyudha.benefits_app.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.wisnuyudha.benefits_app.Model.UMKM;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarEditUMKMActivity extends AppCompatActivity {

    private EditText namaUMKM, pengelolaUMKM, deskripsiUMKM, alamatUMKM, kontakUMKM;
    private TextView inputPengelolaHeader, checkboxHeader;
    private Button buttonDaftarUMKM, buttonHapusUMKM;
    private RadioGroup radioKategori;
    private RadioButton radioMakanan, radioToko, radioMinuman, radioJasa;

    public static final String EXTRA_NAMA_UMKM = "nama_umkm";
    public static final String EXTRA_PENGELOLA_UMKM = "pengelola_umkm";
    public static final String EXTRA_DESKRIPSI_UMKM = "deskripsi_umkm";
    public static final String EXTRA_ALAMAT_UMKM = "alamat_umkm";
    public static final String EXTRA_KONTAK_UMKM = "kontak_umkm";
    public static final String EXTRA_KATEGORI = "kategori";
    public static final String EXTRA_FOTO_UMKM = "foto_umkm";
    Toolbar toolbar;
    SharedPreferences sp;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_edit_umkm);

        namaUMKM = findViewById(R.id.input_nama_umkm);
        inputPengelolaHeader = findViewById(R.id.text_hint_input_pengelola_umkm);
        pengelolaUMKM = findViewById(R.id.input_pengelola_umkm);
        deskripsiUMKM = findViewById(R.id.input_deskripsi_umkm);
        alamatUMKM = findViewById(R.id.input_alamat_umkm);
        kontakUMKM = findViewById(R.id.input_kontak_umkm);
        checkboxHeader = findViewById(R.id.checkbox_header);
        radioMakanan = findViewById(R.id.radio_makanan);
        radioMinuman = findViewById(R.id.radio_minuman);
        radioToko = findViewById(R.id.radio_toko);
        radioJasa = findViewById(R.id.radio_jasa);
        buttonDaftarUMKM = findViewById(R.id.button_daftar_umkm);
        buttonHapusUMKM = findViewById(R.id.button_hapus_umkm);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pendaftaran UMKM");
        }

        sp = getSharedPreferences("LOGIN", MODE_PRIVATE);

        if (sp.contains("Status") && sp.getString("Status", "").equals("Edit")) {

            getSupportActionBar().setTitle("Edit data UMKM");

            buttonDaftarUMKM.setText("Update");
            buttonHapusUMKM.setVisibility(View.VISIBLE);
            namaUMKM.setText(getIntent().getStringExtra(EXTRA_NAMA_UMKM));
            deskripsiUMKM.setText(getIntent().getStringExtra(EXTRA_DESKRIPSI_UMKM));
            alamatUMKM.setText(getIntent().getStringExtra(EXTRA_ALAMAT_UMKM));
            kontakUMKM.setText(getIntent().getStringExtra(EXTRA_KONTAK_UMKM));

            if (getIntent().getStringExtra(EXTRA_KATEGORI).equals("Makanan")) {
                radioMakanan.setChecked(true);
            }
            if (getIntent().getStringExtra(EXTRA_KATEGORI).equals("Minuman")) {
                radioMinuman.setChecked(true);
            }
            if (getIntent().getStringExtra(EXTRA_KATEGORI).equals("Toko/Apotek")) {
                radioToko.setChecked(true);
            }
            if (getIntent().getStringExtra(EXTRA_KATEGORI).equals("Jasa/Lainnya")) {
                radioJasa.setChecked(true);
            }

            if (sp.getString("USER_ROLE", "").equals("admin")) {
                inputPengelolaHeader.setVisibility(View.VISIBLE);
                pengelolaUMKM.setVisibility(View.VISIBLE);
                pengelolaUMKM.setText(getIntent().getStringExtra(EXTRA_PENGELOLA_UMKM));
            }
            else {
                inputPengelolaHeader.setVisibility(View.GONE);
                pengelolaUMKM.setVisibility(View.GONE);
            }
        }
        else {
            buttonDaftarUMKM.setText("Daftar");
            buttonHapusUMKM.setVisibility(View.GONE);
        }

        buttonDaftarUMKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String error = "Field tidak boleh kosong";
                String nama = "";
                String pengelola = "";
                String deskripsi = "";
                String alamat = "";
                String kontak = "";
                String kategori = "";
                String foto = "";

                if (TextUtils.isEmpty(namaUMKM.getText().toString())){
                    namaUMKM.setError(error);
                }
                else if (TextUtils.isEmpty(pengelolaUMKM.getText().toString()) && sp.getString("USER_ROLE", "").equals("admin")){
                    deskripsiUMKM.setError(error);
                }
                else if (TextUtils.isEmpty(deskripsiUMKM.getText().toString())){
                    deskripsiUMKM.setError(error);
                }
                else if (TextUtils.isEmpty(alamatUMKM.getText().toString())){
                    alamatUMKM.setError(error);
                }
                else if (TextUtils.isEmpty(kontakUMKM.getText().toString())){
                    kontakUMKM.setError(error);
                }
                else if (!radioMakanan.isChecked() && !radioToko.isChecked() && !radioMinuman.isChecked() && !radioJasa.isChecked()) {
                    checkboxHeader.setError("Kategori tidak boleh kosong");
                }
                else {
                    nama = namaUMKM.getText().toString();
                    deskripsi = deskripsiUMKM.getText().toString();
                    alamat = alamatUMKM.getText().toString();
                    kontak = kontakUMKM.getText().toString();
                    if (sp.getString("USER_ROLE", "").equals("admin")) {
                        pengelola = pengelolaUMKM.getText().toString();
                    }
                    else {
                        pengelola = sp.getString("USER_NAME", "");
                    }
                    if (radioMakanan.isChecked()) {
                        kategori = "Makanan";
                    }

                    if (radioMinuman.isChecked()) {
                        kategori = "Minuman";
                    }

                    if (radioToko.isChecked()) {
                        kategori = "Toko/Apotek";
                    }

                    if (radioJasa.isChecked()) {
                        kategori = "Jasa/Lainnya";
                    }

                    if (sp.contains("Status") && sp.getString("Status", "").equals("Edit")) {
                        mApiInterface.addUpdateDeleteUMKM("update_umkm", nama, deskripsi, pengelola, alamat, kontak, kategori, foto).enqueue(new Callback<UMKM>() {
                            @Override
                            public void onResponse(Call<UMKM> call, Response<UMKM> response) {
                                if (response.isSuccessful()) {
                                    Log.i(TAG, "Barhasil mengubah data UMKM");
                                    sp.edit().remove("Status").apply();
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<UMKM> call, Throwable t) {

                            }
                        });
                    }
                    else {
                        mApiInterface.addUpdateDeleteUMKM("add_umkm", nama, deskripsi, pengelola, alamat, kontak, kategori, foto).enqueue(new Callback<UMKM>() {
                            @Override
                            public void onResponse(Call<UMKM> call, Response<UMKM> response) {
                                if (response.isSuccessful()) {
                                    Log.i(TAG, "UMKM berhasil didaftarkan" + response.body().toString());
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<UMKM> call, Throwable t) {
                                Log.e(TAG, "Terjadi kesalahan");
                            }
                        });
                    }
                    finish();
                }
            }
        });

        buttonHapusUMKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(DaftarEditUMKMActivity.this)
                        .setTitle("Konfirmasi")
                        .setMessage("Apakah anda yakin ingin menghapus?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String nama = getIntent().getStringExtra(EXTRA_NAMA_UMKM);
                                mApiInterface.addUpdateDeleteUMKM("delete_umkm", nama, "", "", "", "", "", "").enqueue(new Callback<UMKM>() {
                                    @Override
                                    public void onResponse(Call<UMKM> call, Response<UMKM> response) {
                                        if (response.isSuccessful()) {
                                            Log.i(TAG, "Berhasil menghapus UMKM");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UMKM> call, Throwable t) {
                                        Toast.makeText(DaftarEditUMKMActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
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