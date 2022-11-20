package com.wisnuyudha.benefits_app.Activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.Model.UMKM;
import com.wisnuyudha.benefits_app.Adapter.UMKMAdapter;
import com.wisnuyudha.benefits_app.UMKMData;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    public static final String EXTRA_CARI = "extra_cari";
    private RecyclerView rvUMKM;
    private ArrayList<UMKM> list = new ArrayList<>();
    private TextView searchHighlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        searchHighlight = findViewById(R.id.search_highlight);

        rvUMKM = findViewById(R.id.rv_umkm);

        list.addAll(UMKMData.getListData());
        String Query = getIntent().getStringExtra(EXTRA_CARI);
        String searchText = "Anda mencari: " + Query;

        searchHighlight.setText(searchText);
        showRecyclerList();
    }

    private void showRecyclerList(){
        rvUMKM.setLayoutManager(new LinearLayoutManager(this));
        UMKMAdapter listUMKMAdapter = new UMKMAdapter(list);
        rvUMKM.setAdapter(listUMKMAdapter);

        listUMKMAdapter.setOnItemClickCallback(new UMKMAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(UMKM umkm) {
                Toast.makeText(SearchResultActivity.this, "Membuka detail activity UMKM berisikan data " + umkm.getNamaUMKM(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}