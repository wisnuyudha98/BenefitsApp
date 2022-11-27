package com.wisnuyudha.benefits_app.Activity;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.wisnuyudha.benefits_app.R;

public class DaftarUMKMActivity extends AppCompatActivity {

    private EditText namaUMKM, deskripsiUMKM, alamatUMKM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_umkm);

        namaUMKM = findViewById(R.id.input_nama_umkm);
        deskripsiUMKM = findViewById(R.id.input_deskripsi_umkm);
        alamatUMKM = findViewById(R.id.input_alamat_umkm);
    }
}