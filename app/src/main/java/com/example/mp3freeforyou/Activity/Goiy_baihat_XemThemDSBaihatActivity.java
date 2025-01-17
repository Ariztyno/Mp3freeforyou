package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.mp3freeforyou.Adapter.Top5baihatduocyeuthichnhatAdapter;
import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Goiy_baihat_XemThemDSBaihatActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    Top5baihatduocyeuthichnhatAdapter top5;

    ArrayList<Baihat> mangbaihat=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goiy_baihat__xem_them_dsbaihat);
        anhxa();
        init();
        Getdata();
    }

    private void Getdata() {
        Dataservice dataservice= APIService.getService();
        if(PreferenceUtils.getUsername(getApplicationContext())!=null){
            Call<List<Baihat>> callback=dataservice.PostGoiyBaihatXemthem(PreferenceUtils.getUsername(getApplicationContext()));
            callback.enqueue(new Callback<List<Baihat>>() {
                @Override
                public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                    mangbaihat= (ArrayList<Baihat>) response.body();
                    top5=new Top5baihatduocyeuthichnhatAdapter(Goiy_baihat_XemThemDSBaihatActivity.this,android.R.layout.simple_list_item_1,mangbaihat);
                    listView.setAdapter(top5);
                    setListViewHeightBasedOnChildren(listView);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent=new Intent(getApplicationContext(), MusicPlayerActivity.class);
                            intent.putExtra("Cakhuc", mangbaihat.get(position));
                            //Log.d("Top5baihat_Cakhuc",String.valueOf(mangbaihat.get(position).getTenBaiHat()));
                            startActivity(intent);
                        }
                    });
                    Log.d("Top5baihat",String.valueOf(mangbaihat.size()));
                }

                @Override
                public void onFailure(Call<List<Baihat>> call, Throwable t) {

                }
            });
        }else{
            if(PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getApplicationContext()).matches(".*\\d.*")||PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()).matches(".*\\d.*")||PreferenceUtils.getBanListIdCaSi(getApplicationContext()).matches(".*\\d.*")||PreferenceUtils.getBanListIdBaihat(getApplicationContext()).matches(".*\\d.*") || PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext()).matches(".*\\d.*")){
                //check if banlist hoặc quiz choice is null replace with ""
                if(PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getApplicationContext()) == null){
                    PreferenceUtils.saveListIdTheloaibaihatfromQuizChoice("",getApplicationContext());
                }
                if(PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()) == null){
                    PreferenceUtils.saveListIdCasifromQuizChoice("",getApplicationContext());
                }
                if(PreferenceUtils.getBanListIdCaSi(getApplicationContext()) == null){
                    PreferenceUtils.saveBanListIdCaSi("",getApplicationContext());
                }
                if(PreferenceUtils.getBanListIdBaihat(getApplicationContext()) == null){
                    PreferenceUtils.saveBanListIdBaihat("",getApplicationContext());
                }
                if(PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext())==null){
                    PreferenceUtils.saveListenHistoryForNoAcc("",getApplicationContext());
                }

                Call<List<Baihat>> callback=dataservice.PostGoiyBaihatForNoAccXemthem(PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext()),PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getApplicationContext()),PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()),PreferenceUtils.getBanListIdCaSi(getApplicationContext()),PreferenceUtils.getBanListIdBaihat(getApplicationContext()));
                callback.enqueue(new Callback<List<Baihat>>() {
                    @Override
                    public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                        mangbaihat= (ArrayList<Baihat>) response.body();
                        top5=new Top5baihatduocyeuthichnhatAdapter(Goiy_baihat_XemThemDSBaihatActivity.this,android.R.layout.simple_list_item_1,mangbaihat);
                        listView.setAdapter(top5);
                        setListViewHeightBasedOnChildren(listView);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(getApplicationContext(), MusicPlayerActivity.class);
                                intent.putExtra("Cakhuc", mangbaihat.get(position));
                                //Log.d("Top5baihat_Cakhuc",String.valueOf(mangbaihat.get(position).getTenBaiHat()));
                                startActivity(intent);
                            }
                        });
                        Log.d("Top5baihat",String.valueOf(mangbaihat.size()));
                    }

                    @Override
                    public void onFailure(Call<List<Baihat>> call, Throwable t) {

                    }
                });
            }else{
                Call<List<Baihat>> callback=dataservice.PostGoiyBaihatForNoAccNoQuizXemthem();
                callback.enqueue(new Callback<List<Baihat>>() {
                    @Override
                    public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                        mangbaihat= (ArrayList<Baihat>) response.body();
                        top5=new Top5baihatduocyeuthichnhatAdapter(Goiy_baihat_XemThemDSBaihatActivity.this,android.R.layout.simple_list_item_1,mangbaihat);
                        listView.setAdapter(top5);
                        setListViewHeightBasedOnChildren(listView);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(getApplicationContext(), MusicPlayerActivity.class);
                                intent.putExtra("Cakhuc", mangbaihat.get(position));
                                //Log.d("Top5baihat_Cakhuc",String.valueOf(mangbaihat.get(position).getTenBaiHat()));
                                startActivity(intent);
                            }
                        });
                        Log.d("Top5baihat",String.valueOf(mangbaihat.size()));
                    }

                    @Override
                    public void onFailure(Call<List<Baihat>> call, Throwable t) {

                    }
                });
            }
        }

    }

    private void init() {
        //KHỞI TẠO CHO TOOLBAR
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bài hát gợi ý");

        toolbar.setTitleTextColor(Color.parseColor("#ff39aa"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        toolbar=findViewById(R.id.tbUserGoiyXemthemBaihat);
        listView=findViewById(R.id.lvXemthembaihatgoiy);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if(listItem != null){
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
