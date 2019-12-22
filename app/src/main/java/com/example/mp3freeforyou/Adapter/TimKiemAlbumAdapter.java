package com.example.mp3freeforyou.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.DanhsachbaihatActivity;
import com.example.mp3freeforyou.Model.Album;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimKiemAlbumAdapter extends  RecyclerView.Adapter<TimKiemAlbumAdapter.ViewHolder> {
    Context context;
    ArrayList<Album> mangalbum;

    public TimKiemAlbumAdapter(Context context, ArrayList<Album> mangalbum) {
        this.context = context;
        this.mangalbum = mangalbum;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_fragment_timkiem_album,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album=mangalbum.get(position);
        holder.txtTenAlbum.setText(album.getTenAlbum());
        holder.txtCasiAlbum.setText(album.getIdCaSi());
        Picasso.with(context).load(album.getHinhAlbum()).into(holder.imgCasi);
    }

    @Override
    public int getItemCount() {
        return mangalbum.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenAlbum,txtCasiAlbum;
        ImageView imgCasi,CasiLikeBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenAlbum=itemView.findViewById(R.id.txtRowFragmenTimkiemAlbumTenAlbum);
            txtCasiAlbum=itemView.findViewById(R.id.txtRowFragmenTimkiemAlbumTenCaSiAlbum);
            imgCasi=itemView.findViewById(R.id.imgRowFragmentTimKiemAlbum);
            CasiLikeBtn=itemView.findViewById(R.id.imgRowFragmentTimKiemAlbumLikebtn);

            if(PreferenceUtils.getUsername(context)!=null){
                CasiLikeBtn.setVisibility(View.VISIBLE);

                //init
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //init like status
                        Dataservice dataservice= APIService.getService();
                        Call<String> call=dataservice.PostKtAlbumyeuthich(mangalbum.get(getPosition()).getIdAlbum(),PreferenceUtils.getUsername(context));
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String kq=response.body();
                                if(kq.equals("exist")){
                                    CasiLikeBtn.setImageResource(R.drawable.iconloved);
                                }else {
                                    CasiLikeBtn.setImageResource(R.drawable.iconlove);
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("kt_albumtk","callfail");
                            }
                        });

                        //sk onclick
                        CasiLikeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(CasiLikeBtn.getDrawable().getConstantState()==context.getResources().getDrawable(R.drawable.iconlove).getConstantState()){
                                    Log.d("1", "1");
                                    CasiLikeBtn.setImageResource(R.drawable.iconloved);
                                    Dataservice dataservice= APIService.getService();
                                    Call<String> call=dataservice.PostDelInsertAlbumyeuthich(mangalbum.get(getPosition()).getIdAlbum(), PreferenceUtils.getUsername(context));
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String kq=response.body();
                                            if(kq.equals("deletesuccess")){
                                                Toast.makeText(context,"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                            }else if (kq.equals("deletefail")){
                                                Log.d("unlikecasi1","unlike fail");
                                            }else if (kq.equals("insertsuccess")){
                                                Toast.makeText(context,"Đã thích",Toast.LENGTH_SHORT).show();
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
                                    CasiLikeBtn.setImageResource(R.drawable.iconlove);
                                    Dataservice dataservice= APIService.getService();
                                    Call<String> call=dataservice.PostDelInsertAlbumyeuthich(mangalbum.get(getPosition()).getIdAlbum(), PreferenceUtils.getUsername(context));
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String kq=response.body();
                                            if(kq.equals("deletesuccess")){
                                                Toast.makeText(context,"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                            }else if (kq.equals("deletefail")){
                                                Log.d("unlikecasi2","unlike fail");
                                            }else if (kq.equals("insertsuccess")){
                                                Toast.makeText(context,"Đã thích",Toast.LENGTH_SHORT).show();
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
                            }
                        });

                    }
                },5000);
            }else {
                CasiLikeBtn.setVisibility(View.GONE);
            }

            //ấn vào bài hát tìm được
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position=getAdapterPosition();
                    Intent intent=new Intent(context, DanhsachbaihatActivity.class);
                    intent.putExtra("itemalbum",mangalbum.get(position));
                    context.startActivity(intent);
                }
            });
        }
    }
}
