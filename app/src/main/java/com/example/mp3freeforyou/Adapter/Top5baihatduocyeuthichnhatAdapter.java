package com.example.mp3freeforyou.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.MainActivity;
import com.example.mp3freeforyou.Activity.QuanLyDSChanActivity;
import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static com.example.mp3freeforyou.Ultils.Constants.KEY_BANLIST_BAIHAT;
import static com.example.mp3freeforyou.Ultils.Constants.KEY_STRING_BANLIST_ARRAYBAIHAT;
import static com.example.mp3freeforyou.Ultils.FormatNumber.format;
import static com.example.mp3freeforyou.Ultils.PreferenceUtils.getUsername;
import static java.util.Objects.*;

public class Top5baihatduocyeuthichnhatAdapter extends ArrayAdapter<Baihat> {


    Context context=getContext();
    //anh xa các nút trên dialog
    TextView btnLike,btnAddSongToPlaylist,btnDownload,btnDeleteFromPlaylist,btnBlock,btnBlockSong;
    RecyclerView relistcasi;
    AlertDialogCasiAdapter casiadapter;
    ArrayList<Casi> mangcasi= new ArrayList<>();
    View line_1,line_2;


    //delay
    Handler delay_hanler=new Handler();

    //init for rePlaylist
    ArrayList<Playlist> mangplaylist = new ArrayList<Playlist>();
    Alert_Dialog_AddsongtoplaylistAdapter adapter;

    public Top5baihatduocyeuthichnhatAdapter(@NonNull Context context, int resource, @NonNull List<Baihat> objects) {
        super(context, resource, objects);
    }

    class ViewHolder{
        TextView txttenbaihat,txttencasi,txtRank,txtEarNum,txtLoveNum;
        ImageView imghinhbaihat,imglike,imgaddtoplaylist,btnDownload,imgExplore;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView=inflater.inflate(R.layout.row_top5baihatduocyeuthichnhat,null);
            viewHolder=new ViewHolder();
            viewHolder.txttenbaihat=convertView.findViewById(R.id.txtRowTop5TenBaiHat);
            viewHolder.txttencasi=convertView.findViewById(R.id.txtRowTop5Tencasi);
            viewHolder.imghinhbaihat=convertView.findViewById(R.id.imgRowTop5);
            viewHolder.txtRank=convertView.findViewById(R.id.rankbaihat);
            viewHolder.txtEarNum=convertView.findViewById(R.id.txtEarNum);
            viewHolder.txtLoveNum=convertView.findViewById(R.id.txtLoveNum);
            viewHolder.imgExplore=convertView.findViewById(R.id.imgExplore);

            //kiểm tra nếu chưa đã đăng nhập thì mới hiện nút addtoplaylist
            /*if(PreferenceUtils.getUsername(getContext())==null){

                //ẩn hai nút này
                viewHolder.imgaddtoplaylist.setVisibility(View.GONE);
                viewHolder.imglike.setVisibility(View.GONE);

                //chỉnh margin cho nút downlaod
                parameter =  (RelativeLayout.LayoutParams) viewHolder.btnDownload.getLayoutParams();
                parameter.setMargins(0, 0, 10, 0); // left, top, right, bottom
                viewHolder.btnDownload.setLayoutParams(parameter);

            }else{
                viewHolder.imgaddtoplaylist.setVisibility(View.VISIBLE);
                viewHolder.imglike.setVisibility(View.VISIBLE);
            }*/

