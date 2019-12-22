package com.example.mp3freeforyou.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Adapter.TimKiemAdapter;
import com.example.mp3freeforyou.Adapter.TimKiemAlbumAdapter;
import com.example.mp3freeforyou.Adapter.TimKiemCasiAdapter;
import com.example.mp3freeforyou.Adapter.TimKiemChudeAdapter;
import com.example.mp3freeforyou.Adapter.TimKiemPlaylistAdapter;
import com.example.mp3freeforyou.Adapter.TimKiemTheloaiAdapter;
import com.example.mp3freeforyou.Model.Album;
import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.Model.Chudebaihat;
import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.Model.SearchAll;
import com.example.mp3freeforyou.Model.Theloaibaihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Tim_Kiem extends Fragment {
    View view;
    Toolbar toolbar;
    RecyclerView reTimKiem,reTKCasi,reTKAlbum,reTKPlaylist,reTKTheloai,reTKChude;
    TextView txtKotimthay;
    //view cho fragment
    TimKiemAdapter timKiemAdapter; //baihat
    TimKiemCasiAdapter timKiemCasiAdapter;//casi
    TimKiemChudeAdapter timKiemChudeAdapter;
    TimKiemPlaylistAdapter timKiemPlaylistAdapter;
    TimKiemAlbumAdapter timKiemAlbumAdapter;
    TimKiemTheloaiAdapter timKiemTheloaiAdapter;
    //header
    TextView baihatheader,casiheader,albumheader,playlistheader,theloaiheader,chudeheader;

    SearchAll sa;
    ArrayList<Baihat> mangbh;
    ArrayList<Casi> mangcasi;
    ArrayList<Album> mangalbum;
    ArrayList<Playlist> mangplaylist;
    ArrayList<Theloaibaihat> mangtl;
    ArrayList<Chudebaihat> mangchude;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_tim_kiem,container,false);
        anhxa();
        HeaderOnClickClearRecycleView();
        setHasOptionsMenu(true);
        return view;
    }

    private void HeaderOnClickClearRecycleView() {
        baihatheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reTimKiem.getVisibility()==View.VISIBLE){
                    reTimKiem.setVisibility(View.GONE);
                }else{
                    reTimKiem.setVisibility(View.VISIBLE);
                }
            }
        });

        casiheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reTKCasi.getVisibility()==View.VISIBLE){
                    reTKCasi.setVisibility(View.GONE);
                }else{
                    reTKCasi.setVisibility(View.VISIBLE);
                }
            }
        });

        albumheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reTKAlbum.getVisibility()==View.VISIBLE){
                    reTKAlbum.setVisibility(View.GONE);
                }else{
                    reTKAlbum.setVisibility(View.VISIBLE);
                }
            }
        });

        playlistheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reTKPlaylist.getVisibility()==View.VISIBLE){
                    reTKPlaylist.setVisibility(View.GONE);
                }else{
                    reTKPlaylist.setVisibility(View.VISIBLE);
                }
            }
        });

        theloaiheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reTKTheloai.getVisibility()==View.VISIBLE){
                    reTKTheloai.setVisibility(View.GONE);
                }else{
                    reTKTheloai.setVisibility(View.VISIBLE);
                }
            }
        });

        chudeheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reTKChude.getVisibility()==View.VISIBLE){
                    reTKChude.setVisibility(View.GONE);
                }else{
                    reTKChude.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void anhxa() {
        baihatheader=view.findViewById(R.id.baihat);
        casiheader=view.findViewById(R.id.casi);
        albumheader=view.findViewById(R.id.album);
        playlistheader=view.findViewById(R.id.playlist);
        theloaiheader=view.findViewById(R.id.theloai);
        chudeheader=view.findViewById(R.id.chude);
        toolbar=view.findViewById(R.id.tbFragmentTimKiem);
        reTimKiem=view.findViewById(R.id.recycleviewFragmentTimKiem);
        reTKCasi=view.findViewById(R.id.recycleviewFragmentTimKiemCasi);
        reTKAlbum=view.findViewById(R.id.recycleviewFragmentTimKiemAlbum);
        reTKPlaylist=view.findViewById(R.id.recycleviewFragmentTimKiemPlaylist);
        reTKTheloai=view.findViewById(R.id.recycleviewFragmentTimKiemTheloai);
        reTKChude=view.findViewById(R.id.recycleviewFragmentTimKiemChude);
        txtKotimthay=view.findViewById(R.id.txtFragmentTimKiemKhongCoDuLieu);

        //KHỞI TẠO CHO TOOLBAR
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_view,menu);
        final MenuItem menuItem=menu.findItem(R.id.menu_search);

        //tim casi


        final SearchView searchView= (SearchView) menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                TimTukhoa(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void TimTukhoa(String query) {
        Dataservice dataservice= APIService.getService();
        Call<SearchAll> callback=dataservice.GetKetQuaTimKiem(query);
        callback.enqueue(new Callback<SearchAll>() {
            @Override
            public void onResponse(Call<SearchAll> call, Response<SearchAll> response) {
                sa= response.body();
                mangbh=new ArrayList<Baihat>(sa.getBaihat());
                mangcasi=new ArrayList<Casi>(sa.getCasi());
                mangalbum=new ArrayList<Album>(sa.getAlbum());
                mangplaylist=new ArrayList<Playlist>(sa.getPlaylist());
                mangtl=new ArrayList<Theloaibaihat>(sa.getTheloaibaihat());
                mangchude=new ArrayList<Chudebaihat>(sa.getChudebaihat());
                if(mangbh.size()==0 && mangcasi.size()==0 && mangalbum.size()==0 && mangplaylist.size()==0 && mangtl.size()==0 && mangchude.size()==0) {


                    baihatheader.setVisibility(View.GONE);
                    casiheader.setVisibility(View.GONE);
                    albumheader.setVisibility(View.GONE);
                    playlistheader.setVisibility(View.GONE);
                    theloaiheader.setVisibility(View.GONE);
                    chudeheader.setVisibility(View.GONE);

                    reTimKiem.setVisibility(View.GONE);
                    reTKCasi.setVisibility(View.GONE);
                    reTKAlbum.setVisibility(View.GONE);
                    reTKPlaylist.setVisibility(View.GONE);
                    reTKTheloai.setVisibility(View.GONE);
                    reTKChude.setVisibility(View.GONE);
                    txtKotimthay.setVisibility(View.VISIBLE);
                }else {
                    txtKotimthay.setVisibility(View.GONE);


                    if(mangbh.size()>0){
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        timKiemAdapter = new TimKiemAdapter(getActivity(), mangbh);

                        reTimKiem.setLayoutManager(linearLayoutManager);
                        reTimKiem.setAdapter(timKiemAdapter);

                        baihatheader.setVisibility(View.VISIBLE);
                        reTimKiem.setVisibility(View.VISIBLE);
                    }

                    if(mangcasi.size()>0){
                        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
                        timKiemCasiAdapter =new TimKiemCasiAdapter(getActivity(),mangcasi);

                        reTKCasi.setLayoutManager(linearLayoutManager1);
                        reTKCasi.setAdapter(timKiemCasiAdapter);

                        casiheader.setVisibility(View.VISIBLE);
                        reTKCasi.setVisibility(View.VISIBLE);
                    }

                    if(mangalbum.size()>0){
                        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
                        timKiemAlbumAdapter=new TimKiemAlbumAdapter(getActivity(),mangalbum);

                        reTKAlbum.setLayoutManager(linearLayoutManager2);
                        reTKAlbum.setAdapter(timKiemAlbumAdapter);

                        albumheader.setVisibility(View.VISIBLE);
                        reTKAlbum.setVisibility(View.VISIBLE);
                    }

                    if(mangplaylist.size()>0){
                        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getActivity());
                        timKiemPlaylistAdapter=new TimKiemPlaylistAdapter(getActivity(),mangplaylist);

                        reTKPlaylist.setLayoutManager(linearLayoutManager3);
                        reTKPlaylist.setAdapter(timKiemPlaylistAdapter);

                        playlistheader.setVisibility(View.VISIBLE);
                        reTKPlaylist.setVisibility(View.VISIBLE);
                    }

                    if(mangtl.size()>0){
                        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(getActivity());
                        timKiemTheloaiAdapter=new TimKiemTheloaiAdapter(getActivity(),mangtl);
                        reTKTheloai.setLayoutManager(linearLayoutManager4);

                        reTKTheloai.setAdapter(timKiemTheloaiAdapter);


                        theloaiheader.setVisibility(View.VISIBLE);
                        reTKTheloai.setVisibility(View.VISIBLE);
                    }

                    if(mangchude.size()>0){
                        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(getActivity());
                        timKiemChudeAdapter=new TimKiemChudeAdapter(getActivity(),mangchude);

                        reTKChude.setLayoutManager(linearLayoutManager5);
                        reTKChude.setAdapter(timKiemChudeAdapter);

                        chudeheader.setVisibility(View.VISIBLE);
                        reTKChude.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<SearchAll> call, Throwable t) {
                Log.d("lỗi tk",t.getMessage());
            }
        });
    }
    /*private void TimTukhoaBaihat(String query){
        Dataservice dataservice= APIService.getService();
        Call<List<Baihat>> callback=dataservice.GetKetquaTimkiem(query);
        callback.enqueue(new Callback<List<Baihat>>() {
            @Override
            public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                mangbh= (ArrayList<Baihat>) response.body();
                //Log.d("mangbh",String.valueOf(mangbh.size()));
                if(mangbh.size()>0){
                    timKiemAdapter=new TimKiemAdapter(getActivity(),mangbh);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                    reTimKiem.setLayoutManager(linearLayoutManager);
                    reTimKiem.setAdapter(timKiemAdapter);
                    txtKotimthay.setVisibility(View.GONE);
                    reTimKiem.setVisibility(View.VISIBLE);
                }else {
                    reTimKiem.setVisibility(View.GONE);
                    txtKotimthay.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Baihat>> call, Throwable t) {
                Log.d("lỗi tk",t.getMessage());
            }
        });
    }*/

}
