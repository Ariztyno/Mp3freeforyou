package com.example.mp3freeforyou.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

public class Fragment_Tai_Khoan extends Fragment {
    View view;
    String email;
    String password;
    String username;
    String name;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_tai_khoan,container,false);
        username= PreferenceUtils.getUsername(getContext());
        if(username==null){
            FragmentInit_BeforeLogin();
        }
        else{
            FragmentInit_AfterLogin();
        }
        return view;
    }

    private void FragmentInit_BeforeLogin() {
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,new Fragment_BeforeLogin());
        fragmentTransaction.commit();
    }

    private void FragmentInit_AfterLogin() {
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,new Fragment_UserControlBoard());
        fragmentTransaction.commit();
    }

}
