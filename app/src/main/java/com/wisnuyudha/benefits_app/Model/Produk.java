package com.wisnuyudha.benefits_app.Model;

import com.google.gson.annotations.SerializedName;

public class Produk {

    @SerializedName("nama_produk")
    private String nama_produk;

    @SerializedName("deskripsi_produk")
    private String deskripsi_produk;

    @SerializedName("harga_produk")
    private int harga_produk;

    @SerializedName("nama_umkm")
    private String nama_umkm;

    public String getNama_produk() {
        return nama_produk;
    }

    public void setNama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }

    public String getDeskripsi_produk() {
        return deskripsi_produk;
    }

    public void setDeskripsi_produk(String deskripsi_produk) {
        this.deskripsi_produk = deskripsi_produk;
    }

    public int getHarga_produk() {
        return harga_produk;
    }

    public void setHarga_produk(int harga_produk) {
        this.harga_produk = harga_produk;
    }

    public String getNama_umkm() {
        return nama_umkm;
    }

    public void setNama_umkm(String nama_umkm) {
        this.nama_umkm = nama_umkm;
    }
}
