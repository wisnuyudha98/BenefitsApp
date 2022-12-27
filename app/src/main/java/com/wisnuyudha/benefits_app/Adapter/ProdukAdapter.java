package com.wisnuyudha.benefits_app.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.wisnuyudha.benefits_app.Activity.TambahEditProdukActivity;
import com.wisnuyudha.benefits_app.Model.Produk;
import com.wisnuyudha.benefits_app.R;

import java.util.List;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ListViewHolder> {

    private List<Produk> listProduk;
    private OnItemClickCallback onItemClickCallback;
    private Context context;
    private String pengelola;
    SharedPreferences sp;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    public ProdukAdapter(List<Produk> list, Context context, String pengelola){
        this.listProduk = list;
        this.context = context;
        this.pengelola = pengelola;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_produk, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        sp = context.getSharedPreferences("LOGIN", MODE_PRIVATE);
        Produk produk = listProduk.get(position);
        holder.namaProduk.setText(produk.getNama_produk());
        holder.deskripsiProduk.setText(produk.getDeskripsi_produk());
        holder.hargaProduk.setText("Rp. " + produk.getHarga_produk());
        if (sp.contains("USER_ROLE") && (sp.getString("USER_ROLE", "").equals("Admin") || (sp.getString("USER_ROLE", "").equals("Pengusaha") && sp.getString("USER_NAME", "").equals(pengelola)))) {
            holder.buttonEditProduk.setVisibility(View.VISIBLE);
            holder.buttonEditProduk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp.edit().putString("Status", "Edit").apply();
                    Intent intent = new Intent(context, TambahEditProdukActivity.class);
                    intent.putExtra(TambahEditProdukActivity.EXTRA_NAMA_PRODUK, produk.getNama_produk());
                    intent.putExtra(TambahEditProdukActivity.EXTRA_DESKRIPSI_PRODUK, produk.getDeskripsi_produk());
                    intent.putExtra(TambahEditProdukActivity.EXTRA_HARGA_PRODUK, produk.getHarga_produk());
                    context.startActivity(intent);
                }
            });
        }
        else {
            holder.buttonEditProduk.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listProduk.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        TextView namaProduk, deskripsiProduk, hargaProduk;
        Button buttonEditProduk;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            namaProduk = itemView.findViewById(R.id.nama_produk);
            deskripsiProduk = itemView.findViewById(R.id.deskripsi_produk);
            hargaProduk = itemView.findViewById(R.id.harga_produk);
            buttonEditProduk = itemView.findViewById(R.id.button_edit_produk);
        }
    }

    public interface OnItemClickCallback{
        void onItemClicked(Produk produk);
    }
}
