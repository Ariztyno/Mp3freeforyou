package com.example.mp3freeforyou.Adapter;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.QuanLyDSChanActivity;
import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.Model.CustomMediaPlayer;
import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.DOWNLOAD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.fmp_Dianhac;
import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.fmp_Danhsachbaihat;
import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.fmp_infobaihat;
import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.imgnext;
import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.imgplay;
import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.seekBar;
import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.toolbar;
import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.imgprev;
import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.mediaPlayer;
import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.txtTimeSong;
import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.txtTotalTimeSong;
import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.next;
import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.position;
import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.repeat1Only;
import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.repeatAll;
import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.random;
import static com.example.mp3freeforyou.Ultils.Constants.KEY_BANLIST_BAIHAT;
import static com.example.mp3freeforyou.Ultils.PreferenceUtils.getUsername;

public class MusicPlayerDSBaihatAdapter extends RecyclerView.Adapter<MusicPlayerDSBaihatAdapter.ViewHolder> {
    //khai báo các biến phục vụ cho việc chặn các bài hát thuộc ban list hoặc ban lít casi
    ArrayList<Casi> dscasi_biban;
    ArrayList<Casi> user_dscasi_biban;
    ArrayList<Baihat> user_dsbaihat_biban;

    //anh xa các nút trên dialog
    TextView btnLike,btnAddSongToPlaylist,btnDownload,btnDeleteFromPlaylist,btnBlock,btnBlockSong;

    RecyclerView relistcasi;
    AlertDialogCasiAdapter casiadapter;
    ArrayList<Casi> mangcasi= new ArrayList<>();
    View line_1,line_2;

    Handler delay_hanler=new Handler();

    RelativeLayout.LayoutParams parameter;

