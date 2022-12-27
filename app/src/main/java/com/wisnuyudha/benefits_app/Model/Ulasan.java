package com.wisnuyudha.benefits_app.Model;

import com.google.gson.annotations.SerializedName;

public class Ulasan {

    @SerializedName("penulis_ulasan")
    private String penulis_ulasan;

    @SerializedName("nama_umkm")
    private String nama_umkm;

    @SerializedName("isi_ulasan")
    private String isi_ulasan;

    @SerializedName("nilai_ulasan")
    private float nilai_ulasan;

    @SerializedName("referral")
    private String referral;

    public String getPenulis_ulasan() {
        return penulis_ulasan;
    }

    public void setPenulis_ulasan(String penulis_ulasan) {
        this.penulis_ulasan = penulis_ulasan;
    }

    public String getNama_umkm() {
        return nama_umkm;
    }

    public void setNama_umkm(String nama_umkm) {
        this.nama_umkm = nama_umkm;
    }

    public String getIsi_ulasan() {
        return isi_ulasan;
    }

    public void setIsi_ulasan(String isi_ulasan) {
        this.isi_ulasan = isi_ulasan;
    }

    public float getNilai_ulasan() {
        return nilai_ulasan;
    }

    public void setNilai_ulasan(float nilai_ulasan) {
        this.nilai_ulasan = nilai_ulasan;
    }

    public String getReferral() {
        return referral;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }
}
