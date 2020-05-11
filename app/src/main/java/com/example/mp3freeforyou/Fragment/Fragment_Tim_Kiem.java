package com.example.mp3freeforyou.Fragment;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Adapter.SearchHistoryAdapter;
import com.example.mp3freeforyou.Adapter.SearchHistoryV2Adapter;
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
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Tim_Kiem extends Fragment {
    ArrayList<String> manggoiytimkiem=new ArrayList<>();
    ArrayList<String> manglichsutimkiem=new ArrayList<>();
    SearchHistoryV2Adapter searchHistoryV2Adapter_suggest,searchHistoryV2Adapter_history;
    
    SearchHistoryAdapter searchHistoryAdapter;
    public static SearchView searchView;
    List<String> mang=new LinkedList<>();

    View view;
    Toolbar toolbar;
    RecyclerView reTimKiem,reTKCasi,reTKAlbum,reTKPlaylist,reTKTheloai,reTKChude,reSearchHistory,reSearchSuggest;

    ScrollView svSResult,svSHistory;

    TextView txtKotimthay;
    //view cho fragment
    TimKiemAdapter timKiemAdapter; //baihat
    TimKiemCasiAdapter timKiemCasiAdapter;//casi
    TimKiemChudeAdapter timKiemChudeAdapter;
    TimKiemPlaylistAdapter timKiemPlaylistAdapter;
    TimKiemAlbumAdapter timKiemAlbumAdapter;
    TimKiemTheloaiAdapter timKiemTheloaiAdapter;

    //biến cho on typing text
    //List<String> mang;

    //header
    TextView baihatheader,casiheader,albumheader,playlistheader,theloaiheader,chudeheader,txtSearchHistory,txtDeleteSearchHistory;

    SearchAll sa;
    ArrayList<Baihat> mangbh;
    ArrayList<Casi> mangcasi;
    ArrayList<Album> mangalbum;
    ArrayList<Playlist> mangplaylist;
    ArrayList<Theloaibaihat> mangtl;
    ArrayList<Chudebaihat> mangchude;

    ArrayList<Baihat> mangbh_suggest;
    ArrayList<Casi> mangcasi_suggest;
    ArrayList<Album> mangalbum_suggest;
    ArrayList<Playlist> mangplaylist_suggest;
    ArrayList<Theloaibaihat> mangtl_suggest;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_tim_kiem,container,false);
        anhxa();
        HeaderOnClickClearRecycleView();
        ClearHistory();
        HideOrShowHistoryEventOnclick();
        GetDataSearchHistory();
        GetDataSearchSuggest();
        setHasOptionsMenu(true);

        return view;
    }

    private  void GetDataSearchHistory(){
        manglichsutimkiem.clear();

        String[] m = PreferenceUtils.getSearchHistory(getContext()).split(",");

        for(String item:m){
            if(!item.equals("")){
                if(!manglichsutimkiem.contains(item)){
                    manglichsutimkiem.add(item);
                }
            }
        }

        Collections.reverse(manglichsutimkiem);

        searchHistoryV2Adapter_history=new SearchHistoryV2Adapter(manglichsutimkiem,getContext());
        reSearchHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        reSearchHistory.setAdapter(searchHistoryV2Adapter_history);
        searchHistoryV2Adapter_history.notifyDataSetChanged();
    }

    private void GetDataSearchSuggest() {

        //thêm tên bài hát vào mảng mang (gợi ý tìm kiếm)
        Dataservice dataservice=APIService.getService();
        if(PreferenceUtils.getUsername(getContext())!=null){
            //trường hợp người dùng đã đăng nhập

            //lấy list tên bài hát gợi ý
            Call<List<Baihat>> calllistnamesong=dataservice.PostGoiyBaihatXemthem(PreferenceUtils.getUsername(getContext()));
            calllistnamesong.enqueue(new Callback<List<Baihat>>() {
                @Override
                public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                    mangbh_suggest= (ArrayList<Baihat>) response.body();
                    if (mangbh_suggest != null && mangbh_suggest.size() > 0) {
                        for (Baihat item : mangbh_suggest) {
                            String tam="bh_"+item.getTenBaiHat();
                            manggoiytimkiem.add(tam);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Baihat>> call, Throwable t) {

                }
            });

            //lấy list casi
            Call<List<Casi>> calllistnamecasi=dataservice.PostGoiyCasiXemthem(PreferenceUtils.getUsername(getContext()));
            calllistnamecasi.enqueue(new Callback<List<Casi>>() {
                @Override
                public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                    mangcasi_suggest= (ArrayList<Casi>) response.body();
                    if(mangcasi_suggest !=null &&mangcasi_suggest.size()>0){
                        for(Casi item: mangcasi_suggest){
                            String tam="cs_"+item.getTenCaSi();
                            manggoiytimkiem.add(tam);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Casi>> call, Throwable t) {

                }
            });

            //album
            Call<List<Album>> calllistnamealbum=dataservice.PostGoiyAlbumXemthem(PreferenceUtils.getUsername(getContext()));
            calllistnamealbum.enqueue(new Callback<List<Album>>() {
                @Override
                public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                    mangalbum_suggest= (ArrayList<Album>) response.body();
                    if(mangalbum_suggest!=null && mangalbum_suggest.size()>0){
                        for(Album item: mangalbum_suggest){
                            String tam="ab_"+item.getTenAlbum();
                            manggoiytimkiem.add(tam);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Album>> call, Throwable t) {

                }
            });

            //playlist
            Call<List<Playlist>> calllistnameplaylist=dataservice.PostGoiyPlaylistXemThem(PreferenceUtils.getUsername(getContext()));
            calllistnameplaylist.enqueue(new Callback<List<Playlist>>() {
                @Override
                public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                    mangplaylist_suggest= (ArrayList<Playlist>) response.body();
                    if(mangplaylist_suggest!=null && mangplaylist_suggest.size()>0){
                        for(Playlist item: mangplaylist_suggest){
                            String tam="pl_"+item.getTenPlaylist();
                            manggoiytimkiem.add(tam);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Playlist>> call, Throwable t) {

                }
            });

            //the loai
            Call<List<Theloaibaihat>> calllistnametheloai=dataservice.GetDanhSachTheloai();
            calllistnametheloai.enqueue(new Callback<List<Theloaibaihat>>() {
                @Override
                public void onResponse(Call<List<Theloaibaihat>> call, Response<List<Theloaibaihat>> response) {
                    mangtl_suggest= (ArrayList<Theloaibaihat>) response.body();
                    if(mangtl_suggest!=null && mangtl_suggest.size()>0){
                        for(Theloaibaihat item: mangtl_suggest){
                            String tam="tl_"+item.getTenTheLoai();
                            manggoiytimkiem.add(tam);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Theloaibaihat>> call, Throwable t) {

                }
            });
        }else{
            //check if banlist hoặc quiz choice is null replace with ""
            if (PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getContext()) == null) {
                PreferenceUtils.saveListIdTheloaibaihatfromQuizChoice("", getContext());
            }
            if (PreferenceUtils.getListIdCasifromQuizChoice(getContext()) == null) {
                PreferenceUtils.saveListIdCasifromQuizChoice("", getContext());
            }
            if (PreferenceUtils.getBanListIdCaSi(getContext()) == null) {
                PreferenceUtils.saveBanListIdCaSi("", getContext());
            }
            if (PreferenceUtils.getBanListIdBaihat(getContext()) == null) {
                PreferenceUtils.saveBanListIdBaihat("", getContext());
            }
            if (PreferenceUtils.getListenHistoryForNoAcc(getContext()) == null) {
                PreferenceUtils.saveListenHistoryForNoAcc("", getContext());
            }
            if(PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getContext()).matches(".*\\d.*")||PreferenceUtils.getListIdCasifromQuizChoice(getContext()).matches(".*\\d.*")||PreferenceUtils.getBanListIdCaSi(getContext()).matches(".*\\d.*")||PreferenceUtils.getBanListIdBaihat(getContext()).matches(".*\\d.*") || PreferenceUtils.getListenHistoryForNoAcc(getContext()).matches(".*\\d.*")) {

                //check if banlist hoặc quiz choice is null replace with ""
                if (PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getContext()) == null) {
                    PreferenceUtils.saveListIdTheloaibaihatfromQuizChoice("", getContext());
                }
                if (PreferenceUtils.getListIdCasifromQuizChoice(getContext()) == null) {
                    PreferenceUtils.saveListIdCasifromQuizChoice("", getContext());
                }
                if (PreferenceUtils.getBanListIdCaSi(getContext()) == null) {
                    PreferenceUtils.saveBanListIdCaSi("", getContext());
                }
                if (PreferenceUtils.getBanListIdBaihat(getContext()) == null) {
                    PreferenceUtils.saveBanListIdBaihat("", getContext());
                }
                if (PreferenceUtils.getListenHistoryForNoAcc(getContext()) == null) {
                    PreferenceUtils.saveListenHistoryForNoAcc("", getContext());
                }

                //trường hợp chưa đăng nhập
                Call<List<Baihat>> calllistnamesong = dataservice.PostGoiyBaihatForNoAccXemthem(PreferenceUtils.getListenHistoryForNoAcc(getContext()),PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getContext()), PreferenceUtils.getListIdCasifromQuizChoice(getContext()), PreferenceUtils.getBanListIdCaSi(getContext()), PreferenceUtils.getBanListIdBaihat(getContext()));
                calllistnamesong.enqueue(new Callback<List<Baihat>>() {
                    @Override
                    public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                        mangbh_suggest= (ArrayList<Baihat>) response.body();
                        if (mangbh_suggest != null && mangbh_suggest.size() > 0) {
                            for (Baihat item : mangbh_suggest) {
                                manggoiytimkiem.add("bh_"+item.getTenBaiHat());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Baihat>> call, Throwable t) {

                    }
                });

                //casi fornoacc
                Call<List<Casi>> calllistnamecasi=dataservice.PostGoiyCasiForNoAccXemthem(PreferenceUtils.getListenHistoryForNoAcc(getContext()),PreferenceUtils.getListIdCasifromQuizChoice(getContext()),PreferenceUtils.getBanListIdCaSi(getContext()));
                calllistnamecasi.enqueue(new Callback<List<Casi>>() {
                    @Override
                    public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                        mangcasi_suggest= (ArrayList<Casi>) response.body();
                        if(mangcasi_suggest !=null &&mangcasi_suggest.size()>0){
                            for(Casi item: mangcasi_suggest){
                                manggoiytimkiem.add("cs_"+item.getTenCaSi());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Casi>> call, Throwable t) {

                    }
                });

                //album noacc
                Call<List<Album>> calllistnamealbum=dataservice.PostGoiyAlbumForNoAccXemthem(PreferenceUtils.getListenHistoryForNoAcc(getContext()),PreferenceUtils.getListIdCasifromQuizChoice(getContext()),PreferenceUtils.getBanListIdCaSi(getContext()));
                calllistnamealbum.enqueue(new Callback<List<Album>>() {
                    @Override
                    public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                        mangalbum_suggest= (ArrayList<Album>) response.body();
                        if(mangalbum_suggest!=null && mangalbum_suggest.size()>0){
                            for(Album item: mangalbum_suggest){
                                manggoiytimkiem.add("ab_"+item.getTenAlbum());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Album>> call, Throwable t) {

                    }
                });

                //playlist fornoacc
                Call<List<Playlist>> calllistnameplaylist=dataservice.PostGoiyPlaylistForNoAccXemThem(PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getContext()),PreferenceUtils.getListIdCasifromQuizChoice(getContext()),PreferenceUtils.getBanListIdCaSi(getContext()));
                calllistnameplaylist.enqueue(new Callback<List<Playlist>>() {
                    @Override
                    public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                        mangplaylist_suggest= (ArrayList<Playlist>) response.body();
                        if(mangplaylist_suggest!=null && mangplaylist_suggest.size()>0){
                            for(Playlist item: mangplaylist_suggest){
                                manggoiytimkiem.add("pl_"+item.getTenPlaylist());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Playlist>> call, Throwable t) {

                    }
                });

                //the loai
                Call<List<Theloaibaihat>> calllistnametheloai=dataservice.GetDanhSachTheloai();
                calllistnametheloai.enqueue(new Callback<List<Theloaibaihat>>() {
                    @Override
                    public void onResponse(Call<List<Theloaibaihat>> call, Response<List<Theloaibaihat>> response) {
                        mangtl_suggest= (ArrayList<Theloaibaihat>) response.body();
                        if(mangtl_suggest!=null && mangtl_suggest.size()>0){
                            for(Theloaibaihat item: mangtl_suggest){
                                String tam="tl_"+item.getTenTheLoai();
                                manggoiytimkiem.add(tam);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Theloaibaihat>> call, Throwable t) {

                    }
                });
            }else{
                Call<List<Baihat>> calllistnamesong=dataservice.GetAllBaiHat();
                calllistnamesong.enqueue(new Callback<List<Baihat>>() {
                    @Override
                    public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                        mangbh_suggest= (ArrayList<Baihat>) response.body();
                        if (mangbh_suggest != null && mangbh_suggest.size() > 0) {
                            for (Baihat item : mangbh_suggest) {
                                manggoiytimkiem.add("bh_"+item.getTenBaiHat());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Baihat>> call, Throwable t) {

                    }
                });

                //casi noacc nobanquiz
                Call<List<Casi>> calllistnamecasi=dataservice.GetDanhSachCasi();
                calllistnamecasi.enqueue(new Callback<List<Casi>>() {
                    @Override
                    public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                        mangcasi_suggest= (ArrayList<Casi>) response.body();
                        if(mangcasi_suggest !=null &&mangcasi_suggest.size()>0){
                            for(Casi item: mangcasi_suggest){
                                manggoiytimkiem.add("cs_"+item.getTenCaSi());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Casi>> call, Throwable t) {

                    }
                });

                //album noacc noquizban
                Call<List<Album>> calllistnamealbum=dataservice.GetDanhSachAlbum();
                calllistnamealbum.enqueue(new Callback<List<Album>>() {
                    @Override
                    public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                        mangalbum_suggest= (ArrayList<Album>) response.body();
                        if(mangalbum_suggest!=null && mangalbum_suggest.size()>0){
                            for(Album item: mangalbum_suggest){
                                manggoiytimkiem.add("ab_"+item.getTenAlbum());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Album>> call, Throwable t) {

                    }
                });

                //playlist fornoacc
                Call<List<Playlist>> calllistnameplaylist=dataservice.GetDanhSachPlaylist();
                calllistnameplaylist.enqueue(new Callback<List<Playlist>>() {
                    @Override
                    public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                        mangplaylist_suggest= (ArrayList<Playlist>) response.body();
                        if(mangplaylist_suggest!=null && mangplaylist_suggest.size()>0){
                            for(Playlist item: mangplaylist_suggest){
                                manggoiytimkiem.add("pl_"+item.getTenPlaylist());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Playlist>> call, Throwable t) {

                    }
                });

                //the loai
                Call<List<Theloaibaihat>> calllistnametheloai=dataservice.GetDanhSachTheloai();
                calllistnametheloai.enqueue(new Callback<List<Theloaibaihat>>() {
                    @Override
                    public void onResponse(Call<List<Theloaibaihat>> call, Response<List<Theloaibaihat>> response) {
                        mangtl_suggest= (ArrayList<Theloaibaihat>) response.body();
                        if(mangtl_suggest!=null && mangtl_suggest.size()>0){
                            for(Theloaibaihat item: mangtl_suggest){
                                String tam="tl_"+item.getTenTheLoai();
                                manggoiytimkiem.add(tam);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Theloaibaihat>> call, Throwable t) {

                    }
                });
            }
        }
        
        searchHistoryV2Adapter_suggest=new SearchHistoryV2Adapter(manggoiytimkiem,getContext());
        reSearchSuggest.setLayoutManager(new LinearLayoutManager(getContext()));
        reSearchSuggest.setAdapter(searchHistoryV2Adapter_suggest);
        searchHistoryV2Adapter_suggest.notifyDataSetChanged();

    }

    private void HideOrShowHistoryEventOnclick() {
        //init arrow direction for txt
        txtSearchHistory.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_down_20,0,0,0);

        txtSearchHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(svSHistory.getVisibility()==View.VISIBLE){
                    svSHistory.setVisibility(View.GONE);
                    svSResult.setVisibility(View.VISIBLE);
                    txtSearchHistory.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_up_20c,0,0,0);
                }else{
                    if(svSResult.getVisibility()==View.VISIBLE){
                        svSResult.setVisibility(View.GONE);
                        svSHistory.setVisibility(View.VISIBLE);
                        txtSearchHistory.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_down_20,0,0,0);
                    }
                }
            }
        });
    }

    private void ClearHistory() {

        txtDeleteSearchHistory.setOnClickListener(v -> {
            PreferenceUtils.saveSearchHistory("",getContext());

            GetDataSearchHistory();
            Toast.makeText(getContext(),"Đã xóa lịch sử tìm kiếm:"+ PreferenceUtils.getSearchHistory(getContext()),Toast.LENGTH_SHORT).show();
        });
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
        svSResult=view.findViewById(R.id.svSearchResult);
        svSHistory=view.findViewById(R.id.svSearchHistory);
        txtSearchHistory=view.findViewById(R.id.txtSearchHistory);
        txtDeleteSearchHistory=view.findViewById(R.id.txtDeleteSearchHistory);
        reSearchHistory=view.findViewById(R.id.reSearchHistory);
        reSearchSuggest=view.findViewById(R.id.reSearchSuggest);

        //KHỞI TẠO CHO TOOLBAR
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        

        inflater.inflate(R.menu.search_view,menu);
        final MenuItem menuItem=menu.findItem(R.id.menu_search);

        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        searchView= (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                //kiểm tra xem phần tử sắp thêm vào có trong chuỗi banlist hay chưa
                if(!PreferenceUtils.getSearchHistory(getContext()).equals("")){
                    String[] mangquery = PreferenceUtils.getSearchHistory(getContext()).split(",");
                    for(String item: mangquery){
                        if(item.equals(query)){
                            //có thì ko thêm và báo đã có + hiện dialog mới
                            ///AlertDialogAlreadyblocked_Baihat(mangbaihat.get(getLayoutPosition()));
                            Log.d("AddToShistory","Đã có trong danh sách");
                        }else{
                            //thêm vô thôi bro
                            String themmoi=PreferenceUtils.getSearchHistory(getContext())+","+query;
                            PreferenceUtils.saveSearchHistory(themmoi,getContext());
                            Log.d("AddToShistory","Chưa có trong danh sách: ");

                            GetDataSearchHistory();
                        }
                    }

                }else{
                    //thêm vô thôi bro
                    PreferenceUtils.saveSearchHistory(query,getContext());
                    Log.d("AddToShistory","Chưa có trong danh sách: ");

                    GetDataSearchHistory();
                }

                TimTukhoa(query);
                if(svSHistory.getVisibility()==View.VISIBLE){
                    svSHistory.setVisibility(View.GONE);
                    svSResult.setVisibility(View.VISIBLE);
                    txtSearchHistory.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconarrow_up_20c,0,0,0);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String query) {
                /*String[] m = PreferenceUtils.getSearchHistory(getContext()).split(",");

                for(String item:m){
                    if(!mang.contains(item)){
                        mang.add(item);
                    }
                }

                if(!PreferenceUtils.getSearchHistory(getContext()).equals("")){
                    String[] columns = new String[] { "_id", "text" };
                    Object[] temp = new Object[] { 0, "default" };

                    MatrixCursor cursor = new MatrixCursor(columns);
                    for(int i = 0; i < mang.size(); i++) {

                        temp[0] = i;
                        temp[1] = mang.get(i);

                        cursor.addRow(temp);

                    }

                    searchHistoryAdapter=new SearchHistoryAdapter(getContext(), cursor, mang);
                    searchView.setSuggestionsAdapter(searchHistoryAdapter);
                }else{
                    searchHistoryAdapter=new SearchHistoryAdapter(getContext(), null, mang);
                    searchView.setSuggestionsAdapter(searchHistoryAdapter);
                }*/
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

    /*public void loadHistory(Menu menu,List<String> mang) {

        if(!PreferenceUtils.getSearchHistory(getContext()).equals("")){
            String[] columns = new String[] { "_id", "text" };
            Object[] temp = new Object[] { 0, "default" };

            MatrixCursor cursor = new MatrixCursor(columns);
            for(int i = 0; i < mang.size(); i++) {

                temp[0] = i;
                temp[1] = mang.get(i);

                cursor.addRow(temp);

            }

            // SearchView
            SearchManager manager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);

            final SearchView search = (SearchView) menu.findItem(R.id.menu_search).getActionView();

            searchHistoryAdapter=new SearchHistoryAdapter(getContext(), cursor, mang);
            search.setSuggestionsAdapter(searchHistoryAdapter);
        }

    }*/
}
