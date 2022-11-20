package com.wisnuyudha.benefits_app.RestApi;

import com.wisnuyudha.benefits_app.Model.GetProduk;
import com.wisnuyudha.benefits_app.Model.GetUMKM;
import com.wisnuyudha.benefits_app.Model.GetUlasan;
import com.wisnuyudha.benefits_app.Model.GetUser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("benefits.php")
    Call<GetUMKM> getUMKM(@Query("function") String function);
    @GET("benefits.php")
    Call<GetUlasan> getUlasan(@Query("function") String fucntion);
    @GET("benefits.php")
    Call<GetUser> getUser(@Query("function") String function);
    @GET("benefits.php")
    Call<GetProduk> getProduk(@Query("function") String function);
}
