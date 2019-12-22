package com.example.mp3freeforyou.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.XemThemDSTheLoaiActivity;
import com.example.mp3freeforyou.Adapter.TheloaiAdapter;
import com.example.mp3freeforyou.Model.Theloaibaihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.System.in;

public class Fragment_Theloai extends Fragment {
    View view;
    TextView txtXemThemTheloai;
    RecyclerView retheloai;
    ArrayList<Theloaibaihat> mangtheloai;
    TheloaiAdapter theloaiAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_theloai,container,false);
        retheloai=view.findViewById(R.id.recycleviewTheloai);
        txtXemThemTheloai=view.findViewById(R.id.txtViewmoreTheloai);
        GetData();
        //su kien xem them
        XemThem();
        return view;
    }

    private void XemThem() {
        txtXemThemTheloai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), XemThemDSTheLoaiActivity.class);
                startActivity(intent);
            }
        });
    }

    private void GetData() {
        Dataservice dataservice= APIService.getService();
        Call<List<Theloaibaihat>> callback=dataservice.GetDataTheloaiCurrentDay();
        callback.enqueue(new Callback<List<Theloaibaihat>>() {
            @Override
            public void onResponse(Call<List<Theloaibaihat>> call, Response<List<Theloaibaihat>> response) {
                mangtheloai= (ArrayList<Theloaibaihat>) response.body();
                theloaiAdapter=new  TheloaiAdapter(getActivity(),mangtheloai);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                retheloai.setLayoutManager(linearLayoutManager);
                retheloai.setAdapter(theloaiAdapter);

                Log.d("theloai","Yes");
            }

            @Override
            public void onFailure(Call<List<Theloaibaihat>> call, Throwable t) {

            }
        });
    }


}
