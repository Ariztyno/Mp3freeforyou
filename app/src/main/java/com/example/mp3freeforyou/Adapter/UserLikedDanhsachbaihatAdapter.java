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

import com.example.mp3freeforyou.Activity.DanhsachbaihatActivity;
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

public class UserLikedDanhsachbaihatAdapter extends RecyclerView.Adapter<UserLikedDanhsachbaihatAdapter.ViewHolder> {
    ArrayList<Playlist> mangplaylist;
    Alert_Dialog_AddsongtoplaylistAdapter adapter;
    Playlist playlist;
    Context context;
    ArrayList<Baihat> mangbaihat;
    public UserLikedDanhsachbaihatAdapter(Context context, ArrayList<Baihat> mangbaihat) {
        this.context = context;
        this.mangbaihat = mangbaihat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_danhsachbaihatyeuthich,parent,false);

        return new UserLikedDanhsachbaihatAdapter.ViewHolder(view);
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
            txtindex=itemview.findViewById(R.id.txtDSBaihatIndexYeuthich);
            txttenbaihat=itemview.findViewById(R.id.txtRowDSBaihatTenBaiHatYeuthich);
            txtcasi=itemview.findViewById(R.id.txtRowDSBaihatYeuthichTenCaSi);
            imgluotthich=itemview.findViewById(R.id.imgUnlikeBtnRowDSBaihat);
            imgAddsongtoplaylist=itemview.findViewById(R.id.imgAddsongtoplaylistBtnRowDSBaihatYeuthich);

            //su kien thich bài hát
            imgluotthich.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //imgluotthich.setImageResource(R.drawable.iconloved);
                    Dataservice dataservice= APIService.getService();
                    Call<String> callback=dataservice.PostBoThichvaIdCuaBaiHat("1",mangbaihat.get(getPosition()).getIdBaiHat(),PreferenceUtils.getUsername(context));
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String ketqua=response.body();
                            if(ketqua.equals("unlikesuccess")){
                                Toast.makeText(context,"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((DanhsachbaihatActivity)context).finish();
                                        Intent intent=new Intent(context, DanhsachbaihatActivity.class);
                                        String text=PreferenceUtils.getUsername(context)+"1";
                                        intent.putExtra("baihatyeuthich",text);
                                        intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        context.startActivity(intent);
                                    }
                                }, 3000);
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                    //imgluotthich.setEnabled(false);
                }
            });

            if(!PreferenceUtils.getUsername(context).toString().equals("")){
                imgAddsongtoplaylist.setVisibility(View.VISIBLE);
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