            convertView.setTag(viewHolder);

        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        final Baihat baihat=getItem(position);
        Picasso.with(getContext()).load(baihat.getHinhBaiHat()).into(viewHolder.imghinhbaihat);
        viewHolder.txttenbaihat.setText(baihat.getTenBaiHat());
        viewHolder.txttencasi.setText(baihat.getIdCaSi());
        viewHolder.txtRank.setText(position + 1 + "");
        if(baihat.getLuotNghe().equals("")){
            viewHolder.txtEarNum.setText(format(Long.parseLong("0")));
        }else{
            viewHolder.txtEarNum.setText(format(Long.parseLong(baihat.getLuotNghe())));
        }
        if(baihat.getLuotThich().equals("")){
            viewHolder.txtLoveNum.setText(format(Long.parseLong("0")));
        }else{
            viewHolder.txtLoveNum.setText(format(Long.parseLong(baihat.getLuotThich())));
        }
        //UPDATE LUOT THICH
        final ViewHolder finalViewHolder = viewHolder;

        //sự kiện bấm nút 3 chấm imgexplorer
        viewHolder.imgExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog alertDialog=new AlertDialog.Builder(getContext()).create();
                LayoutInflater inflater = LayoutInflater.from(getContext());
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
                                startDownload(baihat.getLinkBaiHat(),baihat.getTenBaiHat());
                            }
                        });

                        btnBlock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog alertDialog=new AlertDialog.Builder(getContext()).create();
                                LayoutInflater inflater = LayoutInflater.from(getContext());
                                View dialogView = inflater.inflate(R.layout.alert_dialog_block, null);

                                btnBlockSong=dialogView.findViewById(R.id.txtBlockSong);
                                relistcasi=dialogView.findViewById(R.id.relistcasi_block);

                                Dataservice dataservice=APIService.getService();
                                Call<List<Casi>> call=dataservice.postgetlistcasifromlistnamecasi(baihat.getIdCaSi());
                                call.enqueue(new Callback<List<Casi>>() {
                                    @Override
                                    public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                                        mangcasi= (ArrayList<Casi>) response.body();
                                        casiadapter =new AlertDialogCasiAdapter(getContext(),mangcasi);
                                        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
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
                                if(PreferenceUtils.getUsername(getContext())!=null){
                                    //đối với người dùng đã đăng nhập
                                    Call<String> callchecksong=dataservice.PostGetBanStatusForSong(PreferenceUtils.getUsername(getContext()),baihat.getIdBaiHat());
                                    callchecksong.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String kq=response.body();
                                            if(kq.equals("yes")){
                                                //init text
                                                String t="  Bỏ chặn bài hát "+baihat.getTenBaiHat();
                                                btnBlockSong.setText(t);
                                                Log.d("init chan/bochan","yes");
                                            }else if(kq.equals("no")){
                                                //init text
                                                String t="  Chặn bài hát "+baihat.getTenBaiHat();
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
                                    if(PreferenceUtils.getBanListIdBaihat(getContext())!=null && !PreferenceUtils.getBanListIdBaihat(getContext()).equals("")){
                                        //trường hợp banlist ko rỗng
                                        String[] mangidbanlistbaihat = PreferenceUtils.getBanListIdBaihat(getContext()).split(",");
                                        boolean contain= Arrays.asList(mangidbanlistbaihat).contains(baihat.getIdBaiHat());
                                        if(contain){
                                            //có là bỏ chặn
                                            String t="  Bỏ chặn bài hát "+baihat.getTenBaiHat();
                                            btnBlockSong.setText(t);
                                        }else{
                                            //chưa có là chặn
                                            String t="  Chặn bài hát "+baihat.getTenBaiHat();
                                            btnBlockSong.setText(t);
                                        }
                                    }else{
                                        //trường hợp banlist bị rỗng
                                        //chưa có là chặn
                                        String t="  Chặn bài hát "+baihat.getTenBaiHat();
                                        btnBlockSong.setText(t);
                                        //Toast.makeText(getContext(),"Đã chặn",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                btnBlockSong.setOnClickListener(new View.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onClick(View v) {
                                        if(PreferenceUtils.getUsername(getContext())!=null){
                                            //đã đăng nhập
                                            //sử dụng shared reference để lưu ban list id bài hát

                                            //kiểm tra nếu dsbanlist null or ko
                                            if(!KEY_BANLIST_BAIHAT.contains(baihat)){
                                                KEY_BANLIST_BAIHAT.add(baihat);
                                            }
                                            PreferenceUtils.saveBanListIdBaihat(KEY_BANLIST_BAIHAT,context);

                                            Dataservice dataservice1=APIService.getService();
                                            Call<String> call1=dataservice1.postaddidbaihattobanlist(PreferenceUtils.getBanListIdBaihat(context),PreferenceUtils.getUsername(context));
                                            call1.enqueue(new Callback<String>() {
                                                @Override
                                                public void onResponse(Call<String> call, Response<String> response) {
                                                    String call1=response.body();
                                                    if(call1.equals("NOTCHANGE")){
                                                        String t="  Chặn bài hát "+baihat.getTenBaiHat();
                                                        btnBlockSong.setText(t);
                                                        Bochansong(baihat);
                                                    }else{
                                                        if(call1.equals("success")){
                                                            String t="  Bỏ chặn bài hát "+baihat.getTenBaiHat();
                                                            btnBlockSong.setText(t);
                                                            Toast.makeText(getContext(),"Đã chặn",Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            Toast.makeText(getContext(),"Có lỗi xảy ra",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<String> call, Throwable t) {
                                                    Toast.makeText(getContext(),""+t,Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            //Toast.makeText(context,"Thực thi thành công",Toast.LENGTH_SHORT).show();
                                        }else {
                                            //chưa đăng nhập
                                            //sử dụng shared reference để lưu ban list id bài hát

                                            //kiểm tra xem phần tử sắp thêm vào có trong chuỗi banlist hay chưa
                                            if(PreferenceUtils.getBanListIdBaihat(getContext())!=null && !PreferenceUtils.getBanListIdBaihat(getContext()).equals("")){
                                                String[] mangidbanlistbaihat = PreferenceUtils.getBanListIdBaihat(getContext()).split(",");
                                                boolean contain= Arrays.asList(mangidbanlistbaihat).contains(baihat.getIdBaiHat());
                                                if(contain){
                                                    //có thì ko thêm và báo đã có + hiện dialog mới
                                                    String t="  Chặn bài hát "+baihat.getTenBaiHat();
                                                    btnBlockSong.setText(t);
                                                    Bochansong(baihat);

                                                }else{
                                                    //thêm vô thôi bro
                                                    String themmoi=PreferenceUtils.getBanListIdBaihat(getContext())+","+baihat.getIdBaiHat();
                                                    PreferenceUtils.saveBanListIdBaihat(themmoi,getContext());
                                                    String t="  Bỏ chặn bài hát "+baihat.getTenBaiHat();
                                                    btnBlockSong.setText(t);
                                                    Toast.makeText(getContext(),"Đã chặn",Toast.LENGTH_SHORT).show();
                                                }
                                            }else{
                                                //thêm vô thôi bro
                                                String themmoi=baihat.getIdBaiHat();
                                                PreferenceUtils.saveBanListIdBaihat(themmoi,getContext());
                                                String t="  Bỏ chặn bài hát "+baihat.getTenBaiHat();
                                                btnBlockSong.setText(t);
                                                Toast.makeText(getContext(),"Đã chặn",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        KEY_BANLIST_BAIHAT.clear();
                                        //alertDialog.dismiss();
                                    }
                                });

                                alertDialog.setView(dialogView);
                                alertDialog.show();
                            }
                        });
                    }
                },2000);


                //nút cho người đã đăng nhập
                if(PreferenceUtils.getUsername(getContext())!=null){
                    //nếu đã đăng nhập
                    //do đây không phải phần dsbh hay bai hat yeu thích nên nút remove khỏi playlist sẽ ko có
                    btnDeleteFromPlaylist.setVisibility(View.GONE);
                    btnDeleteFromPlaylist.setClickable(false);
                    line_1.setVisibility(View.GONE);

                    //init nút thích
                    Dataservice dataservice=APIService.getService();
                    Call<String> call=dataservice.PostKtYeuthichBaihat(baihat.getIdBaiHat(), PreferenceUtils.getUsername(getContext()));
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
                                    if(baihat.getIdBaiHat().equals(""))
                                    {
                                        Log.d("LoiThich",baihat.getIdBaiHat());
                                    }else {

                                        if(btnLike.getResources().getDrawable(R.drawable.iconlove_20).getConstantState()==getContext().getResources().getDrawable(R.drawable.iconlove_20).getConstantState()){
                                            Log.d("1", "1");
                                            Dataservice dataservice= APIService.getService();
                                            Call<String> callback=dataservice.PostBoThichvaIdCuaBaiHat("1", baihat.getIdBaiHat(),PreferenceUtils.getUsername(getContext()));
                                            callback.enqueue(new Callback<String>() {
                                                @Override
                                                public void onResponse(Call<String> call, Response<String> response) {
                                                    String ketqua=response.body();
                                                    if(ketqua.equals("success")){
                                                        btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconloved_20,0,0,0);
                                                        btnLike.setText("  Bỏ thích");
                                                        Toast.makeText(getContext(),"Đã thích",Toast.LENGTH_SHORT).show();
                                                    }else if(ketqua.equals("unlikesuccess")){
                                                        btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconlove_20,0,0,0);
                                                        btnLike.setText("  Yêu thích");
                                                        Toast.makeText(getContext(),"Đã bỏ thích",Toast.LENGTH_SHORT).show();
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
                                            Dataservice dataservice= APIService.getService();
                                            Call<String> callback=dataservice.PostBoThichvaIdCuaBaiHat("1", baihat.getIdBaiHat(),PreferenceUtils.getUsername(getContext()));
                                            callback.enqueue(new Callback<String>() {
                                                @Override
                                                public void onResponse(Call<String> call, Response<String> response) {
                                                    String ketqua=response.body();
                                                    if(ketqua.equals("success")){
                                                        btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconloved_20,0,0,0);
                                                        btnLike.setText("  Bỏ thích");
                                                        Toast.makeText(getContext(),"Đã thích",Toast.LENGTH_SHORT).show();
                                                    }else if(ketqua.equals("unlikesuccess")){
                                                        btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconlove_20,0,0,0);
                                                        btnLike.setText("  Yêu thích");
                                                        Toast.makeText(getContext(),"Đã bỏ thích",Toast.LENGTH_SHORT).show();
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
                                    PreferenceUtils.saveSong(baihat.getIdBaiHat(),getContext());

                                    final AlertDialog alertDialog=new AlertDialog.Builder(getContext()).create();
                                    LayoutInflater inflater = LayoutInflater.from(getContext());
                                    View dialogView = inflater.inflate(R.layout.alert_dialog_addsongtoplaylist, null);

                                    Button btnCancel = dialogView.findViewById(R.id.btnCancelAddSongtoplaylist);
                                    final RecyclerView rePlaylist= dialogView.findViewById(R.id.reAddsongtoplaylist);



                                    //getdata
                                    final Dataservice dataservice=APIService.getService();
                                    Call<List<Playlist>> callback=dataservice.GetDanhSachPlaylistCuaNguoiDung(PreferenceUtils.getUsername(getContext()));
                                    callback.enqueue(new Callback<List<Playlist>>() {
                                        @Override
                                        public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                                            mangplaylist = (ArrayList<Playlist>) response.body();
                                            adapter=new Alert_Dialog_AddsongtoplaylistAdapter(getContext(),mangplaylist);
                                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
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
                                            PreferenceUtils.saveSong(null,getContext());
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

        //kiểm tra nếu đã đăng nhập thì mới hiện nút addtoplaylist và nút
        /*if(PreferenceUtils.getUsername(getContext())!=null){
            viewHolder.imgaddtoplaylist.setVisibility(View.VISIBLE);
            viewHolder.imglike.setVisibility(View.VISIBLE);

            //init nút thích
            Dataservice dataservice=APIService.getService();
            Call<String> call=dataservice.PostKtYeuthichBaihat(baihat.getIdBaiHat(), PreferenceUtils.getUsername(getContext()));
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String ketqua=response.body();
                    if(ketqua.equals("exist")){
                        //floatingActionLikeButton.setEnabled(false);
                        finalViewHolder.imglike.setImageResource(R.drawable.iconloved);
                    }else{
                        //floatingActionLikeButton.setEnabled(true);
                        finalViewHolder.imglike.setImageResource(R.drawable.iconlove);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("kt_likeplaylist","call fail");
                }
            });

            viewHolder.imglike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(baihat.getIdBaiHat().equals(""))
                    {
                        Log.d("LoiThich",baihat.getIdBaiHat());
                    }else {

                        if(finalViewHolder.imglike.getDrawable().getConstantState()==getContext().getResources().getDrawable(R.drawable.iconlove).getConstantState()){
                            Log.d("1", "1");
                            finalViewHolder.imglike.setImageResource(R.drawable.iconloved);
                            Dataservice dataservice= APIService.getService();
                            Call<String> callback=dataservice.PostBoThichvaIdCuaBaiHat("1", baihat.getIdBaiHat(),PreferenceUtils.getUsername(getContext()));
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String ketqua=response.body();
                                    if(ketqua.equals("success")){
                                        Toast.makeText(getContext(),"Đã thích",Toast.LENGTH_SHORT).show();
                                    }else if(ketqua.equals("unlikesuccess")){
                                        Toast.makeText(getContext(),"Đã bỏ thích",Toast.LENGTH_SHORT).show();
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
                            finalViewHolder.imglike.setImageResource(R.drawable.iconlove);
                            Dataservice dataservice= APIService.getService();
                            Call<String> callback=dataservice.PostBoThichvaIdCuaBaiHat("1", baihat.getIdBaiHat(),PreferenceUtils.getUsername(getContext()));
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String ketqua=response.body();
                                    if(ketqua.equals("success")){
                                        Toast.makeText(getContext(),"Đã thích",Toast.LENGTH_SHORT).show();
                                    }else if(ketqua.equals("unlikesuccess")){
                                        Toast.makeText(getContext(),"Đã bỏ thích",Toast.LENGTH_SHORT).show();
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




            //sự kiện addtoplaylist click
            viewHolder.imgaddtoplaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //lưu id bài hát
                    PreferenceUtils.saveSong(baihat.getIdBaiHat(),getContext());

                    final AlertDialog alertDialog=new AlertDialog.Builder(getContext()).create();
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    View dialogView = inflater.inflate(R.layout.alert_dialog_addsongtoplaylist, null);

                    Button btnCancel = dialogView.findViewById(R.id.btnCancelAddSongtoplaylist);
                    final RecyclerView rePlaylist= dialogView.findViewById(R.id.reAddsongtoplaylist);



                    //getdata
                    final Dataservice dataservice=APIService.getService();
                    Call<List<Playlist>> callback=dataservice.GetDanhSachPlaylistCuaNguoiDung(PreferenceUtils.getUsername(getContext()));
                    callback.enqueue(new Callback<List<Playlist>>() {
                        @Override
                        public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                            mangplaylist = (ArrayList<Playlist>) response.body();
                            adapter=new Alert_Dialog_AddsongtoplaylistAdapter(getContext(),mangplaylist);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
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
                            PreferenceUtils.saveSong(null,getContext());
                        }
                    });


                    alertDialog.setView(dialogView);
                    alertDialog.show();
                }
            });
        }else{
            viewHolder.imgaddtoplaylist.setVisibility(View.GONE);
            viewHolder.imglike.setVisibility(View.GONE);
            //chỉnh margin cho nút downlaod
            parameter =  (RelativeLayout.LayoutParams) viewHolder.btnDownload.getLayoutParams();
            parameter.setMargins(0, 0, 10, 0); // left, top, right, bottom
            viewHolder.btnDownload.setLayoutParams(parameter);
        }*/

        //SỰ KIỆN DOWN LOAD
        /*viewHolder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDownload(baihat.getLinkBaiHat(),baihat.getTenBaiHat());
            }
        });*/
        return convertView;
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
        DownloadManager manager=(DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        if (DownloadManager.STATUS_SUCCESSFUL == 8) {
            Toast.makeText(getContext(),"Đã tải xong",Toast.LENGTH_LONG).show();
        }
    }

    private void Bochansong(Baihat baihat){
        if(PreferenceUtils.getUsername(getContext())!=null){
            Dataservice dataservice=APIService.getService();
            Call<String> call=dataservice.postBoChan_Baihat(PreferenceUtils.getUsername(getContext()),baihat.getIdBaiHat());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String kq=response.body();
                    if(kq.equals("success")){
                        Toast.makeText(getContext(),"Đã bỏ chặn",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(),"Có lỗi xảy ra",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getContext(),"Lỗi:"+t,Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            //chưa đăng nhập
            String newidlist="";
            //sử dụng shared reference để lưu ban list id bài hát
            String[] mangidbanlistbaihat = PreferenceUtils.getBanListIdBaihat(getContext()).split(",");
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

            PreferenceUtils.saveBanListIdBaihat(newidlist,getContext());
            Toast.makeText(getContext(),"Đã bỏ chặn",Toast.LENGTH_SHORT).show();
        }
    }

    private void AlertDialogAlreadyblocked_Casi(Casi casi){
        final AlertDialog alertDialog=new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.alert_dialog_already_ban, null);

        TextView txtTitle,btnUnBlock,btnGoToBlockManage;

        txtTitle=dialogView.findViewById(R.id.txtTitleBan);
        btnUnBlock=dialogView.findViewById(R.id.txtBochan);
        btnGoToBlockManage=dialogView.findViewById(R.id.txtQLchan);

        //init
        String title="Ca sĩ "+casi.getTenCaSi()+" đã bị chặn từ trước";
        String bochan="  Bỏ chặn ca sĩ "+casi.getTenCaSi();
        txtTitle.setText(title);
        btnUnBlock.setText(bochan);

        //sự kiện onclick
        btnUnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PreferenceUtils.getUsername(getContext())!=null){
                    Dataservice dataservice=APIService.getService();
                    Call<String> call=dataservice.postBoChan_Casi(PreferenceUtils.getUsername(getContext()),casi.getIdCaSi());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String kq=response.body();
                            if(kq.equals("success")){
                                Toast.makeText(getContext(),"Đã bỏ chặn",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(),"Có lỗi xảy ra",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(getContext(),"Lỗi:"+t,Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    //chưa đăng nhập
                    String newidlist="";
                    //sử dụng shared reference để lưu ban list id bài hát
                    String[] mangidbanlistcasi = PreferenceUtils.getBanListIdCaSi(getContext()).split(",");
                    for (int i = 0; i < mangidbanlistcasi.length; i++) {
                        if(mangidbanlistcasi[i].equals(casi.getIdCaSi())){
                            //ko thêm vô chuỗi mới
                        }else{
                            if(i==mangidbanlistcasi.length-1){
                                newidlist += mangidbanlistcasi[i];
                            }else{
                                newidlist += mangidbanlistcasi[i];
                                newidlist += ",";
                            }
                        }
                    }

                    PreferenceUtils.saveBanListIdCaSi(newidlist,getContext());
                    Toast.makeText(getContext(),"Đã chặn",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnGoToBlockManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent= new Intent(getContext(), QuanLyDSChanActivity.class);
                getContext().startActivity(intent);
            }
        });
        //sự kiện onclick end


        alertDialog.setView(dialogView);
        alertDialog.show();
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
}
