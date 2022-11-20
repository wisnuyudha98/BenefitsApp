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
import com.wisnuyudha.benefits_app.Model.Ulasan;
import com.wisnuyudha.benefits_app.R;

import java.util.ArrayList;

public class UlasanAdapter extends RecyclerView.Adapter<UlasanAdapter.ListViewHolder> {

    private ArrayList<Ulasan> listUlasan;
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    public UlasanAdapter(ArrayList<Ulasan> list){
        this.listUlasan = list;
    }

    @NonNull
    @Override
    public UlasanAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_ulasan, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Ulasan ulasan = listUlasan.get(position);
        Glide.with(holder.itemView.getContext())
                .load(ulasan.getFotoPenulis())
                .apply(new RequestOptions().override(40, 40))
                .into(holder.fotoUlasan);
        holder.penulisUlasan.setText(ulasan.getPenulisUlasan());
        holder.isiUlasan.setText(ulasan.getIsiUlasan());
    }

    @Override
    public int getItemCount() {
        return listUlasan.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{

        ImageView fotoUlasan;
        TextView penulisUlasan, isiUlasan;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoUlasan = itemView.findViewById(R.id.foto_ulasan);
            penulisUlasan = itemView.findViewById(R.id.penulis_ulasan);
            isiUlasan = itemView.findViewById(R.id.isi_ulasan);
        }
    }

    public interface OnItemClickCallback{
        void onItemClicked(Ulasan ulasan);
    }
}
