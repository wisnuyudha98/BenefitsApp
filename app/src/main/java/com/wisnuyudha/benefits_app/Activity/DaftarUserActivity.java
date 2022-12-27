package com.wisnuyudha.benefits_app.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wisnuyudha.benefits_app.Model.User;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DaftarUserActivity extends AppCompatActivity {

    private EditText daftarName, daftarUsername, daftarPassword;
    private RadioButton radioPengguna, radioPengusaha;
    private Button buttonDaftar, buttonPilihGambar;
    private ImageView preview;
    ApiInterface mApiInterface;
    Toolbar toolbar;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Uri path;

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
        buttonPilihGambar = findViewById(R.id.button_pilih_gambar_user);
        preview = findViewById(R.id.preview_gambar_user);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pendaftaran User Baru");
        }

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        daftarPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        buttonPilihGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(
                                intent,
                                "Silahkan upload gambar UMKM anda"),
                        22);
            }
        });

        buttonDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String error = "FIeld tidak boleh kosong";
                String username = "";
                String password = "";
                String nama = "";
                String role = "";
                String foto = "";

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
                    foto = "user/" + username;
                    if (radioPengguna.isChecked()) {
                        role = "Pengguna biasa";
                    }
                    if (radioPengusaha.isChecked()) {
                        role = "Pengusaha";
                    }
                    if (path != null) {
                        ProgressDialog progressDialog = new ProgressDialog(DaftarUserActivity.this);
                        progressDialog.setTitle("Mengunggah gambar...");
                        progressDialog.show();

                        StorageReference ref = storageReference.child(foto);

                        ref.putFile(path)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        progressDialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DaftarUserActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                                        progressDialog.setMessage("Gambar diunggah : " + (int)progress + "%");
                                    }
                                });
                    }

                    Map<String, Object> user = new HashMap<>();
                    user.put("username", username);
                    user.put("password", password);
                    user.put("nama_user", nama);
                    user.put("user_role", role);
                    user.put("foto_user", foto);

                    db.collection("user")
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<User> listUser = queryDocumentSnapshots.toObjects(User.class);
                                    int userNum = listUser.size() + 1;
                                    String userId = "";
                                    if (String.valueOf(userNum).length() == 1) {
                                        userId = "0000" + userNum;
                                    }
                                    if (String.valueOf(userNum).length() == 2) {
                                        userId = "000" + userNum;
                                    }
                                    if (String.valueOf(userNum).length() == 3) {
                                        userId = "00" + userNum;
                                    }
                                    if (String.valueOf(userNum).length() == 4) {
                                        userId = "0" + userNum;
                                    }
                                    if (String.valueOf(userNum).length() == 5) {
                                        userId = String.valueOf(userNum);
                                    }

                                    db.collection("user").document(userId)
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d(TAG, "Pendaftaran berhasil");
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(DaftarUserActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(DaftarUserActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                }
                            });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 22 && resultCode == RESULT_OK && data != null && data.getData()!= null) {
            path = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media
                        .getBitmap(getContentResolver(), path);
                preview.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}