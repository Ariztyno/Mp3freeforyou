package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.mp3freeforyou.Adapter.CasiAdapter;
import com.example.mp3freeforyou.Adapter.DSBaihatChanAdapter;
import com.example.mp3freeforyou.Adapter.DSCasiChanAdapter;
import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLyDSChanActivity extends AppCompatActivity {
    ArrayList<Baihat> mangbaihat=new ArrayList<>();
    DSBaihatChanAdapter adapter;

    ArrayList<Casi> mangcasi=new ArrayList<>();
    DSCasiChanAdapter casiAdapter;

    private TextView txtReBaihatChanEmty,txtReCasiChanEmty,txtTitle;
    private Button btnGetDSChanBaihat,btnGetDSChanCasi;
    private RecyclerView reDSChanBaihat,reDSChanCasi;
    private NestedScrollView svBaihat,svCasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_dschan);
        Anhxa();
        Getdata();
        Log.d("QLDSchan_bh_BL",""+PreferenceUtils.getBanListIdBaihat(getApplicationContext()));
        Log.d("QLDSchan_CS_BL",""+PreferenceUtils.getBanListIdCaSi(getApplicationContext()));

        //SAU 4 GIÂY MỚI CÓ THỂ MỞ ĐƯỢC DS CHẶN BÀI HÁT VÀ CA SĨ
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HideAndShowEvent();
            }
        },7500);
    }

    private void Getdata() {

        Dataservice dataservice= APIService.getService();
        if(PreferenceUtils.getUsername(getApplicationContext())!=null){
            //dành cho người dử dụng account
            //ds baihat bi chan
            Call<List<Baihat>> callchanlistbaihat=dataservice.GetDanhSachBaihatBiChan(PreferenceUtils.getUsername(getApplicationContext()));
            callchanlistbaihat.enqueue(new Callback<List<Baihat>>() {
                @Override
                public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                    mangbaihat= (ArrayList<Baihat>) response.body();
                    adapter=new DSBaihatChanAdapter(QuanLyDSChanActivity.this,mangbaihat);
                    reDSChanBaihat.setLayoutManager(new LinearLayoutManager(QuanLyDSChanActivity.this));
                    reDSChanBaihat.setAdapter(adapter);

                }

                @Override
                public void onFailure(Call<List<Baihat>> call, Throwable t) {

                }
            });

            //ds ca si bij chan
            Call<List<Casi>> callchanlistcasi=dataservice.GetDanhSachCasiBiChan(PreferenceUtils.getUsername(getApplicationContext()));
            callchanlistcasi.enqueue(new Callback<List<Casi>>() {
                @Override
                public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                    mangcasi= (ArrayList<Casi>) response.body();
                    casiAdapter=new DSCasiChanAdapter(QuanLyDSChanActivity.this,mangcasi);
                    reDSChanCasi.setLayoutManager(new LinearLayoutManager(QuanLyDSChanActivity.this));
                    reDSChanCasi.setAdapter(casiAdapter);
                }

                @Override
                public void onFailure(Call<List<Casi>> call, Throwable t) {

                }
            });
        }else {
            //dành cho người ko sử dụng tài khoản
            //KIỂM TRA TRONG TRUOWGN HỢP BANLIST BỊ NULL
            if(PreferenceUtils.getBanListIdBaihat(getApplicationContext())!=null){
                if(PreferenceUtils.getBanListIdBaihat(getApplicationContext()).matches(".*\\d.*")){
                    Call<List<Baihat>> callchanlistbaihat=dataservice.GetDanhSachBaihatBiChanForNoacc(PreferenceUtils.getBanListIdBaihat(getApplicationContext()));
                    callchanlistbaihat.enqueue(new Callback<List<Baihat>>() {
                        @Override
                        public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                            mangbaihat= (ArrayList<Baihat>) response.body();
                            adapter=new DSBaihatChanAdapter(QuanLyDSChanActivity.this,mangbaihat);
                            reDSChanBaihat.setLayoutManager(new LinearLayoutManager(QuanLyDSChanActivity.this));
                            reDSChanBaihat.setAdapter(adapter);
                            Log.d("QLDSchan_bh_notloged",""+mangbaihat.size());
                        }

                        @Override
                        public void onFailure(Call<List<Baihat>> call, Throwable t) {

                        }
                    });
                }
            }

            if(PreferenceUtils.getBanListIdCaSi(getApplicationContext())!=null){
                if(PreferenceUtils.getBanListIdCaSi(getApplicationContext()).matches(".*\\d.*")){
                    //ds ca si bij chan
                    Call<List<Casi>> callchanlistcasi=dataservice.GetDanhSachCasiBiChanForNoacc(PreferenceUtils.getBanListIdCaSi(getApplicationContext()));
                    callchanlistcasi.enqueue(new Callback<List<Casi>>() {
                        @Override
                        public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                            mangcasi= (ArrayList<Casi>) response.body();
                            casiAdapter=new DSCasiChanAdapter(QuanLyDSChanActivity.this,mangcasi);
                            reDSChanCasi.setLayoutManager(new LinearLayoutManager(QuanLyDSChanActivity.this));
                            reDSChanCasi.setAdapter(casiAdapter);
                            Log.d("QLDSchan_CS_notloged",""+mangcasi.size());
                        }

                        @Override
                        public void onFailure(Call<List<Casi>> call, Throwable t) {

                        }
                    });
                }
            }
        }

        Collections.sort(mangbaihat, new Comparator<Baihat>() {
            @Override
            public int compare(Baihat o1, Baihat o2) {
                return o1.getTenBaiHat().compareTo(o2.getTenBaiHat());
            }
        });

        Collections.sort(mangcasi, new Comparator<Casi>() {
            @Override
            public int compare(Casi o1, Casi o2) {
                return o1.getTenCaSi().compareTo(o2.getTenCaSi());
            }
        });
    }

    private void HideAndShowEvent() {
        btnGetDSChanBaihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mangbaihat.size()>0){
                    if(svCasi.getVisibility()==View.VISIBLE){
                        svCasi.setVisibility(View.GONE);
                        svBaihat.setVisibility(View.VISIBLE);
                        txtTitle.setText("Danh sách bài hát bị chặn");
                    }else{
                        if(txtReCasiChanEmty.getVisibility()==View.VISIBLE){
                            txtReCasiChanEmty.setVisibility(View.GONE);
                            svBaihat.setVisibility(View.VISIBLE);
                            txtTitle.setText("Danh sách bài hát bị chặn");
                        }else {
                            svBaihat.setVisibility(View.VISIBLE);
                            txtTitle.setText("Danh sách bài hát bị chặn");
                        }
                    }
                }else{
                    if(svCasi.getVisibility()==View.VISIBLE){
                        svCasi.setVisibility(View.GONE);
                        txtReBaihatChanEmty.setVisibility(View.VISIBLE);
                        txtTitle.setText("Danh sách bài hát bị chặn");
                    }else{
                        if(txtReCasiChanEmty.getVisibility()==View.VISIBLE){
                            txtReCasiChanEmty.setVisibility(View.GONE);
                            txtReBaihatChanEmty.setVisibility(View.VISIBLE);
                            txtTitle.setText("Danh sách bài hát bị chặn");
                        }else {
                            txtReBaihatChanEmty.setVisibility(View.VISIBLE);
                            txtTitle.setText("Danh sách bài hát bị chặn");
                        }

                    }
                }
                /*if(svCasi.getVisibility()==View.VISIBLE){
                    svCasi.setVisibility(View.GONE);
                    if(mangcasi.size()>0){
                        svBaihat.setVisibility(View.VISIBLE);
                        txtReBaihatChanEmty.setVisibility(View.GONE);
                    }else{
                        svBaihat.setVisibility(View.GONE);
                        txtReBaihatChanEmty.setVisibility(View.VISIBLE);
                    }

                }else{
                    if(svBaihat.getVisibility()==View.GONE){
                        if(mangcasi.size()>0){
                            svBaihat.setVisibility(View.VISIBLE);
                            txtReBaihatChanEmty.setVisibility(View.GONE);
                        }else{
                            svBaihat.setVisibility(View.GONE);
                            txtReBaihatChanEmty.setVisibility(View.VISIBLE);
                        }
                    }else {
                        svBaihat.setVisibility(View.GONE);
                        txtReBaihatChanEmty.setVisibility(View.GONE);
                    }
                }*/
            }
        });

        btnGetDSChanCasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(svBaihat.getVisibility()==View.VISIBLE){
                    svBaihat.setVisibility(View.GONE);
                    if(mangcasi.size()>0){
                        svCasi.setVisibility(View.VISIBLE);
                        txtReCasiChanEmty.setVisibility(View.GONE);
                    }else{
                        svCasi.setVisibility(View.GONE);
                        txtReCasiChanEmty.setVisibility(View.VISIBLE);
                    }

                }else{
                    if(svCasi.getVisibility()==View.GONE){
                        if(mangcasi.size()>0){
                            svCasi.setVisibility(View.VISIBLE);
                            txtReCasiChanEmty.setVisibility(View.GONE);
                        }else{
                            svCasi.setVisibility(View.GONE);
                            txtReCasiChanEmty.setVisibility(View.VISIBLE);
                        }
                    }else {
                        if(txtReBaihatChanEmty.getVisibility()==View.VISIBLE){
                            txtReBaihatChanEmty.setVisibility(View.GONE);
                        }
                        svCasi.setVisibility(View.GONE);
                        txtReCasiChanEmty.setVisibility(View.GONE);
                    }
                }*/
                if(mangcasi.size()>0){
                    if(svBaihat.getVisibility()==View.VISIBLE){
                        svBaihat.setVisibility(View.GONE);
                        svCasi.setVisibility(View.VISIBLE);
                        txtTitle.setText("Danh sách ca sĩ bị chặn");
                    }else{
                        if(txtReBaihatChanEmty.getVisibility()==View.VISIBLE){
                            txtReBaihatChanEmty.setVisibility(View.GONE);
                            svCasi.setVisibility(View.VISIBLE);
                            txtTitle.setText("Danh sách ca sĩ bị chặn");
                        }else {
                            svCasi.setVisibility(View.VISIBLE);
                            txtTitle.setText("Danh sách ca sĩ bị chặn");
                        }
                    }
                }else{
                    if(svBaihat.getVisibility()==View.VISIBLE){
                        svBaihat.setVisibility(View.GONE);
                        txtReCasiChanEmty.setVisibility(View.VISIBLE);
                        txtTitle.setText("Danh sách ca sĩ bị chặn");
                    }else{
                        if(txtReBaihatChanEmty.getVisibility()==View.VISIBLE){
                            txtReBaihatChanEmty.setVisibility(View.GONE);
                            txtReCasiChanEmty.setVisibility(View.VISIBLE);
                            txtTitle.setText("Danh sách ca sĩ bị chặn");
                        }else {
                            txtReCasiChanEmty.setVisibility(View.VISIBLE);
                            txtTitle.setText("Danh sách ca sĩ bị chặn");
                        }

                    }
                }
            }
        });
    }

    private void Anhxa() {
        //Recycle
        reDSChanBaihat=findViewById(R.id.reDSChanBaihat);
        reDSChanCasi=findViewById(R.id.reDSChanCasi);

        //BUTTON
        btnGetDSChanBaihat=findViewById(R.id.btnDSBanListSong);
        btnGetDSChanCasi=findViewById(R.id.btnDSBanListSinger);

        //TEXTVIEW
        txtReBaihatChanEmty=findViewById(R.id.txtReBaihatChanEmty);
        txtReCasiChanEmty=findViewById(R.id.txtReCasiChanEmty);
        txtTitle=findViewById(R.id.txtTitle);

        //NestedScrollView
        svBaihat=findViewById(R.id.viewListBaohat);
        svCasi=findViewById(R.id.viewListCasi);
    }
}
