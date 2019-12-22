package com.example.mp3freeforyou.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhsachbaihatAdapter extends RecyclerView.Adapter<DanhsachbaihatAdapter.ViewHolder>{
    ArrayList<Playlist> mangplaylist;
    Alert_Dialog_AddsongtoplaylistAdapter adapter;

    Context context;
    ArrayList<Baihat> mangbaihat;
    public DanhsachbaihatAdapter(Context context, ArrayList<Baihat> mangbaihat) {
        this.context = context;
        this.mangbaihat = mangbaihat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_danhsachbaihat,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Baihat baihat=mangbaihat.get(position);
        holder.txttenbaihat.setText(baihat.getTenBaiHat());
        holder.txtcasi.setText(baihat.getIdCaSi());
        holder.txtindex.setText(position + 1 + "");

    }

    @Override
    public int getItemCount() {
        return mangbaihat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtindex,txttenbaihat,txtcasi;
        ImageView imgluotthich,imgAddsongtoplaylist;
        public  ViewHolder(final View itemview){
            super(itemview);
            txtindex=itemview.findViewById(R.id.txtDSBaihatIndex);
            txttenbaihat=itemview.findViewById(R.id.txtRowDSBaihatTenBaiHat);
            txtcasi=itemview.findViewById(R.id.txtRowDSBaihatTenCaSi);
            imgluotthich=itemview.findViewById(R.id.imgLikeBtnRowDSBaihat);
            imgAddsongtoplaylist=itemview.findViewById(R.id.imgAddsongtoplaylistBtnRowDSBaihat);


            if(PreferenceUtils.getUsername(context)!=null){
                imgAddsongtoplaylist.setVisibility(View.VISIBLE);
                imgluotthich.setVisibility(View.VISIBLE);

                //su kien thich bài hát
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //initnutthich
                        Dataservice dataservice=APIService.getService();
                        Call<String> call=dataservice.PostKtYeuthichBaihat(mangbaihat.get(getPosition()).getIdBaiHat().toString(),PreferenceUtils.getUsername(context).toString());
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String ketqua=response.body();
                                if(ketqua.equals("exist")){
                                    //floatingActionLikeButton.setEnabled(false);
                                    imgluotthich.setImageResource(R.drawable.iconloved);
                                }else{
                                    //floatingActionLikeButton.setEnabled(true);
                                    imgluotthich.setImageResource(R.drawable.iconlove);
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("kt_likeplaylist","call fail");
                            }
                        });

                        //su kien thich bài hát
                        imgluotthich.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(imgluotthich.getDrawable().getConstantState()==context.getResources().getDrawable(R.drawable.iconlove).getConstantState()){
                                    Log.d("1", "1");
                                    imgluotthich.setImageResource(R.drawable.iconloved);
                                    Dataservice dataservice1= APIService.getService();
                                    Call<String> callback=dataservice1.PostBoThichvaIdCuaBaiHat("1",mangbaihat.get(getPosition()).getIdBaiHat().toString(),PreferenceUtils.getUsername(context));
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
                                }else{
                                    Log.d("2", "2");
                                    imgluotthich.setImageResource(R.drawable.iconlove);
                                    Dataservice dataservice2= APIService.getService();
                                    Call<String> callback=dataservice2.PostBoThichvaIdCuaBaiHat("1",mangbaihat.get(getPosition()).getIdBaiHat().toString(),PreferenceUtils.getUsername(context));
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
                        });
                    }
                }, 5000);

                //su kien them bai hat vao playlist
                imgAddsongtoplaylist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //lưu id bài hát
                        PreferenceUtils.saveSong(mangbaihat.get(getPosition()).getIdBaiHat(),context);

                        final AlertDialog alertDialog=new AlertDialog.Builder(context).create();
                        LayoutInflater inflater = LayoutInflater.from(context);
                        View dialogView = inflater.inflate(R.layout.alert_dialog_addsongtoplaylist, null);

                        Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancelAddSongtoplaylist);
                        final RecyclerView rePlaylist= (RecyclerView) dialogView.findViewById(R.id.reAddsongtoplaylist);



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
                                Log.d("UserPlaylistActivity","Lấy được:"+String.valueOf(mangplaylist.size()));
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
            }else {
                imgAddsongtoplaylist.setVisibility(View.GONE);
                imgluotthich.setVisibility(View.GONE);
            }


            //su kien an vào bài hát
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, MusicPlayerActivity.class);
                    intent.putExtra("Cakhuc",mangbaihat.get(getPosition()));
                    context.startActivity(intent);
                }
            });


        }
    }
}
