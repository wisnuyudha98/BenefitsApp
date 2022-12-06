package com.wisnuyudha.benefits_app.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
    private CheckBox checkboxMakananBerat, checkboxMakananRingan, checkboxMinuman, checkboxKerajinan;

    public static final String EXTRA_NAMA_UMKM = "default";
    public static final String EXTRA_PENGELOLA_UMKM = "default";
    public static final String EXTRA_DESKRIPSI_UMKM = "default";
    public static final String EXTRA_ALAMAT_UMKM = "default";
    public static final String EXTRA_KONTAK_UMKM = "default";
    public static final String EXTRA_KATEGORI = "default";
    public static final String EXTRA_FOTO_UMKM = "default";

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
        checkboxMakananBerat = findViewById(R.id.checkbox_makanan_berat);
        checkboxMakananRingan = findViewById(R.id.checkbox_makanan_ringan);
        checkboxMinuman = findViewById(R.id.checkbox_minuman);
        checkboxKerajinan = findViewById(R.id.checkbox_kerajinan);
        buttonDaftarUMKM = findViewById(R.id.button_daftar_umkm);
        buttonHapusUMKM = findViewById(R.id.button_hapus_umkm);

        sp = getSharedPreferences("LOGIN", MODE_PRIVATE);

        if (sp.contains("Status") && sp.getString("Status", "").equals("Edit")) {

            buttonDaftarUMKM.setText("Update");
            buttonHapusUMKM.setVisibility(View.VISIBLE);
            namaUMKM.setText(getIntent().getStringExtra(EXTRA_NAMA_UMKM));
            deskripsiUMKM.setText(getIntent().getStringExtra(EXTRA_DESKRIPSI_UMKM));
            alamatUMKM.setText(getIntent().getStringExtra(EXTRA_ALAMAT_UMKM));
            kontakUMKM.setText(getIntent().getStringExtra(EXTRA_KONTAK_UMKM));

            if (getIntent().getStringExtra(EXTRA_KATEGORI).contains("makanan berat")) {
                checkboxMakananBerat.setChecked(true);
            }
            if (getIntent().getStringExtra(EXTRA_KATEGORI).contains("makanan ringan")) {
                checkboxMakananRingan.setChecked(true);
            }
            if (getIntent().getStringExtra(EXTRA_KATEGORI).contains("minuman")) {
                checkboxMinuman.setChecked(true);
            }
            if (getIntent().getStringExtra(EXTRA_KATEGORI).contains("kerajinan")) {
                checkboxKerajinan.setChecked(true);
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
                else if (!checkboxMakananBerat.isChecked() && !checkboxMakananRingan.isChecked() && !checkboxMinuman.isChecked() && !checkboxKerajinan.isChecked()) {
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
                    if (checkboxMakananBerat.isChecked()) {
                        kategori += "makanan berat";
                    }
                    if (checkboxMakananRingan.isChecked()) {
                        if (!kategori.equals("")) {
                            kategori += ", ";
                        }
                        kategori += "makanan ringan";
                    }
                    if (checkboxMinuman.isChecked()) {
                        if (!kategori.equals("")) {
                            kategori += ", ";
                        }
                        kategori += "minuman";
                    }
                    if (checkboxKerajinan.isChecked()) {
                        if (!kategori.equals("")) {
                            kategori += ", ";
                        }
                        kategori += "kerajinan";
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
                String nama = "";
                mApiInterface.addUpdateDeleteUMKM("delete_umkm", nama, "", "", "", "", "", "").enqueue(new Callback<UMKM>() {
                    @Override
                    public void onResponse(Call<UMKM> call, Response<UMKM> response) {
                        if (response.isSuccessful()) {
                            Log.i(TAG, "Berhasil menghapus UMKM");
                        }
                    }

                    @Override
                    public void onFailure(Call<UMKM> call, Throwable t) {

                    }
                });
            }
        });
    }
}