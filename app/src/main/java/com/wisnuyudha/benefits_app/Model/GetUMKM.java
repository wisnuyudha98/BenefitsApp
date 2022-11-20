package com.wisnuyudha.benefits_app.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUMKM {

    @SerializedName("status")
    String status;
    @SerializedName("message")
    String message;
    @SerializedName("data")
    List<UMKM> listDataUMKM;

    public String getStatus() {
        return status;
    }

    public void setStatus() {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UMKM> getListDataUMKM() {
        return listDataUMKM;
    }

    public void setListDataUMKM(List<UMKM> listDataUMKM) {
        this.listDataUMKM = listDataUMKM;
    }
}
