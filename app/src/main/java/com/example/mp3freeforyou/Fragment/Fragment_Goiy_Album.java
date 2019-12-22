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

import com.example.mp3freeforyou.Activity.Goiy_album_XemThemDSAlbumActivity;
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

public class Fragment_Goiy_Album extends Fragment {
    View view;
    TextView txtXemThemAlbum,txtTile;
    RecyclerView realbum;
    ArrayList<Album> mangalbum;
    AlbumAdapter albumAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_album,container,false);
        realbum=view.findViewById(R.id.recycleviewAlbum);
        txtXemThemAlbum=view.findViewById(R.id.txtViewmoreAlbum);
        txtTile=view.findViewById(R.id.txtTitleAlbum);
        txtTile.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        getData();
        XemThem();
        Anhienre();
        return view;
    }

    private void Anhienre() {
        txtTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(realbum.getVisibility()==View.VISIBLE){
                    realbum.setVisibility(View.GONE);
                }else {
                    realbum.setVisibility(View.VISIBLE);
                }
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

    private void XemThem() {
        txtXemThemAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Goiy_album_XemThemDSAlbumActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
}
