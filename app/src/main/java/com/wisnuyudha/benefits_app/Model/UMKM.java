package com.wisnuyudha.benefits_app.Model;

import com.google.firebase.firestore.GeoPoint;
import com.google.gson.annotations.SerializedName;

public class UMKM {

    @SerializedName("nama_umkm")
    private String nama_umkm;

    @SerializedName("deskripsi_umkm")
    private String deskripsi_umkm;

    @SerializedName("pengelola_umkm")
    private String pengelola_umkm;

    @SerializedName("alamat_umkm")
    private GeoPoint alamat_umkm;

    @SerializedName("kontak_umkm")
    private String kontak_umkm;

    @SerializedName("kategori")
    private String kategori;

    @SerializedName("foto_umkm")
    private String foto_umkm;

    private String lowercase_name;

    public String getNama_umkm() {
        return nama_umkm;
    }

    public void setNama_umkm(String nama_umkm) {
        this.nama_umkm = nama_umkm;
    }

    public String getDeskripsi_umkm() {
        return deskripsi_umkm;
    }

    public void setDeskripsi_umkm(String deskripsi_umkm) {
        this.deskripsi_umkm = deskripsi_umkm;
    }

    public String getPengelola_umkm() {
        return pengelola_umkm;
    }

    public void setPengelola_umkm(String pengelola_umkm) {
        this.pengelola_umkm = pengelola_umkm;
    }

    public GeoPoint getAlamat_umkm() {
        return alamat_umkm;
    }

    public void setAlamat_umkm(GeoPoint alamat_umkm) {
        this.alamat_umkm = alamat_umkm;
    }

    public String getKontak_umkm() {
        return kontak_umkm;
    }

    public void setKontak_umkm(String kontak_umkm) {
        this.kontak_umkm = kontak_umkm;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getFoto_umkm() {
        return foto_umkm;
    }

    public void setFoto_umkm(String foto_umkm) {
        this.foto_umkm = foto_umkm;
    }

    public String getLowercase_name() {
        return lowercase_name;
    }

    public void setLowercase_name(String lowercase_name) {
        this.lowercase_name = lowercase_name;
    }
}
