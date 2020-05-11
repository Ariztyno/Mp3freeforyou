package com.example.mp3freeforyou.Adapter;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.MusicPlayerActivity;
import com.example.mp3freeforyou.Activity.QuanLyDSChanActivity;
import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.Model.Playlist;
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

import static com.example.mp3freeforyou.Ultils.Constants.KEY_BANLIST_BAIHAT;
import static com.example.mp3freeforyou.Ultils.PreferenceUtils.getUsername;

public class DSBaihatChanAdapter extends RecyclerView.Adapter<DSBaihatChanAdapter.ViewHolder> {
    //khai báo các biến phục vụ cho việc chặn các bài hát thuộc ban list hoặc ban lít casi
    ArrayList<Casi> dscasi_biban;
    ArrayList<Casi> user_dscasi_biban;
    ArrayList<Baihat> user_dsbaihat_biban;

    //anh xa các nút trên dialog
    TextView btnLike,btnAddSongToPlaylist,btnDownload,btnDeleteFromPlaylist,btnUnBlock;
    RecyclerView relistcasi;
    AlertDialogCasiAdapter casiadapter;
    ArrayList<Casi> mangcasi= new ArrayList<>();
    View line_1,line_2;

    Handler delay_hanler=new Handler();

    ArrayList<Playlist> mangplaylist;
    Alert_Dialog_AddsongtoplaylistAdapter adapter;

    Context context;
    ArrayList<Baihat> mangbaihat;
    public DSBaihatChanAdapter(Context context, ArrayList<Baihat> mangbaihat) {
        this.context = context;
        this.mangbaihat = mangbaihat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_dschan_baihat,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Baihat baihat=mangbaihat.get(position);
        holder.txttenbaihat.setText(baihat.getTenBaiHat());
        holder.txtcasi.setText(baihat.getIdCaSi());
        Picasso.with(context).load(baihat.getHinhBaiHat()).into(holder.imgBaihat);
    }

