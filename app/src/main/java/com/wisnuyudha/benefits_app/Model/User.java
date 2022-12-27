package com.wisnuyudha.benefits_app.Model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id_user")
    private int id_user;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("user_role")
    private String user_role;

    @SerializedName("nama_user")
    private String nama_user;

    @SerializedName("foto_user")
    private String foto_user;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getFoto_user() {
        return foto_user;
    }

    public void setFoto_user(String foto_user) {
        this.foto_user = foto_user;
    }
}
