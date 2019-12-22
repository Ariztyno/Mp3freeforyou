package com.example.mp3freeforyou.Fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mp3freeforyou.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_MusicPlayer_Dianhac extends Fragment {
    View view;
    CircleImageView imgCircleDianhac;
    public ObjectAnimator obAnimator;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_musicplayer_dianhac,container,false);
        imgCircleDianhac=view.findViewById(R.id.imgCircleMusicPlayerDianhac);
        obAnimator=ObjectAnimator.ofFloat(imgCircleDianhac,"rotation",0f,360f);
        obAnimator.setDuration(10000);
        obAnimator.setRepeatCount(ValueAnimator.INFINITE);
        obAnimator.setRepeatMode(ValueAnimator.RESTART);
        obAnimator.setInterpolator(new LinearInterpolator());
        obAnimator.start();
        return view;
    }

    public void Playnhac(String hinhanh) {
        Picasso.with(getActivity()).load(hinhanh).into(imgCircleDianhac);
    }
}
