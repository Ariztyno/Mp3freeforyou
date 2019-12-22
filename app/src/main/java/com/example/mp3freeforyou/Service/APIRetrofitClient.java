package com.example.mp3freeforyou.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//lớp để khởi tạo Retrofit tương tác với server
public class APIRetrofitClient {
    private  static Retrofit retrofit=null;
    public static Retrofit getClient(String base_url){

        //kiểm tra kết nối với bên server
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .readTimeout(1000000,TimeUnit.MILLISECONDS)
                .writeTimeout(1000000,TimeUnit.MILLISECONDS)
                .connectTimeout(1000000,TimeUnit.MILLISECONDS)
                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();

        //convert từ khóa API bên server thành java
        Gson gson=new GsonBuilder().setLenient().create(); //tạo gson
        retrofit=new Retrofit.Builder() //tạo retrofit
                .baseUrl(base_url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit;
    }
}
