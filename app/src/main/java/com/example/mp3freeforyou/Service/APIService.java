package com.example.mp3freeforyou.Service;

//lớp cầu nối giữa API và lớp Dataservice
public class APIService {
    private static String base_url="https://mp3freeforyou.000webhostapp.com/";

    //lấy dữ liệu phía server
    public static Dataservice getService(){
        return APIRetrofitClient.getClient(base_url).create(Dataservice.class);
    }
}
