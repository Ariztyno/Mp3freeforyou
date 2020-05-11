package com.example.mp3freeforyou.Adapter;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.MusicPlayerActivity;
import com.example.mp3freeforyou.Activity.QuanLyDSChanActivity;
import com.example.mp3freeforyou.Model.Album;
import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.Model.Chudebaihat;
import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.Model.SearchAll;
import com.example.mp3freeforyou.Model.Theloaibaihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.mangbaihat;
import static com.example.mp3freeforyou.Ultils.Constants.KEY_BANLIST_BAIHAT;
import static com.example.mp3freeforyou.Ultils.PreferenceUtils.getUsername;

public class TimKiemAdapter extends RecyclerView.Adapter<TimKiemAdapter.ViewHolder> {
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

    public TimKiemAdapter(Context context, ArrayList<Baihat> mangbaihat) {
        this.context = context;
        this.mangbaihat = mangbaihat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_fragment_timkiem_baihat,parent,false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Baihat baihat=mangbaihat.get(position);
        holder.txtTenBaihat.setText(baihat.getTenBaiHat());
        holder.txtTenCasiBaihat.setText(baihat.getIdCaSi());
        Picasso.with(context).load(baihat.getHinhBaiHat()).into(holder.imgBaihat);
    }

    @Override
    public int getItemCount() {
        return mangbaihat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        // List items of baihat list
        private TextView txtTenBaihat;
        private TextView txtTenCasiBaihat;
        private ImageView imgBaihat,Baihatlikebtn,Baihataddsongtoplaylist,btndownload,imgExplore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Get the view of the elements of baihat list
            txtTenBaihat=itemView.findViewById(R.id.txtRowFragmenTimkiemTenBaiHat);
            txtTenCasiBaihat=itemView.findViewById(R.id.txtRowFragmenTimkiemTenCaSi);
            imgBaihat=itemView.findViewById(R.id.imgRowFragmentTimKiem);
            //Baihatlikebtn=itemView.findViewById(R.id.imgRowFragmentTimKiemLikebtn);
            //Baihataddsongtoplaylist=itemView.findViewById(R.id.imgRowFragmentTimKiemAddsongtoplaylistbtn);
            //btndownload=itemView.findViewById(R.id.imgRowFragmentTimKiemDownloadbtn);

            imgExplore=itemView.findViewById(R.id.imgExplore);

            //sự kiện bấm nút imgExplore
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
                                             if(PreferenceUtils.getUsername(context)!=null){
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
                                                 //Toast.makeText(context,"Thực thi thành công",Toast.LENGTH_SHORT).show();
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
                                                Dataservice dataservice= APIService.getService();
                                                Call<String> callback=dataservice.PostBoThichvaIdCuaBaiHat("1", mangbaihat.get(getLayoutPosition()).getIdBaiHat(),PreferenceUtils.getUsername(context));
                                                callback.enqueue(new Callback<String>() {
                                                    @Override
                                                    public void onResponse(Call<String> call, Response<String> response) {
                                                        String ketqua=response.body();
                                                        if(ketqua.equals("success")){
                                                            btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconloved_20,0,0,0);
                                                            btnLike.setText("  Bỏ thích");
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
                                                Dataservice dataservice= APIService.getService();
                                                Call<String> callback=dataservice.PostBoThichvaIdCuaBaiHat("1", mangbaihat.get(getLayoutPosition()).getIdBaiHat(),PreferenceUtils.getUsername(context));
                                                callback.enqueue(new Callback<String>() {
                                                    @Override
                                                    public void onResponse(Call<String> call, Response<String> response) {
                                                        String ketqua=response.body();
                                                        if(ketqua.equals("success")){
                                                            btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconloved_20,0,0,0);
                                                            btnLike.setText("  Bỏ thích");
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

            //ấn vào bài hát tìm được
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //nếu đã đăng nhập
                    if(PreferenceUtils.getUsername(context)!=null){
                        //chuẩn bị
                        Dataservice dataservice=APIService.getService();

                        Call<List<Casi>> calluser_banlistcasi=dataservice.GetDanhSachCasiBiChan(PreferenceUtils.getUsername(context));
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

                        Call<List<Baihat>> calluser_banlistbaihat=dataservice.GetDanhSachBaihatBiChan(PreferenceUtils.getUsername(context));
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
                                if(locbanlist(mangbaihat.get(getLayoutPosition()),user_dscasi_biban,user_dsbaihat_biban)!=null){
                                    Intent intent=new Intent(context, MusicPlayerActivity.class);
                                    intent.putExtra("Cakhuc", mangbaihat.get(getLayoutPosition()));
                                    context.startActivity(intent);
                                }else{
                                    //hien alert dialog
                                    AlertDialogAlreadyblocked_ONPLAYBaihat(mangbaihat.get(getLayoutPosition()));
                                }
                            }
                        },1500);
                    }else{

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(locbanlist_fornoacc(mangbaihat.get(getLayoutPosition()))!=null){
                                    Intent intent=new Intent(context, MusicPlayerActivity.class);
                                    intent.putExtra("Cakhuc", mangbaihat.get(getLayoutPosition()));
                                    context.startActivity(intent);
                                }else{
                                    //hien alert dialog
                                    AlertDialogAlreadyblocked_ONPLAYBaihat(mangbaihat.get(getLayoutPosition()));
                                }
                            }
                        },1500);
                    }
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
    private void AlertDialogAlreadyblocked_ONPLAYBaihat(Baihat baihat){
        final AlertDialog alertDialog=new AlertDialog.Builder(context).create();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.alert_dialog_already_ban, null);

        TextView txtTitle,btnPlay,btnGoToBlockManage;

        txtTitle=dialogView.findViewById(R.id.txtTitleBan);
        btnPlay=dialogView.findViewById(R.id.txtBochan);
        btnGoToBlockManage=dialogView.findViewById(R.id.txtQLchan);

        //init
        String title="Bài hát "+baihat.getTenBaiHat()+" không phát được do ca sĩ của bài hát hoặc bài hát này đã bị chặn";
        String play="  Phát bài hát";
        txtTitle.setText(title);
        btnPlay.setText(play);

        //sự kiện onclick
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent=new Intent(context, MusicPlayerActivity.class);
                intent.putExtra("Cakhuc",baihat);
                context.startActivity(intent);
            }
        });
        btnGoToBlockManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent= new Intent(context, QuanLyDSChanActivity.class);
                context.startActivity(intent);
            }
        });
        //sự kiện onclick end


        alertDialog.setView(dialogView);
        alertDialog.show();
    }
    private Baihat locbanlist(Baihat baihat,ArrayList<Casi> user_dscasi_biban,ArrayList<Baihat> user_dsbaihat_biban){
        //đã đăng nhập
        Log.d("chanbaihatcheck",PreferenceUtils.getUsername(context));
        //b1 lọc banlistbaihat
        if(user_dsbaihat_biban!=null){
            //boolean contain=banlistbaihat_user.contains(baihat);
            for(Baihat i: user_dsbaihat_biban){
                if(i.getIdBaiHat().equals(baihat.getIdBaiHat())){
                    Log.d("chanbaihatcheck","co trong ban list bai hat");
                    return null;
                }
            }
        }

        //b2 lọc banlistcasi
        if(user_dscasi_biban!=null){
            for(Casi casi: user_dscasi_biban){
                boolean contain=baihat.getIdCaSi().contains(casi.getTenCaSi());
                if(contain){
                    Log.d("chanbaihatcheck","co trong ban list ca si");
                    return null;
                }
            }
        }
        return baihat; //vượt qua hết thì thì trả về bài hát
    }
    private Baihat locbanlist_fornoacc(Baihat baihat){
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
        if(PreferenceUtils.getBanListIdCaSi(context)!=null && !PreferenceUtils.getBanListIdCaSi(context).equals("") && PreferenceUtils.getBanListIdCaSi(context).matches(".*\\d.*")){

            //b1 lấy list ca si
            Call<List<Casi>> callgetbanlistcasi=APIService.getService().GetDanhSachCasiBiChanForNoacc(PreferenceUtils.getBanListIdCaSi(context));
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


            //b2: loc theo banlist id casi
            for(Casi casi: dscasi_biban) {
                boolean contain=baihat.getIdCaSi().contains(casi.getTenCaSi());
                if(contain){
                    Log.d("chanbaihatcheck","co trong ban list ca si");
                    return null;
                }
            }
        }
        return baihat; //vượt qua hết thì thì trả về bài hát
    }
}
