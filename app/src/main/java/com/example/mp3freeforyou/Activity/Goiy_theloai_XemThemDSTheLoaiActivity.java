package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mp3freeforyou.Adapter.TheloaiAdapter;
import com.example.mp3freeforyou.Adapter.XemThemDSTheLoaiAdapter;
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

public class Goiy_theloai_XemThemDSTheLoaiActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView retheloai;
    ArrayList<Theloaibaihat> mangtheloai;
    XemThemDSTheLoaiAdapter theloaiAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goiy_theloai__xem_them_dsthe_loai);
        anhxa();
        init();
        GetData();
    }

    private void GetData() {
        Dataservice dataservice= APIService.getService();
        Call<List<Theloaibaihat>> callback=dataservice.PostGoiyTheloaiXemthem(PreferenceUtils.getUsername(getApplicationContext()));
        callback.enqueue(new Callback<List<Theloaibaihat>>() {
            @Override
            public void onResponse(Call<List<Theloaibaihat>> call, Response<List<Theloaibaihat>> response) {
                mangtheloai= (ArrayList<Theloaibaihat>) response.body();
                theloaiAdapter=new XemThemDSTheLoaiAdapter(Goiy_theloai_XemThemDSTheLoaiActivity.this,mangtheloai);
                retheloai.setLayoutManager(new GridLayoutManager(Goiy_theloai_XemThemDSTheLoaiActivity.this,2));
                retheloai.setAdapter(theloaiAdapter);
                Log.d("theloai","Yes");
            }

            @Override
            public void onFailure(Call<List<Theloaibaihat>> call, Throwable t) {

            }
        });
    }

    private void init() {
        //KHỞI TẠO CHO TOOLBAR
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
        toolbar=findViewById(R.id.tbUserGoiyXemthemTheloai);
        retheloai=findViewById(R.id.recycleviewGoiyXemThemDSTheloai);
    }
}
