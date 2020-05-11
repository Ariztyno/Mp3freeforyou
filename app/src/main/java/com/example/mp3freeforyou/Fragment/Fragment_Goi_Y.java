package com.example.mp3freeforyou.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mp3freeforyou.Activity.GoiYActivity;
import com.example.mp3freeforyou.R;

public class Fragment_Goi_Y extends Fragment {
    View view;
    TextView txtTitle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_goi_y,container,false);
        Anhxa();
        ViewMoreEvent(); // sự kiện bấm nút xem thêm
        return view;
    }

    private void ViewMoreEvent() {
        txtTitle.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), GoiYActivity.class);
            startActivity(intent);
        });
    }

    private void Anhxa() {
        txtTitle=view.findViewById(R.id.txtTitleGoiy);
    }
}
