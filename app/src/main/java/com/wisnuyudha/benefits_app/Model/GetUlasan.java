package com.wisnuyudha.benefits_app.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUlasan {

    @SerializedName("status")
    String status;
    @SerializedName("message")
    String message;
    @SerializedName("data")
    List<Ulasan> listDataUlasan;

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

    public List<Ulasan> getListDataUlasan() {
        return listDataUlasan;
    }

    public void setListDataUlasan(List<Ulasan> listDataUlasan) {
        this.listDataUlasan = listDataUlasan;
    }
}
