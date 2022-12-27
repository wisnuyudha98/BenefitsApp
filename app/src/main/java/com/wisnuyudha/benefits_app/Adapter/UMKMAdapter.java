package com.wisnuyudha.benefits_app.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wisnuyudha.benefits_app.Model.UMKM;
import com.wisnuyudha.benefits_app.R;

import java.util.List;

public class UMKMAdapter extends RecyclerView.Adapter<UMKMAdapter.ListViewHolder> {

    private List<UMKM> listUMKM;
    private OnItemClickCallback onItemClickCallback;
    SharedPreferences sp;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    Context context;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    public UMKMAdapter(List<UMKM> list, Context context){
        this.listUMKM = list;
        this.context = context;
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
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(umkm.getFoto_umkm());

        final long ONE_MEGABYTE = 1024 * 1024 * 100;
        storageReference.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.fotoUMKM.setImageBitmap(bmp);
                    }
                });

        holder.namaUMKM.setText(umkm.getNama_umkm());
        holder.deskripsiUMKM.setText(umkm.getDeskripsi_umkm());

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
            fotoUMKM = itemView.findViewById(R.id.foto_umkm_list);
            namaUMKM = itemView.findViewById(R.id.nama_umkm_list);
            deskripsiUMKM = itemView.findViewById(R.id.deskripsi_umkm_list);
        }
    }

    public interface OnItemClickCallback{
        void onItemClicked(UMKM umkm);
    }
}
