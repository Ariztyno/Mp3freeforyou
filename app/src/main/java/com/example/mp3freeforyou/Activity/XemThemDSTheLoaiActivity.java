package com.example.mp3freeforyou.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mp3freeforyou.Adapter.XemThemDSTheLoaiAdapter;
import com.example.mp3freeforyou.Model.Theloaibaihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XemThemDSTheLoaiActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView reXTDSTheloai;

    ArrayList<Theloaibaihat> mantheloai;
    XemThemDSTheLoaiAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_them_dsthe_loai);
        anhxa();
        init();
        GetData();
    }

    private void GetData() {
        Dataservice dataservice= APIService.getService();
        Call<List<Theloaibaihat>> callback=dataservice.GetDanhSachTheloai();
        callback.enqueue(new Callback<List<Theloaibaihat>>() {
            @Override
            public void onResponse(Call<List<Theloaibaihat>> call, Response<List<Theloaibaihat>> response) {
                mantheloai = (ArrayList<Theloaibaihat>) response.body();
                adapter=new XemThemDSTheLoaiAdapter(XemThemDSTheLoaiActivity.this,mantheloai);
                reXTDSTheloai.setLayoutManager(new GridLayoutManager(XemThemDSTheLoaiActivity.this,2));
                reXTDSTheloai.setAdapter(adapter);
                Log.d("XemThemDSTheloai","Lấy được:"+String.valueOf(mantheloai.size()));
            }

            @Override
            public void onFailure(Call<List<Theloaibaihat>> call, Throwable t) {

            }
        });
    }


    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thể loại");

        toolbar.setTitleTextColor(Color.parseColor("#ff39aa"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        toolbar=findViewById(R.id.tbXemThemDSTheloai);
        reXTDSTheloai=findViewById(R.id.recycleviewXemThemDSTheloai);
    }
}