    ArrayList<Playlist> mangplaylist;
    Alert_Dialog_AddsongtoplaylistAdapter adapter;
    Context context;
    ArrayList<Baihat> mangbaihat;
    public MusicPlayerDSBaihatAdapter(Context context, ArrayList<Baihat> mangbaihat) {
        this.context = context;
        this.mangbaihat = mangbaihat;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_fragment_musicplayer_danhsachbaihat,parent,false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Baihat baihat=mangbaihat.get(position);
        holder.txttencasi.setText(baihat.getIdCaSi());
        holder.txttenbaihat.setText(baihat.getTenBaiHat());
        holder.txtindex.setText((position + 1) +"");
        Picasso.with(context).load(R.drawable.iconfloatingactionbutton_white_25).into(holder.imgCircleMusicPlayerDianhac);
        if(mediaPlayer==null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer.getDataSource().equals(baihat.getLinkBaiHat())){
                        holder.imgCircleMusicPlayerDianhac.setVisibility(View.VISIBLE);
                    }else {
                        holder.imgCircleMusicPlayerDianhac.setVisibility(View.GONE);
                    }
                }
            },1500);
        }else{
            if(mediaPlayer.getDataSource().equals(baihat.getLinkBaiHat())){
                holder.imgCircleMusicPlayerDianhac.setVisibility(View.VISIBLE);
            }else {
                holder.imgCircleMusicPlayerDianhac.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public int getItemCount() {
        return mangbaihat.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder{
        TextView txtindex,txttenbaihat,txttencasi;
        ImageView btnlike,btnaddtoplaylist,btndownload,imgExplore;
        ObjectAnimator obAnimator;
        CircleImageView imgCircleMusicPlayerDianhac;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txttencasi=itemView.findViewById(R.id.txtRowMusicPlayerDSBaihatTencasi);
            txttenbaihat=itemView.findViewById(R.id.txtRowMusicPlayerDSBaihatTenbaihat);
            txtindex=itemView.findViewById(R.id.txtRowMusicPlayerDSBaihatIndex);
            //btnlike=itemView.findViewById(R.id.imgLikeBtnRowMusicPlayerDSBH);
            //btnaddtoplaylist=itemView.findViewById(R.id.imgAddsongtoplaylistBtnRowMusicPlayerDSBH);
            //btndownload=itemView.findViewById(R.id.imgDownloadBtnRowMusicPlayerDSBH);
            imgExplore=itemView.findViewById(R.id.imgExplore);
            imgCircleMusicPlayerDianhac=itemView.findViewById(R.id.imgCircleMusicPlayerDianhac);

            //init isplaying
            obAnimator= ObjectAnimator.ofFloat(imgCircleMusicPlayerDianhac,"rotation",0f,360f);
            obAnimator.setDuration(10000);
            obAnimator.setRepeatCount(ValueAnimator.INFINITE);
            obAnimator.setRepeatMode(ValueAnimator.RESTART);
            obAnimator.setInterpolator(new LinearInterpolator());
            obAnimator.start();

            //sự kiện nút play
            /*imgplay.setOnClickListener(new View.OnClickListener() {
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
                            obAnimator.pause();
                        }
                    }
                    else //ngược lại thì
                    {
                        mediaPlayer.start();
                        imgplay.setImageResource(R.drawable.iconpause);
                        //tiếp tục quay hình
                        if (fmp_Dianhac.obAnimator!=null){
                            fmp_Dianhac.obAnimator.resume();
                            obAnimator.resume();
                        }
                    }
                }
            });*/


            //sukien an nut imgExplore
            imgExplore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog=new AlertDialog.Builder(context).create();
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.alert_dialog_explorer, null);

                    btnLike=dialogView.findViewById(R.id.txtYeuthich);
                    btnAddSongToPlaylist=dialogView.findViewById(R.id.txtThemvaoplaylist);
                    btnDownload=dialogView.findViewById(R.id.txtTaixuong);
                    btnDeleteFromPlaylist=dialogView.findViewById(R.id.txtRemove);
                    btnBlock=dialogView.findViewById(R.id.txtChan);

                    line_1=dialogView.findViewById(R.id.line_div1);
                    line_2=dialogView.findViewById(R.id.line_div2);

                    //disable click các nút trong 1,5s
                    btnLike.setClickable(false);
                    btnAddSongToPlaylist.setClickable(false);
                    btnDownload.setClickable(false);
                    btnDeleteFromPlaylist.setClickable(false);
                    btnBlock.setClickable(false);

                    btnBlock.setText("  Quản lý chặn");

                    delay_hanler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnLike.setClickable(true);
                            btnAddSongToPlaylist.setClickable(true);
                            btnDownload.setClickable(true);
                            btnDeleteFromPlaylist.setClickable(true);
                            btnBlock.setClickable(true);
                        }
                    },2000);

                    //SỰ KIỆN ẤN CÁC NÚT
                    //CÁC NÚT CHO NGƯỜI CHƯA ĐĂNG NHẬP
                    delay_hanler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnDownload.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startDownload(mangbaihat.get(getLayoutPosition()).getLinkBaiHat(),mangbaihat.get(getLayoutPosition()).getTenBaiHat());
                                }
                            });

                            btnBlock.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final AlertDialog alertDialog=new AlertDialog.Builder(context).create();
                                    LayoutInflater inflater = LayoutInflater.from(context);
                                    View dialogView = inflater.inflate(R.layout.alert_dialog_block, null);

                                    btnBlockSong=dialogView.findViewById(R.id.txtBlockSong);
                                    relistcasi=dialogView.findViewById(R.id.relistcasi_block);

                                    Dataservice dataservice=APIService.getService();
                                    Call<List<Casi>> call=dataservice.postgetlistcasifromlistnamecasi(mangbaihat.get(getLayoutPosition()).getIdCaSi());
                                    call.enqueue(new Callback<List<Casi>>() {
                                        @Override
                                        public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                                            mangcasi= (ArrayList<Casi>) response.body();
                                            casiadapter =new AlertDialogCasiAdapter(context,mangcasi);
                                            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
                                            relistcasi.setLayoutManager(linearLayoutManager1);
                                            relistcasi.setAdapter(casiadapter);
                                            Log.d("listcasi_block",""+mangcasi.size());
                                        }

                                        @Override
                                        public void onFailure(Call<List<Casi>> call, Throwable t) {
                                            Log.d("listcasi_onFailure",""+t);
                                        }
                                    });

                                    //init status chan for btnBlockSong
                                    if(PreferenceUtils.getUsername(context)!=null){
                                        //đối với người dùng đã đăng nhập
                                        Call<String> callchecksong=dataservice.PostGetBanStatusForSong(PreferenceUtils.getUsername(context),mangbaihat.get(getLayoutPosition()).getIdBaiHat());
                                        callchecksong.enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, Response<String> response) {
                                                String kq=response.body();
                                                if(kq.equals("yes")){
                                                    //init text
                                                    String t="  Bỏ chặn bài hát "+mangbaihat.get(getLayoutPosition()).getTenBaiHat();
                                                    btnBlockSong.setText(t);
                                                    Log.d("init chan/bochan","yes");
                                                }else if(kq.equals("no")){
                                                    //init text
                                                    String t="  Chặn bài hát "+mangbaihat.get(getLayoutPosition()).getTenBaiHat();
                                                    btnBlockSong.setText(t);
                                                    Log.d("init chan/bochan","no");
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<String> call, Throwable t) {
                                                Log.d("init chan/bochan",""+t);
                                            }
                                        });
                                    }else{
                                        //đối với người chưa đăng nhập
                                        if(PreferenceUtils.getBanListIdBaihat(context)!=null && !PreferenceUtils.getBanListIdBaihat(context).equals("")){
                                            //trường hợp banlist ko rỗng
                                            String[] mangidbanlistbaihat = PreferenceUtils.getBanListIdBaihat(context).split(",");
                                            boolean contain= Arrays.asList(mangidbanlistbaihat).contains(mangbaihat.get(getLayoutPosition()).getIdBaiHat());
                                            if(contain){
                                                //có là bỏ chặn
                                                String t="  Bỏ chặn bài hát "+mangbaihat.get(getLayoutPosition()).getTenBaiHat();
                                                btnBlockSong.setText(t);
                                            }else{
                                                //chưa có là chặn
                                                String t="  Chặn bài hát "+mangbaihat.get(getLayoutPosition()).getTenBaiHat();
                                                btnBlockSong.setText(t);
                                            }
                                        }else{
                                            //trường hợp banlist bị rỗng
                                            //chưa có là chặn
                                            String t="  Chặn bài hát "+mangbaihat.get(getLayoutPosition()).getTenBaiHat();
                                            btnBlockSong.setText(t);
                                            Toast.makeText(context,"Đã chặn",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    btnBlockSong.setOnClickListener(new View.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onClick(View v) {
                                            if(getUsername(context)!=null){
                                                //đã đăng nhập
                                                //sử dụng shared reference để lưu ban list id bài hát
                                                //kiểm tra nếu dsbanlist null or ko
                                                if(!KEY_BANLIST_BAIHAT.contains(mangbaihat.get(getLayoutPosition()))){
                                                    KEY_BANLIST_BAIHAT.add(mangbaihat.get(getLayoutPosition()));
                                                }
                                                PreferenceUtils.saveBanListIdBaihat(KEY_BANLIST_BAIHAT,context);

                                                Dataservice dataservice1=APIService.getService();
                                                Call<String> call1=dataservice1.postaddidbaihattobanlist(PreferenceUtils.getBanListIdBaihat(context),PreferenceUtils.getUsername(context));
                                                call1.enqueue(new Callback<String>() {
                                                    @Override
                                                    public void onResponse(Call<String> call, Response<String> response) {
                                                        String call1=response.body();
                                                        if(call1.equals("NOTCHANGE")){
                                                            String t="  Chặn bài hát "+mangbaihat.get(getLayoutPosition()).getTenBaiHat();
                                                            btnBlockSong.setText(t);
                                                            Bochansong(mangbaihat.get(getLayoutPosition()));
                                                        }else{
                                                            if(call1.equals("success")){
                                                                String t="  Bỏ chặn bài hát "+mangbaihat.get(getLayoutPosition()).getTenBaiHat();
                                                                btnBlockSong.setText(t);
                                                                Toast.makeText(context,"Đã chặn",Toast.LENGTH_SHORT).show();
                                                            }else {
                                                                Toast.makeText(context,"Có lỗi xảy ra",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<String> call, Throwable t) {
                                                        Toast.makeText(context,""+t,Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                //Toast.makeText(context,"Đã chặn",Toast.LENGTH_SHORT).show();
                                            }else {
                                                //chưa đăng nhập
                                                //sử dụng shared reference để lưu ban list id bài hát

                                                //kiểm tra xem phần tử sắp thêm vào có trong chuỗi banlist hay chưa
                                                if(PreferenceUtils.getBanListIdBaihat(context)!=null && !PreferenceUtils.getBanListIdBaihat(context).equals("")){
                                                    String[] mangidbanlistbaihat = PreferenceUtils.getBanListIdBaihat(context).split(",");
                                                    boolean contain= Arrays.asList(mangidbanlistbaihat).contains(mangbaihat.get(getLayoutPosition()).getIdBaiHat());
                                                    if(contain){
                                                        //có thì ko thêm và báo đã có + hiện dialog mới
                                                        String t="  Chặn bài hát "+mangbaihat.get(getLayoutPosition()).getTenBaiHat();
                                                        btnBlockSong.setText(t);
                                                        Bochansong(mangbaihat.get(getLayoutPosition()));

                                                    }else{
                                                        //thêm vô thôi bro
                                                        String themmoi=PreferenceUtils.getBanListIdBaihat(context)+","+mangbaihat.get(getLayoutPosition()).getIdBaiHat();
                                                        PreferenceUtils.saveBanListIdBaihat(themmoi,context);
                                                        String t="  Bỏ chặn bài hát "+mangbaihat.get(getLayoutPosition()).getTenBaiHat();
                                                        btnBlockSong.setText(t);
                                                        Toast.makeText(context,"Đã chặn",Toast.LENGTH_SHORT).show();
                                                    }
                                                }else{
                                                    //thêm vô thôi bro
                                                    String themmoi=mangbaihat.get(getLayoutPosition()).getIdBaiHat();
                                                    PreferenceUtils.saveBanListIdBaihat(themmoi,context);
                                                    String t="  Bỏ chặn bài hát "+mangbaihat.get(getLayoutPosition()).getTenBaiHat();
                                                    btnBlockSong.setText(t);
                                                    Toast.makeText(context,"Đã chặn",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            KEY_BANLIST_BAIHAT.clear();
                                            //.dismiss();
                                        }
                                    });

                                    alertDialog.setView(dialogView);
                                    alertDialog.show();
                                }
                            });
                        }
                    },2000);

                    //nút cho người đã đăng nhập
                    if(PreferenceUtils.getUsername(context)!=null){
                        //nếu đã đăng nhập
                        //do đây không phải phần dsbh hay bai hat yeu thích nên nút remove khỏi playlist sẽ ko có
                        btnDeleteFromPlaylist.setVisibility(View.GONE);
                        btnDeleteFromPlaylist.setClickable(false);
                        line_1.setVisibility(View.GONE);

                        //init nút thích
                        Dataservice dataservice=APIService.getService();
                        Call<String> call=dataservice.PostKtYeuthichBaihat(mangbaihat.get(getLayoutPosition()).getIdBaiHat(), PreferenceUtils.getUsername(context));
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String ketqua=response.body();
                                if(ketqua.equals("exist")){
                                    //floatingActionLikeButton.setEnabled(false);
                                    btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconloved_20,0,0,0);
                                    btnLike.setText("  Bỏ thích");
                                }else{
                                    //floatingActionLikeButton.setEnabled(true);
                                    btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconlove_20,0,0,0);
                                    btnLike.setText("  Yêu thích");
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("kt_likeplaylist","call fail");
                            }
                        });

                        delay_hanler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //sự kiện ấn nút thích
                                btnLike.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(mangbaihat.get(getLayoutPosition()).getIdBaiHat().equals(""))
                                        {
                                            Log.d("LoiThich",mangbaihat.get(getLayoutPosition()).getIdBaiHat());
                                        }else {

                                            if(btnLike.getResources().getDrawable(R.drawable.iconlove_20).getConstantState()==context.getResources().getDrawable(R.drawable.iconlove_20).getConstantState()){
                                                Log.d("1", "1");
                                                btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconloved_20,0,0,0);
                                                btnLike.setText("  Bỏ thích");
                                                Dataservice dataservice= APIService.getService();
                                                Call<String> callback=dataservice.PostBoThichvaIdCuaBaiHat("1", mangbaihat.get(getLayoutPosition()).getIdBaiHat(),PreferenceUtils.getUsername(context));
                                                callback.enqueue(new Callback<String>() {
                                                    @Override
                                                    public void onResponse(Call<String> call, Response<String> response) {
                                                        String ketqua=response.body();
                                                        if(ketqua.equals("success")){
                                                            btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconloved_20,0,0,0);
                                                            btnLike.setText("  Yêu thích");
                                                            Toast.makeText(context,"Đã thích",Toast.LENGTH_SHORT).show();
                                                        }else if(ketqua.equals("unlikesuccess")){
                                                            btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconlove_20,0,0,0);
                                                            btnLike.setText("  Yêu thích");
                                                            Toast.makeText(context,"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                                        }else if(ketqua.equals("unlikefail")){
                                                            Log.d("Bỏ thích", "thất bại");
                                                        }else if(ketqua.equals("deletefail1")){
                                                            Log.d("Bỏ thích", "xóa trước khi bỏ thích thất bại");
                                                        }else if(ketqua.equals("deletefail2")){
                                                            Log.d("Thích", "thêm trước khi thích thất bại");
                                                        }else if(ketqua.equals("fail")){
                                                            Log.d("Thích", "Thích thất bại");
                                                        }else {
                                                            Log.d("Thông tin thích", "Rỗng");
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<String> call, Throwable t) {
                                                        Log.d("Thông tin thích", "call error");
                                                    }
                                                });
                                            }else{
                                                Log.d("2", "2");
                                                btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconlove_20,0,0,0);
                                                btnLike.setText("  Yêu thích");
                                                Dataservice dataservice= APIService.getService();
                                                Call<String> callback=dataservice.PostBoThichvaIdCuaBaiHat("1", mangbaihat.get(getLayoutPosition()).getIdBaiHat(),PreferenceUtils.getUsername(context));
                                                callback.enqueue(new Callback<String>() {
                                                    @Override
                                                    public void onResponse(Call<String> call, Response<String> response) {
                                                        String ketqua=response.body();
                                                        if(ketqua.equals("success")){
                                                            Toast.makeText(context,"Đã thích",Toast.LENGTH_SHORT).show();
                                                        }else if(ketqua.equals("unlikesuccess")){
                                                            Toast.makeText(context,"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                                        }else if(ketqua.equals("unlikefail")){
                                                            Log.d("Bỏ thích", "thất bại");
                                                        }else if(ketqua.equals("deletefail1")){
                                                            Log.d("Bỏ thích", "xóa trước khi bỏ thích thất bại");
                                                        }else if(ketqua.equals("deletefail2")){
                                                            Log.d("Thích", "thêm trước khi thích thất bại");
                                                        }else if(ketqua.equals("fail")){
                                                            Log.d("Thích", "Thích thất bại");
                                                        }else {
                                                            Log.d("Thông tin thích", "Rỗng");
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<String> call, Throwable t) {
                                                        Log.d("Thông tin thích", "call error");
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });

                                //sự kiện ấn nút thêm vào playlist
                                btnAddSongToPlaylist.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //lưu id bài hát
                                        PreferenceUtils.saveSong(mangbaihat.get(getLayoutPosition()).getIdBaiHat(),context);

                                        final AlertDialog alertDialog=new AlertDialog.Builder(context).create();
                                        LayoutInflater inflater = LayoutInflater.from(context);
                                        View dialogView = inflater.inflate(R.layout.alert_dialog_addsongtoplaylist, null);

                                        Button btnCancel = dialogView.findViewById(R.id.btnCancelAddSongtoplaylist);
                                        final RecyclerView rePlaylist= dialogView.findViewById(R.id.reAddsongtoplaylist);



                                        //getdata
                                        final Dataservice dataservice=APIService.getService();
                                        Call<List<Playlist>> callback=dataservice.GetDanhSachPlaylistCuaNguoiDung(PreferenceUtils.getUsername(context));
                                        callback.enqueue(new Callback<List<Playlist>>() {
                                            @Override
                                            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                                                mangplaylist = (ArrayList<Playlist>) response.body();
                                                adapter=new Alert_Dialog_AddsongtoplaylistAdapter(context,mangplaylist);
                                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                                rePlaylist.setLayoutManager(linearLayoutManager);
                                                rePlaylist.setAdapter(adapter);
                                                Log.d("UserPlaylistActivity","Lấy được:"+ mangplaylist.size());
                                            }

                                            @Override
                                            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                                                Log.d("Top5baihatdcyeuthich","callback alert playlist fail");
                                            }
                                        });

                                        btnCancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                alertDialog.dismiss();
                                                PreferenceUtils.saveSong(null,context);
                                            }
                                        });


                                        alertDialog.setView(dialogView);
                                        alertDialog.show();
                                    }
                                });
                            }
                        },2000);
                    }else {
                        //nếu chưa đăng nhập
                        btnLike.setVisibility(View.GONE);
                        btnAddSongToPlaylist.setVisibility(View.GONE);
                        btnDeleteFromPlaylist.setVisibility(View.GONE);
                        btnBlock.setVisibility(View.VISIBLE);
                        line_1.setVisibility(View.GONE);
                        line_2.setVisibility(View.VISIBLE);

                        btnLike.setClickable(false);
                        btnAddSongToPlaylist.setClickable(false);
                        btnDeleteFromPlaylist.setClickable(false);
                        btnBlock.setClickable(true);
                    }

                    alertDialog.setView(dialogView);
                    alertDialog.show();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mediaPlayer.isPlaying() || mediaPlayer!=null){
                        mediaPlayer.stop();
                        mediaPlayer.release();;
                        mediaPlayer=null;
                    }
                    imgplay.setImageResource(R.drawable.iconpause);
                    new PlayMp3().execute(mangbaihat.get(getLayoutPosition()).getLinkBaiHat());
                    //fmp_infobaihat.LoadImgForInfoFrg(mangbaihat.get(position).getHinhBaiHat());
                    //fmp_infobaihat.LoadTextViewForInfoFrg(mangbaihat.get(position).getTenBaiHat());
                    //fmp_infobaihat.LoadSongData(mangbaihat.get(position).getIdCaSi(),mangbaihat.get(position).getIdTheLoai(),mangbaihat.get(position).getTenAlbum(),mangbaihat.get(position).getIdBaiHat());
                    fmp_Dianhac.Playnhac(mangbaihat.get(getLayoutPosition()).getHinhBaiHat());
                    fmp_Danhsachbaihat.Getdata();
                    toolbar.setTitle(mangbaihat.get(getLayoutPosition()).getTenBaiHat());
                    Addtolichsu(mangbaihat.get(getLayoutPosition()).getIdBaiHat());
                    Addluotnghe(mangbaihat.get(getLayoutPosition()).getIdBaiHat());
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
    }

    private void startDownload(String url, String title) {
        //get device name
        String deviceName = android.os.Build.MODEL;

        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url.trim()));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(title);
        request.setDescription("Downloading...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        if(deviceName.contains("A50")){
            request.setDestinationInExternalPublicDir("/Samsung/Music",""+title+System.currentTimeMillis()+".mp3");
        }else{
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC,""+title+System.currentTimeMillis()+".mp3");
        }


        //get download service and enque
        DownloadManager manager=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        if (DownloadManager.STATUS_SUCCESSFUL == 8) {
            Toast.makeText(context,"Đã tải xong",Toast.LENGTH_LONG).show();
        }
    }

    private void Bochansong(Baihat baihat){
        if(PreferenceUtils.getUsername(context)!=null){
            Dataservice dataservice=APIService.getService();
            Call<String> call=dataservice.postBoChan_Baihat(PreferenceUtils.getUsername(context),baihat.getIdBaiHat());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String kq=response.body();
                    if(kq.equals("success")){
                        Toast.makeText(context,"Đã bỏ chặn",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context,"Có lỗi xảy ra",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(context,"Lỗi:"+t,Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            //chưa đăng nhập
            String newidlist="";
            //sử dụng shared reference để lưu ban list id bài hát
            String[] mangidbanlistbaihat = PreferenceUtils.getBanListIdBaihat(context).split(",");
            for (int i = 0; i < mangidbanlistbaihat.length; i++) {
                if(mangidbanlistbaihat[i].equals(baihat.getIdBaiHat())){
                    //ko thêm vô chuỗi mới
                }else{
                    if(i==mangidbanlistbaihat.length-1){
                        newidlist += mangidbanlistbaihat[i];
                    }else{
                        newidlist += mangidbanlistbaihat[i];
                        newidlist += ",";
                    }
                }
            }

            PreferenceUtils.saveBanListIdBaihat(newidlist,context);
            Toast.makeText(context,"Đã bỏ chặn",Toast.LENGTH_SHORT).show();
        }

    }

    private Baihat locbanlist(Baihat baihat,ArrayList<Casi> banlistcasi_fornoacc,ArrayList<Baihat> banlistbaihat_user,ArrayList<Casi> banlistcasi_user){

        //kiểm tra tình trạng đăng nhập
        if(PreferenceUtils.getUsername(context)!=null){
            //đã đăng nhập
            Log.d("chanbaihatcheck",PreferenceUtils.getUsername(context));
            Log.d("chanbaihatcheck",""+banlistbaihat_user.size());
            //b1 lọc banlistbaihat
            if(banlistbaihat_user.size()>0){
                //boolean contain=banlistbaihat_user.contains(baihat);
                for(Baihat i: banlistbaihat_user){
                    if(i.getIdBaiHat().equals(baihat.getIdBaiHat())){
                        Log.d("chanbaihatcheck","co trong ban list bai hat");
                        return null;
                    }
                }
            }

            //b2 lọc banlistcasi
            if(banlistcasi_user.size()>0){
                for(Casi casi: banlistcasi_user){
                    boolean contain=baihat.getIdCaSi().contains(casi.getTenCaSi());
                    if(contain){
                        Log.d("chanbaihatcheck","co trong ban list ca si");
                        return null;
                    }
                }
            }

        }else{
            //chưa đăng nhập

            //chuẩn bị
            //kiễm tra danh cho người chưa có tài khoản

            //nếu danh sách banlist bài hát rỗng bỏ qua bước lọc này
            if(PreferenceUtils.getBanListIdBaihat(context)!=null && !PreferenceUtils.getBanListIdBaihat(context).equals("") && PreferenceUtils.getBanListIdBaihat(context).matches(".*\\d.*")){
                //mảng các idbaihat nam trongbanlist
                String[] mangidbanlistbaihat = PreferenceUtils.getBanListIdBaihat(context).split(",");
                //chuẩn bị end

                //b1: lọc theo banlist id baihat
                boolean contain= Arrays.asList(mangidbanlistbaihat).contains(baihat.getIdBaiHat());
                if(contain){
                    Log.d("chanbaihatcheck","co trong ban list bai hat");
                    return null;
                }
            }

            //nếu danh sách banlist ca sĩ rỗng bỏ qua bước lọc này
            if(banlistcasi_fornoacc.size()>0){
                //b2: loc theo banlist id casi
                for(Casi casi: banlistcasi_fornoacc) {
                    boolean contain=baihat.getIdCaSi().contains(casi.getTenCaSi());
                    if(contain){
                        Log.d("chanbaihatcheck","co trong ban list ca si");
                        return null;
                    }
                }
            }
        }
        return baihat; //vượt qua hết thì thì trả về bài hát
    }

    public void Addtolichsu(String idbaihat){
        if(PreferenceUtils.getUsername(context)!=null && !PreferenceUtils.getUsername(context).equals("")){
            Dataservice dataservice= APIService.getService();
            Call<String> callback=dataservice.PostUpdateLichsuNguoidung(PreferenceUtils.getUsername(context),idbaihat);
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
            if(PreferenceUtils.getListenHistoryForNoAcc(context)!=null && !PreferenceUtils.getListenHistoryForNoAcc(context).equals("0")){
                //lấy mảng các id bai hát trong dslichsunghe
                String[] oldlist=PreferenceUtils.getListenHistoryForNoAcc(context).split(",");

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
                        PreferenceUtils.saveListenHistoryForNoAcc(idbaihat,context);
                    }else{
                        PreferenceUtils.saveListenHistoryForNoAcc(idbaihat+","+temp,context);
                    }

                    Log.d("musicplayer_addls_1",""+PreferenceUtils.getListenHistoryForNoAcc(context));
                }else {
                    //ko -> thêm vào từ bên trái

                    //b cuối lưu lại
                    PreferenceUtils.saveListenHistoryForNoAcc(idbaihat+","+PreferenceUtils.getListenHistoryForNoAcc(context),context);
                    Log.d("musicplayer_addls_2",""+PreferenceUtils.getListenHistoryForNoAcc(context));
                }
            }else{
                PreferenceUtils.saveListenHistoryForNoAcc(idbaihat,context);
                Log.d("musicplayer_addls_3",""+PreferenceUtils.getListenHistoryForNoAcc(context));
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

    //thực hiện phát nhạc lấy link nhạc
    @SuppressLint("StaticFieldLeak")
    class PlayMp3 extends AsyncTask<String ,Void,String> {

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
                            toolbar.setTitle(mangbaihat.get(position).getTenBaiHat());

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
                            toolbar.setTitle(mangbaihat.get(position).getTenBaiHat());
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
                        toolbar.setTitle(mangbaihat.get(position).getTenBaiHat());

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
}
