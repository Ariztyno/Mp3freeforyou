package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mp3freeforyou.Adapter.XemThemDSPlaylistAdapter;
import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XemThemDSPlaylistActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView reXemThemDSPlaylist;

    ArrayList<Playlist> mangplaylist;
    XemThemDSPlaylistAdapter XTDSPlaylistadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_them_dsplaylist);
        anhxa();
        intt();
        GetData();
    }

    private void GetData() {
        Dataservice dataservice= APIService.getService();
        Call<List<Playlist>> callback=dataservice.GetDanhSachPlaylist();
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                mangplaylist= (ArrayList<Playlist>) response.body();
                XTDSPlaylistadapter=new XemThemDSPlaylistAdapter(XemThemDSPlaylistActivity.this,mangplaylist);
                reXemThemDSPlaylist.setLayoutManager(new GridLayoutManager(XemThemDSPlaylistActivity.this,2));
                reXemThemDSPlaylist.setAdapter((XTDSPlaylistadapter));
                Log.d("XemThemPlaylist","Lấy được:"+String.valueOf(mangplaylist.size()));
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });
    }

    private void intt() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Playlist");

        toolbar.setTitleTextColor(Color.parseColor("#ff39aa"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        toolbar = findViewById(R.id.tbXemThemDSPlaylist);
        reXemThemDSPlaylist=findViewById(R.id.recycleviewXemThemDSPlaylist);
    }
}
