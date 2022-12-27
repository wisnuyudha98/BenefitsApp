package com.wisnuyudha.benefits_app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wisnuyudha.benefits_app.Model.UMKM;
import com.wisnuyudha.benefits_app.Model.User;
import com.wisnuyudha.benefits_app.R;

public class MainActivity extends AppCompatActivity{

    private DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    SharedPreferences sp;
    NavigationView navView;
    LinearLayout tertinggi, terenak, terdekat;
    View navHeader;
    private TextView headerNamaUser;
    private ImageView headerFotoUser, tertinggi1, tertinggi2, tertinggi3, terenak1, terenak2, terenak3, terdekat1, terdekat2, terdekat3;
    private EditText search;
    Toolbar toolbar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        sp.edit().remove("Status").apply();
        sp.edit().remove("Search").apply();

        navView = findViewById(R.id.nav_view);
        navHeader = navView.getHeaderView(0);
        headerFotoUser = navHeader.findViewById(R.id.nav_header_foto_user);
        headerNamaUser = navHeader.findViewById(R.id.nav_header_nama_user);
        toolbar = findViewById(R.id.toolbar);
        search = findViewById(R.id.main_search);
        tertinggi = findViewById(R.id.main_penilaian);
        terenak = findViewById(R.id.main_terenak);
        terdekat = findViewById(R.id.main_terdekat);
        tertinggi1 = findViewById(R.id.image_penilaian1);
        tertinggi2 = findViewById(R.id.image_penilaian2);
        tertinggi3 = findViewById(R.id.image_penilaian3);
        terenak1 = findViewById(R.id.image_terenak1);
        terenak2 = findViewById(R.id.image_terenak2);
        terenak3 = findViewById(R.id.image_terenak3);
        terdekat1 = findViewById(R.id.image_terdekat1);
        terdekat2 = findViewById(R.id.image_terdekat2);
        terdekat3 = findViewById(R.id.image_terdekat3);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        setNavHeader();
        setLogos();

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        }

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH && !TextUtils.isEmpty(search.toString())) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    intent.putExtra(SearchResultActivity.EXTRA_CARI, search.getText().toString());
                    startActivity(intent);
                }
                return false;
            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Menu menu = navView.getMenu();

                if (id == R.id.menu_kategori) {
                    boolean b = !menu.findItem(R.id.menu_kategori_makanan).isVisible();

                    menu.findItem(R.id.menu_kategori_makanan).setVisible(b);
                    menu.findItem(R.id.menu_kategori_minuman).setVisible(b);
                    menu.findItem(R.id.menu_kategori_toko).setVisible(b);
                    menu.findItem(R.id.menu_kategori_jasa).setVisible(b);
                }
                if (id == R.id.menu_kategori_makanan) {
                    sp.edit().putString("Search", "kategori").apply();
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    intent.putExtra(SearchResultActivity.EXTRA_KATEGORI, "Makanan");
                    startActivity(intent);
                }
                if (id == R.id.menu_kategori_minuman) {
                    sp.edit().putString("Search", "kategori").apply();
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    intent.putExtra(SearchResultActivity.EXTRA_KATEGORI, "Minuman");
                    startActivity(intent);
                }
                if (id == R.id.menu_kategori_toko) {
                    sp.edit().putString("Search", "kategori").apply();
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    intent.putExtra(SearchResultActivity.EXTRA_KATEGORI, "Toko/Apotek");
                    startActivity(intent);
                }
                if (id == R.id.menu_kategori_jasa) {
                    sp.edit().putString("Search", "kategori").apply();
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    intent.putExtra(SearchResultActivity.EXTRA_KATEGORI, "Jasa/lainnya");
                    startActivity(intent);
                }
                if (id == R.id.menu_login) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
                if (id == R.id.menu_daftar) {
                    Intent daftarUserIntent = new Intent(MainActivity.this, DaftarUserActivity.class);
                    startActivity(daftarUserIntent);
                }
                if (id == R.id.menu_daftar_umkm) {
                    Intent daftarUMKMIntent = new Intent(MainActivity.this, DaftarEditUMKMActivity.class);
                    startActivity(daftarUMKMIntent);
                    return true;
                }
                if (id == R.id.menu_logout) {
                    sp.edit().clear().apply();
                    finish();
                    startActivity(getIntent());
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sp.contains("Search")) {
            sp.edit().remove("Search").apply();
        }
        if (!sp.contains("USER_ROLE")) {
            navView.getMenu().findItem(R.id.menu_login).setVisible(true);
            navView.getMenu().findItem(R.id.menu_daftar).setVisible(true);
            navView.getMenu().findItem(R.id.menu_daftar_umkm).setVisible(false);
            navView.getMenu().findItem(R.id.menu_logout).setVisible(false);
            setNavHeader();
        }
        else if (sp.getString("USER_ROLE", "").equals("pengguna")) {
            navView.getMenu().findItem(R.id.menu_logout).setVisible(true);
            navView.getMenu().findItem(R.id.menu_daftar_umkm).setVisible(false);
            navView.getMenu().findItem(R.id.menu_login).setVisible(false);
            navView.getMenu().findItem(R.id.menu_daftar).setVisible(false);
            setNavHeader();
        }
        else {
            navView.getMenu().findItem(R.id.menu_logout).setVisible(true);
            navView.getMenu().findItem(R.id.menu_daftar_umkm).setVisible(true);
            navView.getMenu().findItem(R.id.menu_login).setVisible(false);
            navView.getMenu().findItem(R.id.menu_daftar).setVisible(false);
            setNavHeader();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSelectedUMKM(UMKM umkm) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_NAMA_UMKM, umkm.getNama_umkm());
        intent.putExtra(DetailActivity.EXTRA_DESKRIPSI_UMKM, umkm.getDeskripsi_umkm());
        intent.putExtra(DetailActivity.EXTRA_KONTAK_UMKM, umkm.getKontak_umkm());
        intent.putExtra(DetailActivity.EXTRA_KATEGORI, umkm.getKategori());
        intent.putExtra(DetailActivity.EXTRA_PENGELOLA_UMKM, umkm.getPengelola_umkm());
        intent.putExtra(DetailActivity.EXTRA_FOTO_UMKM, umkm.getFoto_umkm());
        startActivity(intent);
    }

    public void setLogos() {
        StorageReference photo1 = FirebaseStorage.getInstance().getReference().child("umkm/logo/logolacafaca.jpg");
        StorageReference photo2 = FirebaseStorage.getInstance().getReference().child("umkm/logo/logodkriuk.jpg");
        StorageReference photo3 = FirebaseStorage.getInstance().getReference().child("umkm/logo/logojavadimsum.jpg");
        StorageReference photo4 = FirebaseStorage.getInstance().getReference().child("umkm/logo/logokaza.jpg");
        StorageReference photo5 = FirebaseStorage.getInstance().getReference().child("umkm/logo/logosikembar.jpg");
        StorageReference photo6 = FirebaseStorage.getInstance().getReference().child("umkm/logo/logokegeprek.jpg");
        StorageReference photo7 = FirebaseStorage.getInstance().getReference().child("umkm/logo/logobasosederhana.jpg");
        StorageReference photo8 = FirebaseStorage.getInstance().getReference().child("umkm/logo/logopanconglumer.jpg");
        StorageReference photo9 = FirebaseStorage.getInstance().getReference().child("umkm/logo/logohayangthaitea.jpg");

        final long ONE_MEGABYTE = 1024 * 1024 * 100;
        photo1.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        tertinggi1.setImageBitmap(bmp);
                    }
                });
        photo2.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        tertinggi2.setImageBitmap(bmp);
                    }
                });
        photo3.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        tertinggi3.setImageBitmap(bmp);
                    }
                });
        photo4.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        terenak1.setImageBitmap(bmp);
                    }
                });
        photo5.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        terenak2.setImageBitmap(bmp);
                    }
                });
        photo6.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        terenak3.setImageBitmap(bmp);
                    }
                });
        photo7.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        terdekat1.setImageBitmap(bmp);
                    }
                });
        photo8.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        terdekat2.setImageBitmap(bmp);
                    }
                });
        photo9.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        terdekat3.setImageBitmap(bmp);
                    }
                });
    }

    public void setNavHeader() {
        if (sp.contains("USER_NAME") && !sp.getString("USER_NAME", "").equals("Guest User")) {
            db.collection("user")
                    .whereEqualTo("nama_user", sp.getString("USER_NAME", ""))
                    .limit(1)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                User user = documentSnapshot.toObject(User.class);
                                headerNamaUser.setText(user.getNama_user());
                                StorageReference photo = FirebaseStorage.getInstance().getReference().child(user.getFoto_user());

                                final long ONE_MEGABYTE = 1024 * 1024 * 100;
                                photo.getBytes(ONE_MEGABYTE)
                                        .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                            @Override
                                            public void onSuccess(byte[] bytes) {
                                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                headerFotoUser.setImageBitmap(bmp);
                                            }
                                        });
                            }
                        }
                    });
        }
        else {
            headerNamaUser.setText("Guest user");
            Glide.with(MainActivity.this)
                    .load(R.drawable.ic_launcher_background)
                    .apply(new RequestOptions().override(50, 50))
                    .into(headerFotoUser);
        }
    }
}