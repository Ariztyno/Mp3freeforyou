package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mp3freeforyou.Adapter.DSChudeconAdapter;
import com.example.mp3freeforyou.Model.Chudebaihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DSChudeconActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView reDSChudecon;

    Chudebaihat chude;
    ArrayList<Chudebaihat> mangchude;
    DSChudeconAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dschudecon);

        DataIntent();
        anhxa();
        init();
        if(chude!=null && !chude.getIdChuDe().equals("")){
            GetData(chude.getIdChuDe());
        }else {
            Log.d("DSChudecon","chude bị null");
        }

    }

    private void DataIntent() {
        Intent intent=getIntent();
        if(intent!=null){
            if(intent.hasExtra("itemchudecon")){
                chude= (Chudebaihat) intent.getSerializableExtra("itemchudecon");
            }else {
                Log.d("DSChudecon","intent extra có vấn đề");
            }
        }else {
            Log.d("DSChudecon","intent bị null");
        }
    }

    private void GetData(String idchude) {
        Dataservice dataservice= APIService.getService();
        Call<List<Chudebaihat>> callback=dataservice.GetDanhSachChudeCon(idchude);
        callback.enqueue(new Callback<List<Chudebaihat>>() {
            @Override
            public void onResponse(Call<List<Chudebaihat>> call, Response<List<Chudebaihat>> response) {
                mangchude= (ArrayList<Chudebaihat>) response.body();
                adapter=new DSChudeconAdapter(DSChudeconActivity.this,mangchude);
                reDSChudecon.setLayoutManager(new GridLayoutManager(DSChudeconActivity.this,2));
                reDSChudecon.setAdapter(adapter);
                Log.d("Chudeconlaydc",mangchude.get(0).getTenChuDe());
            }

            @Override
            public void onFailure(Call<List<Chudebaihat>> call, Throwable t) {

            }
        });
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(chude.getTenChuDe());

        toolbar.setTitleTextColor(Color.parseColor("#ff39aa"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        toolbar=findViewById(R.id.tbDSChudecon);
        reDSChudecon=findViewById(R.id.recycleviewDSChudecon);
    }
}
