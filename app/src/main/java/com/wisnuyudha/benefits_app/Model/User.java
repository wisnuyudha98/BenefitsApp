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
    private String userRole;

    @SerializedName("nama_user")
    private String namaUser;

    @SerializedName("foto_user")
    private String fotoUser;

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

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getFotoUser() {
        return fotoUser;
    }

    public void setFotoUser(String fotoUser) {
        this.fotoUser = fotoUser;
    }
}
