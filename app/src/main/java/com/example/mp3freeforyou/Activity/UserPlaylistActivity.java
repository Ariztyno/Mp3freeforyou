package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mp3freeforyou.Adapter.UserPlaylistAdapter;
import com.example.mp3freeforyou.Adapter.UserPlaylistyeuthichAdapter;
import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPlaylistActivity extends AppCompatActivity {

    TextView txtPlaylistcanhan,txtPlaylistyeuthich;
    Toolbar toolbar;
    RecyclerView reUserPlaylist,reUserPlaylistyeuthich;
    ArrayList<Playlist> mangplaylist,mangplaylist_yt;
    UserPlaylistAdapter adapter;
    UserPlaylistyeuthichAdapter adapteryeuthich;
    FloatingActionButton floatingactionbuttonUserPlaylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_playlist);
        anhxa();
        GetData();
        Themplaylist();
        Sukientextview();
    }

    private void Sukientextview() {

        //Ârn hiện playlistcanhan
        txtPlaylistcanhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reUserPlaylist.getVisibility()==View.VISIBLE){
                    reUserPlaylist.setVisibility(View.GONE);
                }else{
                    reUserPlaylist.setVisibility(View.VISIBLE);
                }
            }
        });

        //Ârn hiện playlistyeuthich
        txtPlaylistyeuthich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reUserPlaylistyeuthich.getVisibility()==View.VISIBLE){
                    reUserPlaylistyeuthich.setVisibility(View.GONE);
                }else{
                    reUserPlaylistyeuthich.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void Themplaylist() {
        floatingactionbuttonUserPlaylist.setEnabled(true);
        floatingactionbuttonUserPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog=new AlertDialog.Builder(UserPlaylistActivity.this).create();
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View dialogView = inflater.inflate(R.layout.alert_dialog_user_playlist_addplaylist, null);

                final EditText edtAddplaylist = (EditText) dialogView.findViewById(R.id.edtAddplaylist);
                Button btnSubmit = (Button) dialogView.findViewById(R.id.btnAddplaylist);
                Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancelAddplaylist);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edtAddplaylist.getText().toString().equals("")){
                            Log.d("UserPlaylistAdapter","Tên bị rỗng");
                            Toast.makeText(getApplicationContext(),"Không được để tên rỗng",Toast.LENGTH_LONG).show();
                        }else {
                            //add playlist lên db
                            Dataservice dataservice= APIService.getService();
                            Call<String> callback=dataservice.PostAddnewPlayList(PreferenceUtils.getUsername(getApplicationContext()).toString(),edtAddplaylist.getText().toString()) ;
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String kq=response.body();
                                    if(kq.equals("success")){
                                        Log.d("UserPlaylist","add playlist thành công");
                                        Toast.makeText(getApplicationContext(),"Thêm thành công",Toast.LENGTH_LONG).show();
                                    }else if(kq.equals("fail")){
                                        Log.d("UserPlaylist","sdd playlist thất bại");
                                    }else if(kq.equals("fail1")){
                                        Log.d("UserPlaylist","username hoặc tên playlist rỗng");
                                    }else {
                                        Log.d("UserPlaylist","tên playlist đã tồn tại");
                                        Toast.makeText(getApplicationContext(),"Tên playlist đã tồn tại",Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d("UserPlaylist","callback add fail");
                                }
                            });
                            alertDialog.dismiss();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    overridePendingTransition( 0, 0);
                                    startActivity(getIntent());
                                    overridePendingTransition( 0, 0);
                                }
                            }, 3000);
                        }
                    }
                });

                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });
    }

    private void GetData() {

        //getdata cho playlist cá nhan
        Dataservice dataservice= APIService.getService();
        Log.d("UserPlaylistActivity",""+PreferenceUtils.getUsername(UserPlaylistActivity.this));
        Call<List<Playlist>> callback=dataservice.GetDanhSachPlaylistCuaNguoiDung(PreferenceUtils.getUsername(UserPlaylistActivity.this));
        callback.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                mangplaylist= (ArrayList<Playlist>) response.body();
                adapter=new UserPlaylistAdapter(UserPlaylistActivity.this,mangplaylist);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserPlaylistActivity.this);
                reUserPlaylist.setLayoutManager(linearLayoutManager);
                reUserPlaylist.setAdapter(adapter);
                Log.d("UserPlaylistActivity","Lấy được:"+String.valueOf(mangplaylist.size()));
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.d("UserPlaylistActivity","ko Lấy được:"+String.valueOf(mangplaylist.size()));
            }
        });

        //getdatachoplaylistyeuthich
        Call<List<Playlist>> callbackyeuthich=dataservice.GetDanhSachPlaylistYeuthichCuaNguoiDung(PreferenceUtils.getUsername(UserPlaylistActivity.this));
        callbackyeuthich.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                mangplaylist_yt= (ArrayList<Playlist>) response.body();
                adapteryeuthich=new UserPlaylistyeuthichAdapter(UserPlaylistActivity.this,mangplaylist_yt);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserPlaylistActivity.this);
                reUserPlaylistyeuthich.setLayoutManager(linearLayoutManager);
                reUserPlaylistyeuthich.setAdapter(adapteryeuthich);
                Log.d("UserPlaylistActivity_yt","Lấy được:"+String.valueOf(mangplaylist_yt.size()));
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.d("UserPlaylistActivity_yt","ko Lấy được:"+String.valueOf(mangplaylist_yt.size()));
            }
        });
    }

    private void anhxa() {
        txtPlaylistcanhan=findViewById(R.id.playlistcanhan);
        txtPlaylistyeuthich=findViewById(R.id.playlistyeuthich);
        toolbar=findViewById(R.id.tbUserPlaylist);
        reUserPlaylist=findViewById(R.id.reUserPlaylist);
        reUserPlaylistyeuthich=findViewById(R.id.reUserPlaylistYeuthich);
        floatingactionbuttonUserPlaylist=findViewById(R.id.floatingactionbuttonUserPlaylist);

        //KHỞI TẠO CHO TOOLBAR
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quản lý playlist");

        toolbar.setTitleTextColor(Color.parseColor("#ff39aa"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        floatingactionbuttonUserPlaylist.setEnabled(false);
    }
}
