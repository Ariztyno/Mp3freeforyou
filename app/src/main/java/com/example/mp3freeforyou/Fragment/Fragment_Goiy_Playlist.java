package com.example.mp3freeforyou.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.DanhsachbaihatActivity;
import com.example.mp3freeforyou.Activity.Goiy_playlist_XemThemDSPlaylistActivity;
import com.example.mp3freeforyou.Adapter.PlaylistAdapter;
import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Goiy_Playlist extends Fragment {
    View view;
    TextView txtXemThemPlaylist,txtTitle;
    ListView lvplaylist;
    ArrayList<Playlist> mangplaylist;
    PlaylistAdapter playlistAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_playlist,container,false);
        Anhxa();
        Getdata();
        Xemthem();
        return view;
    }

    private void Getdata() {
        Dataservice dataservice= APIService.getService();
        if(PreferenceUtils.getUsername(getContext())!=null){
            Call<List<Playlist>> callback=dataservice.PostGoiyPlaylist(PreferenceUtils.getUsername(getContext()));
            callback.enqueue(new Callback<List<Playlist>>() {
                @Override
                public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                    mangplaylist= (ArrayList<Playlist>) response.body();
                    playlistAdapter=new PlaylistAdapter(getActivity(),android.R.layout.simple_list_item_1,mangplaylist);
                    lvplaylist.setAdapter(playlistAdapter);
                    setListViewHeightBasedOnChildren(lvplaylist);

                    //item lvplaylist on click event
                    lvplaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent=new Intent(getActivity(), DanhsachbaihatActivity.class);
                            intent.putExtra("itemplaylist",mangplaylist.get(position));
                            startActivity(intent);
                        }
                    });
                    Log.d("success:",mangplaylist.get(1).getTenPlaylist());
                }

                @Override
                public void onFailure(Call<List<Playlist>> call, Throwable t) {
                    Log.d("error:","something wrong");
                }
            });
        }else{
            if(PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getContext()).matches(".*\\d.*")|| PreferenceUtils.getListIdCasifromQuizChoice(getContext()).matches(".*\\d.*") || PreferenceUtils.getBanListIdCaSi(getContext()).matches(".*\\d.*")){

                //trong trường hợp các preference bị null save nó dưới dạng ""
                if(PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getContext())==null){
                    PreferenceUtils.saveListIdTheloaibaihatfromQuizChoice("",getContext());
                }
                if(PreferenceUtils.getListIdCasifromQuizChoice(getContext())==null){
                    PreferenceUtils.saveListIdCasifromQuizChoice("",getContext());
                }
                if(PreferenceUtils.getBanListIdCaSi(getContext())==null){
                    PreferenceUtils.saveBanListIdCaSi("",getContext());
                }

                Call<List<Playlist>> callback=dataservice.PostGoiyPlaylistForNoAcc(PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getContext()),PreferenceUtils.getListIdCasifromQuizChoice(getContext()),PreferenceUtils.getBanListIdCaSi(getContext()));
                callback.enqueue(new Callback<List<Playlist>>() {
                    @Override
                    public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                        mangplaylist= (ArrayList<Playlist>) response.body();
                        playlistAdapter=new PlaylistAdapter(getActivity(),android.R.layout.simple_list_item_1,mangplaylist);
                        lvplaylist.setAdapter(playlistAdapter);
                        setListViewHeightBasedOnChildren(lvplaylist);

                        //item lvplaylist on click event
                        lvplaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(getActivity(), DanhsachbaihatActivity.class);
                                intent.putExtra("itemplaylist",mangplaylist.get(position));
                                startActivity(intent);
                            }
                        });
                        Log.d("success:",mangplaylist.get(1).getTenPlaylist());
                    }

                    @Override
                    public void onFailure(Call<List<Playlist>> call, Throwable t) {
                        Log.d("error:","something wrong");
                    }
                });
            }else{
                Call<List<Playlist>> callback=dataservice.PostGoiyPlaylistForNoAccNoQuiz();
                callback.enqueue(new Callback<List<Playlist>>() {
                    @Override
                    public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                        mangplaylist= (ArrayList<Playlist>) response.body();
                        playlistAdapter=new PlaylistAdapter(getActivity(),android.R.layout.simple_list_item_1,mangplaylist);
                        lvplaylist.setAdapter(playlistAdapter);
                        setListViewHeightBasedOnChildren(lvplaylist);

                        //item lvplaylist on click event
                        lvplaylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent=new Intent(getActivity(), DanhsachbaihatActivity.class);
                                intent.putExtra("itemplaylist",mangplaylist.get(position));
                                startActivity(intent);
                            }
                        });
                        Log.d("success:",mangplaylist.get(1).getTenPlaylist());
                    }

                    @Override
                    public void onFailure(Call<List<Playlist>> call, Throwable t) {
                        Log.d("error:","something wrong");
                    }
                });
            }
        }
    }

    private void Xemthem() {
        txtXemThemPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Goiy_playlist_XemThemDSPlaylistActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    private void Anhxa() {
        lvplaylist=view.findViewById(R.id.lvPlaylist);
        txtXemThemPlaylist=view.findViewById(R.id.txtViewmorePlaylist);
        txtTitle=view.findViewById(R.id.txtTitleplaylist);
        txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if(listItem != null){
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
