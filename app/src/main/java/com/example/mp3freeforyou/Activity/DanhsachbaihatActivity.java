package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mp3freeforyou.Adapter.DanhsachbaihatAdapter;
import com.example.mp3freeforyou.Adapter.DanhsachbaihatLichsuAdapter;
import com.example.mp3freeforyou.Adapter.UserLikedDanhsachbaihatAdapter;
import com.example.mp3freeforyou.Adapter.UserplaylistDanhsachbaihatAdapter;
import com.example.mp3freeforyou.Model.Album;
import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.Model.Hosonguoidung;
import com.example.mp3freeforyou.Model.Nguoidung;
import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.Model.Quangcao;
import com.example.mp3freeforyou.Model.Theloaibaihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhsachbaihatActivity extends AppCompatActivity {
    //khai báo các biến phục vụ cho việc chặn các bài hát thuộc ban list hoặc ban lít casi
    ArrayList<Casi> dscasi_biban=new ArrayList<>();
    ArrayList<Casi> user_dscasi_biban=new ArrayList<>();
    ArrayList<Baihat> user_dsbaihat_biban=new ArrayList<>();

    Quangcao quangcao;

    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    RecyclerView redsbaihat;
    FloatingActionButton floatingActionButton,floatingActionLikeButton;
    ImageView imgHinhBaihat;

    Playlist playlist;
    Theloaibaihat theloai;

    ArrayList<Baihat> mangbaihat;
    DanhsachbaihatAdapter danhsachbaihatAdapter;
    DanhsachbaihatLichsuAdapter danhsachbaihatLichSuAdapter;
    UserplaylistDanhsachbaihatAdapter userplaylistDanhsachbaihatAdapter;
    UserLikedDanhsachbaihatAdapter userLikedDanhsachbaihatAdapter;

    Album album;
    Casi casi;
    String username_lichsu="";
    String username_baihatyeuthich="";
    TextView reBiRong;

    CoordinatorLayout.LayoutParams parameter;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhsachbaihat);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //nhận data sau khi click
        DataIntent();
        //anh xa
        anhxa();
        init();

        //kiem tra neu co du lieu hay ko
        // ! là phủ định
        //nếu là id quảng cáo
        if(quangcao!=null && !quangcao.getTenBaiHat().equals("")){
            floatingActionLikeButton.setVisibility(View.GONE);
            parameter =  (CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams();
            parameter.setMargins(0, 0, 0, 0); // left, top, right, bottom
            floatingActionButton.setLayoutParams(parameter);

            setValueInView(quangcao.getTenBaiHat(),quangcao.getHinhBaiHat());//cập nhật các dữ liệu R.id trong view
            GetDataQuangcao(quangcao.getIdQuangCao());//lấy dữ liệu quảng cáo

        }
        //nếu là id playlist
        if(playlist!=null && !playlist.getTenPlaylist().equals("")){
            setValueInView(playlist.getTenPlaylist(),playlist.getHinhIcon());
            //kt nếu playlist có phải là playlistca nhan hay ko
            if(Integer.valueOf(playlist.getIdHoSoNguoiDung())==0){
                GetDataPlaylist(playlist.getIdPlaylist());
            }else {
                floatingActionLikeButton.setVisibility(View.GONE);
                parameter =  (CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams();
                parameter.setMargins(0, 0, 0, 0); // left, top, right, bottom
                floatingActionButton.setLayoutParams(parameter);

                GetDataPlaylistCanhan(playlist.getIdPlaylist());
            }

            //kiểm tra tình trạng đăng nhập
            if(PreferenceUtils.getUsername(getApplicationContext())!=null){
                //init nút like
                Dataservice dataservice=APIService.getService();
                Call<String> call=dataservice.PostKtYeuthichPlaylist(playlist.getIdPlaylist().toString(),PreferenceUtils.getUsername(getApplicationContext()).toString());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String ketqua=response.body();
                        if(ketqua.equals("exist")){
                            //floatingActionLikeButton.setEnabled(false);
                            floatingActionLikeButton.setImageResource(R.drawable.iconloved);
                        }else{
                            //floatingActionLikeButton.setEnabled(true);
                            floatingActionLikeButton.setImageResource(R.drawable.iconlove);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("kt_likeplaylist","call fail");
                    }
                });

                //nút thích playlist
                floatingActionLikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(floatingActionLikeButton.isEnabled()){
                            Log.d("likeplaylist_enable","true");

                            //nếu chưa thích
                            if(floatingActionLikeButton.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.iconlove).getConstantState()){
                                floatingActionLikeButton.setImageResource(R.drawable.iconloved);
                                Dataservice dataservice=APIService.getService();
                                Call<String> callback=dataservice.PostYeuthichPlaylist(playlist.getIdPlaylist().toString(), PreferenceUtils.getUsername(getApplicationContext()).toString());
                                callback.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String ketqua=response.body();
                                        if(ketqua.equals("success")){
                                            Toast.makeText(getApplicationContext(),"Đã thích",Toast.LENGTH_SHORT).show();
                                        }else if(ketqua.equals("delsuccess")){
                                            Toast.makeText(getApplicationContext(),"Đã bỏ thích ",Toast.LENGTH_SHORT).show();
                                            Log.d("LikePlaylist","delsuccess");
                                        }else if(ketqua.equals("delfail")){
                                            Log.d("LikePlaylist","delfail");
                                        }else if(ketqua.equals("fail2")){
                                            Log.d("LikePlaylist","thêm vào ds yêu thích thất bại");
                                        }else{
                                            Log.d("LikePlaylist","thông tin truyền vào bị rỗng");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.d("Likebtnplaylist","call fail");
                                    }
                                });
                            }else {
                                floatingActionLikeButton.setImageResource(R.drawable.iconlove);
                                Dataservice dataservice=APIService.getService();
                                Call<String> callback=dataservice.PostYeuthichPlaylist(playlist.getIdPlaylist().toString(), PreferenceUtils.getUsername(getApplicationContext()).toString());
                                callback.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String ketqua=response.body();
                                        if(ketqua.equals("success")){
                                            Toast.makeText(getApplicationContext(),"Đã thích",Toast.LENGTH_SHORT).show();
                                        }else if(ketqua.equals("delsuccess")){
                                            Toast.makeText(getApplicationContext(),"Đã bỏ thích ",Toast.LENGTH_SHORT).show();
                                            Log.d("LikePlaylist","delsuccess");
                                        }else if(ketqua.equals("delfail")){
                                            Log.d("LikePlaylist","delfail");
                                        }else if(ketqua.equals("fail2")){
                                            Log.d("LikePlaylist","thêm vào ds yêu thích thất bại");
                                        }else{
                                            Log.d("LikePlaylist","thông tin truyền vào bị rỗng");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.d("Likebtnplaylist","call fail");
                                    }
                                });
                            }

                            //floatingActionLikeButton.setEnabled(false);
                        }else{
                            Log.d("likeplaylist_enable","false");
                        }
                    }
                });
            }else {
                floatingActionLikeButton.setVisibility(View.GONE);
                parameter =  (CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams();
                parameter.setMargins(0, 0, 0, 0); // left, top, right, bottom
                floatingActionButton.setLayoutParams(parameter);
            }
        }

        //nếu là id THELOAIBAIHAT
        if(theloai!=null && !theloai.getTenTheLoai().equals("")){
            setValueInView(theloai.getTenTheLoai(),theloai.getHinhTheLoai());
            GetDataTheloaibaihat(theloai.getIdTheLoai());

            //kiểm tra tình trạng đăng nhập
            if(PreferenceUtils.getUsername(getApplicationContext())!=null){
                //init nút like
                Dataservice dataservice=APIService.getService();
                Call<String> call=dataservice.PostKtTheloaibaihatyeuthich(theloai.getIdTheLoai().toString(),PreferenceUtils.getUsername(getApplicationContext()).toString());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String ketqua=response.body();
                        if(ketqua.equals("exist")){
                            //floatingActionLikeButton.setEnabled(false);
                            floatingActionLikeButton.setImageResource(R.drawable.iconloved);
                        }else{
                            //floatingActionLikeButton.setEnabled(true);
                            floatingActionLikeButton.setImageResource(R.drawable.iconlove);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("kt_likeTHELOAI","call fail");
                    }
                });

                //nút thích playlist
                floatingActionLikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(floatingActionLikeButton.isEnabled()){
                            Log.d("likeplaylist_enable","true");

                            //nếu chưa thích
                            if(floatingActionLikeButton.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.iconlove).getConstantState()){
                                floatingActionLikeButton.setImageResource(R.drawable.iconloved);
                                Dataservice dataservice=APIService.getService();
                                Call<String> callback=dataservice.PostDelInsertTheloaibaihatyeuthich(theloai.getIdTheLoai().toString(), PreferenceUtils.getUsername(getApplicationContext()).toString());
                                callback.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String kq=response.body();
                                        if(kq.equals("deletesuccess")){
                                            Toast.makeText(getApplicationContext(),"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                        }else if (kq.equals("deletefail")){
                                            Log.d("unliketheloai2","unlike fail");
                                        }else if (kq.equals("addsuccess")){
                                            Toast.makeText(getApplicationContext(),"Đã thích",Toast.LENGTH_SHORT).show();
                                            Log.d("unlikecasi2","like success");
                                        }else if (kq.equals("addfail")){
                                            Log.d("unliketheloai2","like fail");
                                        }else {
                                            Log.d("unliketheloai2","emty sent");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.d("Likebtnplaylist","call fail");
                                    }
                                });
                            }else {
                                floatingActionLikeButton.setImageResource(R.drawable.iconlove);
                                Dataservice dataservice=APIService.getService();
                                Call<String> callback=dataservice.PostDelInsertTheloaibaihatyeuthich(theloai.getIdTheLoai().toString(), PreferenceUtils.getUsername(getApplicationContext()).toString());
                                callback.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String kq=response.body();
                                        if(kq.equals("deletesuccess")){
                                            Toast.makeText(getApplicationContext(),"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                        }else if (kq.equals("deletefail")){
                                            Log.d("unliketheloai2","unlike fail");
                                        }else if (kq.equals("addsuccess")){
                                            Toast.makeText(getApplicationContext(),"Đã thích",Toast.LENGTH_SHORT).show();
                                            Log.d("unlikecasi2","like success");
                                        }else if (kq.equals("addfail")){
                                            Log.d("unliketheloai2","like fail");
                                        }else {
                                            Log.d("unliketheloai2","emty sent");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.d("Likebtnplaylist","call fail");
                                    }
                                });
                            }

                            //floatingActionLikeButton.setEnabled(false);
                        }else{
                            Log.d("liketheloai_enable","false");
                        }
                    }
                });
            }else {
                floatingActionLikeButton.setVisibility(View.GONE);
                parameter =  (CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams();
                parameter.setMargins(0, 0, 0, 0); // left, top, right, bottom
                floatingActionButton.setLayoutParams(parameter);
            }
        }

        //nếu là id album
        if(album!=null && !album.getTenAlbum().equals("")){
            setValueInView(album.getTenAlbum(),album.getHinhAlbum());
            GetDataAlbum(album.getIdAlbum());

            if(PreferenceUtils.getUsername(getApplicationContext())!=null){
                //init nút like ALBUM
                Dataservice dataservice=APIService.getService();
                Call<String> call=dataservice.PostKtAlbumyeuthich(album.getIdAlbum().toString(),PreferenceUtils.getUsername(getApplicationContext()).toString());
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String ketqua=response.body();
                        if(ketqua.equals("exist")){
                            //floatingActionLikeButton.setEnabled(false);
                            floatingActionLikeButton.setImageResource(R.drawable.iconloved);
                        }else{
                            //floatingActionLikeButton.setEnabled(true);
                            floatingActionLikeButton.setImageResource(R.drawable.iconlove);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("kt_likealbum","call fail");
                    }
                });

                //nút thích album
                floatingActionLikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(floatingActionLikeButton.isEnabled()){
                            Log.d("likeplaylist_enable","true");

                            //nếu chưa thích
                            if(floatingActionLikeButton.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.iconlove).getConstantState()){
                                Log.d("1", "1");
                                floatingActionLikeButton.setImageResource(R.drawable.iconloved);
                                Dataservice dataservice= APIService.getService();
                                Call<String> call=dataservice.PostDelInsertAlbumyeuthich(album.getIdAlbum(), PreferenceUtils.getUsername(getApplicationContext()));
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String kq=response.body();
                                        if(kq.equals("deletesuccess")){
                                            Toast.makeText(getApplicationContext(),"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                        }else if (kq.equals("deletefail")){
                                            Log.d("unlikecasi1","unlike fail");
                                        }else if (kq.equals("insertsuccess")){
                                            Toast.makeText(getApplicationContext(),"Đã thích",Toast.LENGTH_SHORT).show();
                                            Log.d("unlikecasi1","like success");
                                        }else if (kq.equals("insertfail")){
                                            Log.d("unlikecasi1","like fail");
                                        }else {
                                            Log.d("unlikecasi1","emty sent");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.d("unlikecasi1","call fail");
                                    }
                                });
                            }else {
                                Log.d("2", "2");
                                floatingActionLikeButton.setImageResource(R.drawable.iconlove);
                                Dataservice dataservice= APIService.getService();
                                Call<String> call=dataservice.PostDelInsertAlbumyeuthich(album.getIdAlbum(), PreferenceUtils.getUsername(getApplicationContext()));
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String kq=response.body();
                                        if(kq.equals("deletesuccess")){
                                            Toast.makeText(getApplicationContext(),"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                        }else if (kq.equals("deletefail")){
                                            Log.d("unlikecasi2","unlike fail");
                                        }else if (kq.equals("insertsuccess")){
                                            Toast.makeText(getApplicationContext(),"Đã thích",Toast.LENGTH_SHORT).show();
                                            Log.d("unlikecasi2","like success");
                                        }else if (kq.equals("insertfail")){
                                            Log.d("unlikecasi2","like fail");
                                        }else {
                                            Log.d("unlikecasi2","emty sent");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.d("unlikecasi1","call fail");
                                    }
                                });
                            }

                            //floatingActionLikeButton.setEnabled(false);
                        }else{
                            Log.d("likealbum_enable","false");
                        }
                    }
                });
            }else {
                floatingActionLikeButton.setVisibility(View.GONE);
                parameter =  (CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams();
                parameter.setMargins(0, 0, 0, 0); // left, top, right, bottom
                floatingActionButton.setLayoutParams(parameter);
            }

        }

        //nếu là id CASI
        if(casi!=null && !casi.getTenCaSi().equals("")){
            setValueInView(casi.getTenCaSi(),casi.getHinhCaSi());
            GetDataCasi(casi.getIdCaSi());

            if(PreferenceUtils.getUsername(getApplicationContext())!=null){
                //init nút like casi
                Dataservice dataservice= APIService.getService();
                Call<String> callkt=dataservice.PostKtCasiyeuthich(casi.getIdCaSi(), PreferenceUtils.getUsername(getApplicationContext()));
                callkt.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String kq=response.body();
                        if(kq.equals("exist")){
                            //floatingActionLikeButton.setEnabled(false);
                            floatingActionLikeButton.setImageResource(R.drawable.iconloved);
                        }else{
                            //floatingActionLikeButton.setEnabled(true);
                            floatingActionLikeButton.setImageResource(R.drawable.iconlove);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

                //nút thích album
                floatingActionLikeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(floatingActionLikeButton.isEnabled()){
                            Log.d("likeplaylist_enable","true");
                            //nếu chưa thích
                            if(floatingActionLikeButton.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.iconlove).getConstantState()){
                                Log.d("1", "1");
                                floatingActionLikeButton.setImageResource(R.drawable.iconloved);
                                Dataservice dataservice= APIService.getService();
                                Call<String> call=dataservice.PostDelInsertCasiyeuthich(casi.getIdCaSi(), PreferenceUtils.getUsername(getApplicationContext()));
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String kq=response.body();
                                        if(kq.equals("deletesuccess")){
                                            Toast.makeText(getApplicationContext(),"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                        }else if (kq.equals("deletefail")){
                                            Log.d("unlikecasi1","unlike fail");
                                        }else if (kq.equals("addsuccess")){
                                            Toast.makeText(getApplicationContext(),"Đã thích",Toast.LENGTH_SHORT).show();
                                            Log.d("unlikecasi1","like success");
                                        }else if (kq.equals("addfail")){
                                            Log.d("unlikecasi1","like fail");
                                        }else {
                                            Log.d("unlikecasi1","emty sent");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.d("unlikecasi1","call fail");
                                    }
                                });
                            }else {
                                Log.d("2", "2");
                                floatingActionLikeButton.setImageResource(R.drawable.iconlove);
                                Dataservice dataservice= APIService.getService();
                                Call<String> call=dataservice.PostDelInsertCasiyeuthich(casi.getIdCaSi(), PreferenceUtils.getUsername(getApplicationContext()));
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String kq=response.body();
                                        if(kq.equals("deletesuccess")){
                                            Toast.makeText(getApplicationContext(),"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                        }else if (kq.equals("deletefail")){
                                            Log.d("unlikecasi1","unlike fail");
                                        }else if (kq.equals("addsuccess")){
                                            Toast.makeText(getApplicationContext(),"Đã thích",Toast.LENGTH_SHORT).show();
                                            Log.d("unlikecasi1","like success");
                                        }else if (kq.equals("addfail")){
                                            Log.d("unlikecasi1","like fail");
                                        }else {
                                            Log.d("unlikecasi1","emty sent");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.d("unlikecasi1","call fail");
                                    }
                                });
                            }

                            //floatingActionLikeButton.setEnabled(false);
                        }else{
                            Log.d("likealbum_enable","false");
                        }
                    }
                });
            }else {
                floatingActionLikeButton.setVisibility(View.GONE);
                parameter =  (CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams();
                parameter.setMargins(0, 0, 0, 0); // left, top, right, bottom
                floatingActionButton.setLayoutParams(parameter);
            }
        }

        //lich su
        if(!username_lichsu.equals("")){
            floatingActionLikeButton.setVisibility(View.GONE);
            parameter =  (CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams();
            parameter.setMargins(0, 0, 0, 0); // left, top, right, bottom
            floatingActionButton.setLayoutParams(parameter);

            setValueInView("LỊCH SỬ","https://mp3freeforyou.000webhostapp.com/Hinhanh/macdinh/MACDINH.png");

            GetDataLichsu(PreferenceUtils.getUsername(getApplicationContext()));
        }

        //baihatyeuthich
        if(!username_baihatyeuthich.equals("")){
            floatingActionLikeButton.setVisibility(View.GONE);
            parameter =  (CoordinatorLayout.LayoutParams) floatingActionButton.getLayoutParams();
            parameter.setMargins(0, 0, 0, 0); // left, top, right, bottom
            floatingActionButton.setLayoutParams(parameter);

            setValueInView("BÀI HÁT YÊU THÍCH","https://mp3freeforyou.000webhostapp.com/Hinhanh/macdinh/MACDINH.png");

            GetDataBaihatYeuthich(PreferenceUtils.getUsername(getApplicationContext()));
        }
    }

    private void GetDataBaihatYeuthich(String username) {
        Dataservice dataservice=APIService.getService();
        Call<List<Baihat>> callback=dataservice.GetDanhSachBaihatYeuthich(username);
        callback.enqueue(new Callback<List<Baihat>>() {
            @Override
            public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                mangbaihat= (ArrayList<Baihat>) response.body();
                Log.d("playlist_ds_baihat",String.valueOf(mangbaihat.size()));
                userLikedDanhsachbaihatAdapter=new UserLikedDanhsachbaihatAdapter(DanhsachbaihatActivity.this,mangbaihat);
                redsbaihat.setLayoutManager(new LinearLayoutManager(DanhsachbaihatActivity.this));
                redsbaihat.setAdapter(userLikedDanhsachbaihatAdapter);
                if(userLikedDanhsachbaihatAdapter.getItemCount()==0){
                    reBiRong.setVisibility(View.VISIBLE);
                }
                eventClick();
            }

            @Override
            public void onFailure(Call<List<Baihat>> call, Throwable t) {

            }
        });
    }

    private void GetDataPlaylistCanhan(String idPlaylist) {
        Dataservice dataservice=APIService.getService();
        Call<List<Baihat>> callback=dataservice.GetDanhSachBaihatTheoPlaylist(idPlaylist);
        callback.enqueue(new Callback<List<Baihat>>() {
            @Override
            public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                mangbaihat= (ArrayList<Baihat>) response.body();
                Log.d("playlist_ds_baihat",String.valueOf(mangbaihat.size()));
                userplaylistDanhsachbaihatAdapter=new UserplaylistDanhsachbaihatAdapter(DanhsachbaihatActivity.this,mangbaihat);
                redsbaihat.setLayoutManager(new LinearLayoutManager(DanhsachbaihatActivity.this));
                redsbaihat.setAdapter(userplaylistDanhsachbaihatAdapter);
                if(userplaylistDanhsachbaihatAdapter.getItemCount()==0){
                    reBiRong.setVisibility(View.VISIBLE);
                }
                eventClick();
            }

            @Override
            public void onFailure(Call<List<Baihat>> call, Throwable t) {

            }
        });
    }

    private void GetDataLichsu(String username) {

        if(PreferenceUtils.getUsername(getApplicationContext())!=null && !PreferenceUtils.getUsername(getApplicationContext()).equals("")){
            Dataservice dataservice=APIService.getService();
            Call<List<Baihat>> callback=dataservice.GetDanhSachBaihatTheoLichsu(username);
            callback.enqueue(new Callback<List<Baihat>>() {
                @Override
                public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                    mangbaihat= (ArrayList<Baihat>) response.body();
                    Log.d("playlist_ds_baihat",String.valueOf(mangbaihat.size()));
                    danhsachbaihatLichSuAdapter=new DanhsachbaihatLichsuAdapter(DanhsachbaihatActivity.this,mangbaihat);
                    redsbaihat.setLayoutManager(new LinearLayoutManager(DanhsachbaihatActivity.this));
                    redsbaihat.setAdapter(danhsachbaihatLichSuAdapter);

                    if(danhsachbaihatLichSuAdapter.getItemCount()==0){
                        reBiRong.setVisibility(View.VISIBLE);
                    }

                    eventClick();
                }

                @Override
                public void onFailure(Call<List<Baihat>> call, Throwable t) {

                }
            });
        }else{
            if(PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext())==null || PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext()).equals("")){
                reBiRong.setVisibility(View.VISIBLE);
            }else{
                Call<List<Baihat>> callback=APIService.getService().PostGetListBaiHatFromListenHistoryForNoAcc(PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext()));
                callback.enqueue(new Callback<List<Baihat>>() {
                    @Override
                    public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                        mangbaihat= (ArrayList<Baihat>) response.body();
                        Log.d("playlist_ds_baihat",String.valueOf(mangbaihat.size()));
                        danhsachbaihatLichSuAdapter=new DanhsachbaihatLichsuAdapter(DanhsachbaihatActivity.this,mangbaihat);
                        redsbaihat.setLayoutManager(new LinearLayoutManager(DanhsachbaihatActivity.this));
                        redsbaihat.setAdapter(danhsachbaihatLichSuAdapter);
                        eventClick();
                    }

                    @Override
                    public void onFailure(Call<List<Baihat>> call, Throwable t) {

                    }
                });
            }
        }

    }

    private void GetDataCasi(String idCaSi) {
        Dataservice dataservice=APIService.getService();
        Call<List<Baihat>> callback=dataservice.GetDanhSachBaihatTheoCasi(idCaSi);
        callback.enqueue(new Callback<List<Baihat>>() {
            @Override
            public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                mangbaihat= (ArrayList<Baihat>) response.body();
                Log.d("playlist_ds_baihat",String.valueOf(mangbaihat.size()));
                danhsachbaihatAdapter=new DanhsachbaihatAdapter(DanhsachbaihatActivity.this,mangbaihat);
                redsbaihat.setLayoutManager(new LinearLayoutManager(DanhsachbaihatActivity.this));
                redsbaihat.setAdapter(danhsachbaihatAdapter);
                if(danhsachbaihatAdapter.getItemCount()==0){
                    reBiRong.setVisibility(View.VISIBLE);
                }
                eventClick();
            }

            @Override
            public void onFailure(Call<List<Baihat>> call, Throwable t) {

            }
        });
    }

    private void GetDataAlbum(String idAlbum) {

        Dataservice dataservice=APIService.getService();
        Call<List<Baihat>> callback=dataservice.GetDanhSachBaihatTheoAlbum(idAlbum);
        callback.enqueue(new Callback<List<Baihat>>() {
            @Override
            public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                mangbaihat= (ArrayList<Baihat>) response.body();
                Log.d("playlist_ds_baihat",String.valueOf(mangbaihat.size()));
                danhsachbaihatAdapter=new DanhsachbaihatAdapter(DanhsachbaihatActivity.this,mangbaihat);
                redsbaihat.setLayoutManager(new LinearLayoutManager(DanhsachbaihatActivity.this));
                redsbaihat.setAdapter(danhsachbaihatAdapter);
                if(danhsachbaihatAdapter.getItemCount()==0){
                    reBiRong.setVisibility(View.VISIBLE);
                }
                eventClick();
            }

            @Override
            public void onFailure(Call<List<Baihat>> call, Throwable t) {

            }
        });
    }

    private void GetDataTheloaibaihat(String idTheLoai) {
        Dataservice dataservice=APIService.getService();
        Call<List<Baihat>> callback=dataservice.GetDanhSachBaihatTheoTheLoaiBaiHat(idTheLoai);
        callback.enqueue(new Callback<List<Baihat>>() {
            @Override
            public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                mangbaihat= (ArrayList<Baihat>) response.body();
                Log.d("playlist_ds_baihat",String.valueOf(mangbaihat.size()));
                danhsachbaihatAdapter=new DanhsachbaihatAdapter(DanhsachbaihatActivity.this,mangbaihat);
                redsbaihat.setLayoutManager(new LinearLayoutManager(DanhsachbaihatActivity.this));
                redsbaihat.setAdapter(danhsachbaihatAdapter);
                if(danhsachbaihatAdapter.getItemCount()==0){
                    reBiRong.setVisibility(View.VISIBLE);
                }
                eventClick();
            }

            @Override
            public void onFailure(Call<List<Baihat>> call, Throwable t) {

            }
        });
    }

    private void GetDataPlaylist(String idplaylist) {
        Dataservice dataservice=APIService.getService();
        Call<List<Baihat>> callback=dataservice.GetDanhSachBaihatTheoPlaylist(idplaylist);
        callback.enqueue(new Callback<List<Baihat>>() {
            @Override
            public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                mangbaihat= (ArrayList<Baihat>) response.body();
                Log.d("playlist_ds_baihat",String.valueOf(mangbaihat.size()));
                danhsachbaihatAdapter=new DanhsachbaihatAdapter(DanhsachbaihatActivity.this,mangbaihat);
                redsbaihat.setLayoutManager(new LinearLayoutManager(DanhsachbaihatActivity.this));
                redsbaihat.setAdapter(danhsachbaihatAdapter);
                if(danhsachbaihatAdapter.getItemCount()==0){
                    reBiRong.setVisibility(View.VISIBLE);
                }
                eventClick();
            }

            @Override
            public void onFailure(Call<List<Baihat>> call, Throwable t) {

            }
        });

    }

    private void GetDataQuangcao(String idquangcao) {
        //Log.d("idquangcao",idquangcao);
        //khởi tạo retrofit để đọc dữ liệu
        Dataservice dataservice= APIService.getService();
        Call<List<Baihat>> callback=dataservice.GetDanhSachBaihatTheoQuangcao(idquangcao);
        callback.enqueue(new Callback<List<Baihat>>() {
            @Override
            public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                mangbaihat= (ArrayList<Baihat>) response.body();
                danhsachbaihatAdapter=new DanhsachbaihatAdapter(DanhsachbaihatActivity.this,mangbaihat);
                redsbaihat.setLayoutManager(new LinearLayoutManager(DanhsachbaihatActivity.this));
                redsbaihat.setAdapter(danhsachbaihatAdapter);
                if(danhsachbaihatAdapter.getItemCount()==0){
                    reBiRong.setVisibility(View.VISIBLE);
                }
                eventClick();
                Log.d("dsbaihat",mangbaihat.get(0).getTenBaiHat());
            }

            @Override
            public void onFailure(Call<List<Baihat>> call, Throwable t) {

            }
        });
    }

    private void setValueInView(String ten, String hinh) {
        collapsingToolbarLayout.setTitle(ten);
        try{
            URL url=new URL(hinh); //du lieu cua hình là URL
            Bitmap bitmap= BitmapFactory.decodeStream(url.openConnection().getInputStream()); //chuyen doi hinh sang bitmap de hien thi
            BitmapDrawable bitmapDrawable=new BitmapDrawable(getResources(),bitmap);
            //gắn vào layout
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                collapsingToolbarLayout.setBackground(bitmapDrawable);
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //gán hình ảnh vào
        Picasso.with(this).load(hinh).into(imgHinhBaihat);
    }

    private void init() {
        //khai báo toolbar
        setSupportActionBar(toolbar);
        //tạo nút home khi click sẽ trở về trang trước cho thanh toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#ff39aa"));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.parseColor("#ff39aa"));

        //floating btn
        floatingActionButton.setEnabled(false);
        floatingActionLikeButton.setEnabled(false);
    }

    private void anhxa() {
        coordinatorLayout=findViewById(R.id.coordinatorDSBaihat);
        collapsingToolbarLayout=findViewById(R.id.collapTBLDSBaihat);
        toolbar=findViewById(R.id.ToolbarDSBaihat);
        redsbaihat=findViewById(R.id.recycleviewDSBaihat);
        floatingActionButton=findViewById(R.id.floatingactionbuttonDSBaihat);
        floatingActionLikeButton=findViewById(R.id.floatingactionbuttonLikeDSBaihat);
        imgHinhBaihat=findViewById(R.id.imgDSBaihat);
        reBiRong=findViewById(R.id.reBiRong);
    }

    private void DataIntent() {
        Intent intent=getIntent();
        if(intent!=null){
            if(intent.hasExtra("banner")){
                quangcao= (Quangcao) intent.getSerializableExtra("banner");
                //Toast.makeText(this,quangcao.getTenBaiHat(),Toast.LENGTH_SHORT).show();
            }
            if(intent.hasExtra("itemplaylist")){
                playlist= (Playlist) intent.getSerializableExtra("itemplaylist");

            }

            if(intent.hasExtra("itemtheloai")){
                theloai= (Theloaibaihat) intent.getSerializableExtra("itemtheloai");
            }

            if(intent.hasExtra("itemalbum")){
                album= (Album) intent.getSerializableExtra("itemalbum");
            }

            if(intent.hasExtra("itemcasi")){
                casi= (Casi) intent.getSerializableExtra("itemcasi");
            }

            if(intent.hasExtra("lichsu")){
                username_lichsu=intent.getStringExtra("lichsu");
                Log.d("DataIntent()",""+username_lichsu);

            }

            if(intent.hasExtra("baihatyeuthich")){
                username_baihatyeuthich=intent.getStringExtra("baihatyeuthich");
                Log.d("DataIntent()",""+username_baihatyeuthich);

            }

            /*if(intent.hasExtra("casiyeuthich")){
                username=intent.getStringExtra("casiyeuthich");
                Log.d("DataIntent()",""+username);

            }*/
        }
    }

    private void eventClick(){

        floatingActionLikeButton.setEnabled(true);

        floatingActionButton.setEnabled(true);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dataservice dataservice=APIService.getService();
                //lấy dscasi theo banlistidcai fornoacc
                if(PreferenceUtils.getBanListIdCaSi(getApplicationContext())==null || !PreferenceUtils.getBanListIdCaSi(getApplicationContext()).matches(".*\\d.*")){
                    PreferenceUtils.saveBanListIdCaSi("",getApplicationContext());
                    Log.d("locbanlist","already null");
                }else{
                    Log.d("locbanlist",""+PreferenceUtils.getBanListIdCaSi(getApplicationContext()));
                    Call<List<Casi>> callgetbanlistcasi=dataservice.GetDanhSachCasiBiChanForNoacc(PreferenceUtils.getBanListIdCaSi(getApplicationContext()));
                    callgetbanlistcasi.enqueue(new Callback<List<Casi>>() {
                        @Override
                        public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                            dscasi_biban= (ArrayList<Casi>) response.body();
                            Log.d("locbanlist","callgetbanlistcasi success");
                        }

                        @Override
                        public void onFailure(Call<List<Casi>> call, Throwable t) {
                            Log.d("locbanlist",""+t);
                        }
                    });
                }

                Call<List<Casi>> calluser_banlistcasi=dataservice.GetDanhSachCasiBiChan(PreferenceUtils.getUsername(getApplicationContext()));
                calluser_banlistcasi.enqueue(new Callback<List<Casi>>() {
                    @Override
                    public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                        user_dscasi_biban= (ArrayList<Casi>) response.body();
                        Log.d("locbanlist","calluser_banlistcasi success");
                    }

                    @Override
                    public void onFailure(Call<List<Casi>> call, Throwable t) {

                    }
                });

                Call<List<Baihat>> calluser_banlistbaihat=dataservice.GetDanhSachBaihatBiChan(PreferenceUtils.getUsername(getApplicationContext()));
                calluser_banlistbaihat.enqueue(new Callback<List<Baihat>>() {
                    @Override
                    public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                        user_dsbaihat_biban= (ArrayList<Baihat>) response.body();
                        Log.d("locbanlist","calluser_banlistbaihat success");
                    }

                    @Override
                    public void onFailure(Call<List<Baihat>> call, Throwable t) {

                    }
                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("lbl_user",""+PreferenceUtils.getUsername(getApplicationContext()));
                        Log.d("lbl_userbanlist_bh",""+user_dsbaihat_biban.size());
                        Log.d("lbl_userbanlist_cs",""+user_dscasi_biban.size());
                        ArrayList<Baihat> dsbhdaloc=locbanlist(mangbaihat,dscasi_biban,user_dsbaihat_biban,user_dscasi_biban);

                        if(dsbhdaloc!=null && dsbhdaloc.size()>0){
                            if(dsbhdaloc.size()<mangbaihat.size()){
                                Toast.makeText(getApplicationContext(), "Danh sách khi phát sẽ không phát một số bài do chúng bị chặn", Toast.LENGTH_SHORT).show();
                            }
                            Intent intent=new Intent(DanhsachbaihatActivity.this,MusicPlayerActivity.class);
                            intent.putExtra("DSCakhuc",dsbhdaloc);
                            startActivity(intent);
                        }else {
                            //hiện dialog
                            AlertDialogAlreadyblocked_ONPLAYDSBaihat(dsbhdaloc);
                        }
                    }
                },1500);
            }
        });

    }

    //khi đóng activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PreferenceUtils.savePlaylist(null,getApplicationContext());
        PreferenceUtils.saveNamePlaylist(null,getApplicationContext());
        PreferenceUtils.saveImgIconPlaylist(null,getApplicationContext());
        PreferenceUtils.saveImgPlaylist(null,getApplicationContext());
        PreferenceUtils.saveIdHoSoNguoiDungPlaylist(null,getApplicationContext());
    }


    //kiem tra xem day la request lấy lichsu hay baihatyeuthich
    public static String check(String sample){
        char[] chars = sample.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char c : chars){
            if(Character.isDigit(c)){
                sb.append(c);
            }
        }

        if(sb.toString().equals("0")){
            return "lichsu";
        }else if(sb.toString().equals("1")){
            return "baihatyeuthich";
        }else if(sb.toString().equals("2")){
            return "casi";
        }else {
            return null;
        }
    }

    public ArrayList<Baihat> locbanlist(ArrayList<Baihat> mangbaihat_chualoc, ArrayList<Casi> dscasi_biban_fornoacc,ArrayList<Baihat> user_dsbaihatbiban,ArrayList<Casi> user_dscasibiban){

        //lấy danh sách idbaihat bị ban
        if(PreferenceUtils.getUsername(getApplicationContext())!=null){
            //Đã đăng nhập

            //chuẩn bị
            //lấy chuỗi ds idbaihat nam trong ban list của người dùng

            //chuẩn bị end

            //b1: lọc theo banlist id baihat
            //nếu danh sách banlist rỗng bỏ qua bước lọc này
            if(user_dsbaihatbiban!=null && user_dsbaihatbiban.size() > 0){
                for(Baihat baihat_ban: user_dsbaihatbiban){
                    for(Baihat bai_chualoc:mangbaihat_chualoc){
                        if(baihat_ban.getIdBaiHat().equals(bai_chualoc.getIdBaiHat())){
                            mangbaihat_chualoc.remove(bai_chualoc);
                        }
                    }
                }
            }

            //b2: loc theo banlist id casi
            //nếu danh sách banlist ca sĩ rỗng bỏ qua bước lọc này
            if(user_dscasibiban!=null && user_dscasibiban.size()>0){
                for(Casi casi: user_dscasibiban) {
                    for (Baihat baihat : mangbaihat_chualoc) {
                        boolean contain = baihat.getIdCaSi().contains(casi.getTenCaSi());
                        if (contain) {
                            //nếu có thì xóa nó khỏi danh sách
                            mangbaihat_chualoc.remove(baihat);
                        }
                    }
                }
            }
            return mangbaihat_chualoc;
        }else{
            //chưa đăng nhập
            //nếu danh sách banlist bài hát rỗng bỏ qua bước lọc này
            if(PreferenceUtils.getBanListIdBaihat(getApplicationContext())!=null && !PreferenceUtils.getBanListIdBaihat(getApplicationContext()).equals("") && PreferenceUtils.getBanListIdBaihat(getApplicationContext()).matches(".*\\d.*")){
                //mảng các idbaihat nam trongbanlist
                String[] mangidbanlistbaihat = PreferenceUtils.getBanListIdBaihat(getApplicationContext()).split(",");
                //chuẩn bị end

                //b1: lọc theo banlist id baihat
                for(Baihat item: mangbaihat_chualoc){
                    boolean contain= Arrays.asList(mangidbanlistbaihat).contains(item.getIdBaiHat());
                    if(contain){
                        //nếu có thì xóa nó khỏi danh sách
                        mangbaihat_chualoc.remove(item);
                    }
                }
            }


            //nếu danh sách banlist ca sĩ rỗng bỏ qua bước lọc này
            if(dscasi_biban_fornoacc!=null && dscasi_biban_fornoacc.size()>0){
                //b2: loc theo banlist id casi
                for(Casi casi: dscasi_biban_fornoacc) {
                    for (Baihat baihat : mangbaihat_chualoc) {
                        boolean contain = baihat.getIdCaSi().contains(casi.getTenCaSi());
                        if (contain) {
                            //nếu có thì xóa nó khỏi danh sách
                            mangbaihat_chualoc.remove(baihat);
                        }
                    }
                }
            }
            return mangbaihat_chualoc;
        }
    }

    //aLERT DILAOG DANH DSBH MA TAT CA BAI HAT DEU BỊ CHAN
    private void AlertDialogAlreadyblocked_ONPLAYDSBaihat(ArrayList<Baihat> dsbhdaloc){
        final AlertDialog alertDialog=new AlertDialog.Builder(DanhsachbaihatActivity.this).create();
        LayoutInflater inflater = LayoutInflater.from(DanhsachbaihatActivity.this);
        View dialogView = inflater.inflate(R.layout.alert_dialog_already_ban, null);

        TextView txtTitle,btnPlay,btnGoToBlockManage;

        txtTitle=dialogView.findViewById(R.id.txtTitleBan);
        btnPlay=dialogView.findViewById(R.id.txtBochan);
        btnGoToBlockManage=dialogView.findViewById(R.id.txtQLchan);

        //init
        String title="Không thể phát danh sách được do tất cả bài hát thuộc danh sách này đã bị chặn (do ca sĩ trình bày chúng hoặc chúng bị chặn), để bỏ chặn chúng thì hãy đến trang quản lý danh sách chặn ";
        txtTitle.setText(title);
        String play="  Phát danh sách bài hát";
        btnPlay.setText(play);

        //sự kiện onclick
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent=new Intent(DanhsachbaihatActivity.this, MusicPlayerActivity.class);
                intent.putExtra("DSCakhuc",dsbhdaloc);
                startActivity(intent);
            }
        });
        //sự kiện onclick
        btnGoToBlockManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent= new Intent(DanhsachbaihatActivity.this, QuanLyDSChanActivity.class);
                startActivity(intent);
            }
        });

        //sự kiện onclick end
        alertDialog.setView(dialogView);
        alertDialog.show();
        Log.d("ShowDialog","yes");
    }
}
