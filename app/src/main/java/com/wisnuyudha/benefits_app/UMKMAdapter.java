package com.wisnuyudha.benefits_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class UMKMAdapter extends RecyclerView.Adapter<UMKMAdapter.ListViewHolder> {

    private ArrayList<UMKM> listUMKM;
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    public UMKMAdapter(ArrayList<UMKM> list){
        this.listUMKM = list;
    }
    @NonNull
    @Override
    public UMKMAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_umkm, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UMKMAdapter.ListViewHolder holder, int position) {
        UMKM umkm = listUMKM.get(position);
        Glide.with(holder.itemView.getContext())
                .load(umkm.getFotoUMKM())
                .apply(new RequestOptions().override(75, 75))
                .into(holder.fotoUMKM);
        holder.namaUMKM.setText(umkm.getNamaUMKM());
        holder.deskripsiUMKM.setText(umkm.getDeskripsiUMKM());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(listUMKM.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUMKM.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{

        ImageView fotoUMKM;
        TextView namaUMKM, deskripsiUMKM;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoUMKM = itemView.findViewById(R.id.foto_umkm_cari);
            namaUMKM = itemView.findViewById(R.id.nama_umkm_cari);
            deskripsiUMKM = itemView.findViewById(R.id.deskripsi_umkm_cari);
        }
    }

    public interface OnItemClickCallback{
        void onItemClicked(UMKM umkm);
    }
}
