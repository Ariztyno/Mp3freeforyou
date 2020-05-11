package com.example.mp3freeforyou.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.animation.ObjectAnimator;

import com.example.mp3freeforyou.Adapter.MusicPlayerViewPagerAdapter;
import com.example.mp3freeforyou.Fragment.Fragment_MusicPlayer_Danhsachbaihat;
import com.example.mp3freeforyou.Fragment.Fragment_MusicPlayer_Dianhac;
import com.example.mp3freeforyou.Fragment.Fragment_MusicPlayer_InfoBaiHat;
import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.Model.CustomMediaPlayer;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mp3freeforyou.Ultils.Constants.KEY_ARRAY_MANGBAIHAT;

public class MusicPlayerActivity extends AppCompatActivity {
    RelativeLayout relativelayout_notexpanded;
    LinearLayout layout;
    ViewGroup.LayoutParams params;

    CircleIndicator MusicPlayerindicator;

    public static Toolbar toolbar;
    public static TextView txtTimeSong,txtTotalTimeSong;
    public static SeekBar seekBar;
    public static ImageButton imgplay;
    public static ImageButton imgprev;
    public static ImageButton imgnext;
    ImageButton imgshuffle;
    ImageButton imgrepeat1only;
    ImageButton imgrepeatall;
    ViewPager viewPagermusicplayer;
    public static CustomMediaPlayer mediaPlayer;
    public static int position=0; //bắt giá trị các nút next,prev,...
    public static boolean repeatAll=false;
    public static boolean repeat1Only=false;
    public static boolean random=false;
    public static boolean next =false;

    boolean expanded=true;
    //mạng nhận ds bài hát
    public static ArrayList<Baihat> mangbaihat=new ArrayList<>();

    //Viewpager
    public static MusicPlayerViewPagerAdapter adapterViewPager;

    /**
     * Indicates that only the current fragment will be
     * in the Lifecycle.State#RESUMED state. All other Fragments
     * are capped at Lifecycle.State#STARTED.
     */
    public static final int BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT = 1;

