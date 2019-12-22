package com.example.mp3freeforyou.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;

import com.example.mp3freeforyou.Adapter.MainViewPagerAdapter;
import com.example.mp3freeforyou.Fragment.Fragment_Tai_Khoan;
import com.example.mp3freeforyou.Fragment.Fragment_Tim_Kiem;
import com.example.mp3freeforyou.Fragment.Fragment_Trang_Chu;
import com.example.mp3freeforyou.R;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    /**
     * Indicates that only the current fragment will be
     * in the Lifecycle.State#RESUMED state. All other Fragments
     * are capped at Lifecycle.State#STARTED.
     */
    public static final int BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhxa(); //gán id cho các biến
        init(); //khoi tao
    }

    private void init() {
        MainViewPagerAdapter mainViewPagerAdapter=new MainViewPagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mainViewPagerAdapter.addFragment(new Fragment_Trang_Chu(),"Trang chủ");
        mainViewPagerAdapter.addFragment(new Fragment_Tim_Kiem(),"Tìm kiếm");
        mainViewPagerAdapter.addFragment(new Fragment_Tai_Khoan(),"Tài khoản");

        //set adapter cho viewpager
        viewPager.setAdapter(mainViewPagerAdapter);
        viewPager.getAdapter().notifyDataSetChanged();
        //set viewpager cho Tablayout
        tabLayout.setupWithViewPager(viewPager);
        //set icon cho tablayout
        tabLayout.getTabAt(0).setIcon(R.drawable.iconhome);
        tabLayout.getTabAt(1).setIcon(R.drawable.iconsearched);
        tabLayout.getTabAt(2).setIcon(R.drawable.iconuser);
    }

    private void anhxa() {
        tabLayout=findViewById(R.id.myTabLayout);
        viewPager=findViewById(R.id.myViewPager);
    }


}
