package com.wisnuyudha.benefits_app.Model;

public class Ulasan {
    private String penulisUlasan;
    private String namaProduk;
    private String isiUlasan;
    private float nilaiUlasan;
    private int fotoPenulis;

    public String getPenulisUlasan() {
        return penulisUlasan;
    }

    public void setPenulisUlasan(String penulisUlasan) {
        this.penulisUlasan = penulisUlasan;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
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

    public int getFotoPenulis() {
        return fotoPenulis;
    }

    public void setFotoPenulis(int fotoPenulis) {
        this.fotoPenulis = fotoPenulis;
    }
}
