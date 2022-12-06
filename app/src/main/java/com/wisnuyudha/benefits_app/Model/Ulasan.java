package com.wisnuyudha.benefits_app.Model;

import com.google.gson.annotations.SerializedName;

public class Ulasan {

    @SerializedName("id_ulasan")
    private int id_ulasan;

    @SerializedName("penulis_ulasan")
    private String penulisUlasan;

    @SerializedName("nama_umkm")
    private String namaUMKM;

    @SerializedName("isi_ulasan")
    private String isiUlasan;

    @SerializedName("nilai_ulasan")
    private float nilaiUlasan;

    @SerializedName("referral")
    private String referral;

    public int getId_ulasan() {
        return id_ulasan;
    }

    public void setId_ulasan(int id_ulasan) {
        this.id_ulasan = id_ulasan;
    }

    public String getPenulisUlasan() {
        return penulisUlasan;
    }

    public void setPenulisUlasan(String penulisUlasan) {
        this.penulisUlasan = penulisUlasan;
    }

    public String getNamaUMKM() {
        return namaUMKM;
    }

    public void setNamaUMKM(String namaUMKM) {
        this.namaUMKM = namaUMKM;
    }

    public String getIsiUlasan() {
        return isiUlasan;
    }

    public void setIsiUlasan(String isiUlasan) {
        this.isiUlasan = isiUlasan;
    }

    public float getNilaiUlasan() {
        return nilaiUlasan;
    }

    public void setNilaiUlasan(float nilaiUlasan) {
        this.nilaiUlasan = nilaiUlasan;
    }

    public String getReferral() {
        return referral;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }
}
