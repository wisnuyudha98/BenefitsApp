package com.wisnuyudha.benefits_app.Model;

import com.google.gson.annotations.SerializedName;

public class Produk {

    @SerializedName("id_produk")
    private int idProduk;

    @SerializedName("nama_produk")
    private String namaProduk;

    @SerializedName("deskripsi_produk")
    private String deskripsiProduk;

    @SerializedName("harga_produk")
    private int hargaProduk;

    @SerializedName("nama_umkm")
    private String namaUMKMProduk;

    @SerializedName("foto_produk")
    private String fotoProduk;

    public int getIdProduk() {
        return idProduk;
    }

    public void setIdProduk(int idProduk) {
        this.idProduk = idProduk;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public String getDeskripsiProduk() {
        return deskripsiProduk;
    }

    public void setDeskripsiProduk(String deskripsiProduk) {
        this.deskripsiProduk = deskripsiProduk;
    }

    public int getHargaProduk() {
        return hargaProduk;
    }

    public void setHargaProduk(int hargaProduk) {
        this.hargaProduk = hargaProduk;
    }

    public String getNamaUMKMProduk() {
        return namaUMKMProduk;
    }

    public void setNamaUMKMProduk(String namaUMKMProduk) {
        this.namaUMKMProduk = namaUMKMProduk;
    }

    public String getFotoProduk() {
        return fotoProduk;
    }

    public void setFotoProduk(String fotoProduk) {
        this.fotoProduk = fotoProduk;
    }
}
