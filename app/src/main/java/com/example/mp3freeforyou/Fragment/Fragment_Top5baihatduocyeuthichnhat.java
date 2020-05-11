package com.example.mp3freeforyou.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.BangXepHangActivity;
import com.example.mp3freeforyou.Activity.MusicPlayerActivity;
import com.example.mp3freeforyou.Activity.QuanLyDSChanActivity;
import com.example.mp3freeforyou.Adapter.Top5baihatduocyeuthichnhatAdapter;
import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class Fragment_Top5baihatduocyeuthichnhat extends Fragment {
    //khai báo các biến phục vụ cho việc chặn các bài hát thuộc ban list hoặc ban lít casi
    ArrayList<Casi> dscasi_biban;
    ArrayList<Casi> user_dscasi_biban;
    ArrayList<Baihat> user_dsbaihat_biban;

    private static final int PERMISSION_STORAGE_CODE = 1000;
    View view;
    ArrayList<Baihat> mangbaihat=new ArrayList<Baihat>();
    ListView listViewtop5;
    Top5baihatduocyeuthichnhatAdapter top5;
    TextView txtXemThem,txtTitle;
    ImageView imgLikeBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_top5baihatduocyeuthichnhat,container,false);
        listViewtop5=view.findViewById(R.id.lvTop5);
        //imgLikeBtn=view.findViewById(R.id.imgLikeBtnRowTop5);
        txtXemThem=view.findViewById(R.id.txtViewmoreGoiybaihat);
        txtTitle=view.findViewById(R.id.txtTitleTop5);
        txtTitle.setText("BẢNG XẾP HẠNG");
        txtXemThem.setVisibility(View.VISIBLE);

        //chuẩn bị data


        GetData();
        XemThem();
        return view;
    }

    private void XemThem() {
        txtXemThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),BangXepHangActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    private void GetData() {
        Dataservice dataservice= APIService.getService();
        Call<List<Baihat>> callback=dataservice.GetDataTop5BaihatDuocYeuThichNhat();
        callback.enqueue(new Callback<List<Baihat>>() {
            @Override
            public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                mangbaihat= (ArrayList<Baihat>) response.body();
                top5=new Top5baihatduocyeuthichnhatAdapter(getActivity(),android.R.layout.simple_list_item_1,mangbaihat);
                listViewtop5.setAdapter(top5);
                setListViewHeightBasedOnChildren(listViewtop5);
                listViewtop5.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //nếu đã đăng nhập
                        if(PreferenceUtils.getUsername(getContext())!=null){
                            //chuẩn bị
                            Dataservice dataservice=APIService.getService();

                            Call<List<Casi>> calluser_banlistcasi=dataservice.GetDanhSachCasiBiChan(PreferenceUtils.getUsername(getContext()));
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

                            Call<List<Baihat>> calluser_banlistbaihat=dataservice.GetDanhSachBaihatBiChan(PreferenceUtils.getUsername(getContext()));
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
                                    if(locbanlist(mangbaihat.get(position),user_dscasi_biban,user_dsbaihat_biban)!=null){
                                        Intent intent=new Intent(getContext(), MusicPlayerActivity.class);
                                        intent.putExtra("Cakhuc", mangbaihat.get(position));
                                        startActivity(intent);
                                    }else{
                                        //hien alert dialog
                                        AlertDialogAlreadyblocked_ONPLAYBaihat(mangbaihat.get(position));
                                    }
                                }
                            },1500);
                        }else{

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(locbanlist_fornoacc(mangbaihat.get(position))!=null){
                                        Intent intent=new Intent(getContext(), MusicPlayerActivity.class);
                                        intent.putExtra("Cakhuc", mangbaihat.get(position));
                                        startActivity(intent);
                                    }else{
                                        //hien alert dialog
                                        AlertDialogAlreadyblocked_ONPLAYBaihat(mangbaihat.get(position));
                                    }
                                }
                            },1500);
                        }

                    }
                });
                Log.d("Top5baihat",String.valueOf(mangbaihat.size()));
            }

            @Override
            public void onFailure(Call<List<Baihat>> call, Throwable t) {

            }
        });

    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if(listItem != null){
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    private void AlertDialogAlreadyblocked_ONPLAYBaihat(Baihat baihat){
        final AlertDialog alertDialog=new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = LayoutInflater.from(getContext());
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
        btnGoToBlockManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent= new Intent(getContext(), QuanLyDSChanActivity.class);
                startActivity(intent);
            }
        });
        //sự kiện onclick
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent=new Intent(getContext(), MusicPlayerActivity.class);
                intent.putExtra("Cakhuc",baihat);
                startActivity(intent);
            }
        });
        //sự kiện onclick end


        alertDialog.setView(dialogView);
        alertDialog.show();
    }
    private Baihat locbanlist(Baihat baihat,ArrayList<Casi> user_dscasi_biban,ArrayList<Baihat> user_dsbaihat_biban){
        //đã đăng nhập
        Log.d("chanbaihatcheck",PreferenceUtils.getUsername(getContext()));
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
        if(PreferenceUtils.getBanListIdBaihat(getContext())!=null && !PreferenceUtils.getBanListIdBaihat(getContext()).equals("") && PreferenceUtils.getBanListIdBaihat(getContext()).matches(".*\\d.*")){
            //mảng các idbaihat nam trongbanlist
            String[] mangidbanlistbaihat = PreferenceUtils.getBanListIdBaihat(getContext()).split(",");
            //chuẩn bị end

            //b1: lọc theo banlist id baihat
            boolean contain= Arrays.asList(mangidbanlistbaihat).contains(baihat.getIdBaiHat());
            if(contain){
                Log.d("chanbaihatcheck","co trong ban list bai hat");
                return null;
            }
        }

        //nếu danh sách banlist ca sĩ rỗng bỏ qua bước lọc này
        if(PreferenceUtils.getBanListIdCaSi(getContext())!=null && !PreferenceUtils.getBanListIdCaSi(getContext()).equals("") && PreferenceUtils.getBanListIdCaSi(getContext()).matches(".*\\d.*")){

            //b1 lấy list ca si
            Call<List<Casi>> callgetbanlistcasi=APIService.getService().GetDanhSachCasiBiChanForNoacc(PreferenceUtils.getBanListIdCaSi(getContext()));
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
