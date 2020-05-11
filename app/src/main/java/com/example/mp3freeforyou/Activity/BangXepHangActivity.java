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
import android.widget.TextView;
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

public class BangXepHangActivity extends AppCompatActivity {
    Toolbar toolbar;
    RelativeLayout relativeListen,relativeLike;
    TextView txtLikeRank,txtListenRank,emptyListenRank,emptyLikeRank;
    ListView listViewLike,listViewListen;
    Top5baihatduocyeuthichnhatAdapter TopLike,TopListen;

    ArrayList<Baihat> mangbaihat_like=new ArrayList<>();
    ArrayList<Baihat> mangbaihat_listen=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bang_xep_hang);
        anhxa();
        init();
        Getdata();
        HideShowEvent();
    }

    private void HideShowEvent() {
        txtLikeRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(relativeLike.getVisibility()==View.VISIBLE){
                    relativeLike.setVisibility(View.GONE);
                    relativeListen.setVisibility(View.VISIBLE);

                    txtLikeRank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_up_20c,0,0,0);
                    txtListenRank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_down_20,0,0,0);
                }else{
                    if(relativeListen.getVisibility()==View.VISIBLE){
                        relativeLike.setVisibility(View.VISIBLE);
                        relativeListen.setVisibility(View.GONE);

                        txtLikeRank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_down_20,0,0,0);
                        txtListenRank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_up_20c,0,0,0);
                    }else {
                        relativeLike.setVisibility(View.VISIBLE);
                        relativeListen.setVisibility(View.GONE);

                        txtLikeRank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_down_20,0,0,0);
                        txtListenRank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_up_20c,0,0,0);
                    }
                }
            }
        });

        txtListenRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(relativeListen.getVisibility()==View.VISIBLE){
                    relativeListen.setVisibility(View.GONE);
                    relativeLike.setVisibility(View.VISIBLE);

                    txtLikeRank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_down_20,0,0,0);
                    txtListenRank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_up_20c,0,0,0);
                }else{
                    if(relativeLike.getVisibility()==View.VISIBLE){
                        relativeListen.setVisibility(View.VISIBLE);
                        relativeLike.setVisibility(View.GONE);

                        txtLikeRank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_up_20c,0,0,0);
                        txtListenRank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_down_20,0,0,0);
                    }else {
                        relativeListen.setVisibility(View.VISIBLE);
                        relativeLike.setVisibility(View.GONE);

                        txtLikeRank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_up_20c,0,0,0);
                        txtListenRank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_down_20,0,0,0);
                    }
                }
            }
        });
    }

    private void init() {
        //KHỞI TẠO CHO TOOLBAR
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bảng xếp hạng");

        toolbar.setTitleTextColor(Color.parseColor("#ff39aa"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //khoi tao hien thi listview

        //ICON of txt init
        txtLikeRank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_down_20,0,0,0);
        txtListenRank.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_up_20c,0,0,0);

        //init truong hop data ko load dc
        emptyListenRank.setText("Chưa cập nhật");
        emptyLikeRank.setText("Chưa cập nhật");

        listViewListen.setEmptyView(emptyListenRank);
        listViewLike.setEmptyView(emptyLikeRank);
    }

    private void Getdata() {
        //bang xep hang theo luot thisch
        Dataservice dataservice= APIService.getService();
        Call<List<Baihat>> callback=dataservice.GetDataBangxephangBaihatDuocYeuThichNhat();
        callback.enqueue(new Callback<List<Baihat>>() {
            @Override
            public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                mangbaihat_like= (ArrayList<Baihat>) response.body();
                TopLike=new Top5baihatduocyeuthichnhatAdapter(BangXepHangActivity.this,android.R.layout.simple_list_item_1,mangbaihat_like);
                listViewLike.setAdapter(TopLike);
                setlistViewHeightBasedOnChildren(listViewLike);
                listViewLike.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent=new Intent(getApplicationContext(), MusicPlayerActivity.class);
                        intent.putExtra("Cakhuc", mangbaihat_like.get(position));
                        //Log.d("Top5baihat_Cakhuc",String.valueOf(mangbaihat.get(position).getTenBaiHat()));
                        startActivity(intent);
                    }
                });
                Log.d("Top100baihat_Like",String.valueOf(mangbaihat_like.size()));
            }

            @Override
            public void onFailure(Call<List<Baihat>> call, Throwable t) {

            }
        });

        //bang xep hang theo luot nghe
        Call<List<Baihat>> callListenRank=dataservice.GetDataBangxephangBaihatDuocNgheNhieuNhat();
        callListenRank.enqueue(new Callback<List<Baihat>>() {
            @Override
            public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                mangbaihat_listen= (ArrayList<Baihat>) response.body();
                TopListen=new Top5baihatduocyeuthichnhatAdapter(BangXepHangActivity.this,android.R.layout.simple_list_item_1,mangbaihat_listen);
                listViewListen.setAdapter(TopListen);
                setlistViewHeightBasedOnChildren(listViewListen);
                listViewListen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent=new Intent(getApplicationContext(), MusicPlayerActivity.class);
                        intent.putExtra("Cakhuc", mangbaihat_listen.get(position));
                        //Log.d("Top5baihat_Cakhuc",String.valueOf(mangbaihat.get(position).getTenBaiHat()));
                        startActivity(intent);
                    }
                });
                Log.d("Top100baihat_Listen",String.valueOf(mangbaihat_listen.size()));
            }

            @Override
            public void onFailure(Call<List<Baihat>> call, Throwable t) {

            }
        });
    }

    private void anhxa() {
        toolbar=findViewById(R.id.tbbangxephangXemthem);
        listViewLike=findViewById(R.id.lvXemthembangxephangLike);
        listViewListen=findViewById(R.id.lvXemthembangxephangListen);

        txtLikeRank=findViewById(R.id.txtLikeRank);
        txtListenRank=findViewById(R.id.txtListenRank);

        //ko co data
        emptyListenRank=findViewById(R.id.emptyListenRank);
        emptyLikeRank=findViewById(R.id.emptyLikeRank);

        //relativelayout
        relativeListen=findViewById(R.id.relativeListen);
        relativeLike=findViewById(R.id.relativeLike);
    }

    public void setlistViewHeightBasedOnChildren(ListView listViewLike) {
        ListAdapter listAdapter = listViewLike.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listViewLike.getPaddingTop() + listViewLike.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listViewLike.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listViewLike);

            if(listItem != null){
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listViewLike.getLayoutParams();
        params.height = totalHeight + (listViewLike.getDividerHeight() * (listAdapter.getCount() - 1));
        listViewLike.setLayoutParams(params);
        listViewLike.requestLayout();
    }
}
