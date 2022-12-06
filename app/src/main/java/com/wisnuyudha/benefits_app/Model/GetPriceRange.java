package com.wisnuyudha.benefits_app.Model;

import com.google.gson.annotations.SerializedName;

public class GetPriceRange {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("harga_tertinggi")
    private Produk hargaTertinggi;

    @SerializedName("harga_terrendah")
    private Produk hargaTerrendah;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Produk getHargaTertinggi() {
        return hargaTertinggi;
    }

    public void setHargaTertinggi(Produk hargaTertinggi) {
        this.hargaTertinggi = hargaTertinggi;
    }

    public Produk getHargaTerrendah() {
        return hargaTerrendah;
    }

    public void setHargaTerrendah(Produk hargaTerrendah) {
        this.hargaTerrendah = hargaTerrendah;
    }
}
