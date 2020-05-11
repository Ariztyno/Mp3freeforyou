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

import com.example.mp3freeforyou.Activity.Goiy_casi_XemThemDSCaSiActivity;
import com.example.mp3freeforyou.Adapter.CasiAdapter;
import com.example.mp3freeforyou.Adapter.TimKiemCasiAdapter;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Goiy_Casi extends Fragment {
    View view;
    TextView txtXemThem,txtTitle;
    RecyclerView reCasi;

    ArrayList<Casi> mangcasi;
    CasiAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_casi,container,false);
        txtTitle=view.findViewById(R.id.txtTitleCasi);
        txtXemThem=view.findViewById(R.id.txtViewmoreCasi);
        reCasi=view.findViewById(R.id.recycleviewCasi);
        txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        if(PreferenceUtils.getUsername(getContext())==null){
            getDataIfNotLogged();
        }else{
            getDataIfLogged();
        }
        Xemthem();
        Anhenre();
        return view;
    }

    private void getDataIfNotLogged() {
        Dataservice dataservice= APIService.getService();
        if(PreferenceUtils.getListIdCasifromQuizChoice(getContext()).matches(".*\\d.*") || PreferenceUtils.getBanListIdCaSi(getContext()).matches(".*\\d.*") || PreferenceUtils.getListenHistoryForNoAcc(getContext()).matches(".*\\d.*")){

            //check if banlist hoặc quiz choice is null replace with ""
            if(PreferenceUtils.getListIdCasifromQuizChoice(getContext()) == null){
                PreferenceUtils.saveListIdCasifromQuizChoice("",getContext());
            }
            if(PreferenceUtils.getBanListIdCaSi(getContext()) == null){
                PreferenceUtils.saveBanListIdCaSi("",getContext());
            }
            if(PreferenceUtils.getListenHistoryForNoAcc(getContext())==null){
                PreferenceUtils.saveListenHistoryForNoAcc("",getContext());
            }

            Call<List<Casi>> call=dataservice.PostGoiyCasiForNoAcc(PreferenceUtils.getListenHistoryForNoAcc(getContext()),PreferenceUtils.getListIdCasifromQuizChoice(getContext()),PreferenceUtils.getBanListIdCaSi(getContext()));
            call.enqueue(new Callback<List<Casi>>() {
                @Override
                public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                    mangcasi= (ArrayList<Casi>) response.body();
                    LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
                    adapter =new CasiAdapter(getActivity(),mangcasi);
                    reCasi.setLayoutManager(linearLayoutManager1);
                    reCasi.setAdapter(adapter);
                    Log.d("recasi","yes");
                }

                @Override
                public void onFailure(Call<List<Casi>> call, Throwable t) {
                    Log.d("recasi","no"+t);
                }
            });
        }else{


            Call<List<Casi>> call=dataservice.PostGoiyCasiForNoAccNoQuizNoBan();
            call.enqueue(new Callback<List<Casi>>() {
                @Override
                public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                    mangcasi= (ArrayList<Casi>) response.body();
                    LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
                    adapter =new CasiAdapter(getActivity(),mangcasi);
                    reCasi.setLayoutManager(linearLayoutManager1);
                    reCasi.setAdapter(adapter);
                    Log.d("recasi",""+adapter.getItemCount());
                    Log.d("recasi","quiz ban his all null");
                }

                @Override
                public void onFailure(Call<List<Casi>> call, Throwable t) {
                    Log.d("recasi","no"+t);
                }
            });
        }

    }

    private void Xemthem() {
        txtXemThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Goiy_casi_XemThemDSCaSiActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    private void getDataIfLogged() {
        Dataservice dataservice= APIService.getService();
        Call<List<Casi>> call=dataservice.PostGoiyCasi(PreferenceUtils.getUsername(getContext()));
        call.enqueue(new Callback<List<Casi>>() {
            @Override
            public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                mangcasi= (ArrayList<Casi>) response.body();
                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
                adapter =new CasiAdapter(getActivity(),mangcasi);
                reCasi.setLayoutManager(linearLayoutManager1);
                reCasi.setAdapter(adapter);
                Log.d("recasi","yes");
            }

            @Override
            public void onFailure(Call<List<Casi>> call, Throwable t) {
                Log.d("recasi","no"+t);
            }
        });

    }

    private void Anhenre() {
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reCasi.getVisibility()==View.VISIBLE){
                    reCasi.setVisibility(View.GONE);
                }else {
                    reCasi.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
