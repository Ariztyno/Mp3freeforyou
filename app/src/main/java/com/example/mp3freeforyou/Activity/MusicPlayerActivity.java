package com.example.mp3freeforyou.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mp3freeforyou.Adapter.MusicPlayerViewPagerAdapter;
import com.example.mp3freeforyou.Fragment.Fragment_MusicPlayer_Danhsachbaihat;
import com.example.mp3freeforyou.Fragment.Fragment_MusicPlayer_Dianhac;
import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicPlayerActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtTimeSong,txtTotalTimeSong;
    SeekBar seekBar;
    ImageButton imgplay,imgprev,imgnext,imgshuffle,imgrepeat1only,imgrepeatall;
    ViewPager viewPagermusicplayer;
    MediaPlayer mediaPlayer;
    int position=0; //bắt giá trị các nút next,prev,...
    boolean repeatAll=false;
    boolean repeat1Only=false;
    boolean random=false;
    boolean next =false;
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

    Fragment_MusicPlayer_Danhsachbaihat fmp_Danhsachbaihat;
    Fragment_MusicPlayer_Dianhac fmp_Dianhac;
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

    private void eventClick() {
        //khi ca khúc phát sẽ thay đổi hình dạng của nút
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //kiểm tra dữ liệu có lấy đc hay ko?
                if(adapterViewPager.getItem(1)!=null){
                    if(mangbaihat.size()>0){
                        fmp_Dianhac.Playnhac(mangbaihat.get(0).getHinhBaiHat());
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
                        fmp_Dianhac.Playnhac(mangbaihat.get(position).getHinhBaiHat());
                        getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());
                        UpdateTime();

                        Addtolichsu(mangbaihat.get(position).getIdBaiHat().toString());
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
                        fmp_Dianhac.Playnhac(mangbaihat.get(position).getHinhBaiHat());
                        getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());
                        UpdateTime();

                        Addtolichsu(mangbaihat.get(position).getIdBaiHat().toString());
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
        mangbaihat.clear();
        if(intent!=null){
            if(intent.hasExtra("Cakhuc")){
                Baihat baihat=intent.getParcelableExtra("Cakhuc");
                Log.d("Cakhuc",baihat.getTenBaiHat());

                mangbaihat.add(baihat); //hứng dữ liệu vào danh sách
                //Toast.makeText(this,baihat.getTenBaiHat(),Toast.LENGTH_SHORT).show();
            }
            if(intent.hasExtra("DSCakhuc")){
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
                mangbaihat.clear();
            }
        });
        //toolbar end

        //khoi tao cho Fragment
        fmp_Dianhac=new Fragment_MusicPlayer_Dianhac();
        fmp_Danhsachbaihat=new Fragment_MusicPlayer_Danhsachbaihat();
        //khoi tao cho Fragment end
        //viewpager adapter
        adapterViewPager=new MusicPlayerViewPagerAdapter(getSupportFragmentManager(),BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapterViewPager.AddFragment(fmp_Danhsachbaihat);
        adapterViewPager.AddFragment(fmp_Dianhac);

        viewPagermusicplayer.setAdapter(adapterViewPager);//set adapter

        //viewpager adapter end

        fmp_Dianhac= (Fragment_MusicPlayer_Dianhac) adapterViewPager.getItem(1);//gán hình lên đĩa nhạc
        //PLAY BÀI ĐẦU TIÊN TRONG DS khi mở vào
        if(mangbaihat.size()>0){
            getSupportActionBar().setTitle(mangbaihat.get(0).getTenBaiHat());
            new PlayMp3().execute(mangbaihat.get(0).getLinkBaiHat());
            imgplay.setImageResource(R.drawable.iconpause);

            Addtolichsu(mangbaihat.get(position).getIdBaiHat().toString());
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
                mediaPlayer = new MediaPlayer();
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
                            fmp_Dianhac.Playnhac(mangbaihat.get(position).getHinhBaiHat());
                            getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());

                            Addtolichsu(mangbaihat.get(position).getIdBaiHat().toString());
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
                            fmp_Dianhac.Playnhac(mangbaihat.get(position).getHinhBaiHat());
                            getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());
                            Addtolichsu(mangbaihat.get(position).getIdBaiHat().toString());
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
                        fmp_Dianhac.Playnhac(mangbaihat.get(position).getHinhBaiHat());
                        getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());

                        Addtolichsu(mangbaihat.get(position).getIdBaiHat().toString());
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

    private void Addtolichsu(String idbaihat){
        if(PreferenceUtils.getUsername(getApplicationContext())!=null){
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
        }
    }

    //sukien nut them vao playlist va nut thich
    private void button(Baihat baihat){
        if(!PreferenceUtils.getUsername(getApplicationContext()).equals("")){

        }
    }
}
