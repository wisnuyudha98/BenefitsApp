package com.wisnuyudha.benefits_app.RestApi;

import com.wisnuyudha.benefits_app.Model.GetListUMKM;
import com.wisnuyudha.benefits_app.Model.GetPriceRange;
import com.wisnuyudha.benefits_app.Model.GetProduk;
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
    Call<GetListUMKM> getAllUMKM(@Query("function") String function);

    @GET("benefits.php")
    Call<GetListUMKM> getUMKMFromKategori(
            @Query("function") String function,
            @Query("kategori") String kategori);

    @GET("benefits.php")
    Call<GetListUMKM> getUMKM(
            @Query("function") String function,
            @Query("nama_umkm") String nama_umkm);

    @GET("benefits.php")
    Call<GetUlasan> getUlasan(
            @Query("function") String function,
            @Query("nama_umkm") String nama_umkm);

    @GET("benefits.php")
    Call<GetUser> getUser(
            @Query("function") String function,
            @Query("username") String username,
            @Query("password") String password);

    @GET("benefits.php")
    Call<GetUser> getFotoUser(
            @Query("function") String function,
            @Query("user_name") String user_name);

    @GET("benefits.php")
    Call<GetProduk> getProduk(
            @Query("function") String function,
            @Query("nama_umkm") String nama_umkm);

    @GET("benefits.php")
    Call<GetPriceRange> getPriceRange(
            @Query("function") String function,
            @Query("nama_umkm") String nama_umkm);

    @FormUrlEncoded
    @POST("benefits.php")
    Call<UMKM> addUpdateDeleteUMKM(
            @Query("function") String function,
            @Field("nama_umkm") String nama_umkm,
            @Field("deskripsi_umkm") String deskripsi_umkm,
            @Field("pengelola_umkm") String pengelola_umkm,
            @Field("alamat_umkm") String alamat_umkm,
            @Field("kontak_umkm") String kontak_umkm,
            @Field("kategori") String kategori,
            @Field("foto_umkm") String foto_umkm);

    @FormUrlEncoded
    @POST("add_update_delete.php")
    Call<Ulasan> addUlasan(
            @Query("function") String function,
            @Field("penulis_ulasan") String penulis_ulasan,
            @Field("nama_produk") String nama_produk,
            @Field("isi_ulasan") String isi_ulasan,
            @Field("nilai_ulasan") float nilai_ulasan,
            @Field("referral") String referral);

    @FormUrlEncoded
    @POST("add_update_delete.php")
    Call<User> addUser(
            @Query("function") String function,
            @Field("username") String username,
            @Field("password") String password,
            @Field("nama_user") String nama_user,
            @Field("user_role") String user_role,
            @Field("foto_user") String foto_user);

    @FormUrlEncoded
    @POST("add_update_delete.php")
    Call<Produk> addUpdateDeleteProduk(
            @Query("function") String function,
            @Field("nama_produk") String nama_produk,
            @Field("deskripsi_produk") String deskripsi_produk,
            @Field("harga_produk") int harga_produk,
            @Field("nama_umkm") String nama_umkm);

}
