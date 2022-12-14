package com.wisnuyudha.benefits_app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.wisnuyudha.benefits_app.Adapter.UMKMAdapter;
import com.wisnuyudha.benefits_app.Config;
import com.wisnuyudha.benefits_app.Model.GetListUMKM;
import com.wisnuyudha.benefits_app.Model.GetUser;
import com.wisnuyudha.benefits_app.Model.UMKM;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    private DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    NavigationView navView;
    RecyclerView mainRv;
    ApiInterface mApiInterface;
    View navHeader;
    private TextView headerNamaUser;
    private ImageView headerFotoUser;
    Toolbar toolbar;

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
        mainRv = findViewById(R.id.rv_main_umkm_list);
        navHeader = navView.getHeaderView(0);
        headerFotoUser = navHeader.findViewById(R.id.nav_header_foto_user);
        headerNamaUser = navHeader.findViewById(R.id.nav_header_nama_user);
        toolbar = findViewById(R.id.toolbar);

        List<UMKM> listUMKM = Collections.emptyList();
        mainRv.setAdapter(new UMKMAdapter(listUMKM));

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        setNavHeader();
        data();

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        }

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_cari) {
                    Intent searchIntent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(searchIntent);
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

    public void data() {
        Call<GetListUMKM> listUMKMCall = mApiInterface.getAllUMKM("get_umkm_all");
        listUMKMCall.enqueue(new Callback<GetListUMKM>() {
            @Override
            public void onResponse(Call<GetListUMKM> call, Response<GetListUMKM> response) {
                List<UMKM> listUMKM = response.body().getListDataUMKM();
                UMKMAdapter listUMKMAdapter = new UMKMAdapter(listUMKM);
                mainRv.setAdapter(listUMKMAdapter);
                mainRv.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                listUMKMAdapter.setOnItemClickCallback(new UMKMAdapter.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(UMKM umkm) {
                        showSelectedUMKM(umkm);
                    }
                });
            }

            @Override
            public void onFailure(Call<GetListUMKM> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Terjadi kesalahan dalam memuat data UMKM", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, sp.getString("USER_NAME", "") + " - " + sp.getString("USER_ROLE", "") + " - " + sp.getString("USER_PHOTO", ""), Toast.LENGTH_SHORT).show();
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
        intent.putExtra(DetailActivity.EXTRA_NAMA_UMKM, umkm.getNamaUMKM());
        intent.putExtra(DetailActivity.EXTRA_DESKRIPSI_UMKM, umkm.getDeskripsiUMKM());
        intent.putExtra(DetailActivity.EXTRA_ALAMAT_UMKM, umkm.getAlamatUMKM());
        intent.putExtra(DetailActivity.EXTRA_KONTAK_UMKM, umkm.getKontakUMKM());
        intent.putExtra(DetailActivity.EXTRA_KATEGORI, umkm.getKategori());
        intent.putExtra(DetailActivity.EXTRA_PENGELOLA_UMKM, umkm.getPengelolaUMKM());
        intent.putExtra(DetailActivity.EXTRA_FOTO_UMKM, umkm.getFotoUMKM());
        startActivity(intent);
    }

    public void setNavHeader() {
        if (sp.contains("USER_NAME")) {
            headerNamaUser.setText(sp.getString("USER_NAME", ""));
            Glide.with(MainActivity.this)
                    .load(Config.USER_IMAGES_URL + sp.getString("USER_PHOTO", ""))
                    .apply(new RequestOptions().override(50,50))
                    .into(headerFotoUser);
        }
        else {
            headerNamaUser.setText("Guest user");
            Glide.with(MainActivity.this)
                    .load(R.drawable.ic_launcher_background)
                    .apply(new RequestOptions().override(50, 50))
                    .into(headerFotoUser);
        }
    }

    public void getFotoUser() {
        String username = sp.getString("USER_NAME", "");
        Call<GetUser> userCall = mApiInterface.getFotoUser("get_foto_user", username);
        userCall.enqueue(new Callback<GetUser>() {
            @Override
            public void onResponse(Call<GetUser> call, Response<GetUser> response) {
                String foto_user = response.body().getUser().getFotoUser();
                Glide.with(MainActivity.this)
                        .load(Config.USER_IMAGES_URL + foto_user)
                        .apply(new RequestOptions().override(50, 50))
                        .into(headerFotoUser);
            }

            @Override
            public void onFailure(Call<GetUser> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Terjadi kesalahan dalam memuat foto user", Toast.LENGTH_SHORT).show();
            }
        });
    }
}