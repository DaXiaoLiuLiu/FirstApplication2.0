package com.example.myapplication1.network.Info;

import com.example.myapplication1.network.Info.entity.UInfo;
import com.example.myapplication1.network.Info.entity.UserInfoResponse;
import com.example.myapplication1.network.Info.entity.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {
    @FormUrlEncoded
    @POST("login")
    Call<UserResponse> getLogin(@Field("name") String name, @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<UserResponse> createUser(@Field("name") String name,@Field("password") String password);

    @FormUrlEncoded
    @POST("getInfo")
    Call<UserInfoResponse> getInfo(@Field("name") String name);

    @POST("setInfo")
    Call<UserInfoResponse> setInfo(@Body UInfo uInfo);
}