    public static Fragment_MusicPlayer_Danhsachbaihat fmp_Danhsachbaihat;
    public static Fragment_MusicPlayer_Dianhac fmp_Dianhac;
    public static Fragment_MusicPlayer_InfoBaiHat fmp_infobaihat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        //kiểm tra kết nối mạng
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Anhxa();
        GetDataFromIntent();
        init();
        eventClick();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
        if(mangbaihat!=null){
            SharedPreferences appSharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this.getApplicationContext());
            SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
            Gson gson = new Gson();
            String json = gson.toJson(mangbaihat);
            prefsEditor.putString("MangBaiHat", json);
            prefsEditor.apply();
        }
        mangbaihat.clear();
    }

    private void eventClick() {
        //khi ca khúc phát sẽ thay đổi hình dạng của nút
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //kiểm tra dữ liệu có lấy đc hay ko?
                if(adapterViewPager.getItem(1)!=null){
                    if(mangbaihat.size()>0){
                        //fmp_infobaihat.LoadImgForInfoFrg(mangbaihat.get(0).getHinhBaiHat());
                        //fmp_infobaihat.LoadTextViewForInfoFrg(mangbaihat.get(0).getTenBaiHat());
                        //fmp_infobaihat.LoadSongData(mangbaihat.get(0).getIdCaSi(),mangbaihat.get(0).getIdTheLoai(),mangbaihat.get(0).getTenAlbum(),mangbaihat.get(0).getIdBaiHat());

                        fmp_Dianhac.Playnhac(mangbaihat.get(0).getHinhBaiHat());
                        fmp_Danhsachbaihat.Getdata();
                        handler.removeCallbacks(this);//xóa mấy hình cũ khi load hình

                    }else {
                        //nếu ko đọc đc dữ liệu
                        handler.postDelayed(this,300);
                    }
                }
            }
        },500);//0.5 giây

        //sự kiện nút play
        imgplay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                //NẾU ĐANG PHÁT THÌ SẼ PAUSE LẠI
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    imgplay.setImageResource(R.drawable.iconplay);
                    //ngừng việc quay hình
                    if (fmp_Dianhac.obAnimator!=null){
                        fmp_Dianhac.obAnimator.pause();
                    }
                }
                else //ngược lại thì
                {
                    mediaPlayer.start();
                    imgplay.setImageResource(R.drawable.iconpause);
                    //tiếp tục quay hình
                    if (fmp_Dianhac.obAnimator!=null){
                        fmp_Dianhac.obAnimator.resume();
                    }
                }
            }
        });

        //sự kiện nút lập lại all
        imgrepeatall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeatAll==false){
                    if(random==true){
                        random=false;
                        imgshuffle.setImageResource(R.drawable.iconshuffle);
                        imgrepeatall.setImageResource(R.drawable.iconrepeat);
                        repeatAll=true;
                    }else if(repeat1Only==true){
                        repeat1Only=false;
                        imgrepeat1only.setImageResource(R.drawable.iconrepeated1);
                        imgrepeatall.setImageResource(R.drawable.iconrepeat);
                        repeatAll=true;
                    }else {
                        imgrepeatall.setImageResource(R.drawable.iconrepeat);
                        repeatAll=true;
                    }
                }else {
                    imgrepeatall.setImageResource(R.drawable.iconrepeated);
                    repeatAll=false;
                }
            }
        });

        //sự kiện nút lập bài hát đang phát
        imgrepeat1only.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeat1Only==false){
                    if(random==true){
                        random=false;
                        imgshuffle.setImageResource(R.drawable.iconshuffle);
                        imgrepeat1only.setImageResource(R.drawable.iconrepeat1);
                        repeat1Only=true;
                    }else if(repeatAll==true){
                        repeatAll=false;
                        imgrepeatall.setImageResource(R.drawable.iconrepeated);
                        imgrepeat1only.setImageResource(R.drawable.iconrepeat1);
                        repeat1Only=true;
                    }else {
                        imgrepeat1only.setImageResource(R.drawable.iconrepeat1);
                        repeat1Only=true;
                    }
                }else {
                    imgrepeat1only.setImageResource(R.drawable.iconrepeated1);
                    repeat1Only=false;
                }
            }
        });

        //sự kiện nút ramdom
        imgshuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(random==false){
                    if(repeat1Only==true){
                        repeat1Only=false;
                        imgrepeat1only.setImageResource(R.drawable.iconrepeated1);
                        imgshuffle.setImageResource(R.drawable.iconshuffled);
                        random=true;
                    }else if(repeatAll==true){
                        repeatAll=false;
                        imgrepeatall.setImageResource(R.drawable.iconrepeated);
                        imgshuffle.setImageResource(R.drawable.iconshuffled);
                        random=true;
                    }else {
                        imgshuffle.setImageResource(R.drawable.iconshuffled);
                        random=true;
                    }
                }else {
                    imgshuffle.setImageResource(R.drawable.iconshuffle);
                    random=false;
                }
            }
        });

        //sự kiện seekbar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        //nút next
        imgnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mangbaihat.size()>0){
                    if(mediaPlayer.isPlaying() || mediaPlayer!=null){
                        mediaPlayer.stop();
                        mediaPlayer.release();;
                        mediaPlayer=null;
                    }
                    if(position<mangbaihat.size()){
                        imgplay.setImageResource(R.drawable.iconpause);
                        position++;
                        if(repeat1Only==true){
                            if(position==0){
                                position=mangbaihat.size();
                            }
                            position-=1;
                        }
                        if(random==true){
                            Random rd=new Random();
                            int index=rd.nextInt(mangbaihat.size());
                            if(index==position){
                                position=index-1;
                            }else {
                                position=index;
                            }
                        }
                        if(position>(mangbaihat.size()-1)){
                            position=0;
                        }
                        new PlayMp3().execute(mangbaihat.get(position).getLinkBaiHat());
                        //fmp_infobaihat.LoadImgForInfoFrg(mangbaihat.get(position).getHinhBaiHat());
                        //fmp_infobaihat.LoadTextViewForInfoFrg(mangbaihat.get(position).getTenBaiHat());
                        //fmp_infobaihat.LoadSongData(mangbaihat.get(position).getIdCaSi(),mangbaihat.get(position).getIdTheLoai(),mangbaihat.get(position).getTenAlbum(),mangbaihat.get(position).getIdBaiHat());
                        fmp_Dianhac.Playnhac(mangbaihat.get(position).getHinhBaiHat());
                        fmp_Danhsachbaihat.Getdata();
                        getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());
                        UpdateTime();

                        Addtolichsu(mangbaihat.get(position).getIdBaiHat().toString());
                        Addluotnghe(mangbaihat.get(position).getIdBaiHat().toString());
                    }
                }
                //sau 5 giây mới ấn tiếp đc tránh gây crash
                imgnext.setClickable(false);
                imgprev.setClickable(false);
                Handler handler5s=new Handler();
                handler5s.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgnext.setClickable(true);
                        imgprev.setClickable(true);
                    }
                },5000);//5 giây

            }
        });

        //nút prev
        imgprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mangbaihat.size()>0){
                    if(mediaPlayer.isPlaying() || mediaPlayer!=null){
                        mediaPlayer.stop();
                        mediaPlayer.release();;
                        mediaPlayer=null;
                    }
                    if(position<mangbaihat.size()){
                        imgplay.setImageResource(R.drawable.iconpause);
                        position--;
                        if(position<0){
                            position=mangbaihat.size()-1;
                        }
                        if(repeat1Only==true){
                            position+=1;
                        }
                        if(random==true){
                            Random rd=new Random();
                            int index=rd.nextInt(mangbaihat.size());
                            if(index==position){
                                position=index-1;
                            }else {
                                position=index;
                            }
                        }
                        new PlayMp3().execute(mangbaihat.get(position).getLinkBaiHat());
                        //fmp_infobaihat.LoadImgForInfoFrg(mangbaihat.get(position).getHinhBaiHat());
                        //fmp_infobaihat.LoadTextViewForInfoFrg(mangbaihat.get(position).getTenBaiHat());
                        //fmp_infobaihat.LoadSongData(mangbaihat.get(position).getIdCaSi(),mangbaihat.get(position).getIdTheLoai(),mangbaihat.get(position).getTenAlbum(),mangbaihat.get(position).getIdBaiHat());
                        fmp_Dianhac.Playnhac(mangbaihat.get(position).getHinhBaiHat());
                        fmp_Danhsachbaihat.Getdata();
                        getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());
                        UpdateTime();

                        Addtolichsu(mangbaihat.get(position).getIdBaiHat().toString());
                        Addluotnghe(mangbaihat.get(position).getIdBaiHat().toString());
                    }
                }
                //sau 5 giây mới ấn tiếp đc tránh gây crash
                imgnext.setClickable(false);
                imgprev.setClickable(false);
                Handler handler5s=new Handler();
                handler5s.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgnext.setClickable(true);
                        imgprev.setClickable(true);
                    }
                },5000);//5 giây

            }
        });
    }

    private void GetDataFromIntent() {
        //nhận intent
        Intent intent=getIntent();

        if(intent!=null){
            if(intent.hasExtra("Cakhuc")){
                //mediaPlayer.stop();
                mangbaihat.clear();
                Baihat baihat=intent.getParcelableExtra("Cakhuc");
                Log.d("Cakhuc",baihat.getTenBaiHat());

                mangbaihat.add(baihat); //hứng dữ liệu vào danh sách
                //Toast.makeText(this,baihat.getTenBaiHat(),Toast.LENGTH_SHORT).show();
            }
            if(intent.hasExtra("DSCakhuc")){
                //mediaPlayer.stop();
                mangbaihat.clear();
                ArrayList<Baihat> array=intent.getParcelableArrayListExtra("DSCakhuc");
                mangbaihat=array;
            }
        }
    }

    private void init() {
        //toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        toolbar.setTitleTextColor(Color.parseColor("#ff39aa"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //thoát màn hình đang play thì ngừng phát và clear danh sách
                mediaPlayer.stop();
                if(mangbaihat!=null){
                    SharedPreferences appSharedPrefs = PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(mangbaihat);
                    prefsEditor.putString("MangBaiHat", json);
                    prefsEditor.apply();
                }
                mangbaihat.clear();
            }
        });
        //toolbar end

        //khoi tao cho Fragment
        fmp_Dianhac=new Fragment_MusicPlayer_Dianhac();
        fmp_Danhsachbaihat=new Fragment_MusicPlayer_Danhsachbaihat();
        //fmp_infobaihat=new Fragment_MusicPlayer_InfoBaiHat();
        //khoi tao cho Fragment end
        //viewpager adapter
        adapterViewPager=new MusicPlayerViewPagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapterViewPager.AddFragment(fmp_Danhsachbaihat);
        adapterViewPager.AddFragment(fmp_Dianhac);
        //adapterViewPager.AddFragment(fmp_infobaihat);

        viewPagermusicplayer.setAdapter(adapterViewPager);//set adapter
        MusicPlayerindicator.setViewPager(viewPagermusicplayer);
        //viewpager adapter end

        fmp_Dianhac= (Fragment_MusicPlayer_Dianhac) adapterViewPager.getItem(1);//gán hình lên đĩa nhạc
        //fmp_infobaihat= (Fragment_MusicPlayer_InfoBaiHat) adapterViewPager.getItem(2);
        //PLAY BÀI ĐẦU TIÊN TRONG DS khi mở vào
        if(mangbaihat.size()>0){
            getSupportActionBar().setTitle(mangbaihat.get(0).getTenBaiHat());
            new PlayMp3().execute(mangbaihat.get(0).getLinkBaiHat());
            imgplay.setImageResource(R.drawable.iconpause);

            Addtolichsu(mangbaihat.get(position).getIdBaiHat().toString());
            Addluotnghe(mangbaihat.get(position).getIdBaiHat().toString());
        }
        //PLAY BÀI ĐẦU TIÊN TRONG DS khi mở vào end
    }

    private void Anhxa() {
        toolbar=findViewById(R.id.tbMusicPlayer);
        seekBar=findViewById(R.id.SeekBarMusicPlayer);
        txtTimeSong=findViewById(R.id.txtMusicPlayerTimeSong);
        txtTotalTimeSong=findViewById(R.id.txtMusicPlayerTotalTimeSong);
        imgnext=findViewById(R.id.imgBtnMusicPlayerNext);
        imgplay=findViewById(R.id.imgBtnMusicPlayerPlay);
        imgprev=findViewById(R.id.imgBtnMusicPlayerPrevious);
        imgshuffle=findViewById(R.id.imgBtnMusicPlayerShuffle);
        imgrepeat1only=findViewById(R.id.imgBtnMusicPlayerRepeated1Only);
        imgrepeatall=findViewById(R.id.imgBtnMusicPlayerRepeatAll);
        viewPagermusicplayer=findViewById(R.id.ViewPagerMusicPlayer);
        MusicPlayerindicator=findViewById(R.id.MusicPlayerindicator);

        layout=findViewById(R.id.root);
    }

    //thực hiện phát nhạc lấy link nhạc
    class PlayMp3 extends AsyncTask<String ,Void,String>{

        //trả link bài hát
        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        //nhận dữ liệu từ link của doinbackground
        @Override
        protected void onPostExecute(String baihat) {
            super.onPostExecute(baihat);//nhận dữ liệu
            try {
                mediaPlayer = new CustomMediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                //nếu trườn hợp load dũ liệu bị lỗi thì sẽ được load lại
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });
                mediaPlayer.setDataSource(baihat);
                //muốn phát nhạc phải gọi prepare
                mediaPlayer.prepare();

                //lưu bài hát vào lịch sử

            }catch (IOException e){
                e.printStackTrace();
            }

            mediaPlayer.start();
            TimeSong();
            UpdateTime();
        }
    }

    //cập nhật tổng thời gian của ca khúc
    private void TimeSong() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm:ss");
        txtTotalTimeSong.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());

    }

    //cập nhật seekbar khi play nhạc
    private void UpdateTime(){
        //sau 0.3 giây cập nhật lại thời gian từ dữ liệu bài hát
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //nếu có dữ liệu mới cập nhật
                if(mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("mm:ss");
                    txtTimeSong.setText((simpleDateFormat.format(mediaPlayer.getCurrentPosition())));
                    handler.postDelayed(this,300);

                    //sự kiện phát hết bài
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            next=true;
                            try{
                                Thread.sleep(1000);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        },300);//0.3 giây

        //khi hanler bài hát phát xong, 1 giây sẽ tự động chuyển bài
        final Handler handler1=new Handler();
        handler1.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                if(next==true){
                    if(position<mangbaihat.size()){
                        imgplay.setImageResource(R.drawable.iconpause);
                        position++;
                        if(repeat1Only==true){
                            if(position==0){
                                position=mangbaihat.size();
                            }
                            position-=1;

                            new PlayMp3().execute(mangbaihat.get(position).getLinkBaiHat());
                            //fmp_infobaihat.LoadImgForInfoFrg(mangbaihat.get(position).getHinhBaiHat());
                            //fmp_infobaihat.LoadTextViewForInfoFrg(mangbaihat.get(position).getTenBaiHat());
                            //fmp_infobaihat.LoadSongData(mangbaihat.get(position).getIdCaSi(),mangbaihat.get(position).getIdTheLoai(),mangbaihat.get(position).getTenAlbum(),mangbaihat.get(position).getIdBaiHat());
                            fmp_Dianhac.Playnhac(mangbaihat.get(position).getHinhBaiHat());
                            fmp_Danhsachbaihat.Getdata();
                            getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());

                            Addtolichsu(mangbaihat.get(position).getIdBaiHat().toString());
                            Addluotnghe(mangbaihat.get(position).getIdBaiHat().toString());
                        }
                        if(random==true){
                            Random rd=new Random();
                            int index=rd.nextInt(mangbaihat.size());
                            if(index==position){
                                position=index-1;
                            }else {
                                position=index;
                            }

                            new PlayMp3().execute(mangbaihat.get(position).getLinkBaiHat());
                            //fmp_infobaihat.LoadImgForInfoFrg(mangbaihat.get(position).getHinhBaiHat());
                            //fmp_infobaihat.LoadTextViewForInfoFrg(mangbaihat.get(position).getTenBaiHat());
                            //fmp_infobaihat.LoadSongData(mangbaihat.get(position).getIdCaSi(),mangbaihat.get(position).getIdTheLoai(),mangbaihat.get(position).getTenAlbum(),mangbaihat.get(position).getIdBaiHat());
                            fmp_Dianhac.Playnhac(mangbaihat.get(position).getHinhBaiHat());
                            fmp_Danhsachbaihat.Getdata();
                            getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());
                            Addtolichsu(mangbaihat.get(position).getIdBaiHat().toString());
                            Addluotnghe(mangbaihat.get(position).getIdBaiHat().toString());
                        }

                        if(position>(mangbaihat.size()-1)){
                            if(repeatAll==true){
                                position=0;

                                /*new PlayMp3().execute(mangbaihat.get(position).getLinkBaiHat());
                                fmp_Dianhac.Playnhac(mangbaihat.get(position).getHinhBaiHat());
                                getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());

                                Addtolichsu(mangbaihat.get(position).getIdBaiHat().toString());*/
                            }else {
                                //position=0;

                                mediaPlayer.pause();
                                imgplay.setImageResource(R.drawable.iconplay);
                                //ngừng việc quay hình
                                if (fmp_Dianhac.obAnimator!=null){
                                    fmp_Dianhac.obAnimator.pause();
                                }
                            }
                        }
                        new PlayMp3().execute(mangbaihat.get(position).getLinkBaiHat());
                        //fmp_infobaihat.LoadImgForInfoFrg(mangbaihat.get(position).getHinhBaiHat());
                        //fmp_infobaihat.LoadTextViewForInfoFrg(mangbaihat.get(position).getTenBaiHat());
                        //fmp_infobaihat.LoadSongData(mangbaihat.get(position).getIdCaSi(),mangbaihat.get(position).getIdTheLoai(),mangbaihat.get(position).getTenAlbum(),mangbaihat.get(position).getIdBaiHat());
                        fmp_Dianhac.Playnhac(mangbaihat.get(position).getHinhBaiHat());
                        fmp_Danhsachbaihat.Getdata();
                        getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());

                        Addtolichsu(mangbaihat.get(position).getIdBaiHat().toString());
                        Addluotnghe(mangbaihat.get(position).getIdBaiHat().toString());
                    }
                    //sau 5 giây mới ấn tiếp đc tránh gây crash
                    imgnext.setClickable(false);
                    imgprev.setClickable(false);
                    Handler handler5s=new Handler();
                    handler5s.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imgnext.setClickable(true);
                            imgprev.setClickable(true);
                        }
                    },5000);//5 giây

                    next=false;
                    handler1.removeCallbacks(this);
                }else {
                    handler1.postDelayed(this,1000);
                }
            }
        },1000);//1 giây
    }

    public void Addtolichsu(String idbaihat){
        if(PreferenceUtils.getUsername(getApplicationContext())!=null && !PreferenceUtils.getUsername(getApplicationContext()).equals("")){
            Dataservice dataservice= APIService.getService();
            Call<String> callback=dataservice.PostUpdateLichsuNguoidung(PreferenceUtils.getUsername(getApplicationContext()),idbaihat);
            callback.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String kq=response.body();
                    if(kq.equals("success")){
                        Log.d("musicplayer_addls","Thành công ls");
                    }else if(kq.equals("fail")){
                        Log.d("musicplayer_addls","Thất bại ls");
                    }else if(kq.equals("Ko xóa lịch sử đã tồn tại thành công!")){
                        Log.d("musicplayer_addls","ko del đc vi trí cũ");
                    }else {
                        Log.d("musicplayer_addls","Tk ko tồn tại");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
        }else{
            Log.d("musicplayer_addls","notlogin");
            if(PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext())!=null && !PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext()).equals("")){
                //lấy mảng các id bai hát trong dslichsunghe
                String[] oldlist=PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext()).split(",");

                ArrayList<String> oldlistidbaihat= new ArrayList<>(Arrays.asList(oldlist)); //convert sang Arraylist để dễ xử lý

                //bỏ tất cả những biến trùng
                Set<String> set = new HashSet<>(oldlistidbaihat);
                oldlistidbaihat.clear();
                oldlistidbaihat.addAll(set);

                //kiểm tra xem idbaihat sap thêm có trong mang chua
                if(oldlistidbaihat.contains(idbaihat)){
                    oldlistidbaihat.remove(idbaihat); //xóa khỏi danh sach
                    //có -> tìm và loại bỏ nơi nó xuất hiện
                    //   -> thêm lại vào bên trái
                    String temp ="";

                    for(int i=0;i<oldlistidbaihat.size();i++){
                        if(!oldlistidbaihat.get(i).equals(idbaihat)){
                            if(i==oldlistidbaihat.size()-1){
                                temp+=oldlistidbaihat.get(i);
                            }else{
                                temp+=oldlistidbaihat.get(i);
                                temp+=",";
                            }
                        }
                    }

                    //b cuối lưu lại
                    if(temp.equals("")){
                        PreferenceUtils.saveListenHistoryForNoAcc(idbaihat,getApplicationContext());
                    }else{
                        PreferenceUtils.saveListenHistoryForNoAcc(idbaihat+","+temp,getApplicationContext());
                    }

                    Log.d("musicplayer_addls_1",""+PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext()));
                }else {
                    //ko -> thêm vào từ bên trái

                    //b cuối lưu lại
                    PreferenceUtils.saveListenHistoryForNoAcc(idbaihat+","+PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext()),getApplicationContext());
                    Log.d("musicplayer_addls_2",""+PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext()));
                }
            }else{
                PreferenceUtils.saveListenHistoryForNoAcc(idbaihat,getApplicationContext());
                Log.d("musicplayer_addls_3",""+PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext()));
            }

        }
    }

    public void Addluotnghe(String idbaihat){
        Dataservice dataservice_luotnghe=APIService.getService();
        Call<String> call_updateluotnghe=dataservice_luotnghe.PostUpdateLuotNghe("1",idbaihat);
        call_updateluotnghe.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq=response.body();
                if(kq.equals("success")){
                    Log.d("musicplayer_addln","Thành công thêm lượt nghe");
                }if(kq.equals("fail")){
                    Log.d("musicplayer_addln","Thất bại thêm lượt nghe");
                }else{
                    Log.d("musicplayer_addln","Lỗi gửi tham số lên hệ thống");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("musicplayer_addln","onFailure");
            }
        });
    }

    //sukien nut them vao playlist va nut thich
    private void RefreshFragmentDSBH(Fragment_MusicPlayer_Danhsachbaihat fmp_Danhsachbaihat){
        fmp_Danhsachbaihat.Getdata();
    }
}
