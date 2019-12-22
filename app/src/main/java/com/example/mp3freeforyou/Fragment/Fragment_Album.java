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

import com.example.mp3freeforyou.Activity.XemThemDSAlbumActivity;
import com.example.mp3freeforyou.Adapter.AlbumAdapter;
import com.example.mp3freeforyou.Model.Album;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Album extends Fragment {
    View view;
    TextView txtXemThemAlbum;
    RecyclerView realbum;
    ArrayList<Album> mangalbum;
    AlbumAdapter albumAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_album,container,false);
        realbum=view.findViewById(R.id.recycleviewAlbum);
        txtXemThemAlbum=view.findViewById(R.id.txtViewmoreAlbum);
        getData();
        XemThem();
        return view;
    }

    private void XemThem() {
        txtXemThemAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), XemThemDSAlbumActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        Dataservice dataservice= APIService.getService();
        Call<List<Album>> callback=dataservice.GetDataAlbumCurrentDay();
        callback.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                mangalbum= (ArrayList<Album>) response.body();
                albumAdapter=new AlbumAdapter(getActivity(),mangalbum);
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                realbum.setLayoutManager(linearLayoutManager);
                realbum.setAdapter(albumAdapter);
                Log.d("Album:",mangalbum.get(0).getTenAlbum());
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });
    }


}
