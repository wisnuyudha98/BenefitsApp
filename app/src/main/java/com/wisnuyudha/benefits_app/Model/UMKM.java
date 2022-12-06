package com.wisnuyudha.benefits_app.Model;

import com.google.gson.annotations.SerializedName;

public class UMKM {

    @SerializedName("id_umkm")
    private int id_umkm;

    @SerializedName("nama_umkm")
    private String namaUMKM;

    @SerializedName("deskripsi_umkm")
    private String deskripsiUMKM;

    @SerializedName("pengelola_umkm")
    private String pengelolaUMKM;

    @SerializedName("alamat_umkm")
    private String alamatUMKM;

    @SerializedName("kontak_umkm")
    private String kontakUMKM;

    @SerializedName("kategori")
    private String kategori;

    @SerializedName("foto_umkm")
    private String fotoUMKM;

    public int getId_umkm() {
        return id_umkm;
    }

    public void setId_umkm(int id_umkm) {
        this.id_umkm = id_umkm;
    }

    public String getNamaUMKM() {
        return namaUMKM;
    }

    public void setNamaUMKM(String namaUMKM) {
        this.namaUMKM = namaUMKM;
    }

    public String getDeskripsiUMKM() {
        return deskripsiUMKM;
    }

    public void setDeskripsiUMKM(String deskripsiUMKM) {
        this.deskripsiUMKM = deskripsiUMKM;
    }

    public String getPengelolaUMKM() {
        return pengelolaUMKM;
    }

    public void setPengelolaUMKM(String pengelolaUMKM) {
        this.pengelolaUMKM = pengelolaUMKM;
    }

    public String getAlamatUMKM() {
        return alamatUMKM;
    }

    public void setAlamatUMKM(String alamatUMKM) {
        this.alamatUMKM = alamatUMKM;
    }

    public String getKontakUMKM() {
        return kontakUMKM;
    }

    public void setKontakUMKM(String kontakUMKM) {
        this.kontakUMKM = kontakUMKM;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getFotoUMKM() {
        return fotoUMKM;
    }

    public void setFotoUMKM(String fotoUMKM) {
        this.fotoUMKM = fotoUMKM;
    }
}
