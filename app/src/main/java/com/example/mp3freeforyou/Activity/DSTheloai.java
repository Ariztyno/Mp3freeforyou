package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mp3freeforyou.Adapter.DSTheloaiAdapter;
import com.example.mp3freeforyou.Model.Chudebaihat;
import com.example.mp3freeforyou.Model.Theloaibaihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DSTheloai extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView reDSTheloai;

    Chudebaihat chude;
    ArrayList<Theloaibaihat> mangtheloai;
    DSTheloaiAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dstheloai);
        Dataintent();
        anhxa();
        init();
        GetData(chude.getIdChuDe());
    }

    private void Dataintent() {
        Intent intent=getIntent();
        if(intent!=null) {
            if(intent.hasExtra("itemchude")){
                chude= (Chudebaihat) intent.getSerializableExtra("itemchude");
            }
        }
    }

    private void GetData(String idchude) {
        Dataservice dataservice= APIService.getService();
        Call<List<Theloaibaihat>> callback=dataservice.GetDanhSachTheloaiTheoChude(idchude);
        callback.enqueue(new Callback<List<Theloaibaihat>>() {
            @Override
            public void onResponse(Call<List<Theloaibaihat>> call, Response<List<Theloaibaihat>> response) {
                mangtheloai= (ArrayList<Theloaibaihat>) response.body();
                adapter=new DSTheloaiAdapter(DSTheloai.this,mangtheloai);
                reDSTheloai.setLayoutManager(new GridLayoutManager(DSTheloai.this,2));
                reDSTheloai.setAdapter(adapter);
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
        toolbar=findViewById(R.id.tbDSTheloai);
        reDSTheloai=findViewById(R.id.recycleviewDSTheloai);
    }
}
