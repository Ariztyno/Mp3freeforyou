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
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Goiy_playlist_XemThemDSPlaylistActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView reXemThemDSPlaylist;
    ArrayList<Playlist> mangplaylist;
    XemThemDSPlaylistAdapter XTDSPlaylistadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goiy_playlist__xem_them_dsplaylist);
        Anhxa();
        Getdata();
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.tbGoiyXemThemDSPlaylist);
        reXemThemDSPlaylist=findViewById(R.id.recycleviewGoiyXemThemDSPlaylist);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Playlist gợi ý");

        toolbar.setTitleTextColor(Color.parseColor("#ff39aa"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Getdata() {
        Dataservice dataservice= APIService.getService();
        if(PreferenceUtils.getUsername(getApplicationContext())!=null){
            Call<List<Playlist>> callback=dataservice.PostGoiyPlaylistXemThem(PreferenceUtils.getUsername(getApplicationContext()));
            callback.enqueue(new Callback<List<Playlist>>() {
                @Override
                public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                    mangplaylist= (ArrayList<Playlist>) response.body();
                    XTDSPlaylistadapter=new XemThemDSPlaylistAdapter(Goiy_playlist_XemThemDSPlaylistActivity.this,mangplaylist);
                    reXemThemDSPlaylist.setLayoutManager(new GridLayoutManager(Goiy_playlist_XemThemDSPlaylistActivity.this,2));
                    reXemThemDSPlaylist.setAdapter((XTDSPlaylistadapter));
                    Log.d("XemThemPlaylist","Lấy được:"+String.valueOf(mangplaylist.size()));
                }

                @Override
                public void onFailure(Call<List<Playlist>> call, Throwable t) {
                    Log.d("error:","something wrong");
                }
            });
        }else{
            if(PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getApplicationContext()).matches(".*\\d.*")|| PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()).matches(".*\\d.*") || PreferenceUtils.getBanListIdCaSi(getApplicationContext()).matches(".*\\d.*")){

                //trong trường hợp các preference bị null save nó dưới dạng ""
                if(PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getApplicationContext())==null){
                    PreferenceUtils.saveListIdTheloaibaihatfromQuizChoice("",getApplicationContext());
                }
                if(PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext())==null){
                    PreferenceUtils.saveListIdCasifromQuizChoice("",getApplicationContext());
                }
                if(PreferenceUtils.getBanListIdCaSi(getApplicationContext())==null){
                    PreferenceUtils.saveBanListIdCaSi("",getApplicationContext());
                }

                Call<List<Playlist>> callback=dataservice.PostGoiyPlaylistForNoAccXemThem(PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getApplicationContext()),PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()),PreferenceUtils.getBanListIdCaSi(getApplicationContext()));
                callback.enqueue(new Callback<List<Playlist>>() {
                    @Override
                    public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                        mangplaylist= (ArrayList<Playlist>) response.body();
                        XTDSPlaylistadapter=new XemThemDSPlaylistAdapter(Goiy_playlist_XemThemDSPlaylistActivity.this,mangplaylist);
                        reXemThemDSPlaylist.setLayoutManager(new GridLayoutManager(Goiy_playlist_XemThemDSPlaylistActivity.this,2));
                        reXemThemDSPlaylist.setAdapter((XTDSPlaylistadapter));
                        Log.d("XemThemPlaylist","Lấy được:"+String.valueOf(mangplaylist.size()));
                    }

                    @Override
                    public void onFailure(Call<List<Playlist>> call, Throwable t) {
                        Log.d("error:","something wrong");
                    }
                });
            }else{
                Call<List<Playlist>> callback=dataservice.PostGoiyPlaylistForNoAccNoQuizXemThem();
                callback.enqueue(new Callback<List<Playlist>>() {
                    @Override
                    public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                        mangplaylist= (ArrayList<Playlist>) response.body();
                        XTDSPlaylistadapter=new XemThemDSPlaylistAdapter(Goiy_playlist_XemThemDSPlaylistActivity.this,mangplaylist);
                        reXemThemDSPlaylist.setLayoutManager(new GridLayoutManager(Goiy_playlist_XemThemDSPlaylistActivity.this,2));
                        reXemThemDSPlaylist.setAdapter((XTDSPlaylistadapter));
                        Log.d("XemThemPlaylist","Lấy được:"+String.valueOf(mangplaylist.size()));
                    }

                    @Override
                    public void onFailure(Call<List<Playlist>> call, Throwable t) {
                        Log.d("error:","something wrong");
                    }
                });
            }
        }
    }
}
