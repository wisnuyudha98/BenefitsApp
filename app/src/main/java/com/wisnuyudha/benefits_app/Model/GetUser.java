package com.wisnuyudha.benefits_app.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUser {

    @SerializedName("status")
    String status;
    @SerializedName("message")
    String message;
    @SerializedName("data")
    List<User> listDataUser;

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

    public List<User> getListDataUser() {
        return listDataUser;
    }

    public void setListDataUser(List<User> listDataUser) {
        this.listDataUser = listDataUser;
    }
}
