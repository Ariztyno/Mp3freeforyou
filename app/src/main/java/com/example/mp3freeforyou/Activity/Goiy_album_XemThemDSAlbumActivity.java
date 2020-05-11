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
import com.example.mp3freeforyou.Model.Album;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Goiy_album_XemThemDSAlbumActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView reAlbum;
    ArrayList<Album> mangalbum;
    XemThemDSAlbumAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goiy_album__xem_them_dsalbum);
        anhxa();
        init();
        GetData();
    }

    private void GetData() {
        Dataservice dataservice= APIService.getService();
        if(PreferenceUtils.getUsername(getApplicationContext())!=null){
            Call<List<Album>> callback=dataservice.PostGoiyAlbumXemthem(PreferenceUtils.getUsername(getApplicationContext()));
            callback.enqueue(new Callback<List<Album>>() {
                @Override
                public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                    mangalbum= (ArrayList<Album>) response.body();
                    adapter=new XemThemDSAlbumAdapter(Goiy_album_XemThemDSAlbumActivity.this,mangalbum);
                    reAlbum.setLayoutManager(new GridLayoutManager(Goiy_album_XemThemDSAlbumActivity.this,2));
                    reAlbum.setAdapter(adapter);
                    Log.d("XemThemAlbum","Lấy được:"+String.valueOf(mangalbum.size()));
                }

                @Override
                public void onFailure(Call<List<Album>> call, Throwable t) {

                }
            });
        }else{
            if(PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()).matches(".*\\d.*") || PreferenceUtils.getBanListIdCaSi(getApplicationContext()).matches(".*\\d.*") || PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext()).matches(".*\\d.*")){
                //check if banlist hoặc quiz choice is null replace with ""
                if(PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()) == null){
                    PreferenceUtils.saveListIdCasifromQuizChoice("",getApplicationContext());
                }
                if(PreferenceUtils.getBanListIdCaSi(getApplicationContext()) == null){
                    PreferenceUtils.saveBanListIdCaSi("",getApplicationContext());
                }
                if(PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext())==null){
                    PreferenceUtils.saveListenHistoryForNoAcc("",getApplicationContext());
                }

                Call<List<Album>> callback=dataservice.PostGoiyAlbumForNoAccXemthem(PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext()),PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()),PreferenceUtils.getBanListIdCaSi(getApplicationContext()));
                callback.enqueue(new Callback<List<Album>>() {
                    @Override
                    public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                        mangalbum= (ArrayList<Album>) response.body();
                        adapter=new XemThemDSAlbumAdapter(Goiy_album_XemThemDSAlbumActivity.this,mangalbum);
                        reAlbum.setLayoutManager(new GridLayoutManager(Goiy_album_XemThemDSAlbumActivity.this,2));
                        reAlbum.setAdapter(adapter);
                        Log.d("XemThemAlbum","Lấy được:"+String.valueOf(mangalbum.size()));
                    }

                    @Override
                    public void onFailure(Call<List<Album>> call, Throwable t) {

                    }
                });
            }else{
                Call<List<Album>> callback=dataservice.PostGoiyAlbumForNoAccNoquizNobanXemthem();
                callback.enqueue(new Callback<List<Album>>() {
                    @Override
                    public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                        mangalbum= (ArrayList<Album>) response.body();
                        adapter=new XemThemDSAlbumAdapter(Goiy_album_XemThemDSAlbumActivity.this,mangalbum);
                        reAlbum.setLayoutManager(new GridLayoutManager(Goiy_album_XemThemDSAlbumActivity.this,2));
                        reAlbum.setAdapter(adapter);
                        Log.d("XemThemAlbum","Lấy được:"+String.valueOf(mangalbum.size()));
                    }

                    @Override
                    public void onFailure(Call<List<Album>> call, Throwable t) {

                    }
                });
            }
        }

    }

    private void init() {
        //KHỞI TẠO CHO TOOLBAR
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Album gợi ý");

        toolbar.setTitleTextColor(Color.parseColor("#ff39aa"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        toolbar=findViewById(R.id.tbUserGoiyXemthemAlbum);
        reAlbum=findViewById(R.id.recycleviewGoiyXemThemDSAlbum);
    }
}
