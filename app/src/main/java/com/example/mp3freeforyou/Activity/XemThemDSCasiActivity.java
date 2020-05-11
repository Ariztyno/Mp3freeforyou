package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XemThemDSCasiActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerViewXTDSAlbum;

    ArrayList<Casi> mangcasi;
    CasiAdapter adapter;

    private void GetData() {
        Dataservice dataservice= APIService.getService();
        Call<List<Casi>> callback=dataservice.GetDanhSachCasi();
        callback.enqueue(new Callback<List<Casi>>() {
            @Override
            public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                mangcasi= (ArrayList<Casi>) response.body();
                adapter=new CasiAdapter(XemThemDSCasiActivity.this,mangcasi);
                recyclerViewXTDSAlbum.setLayoutManager(new GridLayoutManager(XemThemDSCasiActivity.this,2));
                recyclerViewXTDSAlbum.setAdapter(adapter);
                Log.d("XemThemCasi","Lấy được:"+String.valueOf(mangcasi.size()));
            }

            @Override
            public void onFailure(Call<List<Casi>> call, Throwable t) {

            }
        });
    }

    private void init() {
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

    private void Anhxa() {
        toolbar=findViewById(R.id.tbXemThemDSCasi);
        recyclerViewXTDSAlbum=findViewById(R.id.recycleviewXemThemDSCasi);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_them_dscasi);
        Anhxa();
        init();
        GetData();
    }
}
