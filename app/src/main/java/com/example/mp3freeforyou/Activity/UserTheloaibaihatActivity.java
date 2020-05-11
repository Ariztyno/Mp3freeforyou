package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mp3freeforyou.Adapter.UserTheloaibaihatAdapter;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.Model.Theloaibaihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserTheloaibaihatActivity extends AppCompatActivity {
    ArrayList<Theloaibaihat> mangtheloai;
    UserTheloaibaihatAdapter adapter;
    RecyclerView reUserTheloaiYeuthich;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_theloaibaihat);
        anhxa();
        getdata();
    }

    private void getdata() {
        Dataservice dataservice= APIService.getService();
        Call<List<Theloaibaihat>> theloaiCall=dataservice.GetTheloaibaihatyeuthich(PreferenceUtils.getUsername(getApplicationContext()));
        theloaiCall.enqueue(new Callback<List<Theloaibaihat>>() {
            @Override
            public void onResponse(Call<List<Theloaibaihat>> call, Response<List<Theloaibaihat>> response) {
                mangtheloai= (ArrayList<Theloaibaihat>) response.body();
                adapter=new UserTheloaibaihatAdapter(UserTheloaibaihatActivity.this,mangtheloai);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserTheloaibaihatActivity.this);
                reUserTheloaiYeuthich.setLayoutManager(linearLayoutManager);
                reUserTheloaiYeuthich.setAdapter(adapter);
                Log.d("UserTheloaiActivity","Lấy được:"+String.valueOf(mangtheloai.size()));
            }

            @Override
            public void onFailure(Call<List<Theloaibaihat>> call, Throwable t) {
                Log.d("UserTheloaiActivity","call fail "+t.toString());
            }
        });
    }

    private void anhxa() {
        toolbar=findViewById(R.id.tbUserTheloai);
        reUserTheloaiYeuthich=findViewById(R.id.reUserTheloaiYeuthich);

        //KHỞI TẠO CHO TOOLBAR
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ca sĩ yêu thích");

        toolbar.setTitleTextColor(Color.parseColor("#ff39aa"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
