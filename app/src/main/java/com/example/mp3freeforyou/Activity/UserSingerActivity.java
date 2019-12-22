package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mp3freeforyou.Adapter.UserCasiAdapter;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSingerActivity extends AppCompatActivity {
    ArrayList<Casi> mangcasi;
    UserCasiAdapter adapter;
    RecyclerView recCasiyeuthich;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_singer);
        anhxa();
        getdata();
    }

    private void getdata() {
        Dataservice dataservice= APIService.getService();
        Call<List<Casi>> casiCall=dataservice.GetCasiyeuthich(PreferenceUtils.getUsername(getApplicationContext()));
        casiCall.enqueue(new Callback<List<Casi>>() {
            @Override
            public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                mangcasi= (ArrayList<Casi>) response.body();
                adapter=new UserCasiAdapter(UserSingerActivity.this,mangcasi);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserSingerActivity.this);
                recCasiyeuthich.setLayoutManager(linearLayoutManager);
                recCasiyeuthich.setAdapter(adapter);
                Log.d("UserSingerActivity","Lấy được:"+String.valueOf(mangcasi.size()));
            }

            @Override
            public void onFailure(Call<List<Casi>> call, Throwable t) {
                Log.d("UserSingerActivity","call fail "+t.toString());
            }
        });
    }

    private void anhxa() {
        recCasiyeuthich=findViewById(R.id.reUserCasiYeuthich);
        toolbar=findViewById(R.id.tbUserCasi);

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
