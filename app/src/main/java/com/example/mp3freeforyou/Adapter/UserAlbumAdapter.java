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
import com.example.mp3freeforyou.Activity.UserAlbumActivity;
import com.example.mp3freeforyou.Activity.UserSingerActivity;
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

public class UserAlbumAdapter extends RecyclerView.Adapter<UserAlbumAdapter.ViewHolder> {
    final Context context;
    ArrayList<Album> mangalbum;

    public UserAlbumAdapter(Context context, ArrayList<Album> mangalbum) {
        this.context = context;
        this.mangalbum = mangalbum;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_useralbum,parent,false);
        return new UserAlbumAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album=mangalbum.get(position);
        holder.txtTenAlbum.setText(album.getTenAlbum());
        holder.txtTenCasiAlbum.setText(album.getIdCaSi());
        Picasso.with(context).load(album.getHinhAlbum()).into(holder.imghinhalbum);
    }

    @Override
    public int getItemCount() {
        return mangalbum.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenAlbum,txtTenCasiAlbum;
        ImageView imghinhalbum,btnUnlike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenAlbum=itemView.findViewById(R.id.txtRowUserAlbumTenAlbum);
            txtTenCasiAlbum=itemView.findViewById(R.id.txtRowUserAlbumTenCaSiAlbum);
            imghinhalbum=itemView.findViewById(R.id.imgRowUserAlbum);
            btnUnlike=itemView.findViewById(R.id.imgRowUserAlbumLikebtn);

            //xóa album
            btnUnlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dataservice dataservice= APIService.getService();
                    Call<String> call=dataservice.PostDelInsertAlbumyeuthich(mangalbum.get(getPosition()).getIdAlbum(), PreferenceUtils.getUsername(context));
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String kq=response.body();
                            if(kq.equals("deletesuccess")){
                                Toast.makeText(context,"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((UserAlbumActivity)context).finish();
                                        Intent intent=new Intent(context, UserAlbumActivity.class);
                                        intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        context.startActivity(intent);
                                    }
                                },3000);
                            }else {
                                Log.d("unlikealbum","something fail");
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("unlikealbum","call fail");
                        }
                    });
                }
            });

            //itemview onclick
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DanhsachbaihatActivity.class);
                    intent.putExtra("itemalbum",mangalbum.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
