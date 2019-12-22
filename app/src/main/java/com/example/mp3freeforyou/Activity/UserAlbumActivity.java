package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mp3freeforyou.Adapter.UserAlbumAdapter;
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

public class UserAlbumActivity extends AppCompatActivity {
    ArrayList<Album> mangalbum;
    RecyclerView reUserAlbum;
    Toolbar toolbar;

    UserAlbumAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_album);
        anhxa();
        init();
        Getdata();
    }

    private void Getdata() {
        Dataservice dataservice= APIService.getService();
        Call<List<Album>> call=dataservice.GetAlbumyeuthich(PreferenceUtils.getUsername(getApplicationContext()));
        call.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                mangalbum= (ArrayList<Album>) response.body();
                adapter=new UserAlbumAdapter(UserAlbumActivity.this,mangalbum);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserAlbumActivity.this);
                reUserAlbum.setLayoutManager(linearLayoutManager);
                reUserAlbum.setAdapter(adapter);
                Log.d("UserAlbumre","mangalbum= "+mangalbum.size());
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                Log.d("UserAlbumre","call fail");
            }
        });
    }

    private void init() {
        //KHỞI TẠO CHO TOOLBAR
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Album yêu thích");

        toolbar.setTitleTextColor(Color.parseColor("#ff39aa"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        reUserAlbum=findViewById(R.id.reUserAlbumYeuthich);
        toolbar=findViewById(R.id.tbUserAlbum);
    }
}
