package com.wisnuyudha.benefits_app.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wisnuyudha.benefits_app.Model.User;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarUserActivity extends AppCompatActivity {

    private EditText daftarName, daftarUsername, daftarPassword;
    private RadioButton radioPengguna, radioPengusaha;
    private Button buttonDaftar;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_user);

        daftarName = findViewById(R.id.nama_daftar);
        daftarUsername = findViewById(R.id.username_daftar);
        daftarPassword = findViewById(R.id.password_daftar);
        radioPengguna = findViewById(R.id.daftar_pengguna_radio);
        radioPengusaha = findViewById(R.id.daftar_pengusaha_radio);
        buttonDaftar = findViewById(R.id.button_daftar_user);
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        daftarPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        buttonDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String error = "FIeld tidak boleh kosong";
                String username = "";
                String password = "";
                String nama = "";
                String role = "";

                if (TextUtils.isEmpty(daftarName.getText().toString())) {
                    daftarName.setError(error);
                }
                else if (TextUtils.isEmpty(daftarUsername.getText().toString())) {
                    daftarUsername.setError(error);
                }
                else if (TextUtils.isEmpty(daftarPassword.getText().toString())) {
                    daftarPassword.setError(error);
                }
                else {
                    username = daftarUsername.getText().toString();
                    password = daftarPassword.getText().toString();
                    nama = daftarName.getText().toString();
                    if (radioPengguna.isChecked()) {
                        role = "Pengguna biasa";
                    }
                    if (radioPengusaha.isChecked()) {
                        role = "Pengusaha";
                    }

                    mApiInterface.addUser("add_user", username, password, nama, role, "").enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                Log.i(TAG, "Pendaftaran berhasil" + response.body().toString());
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e(TAG, "Terjadi kesalahan");
                        }
                    });

                    Toast.makeText(DaftarUserActivity.this, "Data user dimasukkan ke database jika berhasil", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
}