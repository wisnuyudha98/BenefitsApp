package com.wisnuyudha.benefits_app.Adapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wisnuyudha.benefits_app.Config;
import com.wisnuyudha.benefits_app.Model.GetUser;
import com.wisnuyudha.benefits_app.Model.Ulasan;
import com.wisnuyudha.benefits_app.R;
import com.wisnuyudha.benefits_app.RestApi.ApiClient;
import com.wisnuyudha.benefits_app.RestApi.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UlasanAdapter extends RecyclerView.Adapter<UlasanAdapter.ListViewHolder> {

    private List<Ulasan> listUlasan;
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    public UlasanAdapter(List<Ulasan> list){
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
        String nama_penulis = ulasan.getPenulis_ulasan();

        holder.penulisUlasan.setText(ulasan.getPenulis_ulasan());
        holder.referralUlasan.setText(ulasan.getReferral());
        holder.nilaiUlasan.setRating(ulasan.getNilai_ulasan());
        holder.isiUlasan.setText(ulasan.getIsi_ulasan());
    }

    @Override
    public int getItemCount() {
        return listUlasan.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{

        ImageView fotoUlasan;
        TextView penulisUlasan, isiUlasan, referralUlasan;
        RatingBar nilaiUlasan;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            fotoUlasan = itemView.findViewById(R.id.foto_ulasan);
            penulisUlasan = itemView.findViewById(R.id.penulis_ulasan);
            referralUlasan = itemView.findViewById(R.id.referral_ulasan);
            nilaiUlasan = itemView.findViewById(R.id.rating_ulasan);
            isiUlasan = itemView.findViewById(R.id.isi_ulasan);
        }
    }

    public interface OnItemClickCallback{
        void onItemClicked(Ulasan ulasan);
    }
}
