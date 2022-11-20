package com.wisnuyudha.benefits_app.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProduk {

    @SerializedName("status")
    String status;
    @SerializedName("message")
    String message;
    @SerializedName("data")
    List<Produk> listDataProduk;

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

    public List<Produk> getListDataProduk() {
        return listDataProduk;
    }

    public void setListDataProduk(List<Produk> listDataProduk) {
        this.listDataProduk = listDataProduk;
    }
}
