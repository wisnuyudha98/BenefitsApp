package com.wisnuyudha.benefits_app.Activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wisnuyudha.benefits_app.Model.UMKM;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DaftarEditUMKMActivity extends AppCompatActivity {

    private EditText namaUMKM, pengelolaUMKM, deskripsiUMKM, kontakUMKM;
    private TextView inputPengelolaHeader, checkboxHeader;
    private ImageView preview;
    private Button buttonDaftarUMKM, buttonHapusUMKM, buttonPilihGambar;
    private RadioGroup radioKategori;
    private RadioButton radioMakanan, radioToko, radioMinuman, radioJasa;

    public static final String EXTRA_NAMA_UMKM = "nama_umkm";
    public static final String EXTRA_PENGELOLA_UMKM = "pengelola_umkm";
    public static final String EXTRA_DESKRIPSI_UMKM = "deskripsi_umkm";
    public static final String EXTRA_KONTAK_UMKM = "kontak_umkm";
    public static final String EXTRA_KATEGORI = "kategori";
    public static final String EXTRA_FOTO_UMKM = "foto_umkm";
    Toolbar toolbar;
    SharedPreferences sp;
    ApiInterface mApiInterface;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String namaOriginal;
    private Uri path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_edit_umkm);

        namaUMKM = findViewById(R.id.input_nama_umkm);
        inputPengelolaHeader = findViewById(R.id.text_hint_input_pengelola_umkm);
        pengelolaUMKM = findViewById(R.id.input_pengelola_umkm);
        deskripsiUMKM = findViewById(R.id.input_deskripsi_umkm);
        kontakUMKM = findViewById(R.id.input_kontak_umkm);
        preview = findViewById(R.id.preview_gambar_umkm);
        checkboxHeader = findViewById(R.id.checkbox_header);
        radioMakanan = findViewById(R.id.radio_makanan);
        radioMinuman = findViewById(R.id.radio_minuman);
        radioToko = findViewById(R.id.radio_toko);
        radioJasa = findViewById(R.id.radio_jasa);
        buttonDaftarUMKM = findViewById(R.id.button_daftar_umkm);
        buttonHapusUMKM = findViewById(R.id.button_hapus_umkm);
        buttonPilihGambar = findViewById(R.id.button_pilih_gambar);
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
            kontakUMKM.setText(getIntent().getStringExtra(EXTRA_KONTAK_UMKM));

            namaOriginal = namaUMKM.getText().toString();

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

        buttonDaftarUMKM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String error = "Field tidak boleh kosong";
                String nama = "";
                String deskripsi = "";
                String pengelola = "";
                GeoPoint alamat = new GeoPoint(0, 0);
                String kontak = "";
                String kategori = "";
                String foto = "";

                if (TextUtils.isEmpty(namaUMKM.getText().toString())){
                    namaUMKM.setError(error);
                }
                else if (TextUtils.isEmpty(deskripsiUMKM.getText().toString())){
                    deskripsiUMKM.setError(error);
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
                    kontak = kontakUMKM.getText().toString();
                    foto = "umkm/" + nama.toLowerCase(Locale.ROOT);
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
                    if (path != null) {
                        ProgressDialog progressDialog = new ProgressDialog(DaftarEditUMKMActivity.this);
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
                                        Toast.makeText(DaftarEditUMKMActivity.this, "Gagal mengunggah gambar", Toast.LENGTH_SHORT).show();
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

                    Map<String, Object> dataUMKM = new HashMap<>();
                    dataUMKM.put("nama_umkm", nama);
                    dataUMKM.put("deskripsi_umkm", deskripsi);
                    dataUMKM.put("pengelola_umkm", pengelola);
                    dataUMKM.put("alamat_umkm", alamat);
                    dataUMKM.put("kontak_umkm", kontak);
                    dataUMKM.put("kategori", kategori);
                    dataUMKM.put("foto_umkm", foto);

                    if (sp.contains("Status") && sp.getString("Status", "").equals("Edit")) {
                        db.collection("umkm")
                                .whereEqualTo("nama_umkm", namaOriginal)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                            String id = documentSnapshot.getId();
                                            db.collection("umkm").document(id).set(dataUMKM, SetOptions.merge())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Log.d(TAG, "Berhasil mengubah data");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(DaftarEditUMKMActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DaftarEditUMKMActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else {
                        db.collection("umkm")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        List<UMKM> data = task.getResult().toObjects(UMKM.class);
                                        int i = 0;
                                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                            i++;
                                            if (i == data.size()) {
                                                int UMKMNum = Integer.parseInt(documentSnapshot.getId()) + 1;
                                                String UMKMId = "";

                                                if (String.valueOf(UMKMNum).length() == 1) {
                                                    UMKMId = "0000" + UMKMNum;
                                                }
                                                if (String.valueOf(UMKMNum).length() == 2) {
                                                    UMKMId = "000" + UMKMNum;
                                                }
                                                if (String.valueOf(UMKMNum).length() == 3) {
                                                    UMKMId = "00" + UMKMNum;
                                                }
                                                if (String.valueOf(UMKMNum).length() == 4) {
                                                    UMKMId = "0" + UMKMNum;
                                                }
                                                if (String.valueOf(UMKMNum).length() == 5) {
                                                    UMKMId = String.valueOf(UMKMNum);
                                                }

                                                db.collection("umkm").document(UMKMId)
                                                        .set(dataUMKM)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Log.d(TAG, "Berhasil mengubah data");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(DaftarEditUMKMActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DaftarEditUMKMActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
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
                                db.collection("umkm")
                                        .whereEqualTo("nama_umkm", namaOriginal)
                                        .limit(1)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                                    String id = documentSnapshot.getId();
                                                    db.collection("umkm").document(id).delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Log.d(TAG, "Berhasil menghapus umkm");
                                                                    Intent intent = new Intent(DaftarEditUMKMActivity.this, MainActivity.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(DaftarEditUMKMActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
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