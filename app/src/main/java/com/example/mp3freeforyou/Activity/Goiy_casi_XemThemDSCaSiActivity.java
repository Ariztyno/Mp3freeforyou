package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mp3freeforyou.Adapter.CasiAdapter;
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

public class Goiy_casi_XemThemDSCaSiActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView reCasi;

    ArrayList<Casi> mangcasi;
    CasiAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goiy_casi__xem_them_dsca_si);
        anhxa();
        init();
        GetData();
    }

    private void GetData() {
        Dataservice dataservice= APIService.getService();
        Call<List<Casi>> call=dataservice.PostGoiyCasiXemthem(PreferenceUtils.getUsername(getApplicationContext()));
        call.enqueue(new Callback<List<Casi>>() {
            @Override
            public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                mangcasi= (ArrayList<Casi>) response.body();
                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(Goiy_casi_XemThemDSCaSiActivity.this);
                adapter =new CasiAdapter(Goiy_casi_XemThemDSCaSiActivity.this,mangcasi);
                reCasi.setLayoutManager(linearLayoutManager1);
                reCasi.setAdapter(adapter);
                Log.d("XemthemCasiGoiY","Yes");
            }
            @Override
            public void onFailure(Call<List<Casi>> call, Throwable t) {
                Log.d("XemthemCasiGoiY","Yes "+t);
            }
        });
    }

    private void init() {
        //KHỞI TẠO CHO TOOLBAR
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ca sĩ");

        toolbar.setTitleTextColor(Color.parseColor("#ff39aa"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        toolbar=findViewById(R.id.tbUserGoiyXemthemCasi);
        reCasi=findViewById(R.id.recycleviewGoiyXemThemDSCasi);
    }
}
