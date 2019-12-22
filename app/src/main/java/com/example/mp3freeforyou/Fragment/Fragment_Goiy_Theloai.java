package com.example.mp3freeforyou.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.Goiy_theloai_XemThemDSTheLoaiActivity;
import com.example.mp3freeforyou.Adapter.TheloaiAdapter;
import com.example.mp3freeforyou.Model.Theloaibaihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Goiy_Theloai extends Fragment {
    View view;
    TextView txtXemThemTheloai,txtTitle;
    RecyclerView retheloai;
    ArrayList<Theloaibaihat> mangtheloai;
    TheloaiAdapter theloaiAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_theloai,container,false);
        retheloai=view.findViewById(R.id.recycleviewTheloai);
        txtXemThemTheloai=view.findViewById(R.id.txtViewmoreTheloai);
        txtTitle=view.findViewById(R.id.txtTitletheloai);
        txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        GetData();
        Xemthem();
        AnhienRe();
        return view;
    }

    private void AnhienRe() {
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(retheloai.getVisibility()==View.VISIBLE){
                    retheloai.setVisibility(View.GONE);
                }else {
                    retheloai.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void GetData() {
        Dataservice dataservice= APIService.getService();
        Call<List<Theloaibaihat>> callback=dataservice.PostGoiyTheloai(PreferenceUtils.getUsername(getContext()));
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

    private void Xemthem() {
        txtXemThemTheloai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Goiy_theloai_XemThemDSTheLoaiActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
}
