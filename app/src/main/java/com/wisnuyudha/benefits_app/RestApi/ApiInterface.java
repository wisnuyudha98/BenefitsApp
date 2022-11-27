package com.wisnuyudha.benefits_app.RestApi;

import com.wisnuyudha.benefits_app.Model.GetProduk;
import com.wisnuyudha.benefits_app.Model.GetUMKM;
import com.wisnuyudha.benefits_app.Model.GetUlasan;
import com.wisnuyudha.benefits_app.Model.GetUser;
import com.wisnuyudha.benefits_app.Model.Produk;
import com.wisnuyudha.benefits_app.Model.UMKM;
import com.wisnuyudha.benefits_app.Model.Ulasan;
import com.wisnuyudha.benefits_app.Model.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("benefits.php")
    Call<GetUMKM> getUMKM(@Query("function") String function);

    @GET("benefits.php")
    Call<GetUlasan> getUlasan(@Query("function") String function, String nama_umkm);

    @GET("benefits.php")
    Call<GetUser> getUser(@Query("function") String function);

    @GET("benefits.php")
    Call<GetProduk> getProduk(@Query("function") String function, String nama_umkm);

    @FormUrlEncoded
    @POST("benefits.php")
    Call<UMKM> addUpdateUMKM(
            @Field("nama_umkm") String nama_umkm,
            @Field("deskripsi_umkm") String deskripsi_umkm,
            @Field("pengelola_umkm") String pengelola_umkm,
            @Field("alamat_umkm") String alamat_umkm,
            @Field("kontak_umkm") String kontak_umkm,
            @Field("kategori") String kategori);

    @FormUrlEncoded
    @POST("benefits.php")
    Call<Ulasan> addUlasan(
            @Field("penulis_ulasan") String penulis_ulasan,
            @Field("nama_produk") String nama_produk,
            @Field("isi_ulasan") String isi_ulasan,
            @Field("nilai_ulasan") float nilai_ulasan,
            @Field("referral") String referral);

    @FormUrlEncoded
    @POST("benefits.php")
    Call<User> addUser(
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama-user") String nama_user,
            @Field("user_role") String user_role);

    @FormUrlEncoded
    @POST("benefits.php")
    Call<Produk> addUpdateProduk(
            @Field("nama_produk") String nama_produk,
            @Field("deskripsi_produk") String deskripsi_produk,
            @Field("harga_produk") int harga_produk,
            @Field("nama_umkm") String nama_umkm);
}
