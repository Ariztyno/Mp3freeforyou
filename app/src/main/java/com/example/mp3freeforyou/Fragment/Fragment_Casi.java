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

import com.example.mp3freeforyou.Activity.XemThemDSCasiActivity;
import com.example.mp3freeforyou.Adapter.CasiAdapter;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Casi extends Fragment {
    View view;
    RecyclerView recasi;
    ArrayList<Casi> mangcasi=new ArrayList<>();
    CasiAdapter adapter;
    TextView txtXemThemCasi;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_casi,container,false);
        recasi=view.findViewById(R.id.recycleviewCasi);
        txtXemThemCasi=view.findViewById(R.id.txtViewmoreCasi);
        getData();
        XemThem();
        return view;
    }

    private void XemThem() {
        txtXemThemCasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), XemThemDSCasiActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        Dataservice dataservice= APIService.getService();
        Call<List<Casi>> call=dataservice.GetDataCasiCurrentDay();
        call.enqueue(new Callback<List<Casi>>() {
            @Override
            public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                mangcasi= (ArrayList<Casi>) response.body();
                LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
                adapter =new CasiAdapter(getActivity(),mangcasi);
                recasi.setLayoutManager(linearLayoutManager1);
                recasi.setAdapter(adapter);
                Log.d("MainAc_Frag_Casi",""+mangcasi.size());
            }

            @Override
            public void onFailure(Call<List<Casi>> call, Throwable t) {
                Log.d("MainAc_Frag_Casi",""+t);
            }
        });
    }
}
