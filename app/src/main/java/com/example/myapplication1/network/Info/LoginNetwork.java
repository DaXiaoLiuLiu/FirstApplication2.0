package com.example.myapplication1.network.Info;


import android.util.Log;

import com.example.myapplication1.network.Info.entity.User;
import com.example.myapplication1.network.Info.entity.UserResponse;
import com.example.myapplication1.network.ServiceCreator;

import java.io.IOException;

import retrofit2.Response;

public class LoginNetwork {
    //获取实例，才可以进行网络请求
    private static final ServiceCreator<LoginService> serviceCreator = new ServiceCreator<>();
    private static final LoginService loginService=
            serviceCreator.ServiceCreate_L(LoginService.class);

    private static UserResponse LoginResult;//获取登录请求的结果
    private static UserResponse SignResult ;;//获取注册请求的结果

    //返回的结果，可能为null,这里需要改成同步方法
    public static UserResponse sign_in(User user) {

        if (user == null) {//这边进行判空处理
            Log.d("LoginNetWork", "user is empty");
        } else {
            try {
                Response<UserResponse> response;
                response = loginService.getLogin(user.getUserName(), user.getPassword()).execute();
                if (response.body() == null) {
                    Log.d("LoginNetWork","Login: 返回错误，返回的结果为空");
                    LoginResult = null;
                } else {
                    LoginResult = response.body();
                }
            } catch (IOException e) {
                Log.d("LoginNetWork","Login: 网络错误");
                e.printStackTrace();
            }


        }
        return LoginResult;
    }

    //返回的结果，可能为null
    public static UserResponse sign_up(User user){
        if(user == null){//这边进行判空处理
            Log.d("LoginNetWork","Register user is empty");
        }
        else {

            try {
                Response<UserResponse> response = loginService.
                        createUser(user.getUserName(),user.getPassword()).execute();
                if(response.body() == null){
                    Log.d("RegisterNetWork","Register: 返回错误，返回的结果为空");
                    SignResult = new UserResponse(false,"");
                }
                else {
                    SignResult = response.body();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("RegisterNetWork","Register: 网络连接错误");
            }
        }
        return SignResult;
    }

}