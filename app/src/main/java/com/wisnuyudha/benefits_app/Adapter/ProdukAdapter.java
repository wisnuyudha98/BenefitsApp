package com.wisnuyudha.benefits_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wisnuyudha.benefits_app.Model.Produk;
import com.wisnuyudha.benefits_app.R;

import java.util.ArrayList;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ListViewHolder> {

    private ArrayList<Produk> listProduk;
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    public ProdukAdapter(ArrayList<Produk> list){
        this.listProduk = list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_produk, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Produk produk = listProduk.get(position);
        Glide.with(holder.itemView.getContext())
                .load(produk.getFotoProduk())
                .apply(new RequestOptions().override(75, 75))
                .into(holder.fotoProduk);
        holder.namaProduk.setText(produk.getNamaProduk());
        holder.deskripsiProduk.setText(produk.getDeskripsiProduk());
        holder.stokProduk.setText(produk.getStokProduk());
        holder.hargaProduk.setText(produk.getHargaProduk());
    }

    @Override
    public int getItemCount() {
        return listProduk.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView fotoProduk;
        TextView namaProduk, deskripsiProduk, stokProduk, hargaProduk;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoProduk = itemView.findViewById(R.id.foto_produk);
            namaProduk = itemView.findViewById(R.id.nama_produk);
            deskripsiProduk = itemView.findViewById(R.id.deskripsi_produk);
            stokProduk = itemView.findViewById(R.id.stok_produk);
            hargaProduk = itemView.findViewById(R.id.harga_produk);
        }
    }

    public interface OnItemClickCallback{
        void onItemClicked(Produk produk);
    }
}
