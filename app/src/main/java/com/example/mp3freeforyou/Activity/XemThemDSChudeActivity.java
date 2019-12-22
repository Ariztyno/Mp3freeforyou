package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mp3freeforyou.Adapter.XemThemDSChudeAdapter;
import com.example.mp3freeforyou.Model.Chudebaihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XemThemDSChudeActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView reXemThemDSChude;

    ArrayList<Chudebaihat> mangchude;
    XemThemDSChudeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_them_dschude);
        anhxa();
        init();
        GetData();
    }

    private void GetData() {
        Dataservice dataservice= APIService.getService();
        Call<List<Chudebaihat>> callback=dataservice.GetDanhSachChude();
        callback.enqueue(new Callback<List<Chudebaihat>>() {
            @Override
            public void onResponse(Call<List<Chudebaihat>> call, Response<List<Chudebaihat>> response) {
                mangchude= (ArrayList<Chudebaihat>) response.body();
                adapter=new XemThemDSChudeAdapter(XemThemDSChudeActivity.this,mangchude);
                reXemThemDSChude.setLayoutManager(new GridLayoutManager(XemThemDSChudeActivity.this,2));
                reXemThemDSChude.setAdapter(adapter);
                Log.d("XemThemDSChudeActivity",String.valueOf(mangchude.size()));
            }

            @Override
            public void onFailure(Call<List<Chudebaihat>> call, Throwable t) {

            }
        });
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chủ đề");

        toolbar.setTitleTextColor(Color.parseColor("#ff39aa"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        toolbar=findViewById(R.id.tbXemThemDSChude);
        reXemThemDSChude=findViewById(R.id.recycleviewXemThemDSChude);

    }
}