    @Override
    public int getItemCount() {
        return mangbaihat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txttenbaihat, txtcasi;
        ImageView imgBaihat, imgExplore;

        public ViewHolder(final View itemview) {
            super(itemview);
            txttenbaihat=itemview.findViewById(R.id.txtRowDSChan_TenBaiHat);
            txtcasi=itemview.findViewById(R.id.txtRowDSChan_TenCaSi);
            imgBaihat=itemview.findViewById(R.id.imgDSChan_Baihat);
            imgExplore=itemview.findViewById(R.id.imgExplore);

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
                    btnUnBlock=dialogView.findViewById(R.id.txtChan);

                    //init test cho nút Bỏ chặn
                    String t="  Bỏ chặn bài hát "+mangbaihat.get(getLayoutPosition()).getTenBaiHat();
                    btnUnBlock.setText(t);

                    line_1=dialogView.findViewById(R.id.line_div1);
                    line_2=dialogView.findViewById(R.id.line_div2);

                    //disable click các nút trong 1,5s
                    btnLike.setClickable(false);
                    btnAddSongToPlaylist.setClickable(false);
                    btnDownload.setClickable(false);
                    btnDeleteFromPlaylist.setClickable(false);
                    btnUnBlock.setClickable(false);

                    delay_hanler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnLike.setClickable(true);
                            btnAddSongToPlaylist.setClickable(true);
                            btnDownload.setClickable(true);
                            btnDeleteFromPlaylist.setClickable(true);
                            btnUnBlock.setClickable(true);
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

                            //bỏ block phiên bản chưa đăng nhập
                            btnUnBlock.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //kiem tra tinh trang dang nhap
                                    //da dang nhap
                                    if(PreferenceUtils.getUsername(context)!=null){
                                        Dataservice dataservice=APIService.getService();
                                        Call<String> callunblock=dataservice.postBoChan_Baihat(PreferenceUtils.getUsername(context),mangbaihat.get(getLayoutPosition()).getIdBaiHat());
                                        callunblock.enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, Response<String> response) {
                                                String kq=response.body();
                                                if(kq.equals("success")){
                                                    Toast.makeText(context,"Đã bỏ chặn",Toast.LENGTH_SHORT).show();

                                                    alertDialog.dismiss();

                                                    //load lại trang khi bỏ thích
                                                    ((QuanLyDSChanActivity)context).finish();
                                                    Intent intent=new Intent(context, QuanLyDSChanActivity.class);
                                                    String text=PreferenceUtils.getUsername(context)+"1";
                                                    //intent.putExtra("baihatyeuthich",text);
                                                    intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                    context.startActivity(intent);
                                                }else{
                                                    if(kq.equals("fail")){
                                                        Toast.makeText(context,"fail",Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        Toast.makeText(context,"Somethingwrong",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<String> call, Throwable t) {
                                                Toast.makeText(context,""+t,Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else {
                                        //chưa đăng nhập
                                        String newidlist="";
                                        //sử dụng shared reference để lưu ban list id bài hát
                                        String[] mangidbanlistbaihat = PreferenceUtils.getBanListIdBaihat(context).split(",");
                                        for (int i = 0; i < mangidbanlistbaihat.length; i++) {
                                            if(mangidbanlistbaihat[i].equals(mangbaihat.get(getLayoutPosition()).getIdBaiHat())){
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

                                        alertDialog.dismiss();

                                        //load lại trang khi bỏ thích
                                        ((QuanLyDSChanActivity)context).finish();
                                        Intent intent=new Intent(context, QuanLyDSChanActivity.class);
                                        String text=PreferenceUtils.getUsername(context)+"1";
                                        //intent.putExtra("baihatyeuthich",text);
                                        intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        context.startActivity(intent);
                                    }
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
                        btnUnBlock.setVisibility(View.VISIBLE);
                        line_1.setVisibility(View.GONE);
                        line_2.setVisibility(View.VISIBLE);

                        btnLike.setClickable(false);
                        btnAddSongToPlaylist.setClickable(false);
                        btnDeleteFromPlaylist.setClickable(false);
                        btnUnBlock.setClickable(true);
                    }

                    alertDialog.setView(dialogView);
                    alertDialog.show();
                }
            });

            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MusicPlayerActivity.class);
                    intent.putExtra("Cakhuc", mangbaihat.get(getLayoutPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }

    private void startDownload(String url, String title) {
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url.trim()));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(title);
        request.setDescription("Downloading...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir("/Samsung/Music",""+title+System.currentTimeMillis()+".mp3");

        //get download service and enque
        DownloadManager manager=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        if (DownloadManager.STATUS_SUCCESSFUL == 8) {
            Toast.makeText(context,"Đã tải xong",Toast.LENGTH_LONG).show();
        }
    }

    private void AlertDialogAlreadyblocked_Baihat(Baihat baihat){
        final AlertDialog alertDialog=new AlertDialog.Builder(context).create();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.alert_dialog_already_ban, null);

        TextView txtTitle,btnUnBlock,btnGoToBlockManage;

        txtTitle=dialogView.findViewById(R.id.txtTitleBan);
        btnUnBlock=dialogView.findViewById(R.id.txtBochan);
        btnGoToBlockManage=dialogView.findViewById(R.id.txtQLchan);

        //init
        String title="Bài hát "+baihat.getTenBaiHat()+" đã bị chặn từ trước";
        String bochan="  Bỏ chặn bài hát "+baihat.getTenBaiHat();
        txtTitle.setText(title);
        btnUnBlock.setText(bochan);

        //sự kiện onclick
        btnUnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PreferenceUtils.getUsername(context)!=null){
                    Dataservice dataservice=APIService.getService();
                    Call<String> call=dataservice.postBoChan_Baihat(PreferenceUtils.getUsername(context),baihat.getIdBaiHat());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String kq=response.body();
                            if(kq.equals("success")){
                                Toast.makeText(context,"Đã bỏ chặn",Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
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
                    alertDialog.dismiss();
                }

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
