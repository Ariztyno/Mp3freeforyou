package com.example.mp3freeforyou.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Adapter.TimKiemAlbumAdapter;
import com.example.mp3freeforyou.Adapter.TimKiemCasiAdapter;
import com.example.mp3freeforyou.Adapter.TimKiemPlaylistAdapter;
import com.example.mp3freeforyou.Adapter.TimKiemTheloaiAdapter;
import com.example.mp3freeforyou.Model.Album;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.Model.Theloaibaihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_MusicPlayer_InfoBaiHat extends Fragment {
    ArrayList<Casi> mangcasi=new ArrayList<>();
    ArrayList<Theloaibaihat> mangtl=new ArrayList<>();
    ArrayList<Album> mangalbum=new ArrayList<>();
    ArrayList<Playlist> mangplaylist=new ArrayList<>();

    ImageView imgCircleSong;
    TimKiemCasiAdapter CaSiAdapter;
    TimKiemTheloaiAdapter TheloaiAdapter;
    TimKiemAlbumAdapter AlbumAdapter;
    TimKiemPlaylistAdapter PlaylistAdapter;
    TextView txtXemThemListCaSi,txtXemThemListTheLoai,txtXemThemListAlbum,txtXemThemListPlaylist,txtSongName;
    RecyclerView reListTheloaiOfSong,reListSingerOfSong,reListAlbumOfSong,reListPlaylistOfSong;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_musicplayer_infobaihat,container,false);

        imgCircleSong=view.findViewById(R.id.imgMediaPlayer_InfoBaiHat);
        reListTheloaiOfSong=view.findViewById(R.id.reListTheloaiOfSong);
        reListSingerOfSong=view.findViewById(R.id.reListSingerOfSong);
        reListAlbumOfSong=view.findViewById(R.id.reListAlbumOfSong);
        reListPlaylistOfSong=view.findViewById(R.id.reListPlaylistOfSong);
        txtXemThemListCaSi=view.findViewById(R.id.txtViewmoreCasi);
        txtXemThemListTheLoai=view.findViewById(R.id.txtViewmoreTheloai);
        txtXemThemListAlbum=view.findViewById(R.id.txtViewmoreAlbum);
        txtXemThemListPlaylist=view.findViewById(R.id.txtViewmorePlaylist);
        txtSongName=view.findViewById(R.id.txtSongName);

        txtXemThemListCaSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reListSingerOfSong.getVisibility()==View.VISIBLE){
                    txtXemThemListCaSi.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_up,0,0,0);
                    reListSingerOfSong.setVisibility(View.GONE);
                }else{
                    txtXemThemListCaSi.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_down,0,0,0);
                    reListSingerOfSong.setVisibility(View.VISIBLE);
                }
            }
        });

        txtXemThemListTheLoai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reListTheloaiOfSong.getVisibility()==View.VISIBLE){
                    txtXemThemListTheLoai.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_up,0,0,0);
                    reListTheloaiOfSong.setVisibility(View.GONE);
                }else{
                    txtXemThemListTheLoai.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_down,0,0,0);
                    reListTheloaiOfSong.setVisibility(View.VISIBLE);
                }
            }
        });

        txtXemThemListAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reListAlbumOfSong.getVisibility()==View.VISIBLE){
                    txtXemThemListAlbum.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_up,0,0,0);
                    reListAlbumOfSong.setVisibility(View.GONE);
                }else{
                    txtXemThemListAlbum.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_down,0,0,0);
                    reListAlbumOfSong.setVisibility(View.VISIBLE);
                }
            }
        });

        txtXemThemListPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reListPlaylistOfSong.getVisibility()==View.VISIBLE){
                    txtXemThemListPlaylist.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_up,0,0,0);
                    reListPlaylistOfSong.setVisibility(View.GONE);
                }else{
                    txtXemThemListPlaylist.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_down,0,0,0);
                    reListPlaylistOfSong.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    public void HideShowRecycleView() {

    }


    public void LoadImgForInfoFrg(String hinhanh) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(hinhanh.equals("")){
                    Log.d("fmp_info","link:"+hinhanh);
                }else {
                    Log.d("fmp_info","link:"+hinhanh);
                    Picasso.with(getContext()).load(hinhanh).placeholder(R.drawable.loading).error(R.drawable.ic_launcher_background).into(imgCircleSong);
                }

            }
        },3000);

    }

    public void LoadTextViewForInfoFrg(String songname){
        txtSongName.setText(songname);
    }

    public void LoadSongData(String songlistcasi,String ListIdTheLoai,String ListIdAlbum,String ListIdPlaylist){
        Call<List<Casi>> callcasi= APIService.getService().postgetlistcasifromlistnamecasi(songlistcasi);
        callcasi.enqueue(new Callback<List<Casi>>() {
            @Override
            public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                mangcasi= (ArrayList<Casi>) response.body();
                Log.d("mp_callcasi",""+mangcasi.size());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                CaSiAdapter=new TimKiemCasiAdapter(getActivity(),mangcasi);
                reListSingerOfSong.setLayoutManager(linearLayoutManager);
                reListSingerOfSong.setAdapter(CaSiAdapter);
                CaSiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Casi>> call, Throwable t) {
                Log.d("mp_callcasi",""+t);
            }
        });

        Call<List<Theloaibaihat>> calltheloai=APIService.getService().postgetlisttheloaifromlistidtheloai(ListIdTheLoai);
        calltheloai.enqueue(new Callback<List<Theloaibaihat>>() {
            @Override
            public void onResponse(Call<List<Theloaibaihat>> call, Response<List<Theloaibaihat>> response) {
                mangtl= (ArrayList<Theloaibaihat>) response.body();
                Log.d("mp_calltheloai",""+mangtl.size());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                TheloaiAdapter=new TimKiemTheloaiAdapter(getActivity(),mangtl);
                reListTheloaiOfSong.setLayoutManager(linearLayoutManager);
                reListTheloaiOfSong.setAdapter(TheloaiAdapter);
                TheloaiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Theloaibaihat>> call, Throwable t) {
                Log.d("mp_calltheloai",""+t);
            }
        });

        Call<List<Album>> callalbum=APIService.getService().postgetlistalbumfromlistidalbum(ListIdAlbum);
        callalbum.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                mangalbum= (ArrayList<Album>) response.body();
                Log.d("mp_callalbum",""+mangalbum.size());
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                AlbumAdapter=new TimKiemAlbumAdapter(getActivity(),mangalbum);
                reListAlbumOfSong.setLayoutManager(linearLayoutManager);
                reListAlbumOfSong.setAdapter(AlbumAdapter);
                AlbumAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                Log.d("mp_callalbum",""+t);
            }
        });

        Call<List<Playlist>> callplaylist=APIService.getService().postgetlistplaylistfromlistidplaylist(ListIdPlaylist);
        callplaylist.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                mangplaylist= (ArrayList<Playlist>) response.body();
                Log.d("mp_callplaylist",""+mangplaylist.size());
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                PlaylistAdapter=new TimKiemPlaylistAdapter(getActivity(),mangplaylist);
                reListPlaylistOfSong.setLayoutManager(linearLayoutManager);
                reListPlaylistOfSong.setAdapter(PlaylistAdapter);
                PlaylistAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.d("mp_callplaylist",""+t);
            }
        });
    }
}
