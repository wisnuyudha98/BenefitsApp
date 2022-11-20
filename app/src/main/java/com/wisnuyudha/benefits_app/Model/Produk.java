package com.wisnuyudha.benefits_app.Model;

public class Produk {
    private String namaProduk;
    private String deskripsiProduk;
    private int hargaProduk;
    private int stokProduk;
    private boolean tersedia;
    private String namaUMKMProduk;
    private int fotoProduk;

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public String getDeskripsiProduk() {
        return deskripsiProduk;
    }

    public void setDeskripsiProduk(String deskripsiProduk) {
        this.deskripsiProduk = deskripsiProduk;
    }

    public int getHargaProduk() {
        return hargaProduk;
    }

    public void setHargaProduk(int hargaProduk) {
        this.hargaProduk = hargaProduk;
    }

    public int getStokProduk() {
        return stokProduk;
    }

    public void setStokProduk(int stokProduk) {
        this.stokProduk = stokProduk;
    }

    public boolean isTersedia() {
        return tersedia;
    }

    public void setTersedia(boolean tersedia) {
        this.tersedia = tersedia;
    }

    public String getNamaUMKMProduk() {
        return namaUMKMProduk;
    }

    public void setNamaUMKMProduk(String namaUMKMProduk) {
        this.namaUMKMProduk = namaUMKMProduk;
    }

    public int getFotoProduk() {
        return fotoProduk;
    }

    public void setFotoProduk(int fotoProduk) {
        this.fotoProduk = fotoProduk;
    }
}
