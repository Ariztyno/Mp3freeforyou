package com.example.mp3freeforyou.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.XemThemDSChudeActivity;
import com.example.mp3freeforyou.Adapter.ChudeAdapter;
import com.example.mp3freeforyou.Model.Chudebaihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Chude extends Fragment {
    View view;
    TextView txtXemThemChude;
    RecyclerView rechude;
    ArrayList<Chudebaihat> mangchude;
    ChudeAdapter chudeAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_chude,container,false);
        rechude=view.findViewById(R.id.recycleviewChude);
        txtXemThemChude=view.findViewById(R.id.txtViewmoreChude);
        GetData();
        //sukien xem them toan bo danh sach chu de
        XemThem();
        return view;
    }

    private void XemThem() {
        txtXemThemChude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), XemThemDSChudeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void GetData() {
        Dataservice dataservice= APIService.getService();
        Call<List<Chudebaihat>> callback=dataservice.GetDataChudeCurrentDay();
        callback.enqueue(new Callback<List<Chudebaihat>>() {
            @Override
            public void onResponse(Call<List<Chudebaihat>> call, Response<List<Chudebaihat>> response) {
                mangchude= (ArrayList<Chudebaihat>) response.body();
                chudeAdapter=new ChudeAdapter(getActivity(),mangchude);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                rechude.setLayoutManager(linearLayoutManager);
                rechude.setAdapter(chudeAdapter);
                Log.d("bbb:",mangchude.get(0).getTenChuDe());
            }

            @Override
            public void onFailure(Call<List<Chudebaihat>> call, Throwable t) {

            }
        });
    }


}
