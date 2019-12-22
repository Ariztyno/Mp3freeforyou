package com.example.mp3freeforyou.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mp3freeforyou.Activity.LoginActivity;
import com.example.mp3freeforyou.R;

public class Fragment_BeforeLogin extends Fragment {
    View view;

    Button btnDangnhap;
    TextView txtContent;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_beforelogin,container,false);
        anhxa();
        Dentrangdangnhaphoacdangky();
        return view;
    }

    private void Dentrangdangnhaphoacdangky() {
        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void anhxa() {
        txtContent=view.findViewById(R.id.txtFrgBeforeLoginContent);
        btnDangnhap=view.findViewById(R.id.btnToLoginpage);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
}

