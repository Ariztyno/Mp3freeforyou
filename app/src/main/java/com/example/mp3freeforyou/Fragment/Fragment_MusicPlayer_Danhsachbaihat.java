package com.example.mp3freeforyou.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.MusicPlayerActivity;
import com.example.mp3freeforyou.Adapter.MusicPlayerDSBaihatAdapter;
import com.example.mp3freeforyou.R;

public class Fragment_MusicPlayer_Danhsachbaihat extends Fragment {
    View view;
    RecyclerView recyclerViewFragMusicPlayerDSBaihat;

    MusicPlayerDSBaihatAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_musicplayer_danhsachbaihat,container,false);
        recyclerViewFragMusicPlayerDSBaihat=view.findViewById(R.id.recycleviewFragmentMusicPlayerDSBaihat);
        if(MusicPlayerActivity.mangbaihat.size()>0){
            adapter=new MusicPlayerDSBaihatAdapter(getActivity(), MusicPlayerActivity.mangbaihat);
            recyclerViewFragMusicPlayerDSBaihat.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewFragMusicPlayerDSBaihat.setAdapter(adapter);
        }else {
            Log.d("FrMP_DSBaihat","mangbaihat bị rỗng");
        }

        return view;
    }
}
