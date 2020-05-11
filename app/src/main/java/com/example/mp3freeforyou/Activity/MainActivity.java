package com.example.mp3freeforyou.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.mp3freeforyou.Adapter.MainViewPagerAdapter;
import com.example.mp3freeforyou.Fragment.Fragment_Tai_Khoan;
import com.example.mp3freeforyou.Fragment.Fragment_Tim_Kiem;
import com.example.mp3freeforyou.Fragment.Fragment_Trang_Chu;
import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.mediaPlayer;
import static com.example.mp3freeforyou.Ultils.Constants.KEY_ARRAY_MANGBAIHAT;

public class MainActivity extends AppCompatActivity {
    ArrayList<Baihat> temp=new ArrayList<>();

    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton btnMusicplayer;

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
        log_check_quizchoice();

        anhxa(); //gán id cho các biến
        init(); //khoi tao

        btnMusicplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Type type = new TypeToken<List<Baihat>>(){}.getType();

                SharedPreferences appSharedPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getApplicationContext());
                Gson gson = new Gson();
                String json = appSharedPrefs.getString("MangBaiHat", "");
                temp = gson.fromJson(json, type);
                if(temp!=null){
                    Intent intent=new Intent(MainActivity.this,MusicPlayerActivity.class);
                    intent.putExtra("DSCakhuc",temp);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Không có danh sách phát nào gần đây cả",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void log_check_quizchoice() {
        Log.d("mainKS_ARRAYTHELOAI",""+ PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getApplicationContext()));
        Log.d("mainKS_ARRAYCASI",""+PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()));
        Log.d("main",""+PreferenceUtils.getUsername(getApplicationContext()));
    }

    private void init() {
        MainViewPagerAdapter mainViewPagerAdapter=new MainViewPagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mainViewPagerAdapter.addFragment(new Fragment_Trang_Chu(),"Trang chủ");
        mainViewPagerAdapter.addFragment(new Fragment_Tim_Kiem(),"Tìm kiếm");
        mainViewPagerAdapter.addFragment(new Fragment_Tai_Khoan(),"Tài khoản");


        //set adapter cho viewpager
        viewPager.setAdapter(mainViewPagerAdapter);
        Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();
        //set viewpager cho Tablayout
        tabLayout.setupWithViewPager(viewPager);
        //set icon cho tablayout
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.iconhome);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.iconsearched);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.iconuser);
    }

    private void anhxa() {
        tabLayout=findViewById(R.id.myTabLayout);
        viewPager=findViewById(R.id.myViewPager);
        btnMusicplayer=findViewById(R.id.floatingactionbuttonMusicplayer);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
