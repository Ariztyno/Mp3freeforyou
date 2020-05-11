package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIRetrofitClient;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoiYActivity extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goi_y);
        anhxa();
        init();
        getListQuizanswer();
    }

    private void getListQuizanswer() {
        Dataservice dataservice= APIService.getService();
        //lấy list quiz idtheloai
        Call<String> calllistidtheloaiquiz=dataservice.getlistidtheloaiHosoNguoidung(PreferenceUtils.getUsername(getApplicationContext()));
        calllistidtheloaiquiz.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String ketqua=response.body();
                if(!ketqua.equals("")){
                    //lưu vô preference
                    PreferenceUtils.saveListIdTheloaibaihatfromQuizChoice(ketqua,getApplicationContext());
                    Log.d("GoiYActivity_idtheloai","success:"+PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getApplicationContext()));
                }else {
                    Log.d("GoiYActivity_idtheloai","fail");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("GoiYActivity","list quiz idtheloai somthing wrong");
            }
        });

        //lấy list id casi quiz
        Call<String> calllistidcasiquiz=dataservice.getlistidcasiHosoNguoidung(PreferenceUtils.getUsername(getApplicationContext()));
        calllistidcasiquiz.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String ketqua=response.body();
                if(!ketqua.equals("")){
                    //lưu vô preference
                    PreferenceUtils.saveListIdCasifromQuizChoice(ketqua,getApplicationContext());
                    Log.d("GoiYActivity_listidcasi","success:"+PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()));
                }else {
                    Log.d("GoiYActivity_listidcasi","fail");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("GoiYActivity","list quiz idcasi somthing wrong");
            }
        });
    }

    private void init() {
        //KHỞI TẠO CHO TOOLBAR
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Gợi ý cho bạn");

        toolbar.setTitleTextColor(Color.parseColor("#ff39aa"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        toolbar=findViewById(R.id.tbUserGoiy);
    }
}
