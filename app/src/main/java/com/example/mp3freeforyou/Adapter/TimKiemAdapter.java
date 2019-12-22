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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mp3freeforyou.Activity.MusicPlayerActivity.mangbaihat;

public class TimKiemAdapter extends RecyclerView.Adapter<TimKiemAdapter.ViewHolder> {
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
        private ImageView imgBaihat,Baihatlikebtn,Baihataddsongtoplaylist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Get the view of the elements of baihat list
            txtTenBaihat=itemView.findViewById(R.id.txtRowFragmenTimkiemTenBaiHat);
            txtTenCasiBaihat=itemView.findViewById(R.id.txtRowFragmenTimkiemTenCaSi);
            imgBaihat=itemView.findViewById(R.id.imgRowFragmentTimKiem);
            Baihatlikebtn=itemView.findViewById(R.id.imgRowFragmentTimKiemLikebtn);
            Baihataddsongtoplaylist=itemView.findViewById(R.id.imgRowFragmentTimKiemAddsongtoplaylistbtn);

            //ấn vào bài hát tìm được
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, MusicPlayerActivity.class);
                    //chuyen 1 bai nên "cakhuc"
                    intent.putExtra("Cakhuc",mangbaihat.get(getPosition()));
                    context.startActivity(intent);
                }
            });

            //ấn nút thêm bài hát vào playlist
            if(PreferenceUtils.getUsername(context)!=null){
                Baihataddsongtoplaylist.setVisibility(View.VISIBLE);
                Baihatlikebtn.setVisibility(View.VISIBLE);

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
                                    Baihatlikebtn.setImageResource(R.drawable.iconloved);
                                }else{
                                    //floatingActionLikeButton.setEnabled(true);
                                    Baihatlikebtn.setImageResource(R.drawable.iconlove);
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("kt_likeplaylist","call fail");
                            }
                        });

                        Baihatlikebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(Baihatlikebtn.getDrawable().getConstantState()==context.getResources().getDrawable(R.drawable.iconlove).getConstantState()){
                                    Log.d("1", "1");
                                    Baihatlikebtn.setImageResource(R.drawable.iconloved);
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
                                    Baihatlikebtn.setImageResource(R.drawable.iconlove);
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

                //ấn vào nút thích



                //su kien them bai hat vao playlist
                Baihataddsongtoplaylist.setOnClickListener(new View.OnClickListener() {
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
                Baihataddsongtoplaylist.setVisibility(View.GONE);
                Baihatlikebtn.setVisibility(View.GONE);
            }


        }
    }
}
