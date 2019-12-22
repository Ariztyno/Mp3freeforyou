package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mp3freeforyou.Adapter.XemThemDSAlbumAdapter;
import com.example.mp3freeforyou.Adapter.XemThemDSPlaylistAdapter;
import com.example.mp3freeforyou.Model.Album;
import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XemThemDSAlbumActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerViewXTDSAlbum;

    ArrayList<Album> mangalbum;
    XemThemDSAlbumAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_them_dsalbum);
        Anhxa();
        init();
        GetData();
    }

    private void GetData() {
        Dataservice dataservice= APIService.getService();
        Call<List<Album>> callback=dataservice.GetDanhSachAlbum();
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                mangalbum= (ArrayList<Album>) response.body();
                adapter=new XemThemDSAlbumAdapter(XemThemDSAlbumActivity.this,mangalbum);
                recyclerViewXTDSAlbum.setLayoutManager(new GridLayoutManager(XemThemDSAlbumActivity.this,2));
                recyclerViewXTDSAlbum.setAdapter(adapter);
                Log.d("XemThemAlbum","Lấy được:"+String.valueOf(mangalbum.size()));
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Albums");

        toolbar.setTitleTextColor(Color.parseColor("#ff39aa"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Anhxa() {
        toolbar=findViewById(R.id.tbXemThemDSAlbum);
        recyclerViewXTDSAlbum=findViewById(R.id.recycleviewXemThemDSAlbum);
    }
}
