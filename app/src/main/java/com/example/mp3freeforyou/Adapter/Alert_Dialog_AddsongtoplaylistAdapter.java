package com.example.mp3freeforyou.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.DanhsachbaihatActivity;
import com.example.mp3freeforyou.Activity.UserPlaylistActivity;
import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Alert_Dialog_AddsongtoplaylistAdapter extends RecyclerView.Adapter<Alert_Dialog_AddsongtoplaylistAdapter.ViewHolder> {
    final Context context;
    ArrayList<Playlist> mangplaylist;

    public Alert_Dialog_AddsongtoplaylistAdapter(Context context, ArrayList<Playlist> mangplaylist) {
        this.context = context;
        this.mangplaylist = mangplaylist;
    }

    @NonNull
    @Override
    public Alert_Dialog_AddsongtoplaylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_userplaylist,parent,false);
        return new Alert_Dialog_AddsongtoplaylistAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Alert_Dialog_AddsongtoplaylistAdapter.ViewHolder holder, int position) {
        Playlist playlist=mangplaylist.get(position);
        holder.txtTenPlaylist.setText(playlist.getTenPlaylist());
        Picasso.with(context).load(playlist.getHinhIcon()).into(holder.imgPlaylist);
    }

    @Override
    public int getItemCount() {
        return mangplaylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenPlaylist;
        ImageView imgPlaylist,PlaylistDelBtn,PlaylistChangeName;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtTenPlaylist=itemView.findViewById(R.id.imgRowFragmentUserPlaylistTenPlaylist);
            imgPlaylist=itemView.findViewById(R.id.imgRowUserPlaylist);
            PlaylistDelBtn=itemView.findViewById(R.id.imgRowUserPlaylistDelbtn);
            PlaylistChangeName=itemView.findViewById(R.id.imgRowUserPlaylistChangnamebtn);

            //vô hiệu hóa hai nút do đây là thêm bài hat vào playlist cá nhân
            PlaylistDelBtn.setVisibility(View.GONE);
            PlaylistChangeName.setVisibility(View.GONE);
            PlaylistDelBtn.setEnabled(false);
            PlaylistChangeName.setEnabled(false);

            //init màu chữ
            txtTenPlaylist.setTextColor(Color.BLACK);

            //add song to playlist
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dataservice dataservice=APIService.getService();
                    Call<String> call=dataservice.PostAddsongtoplaylist(mangplaylist.get(getPosition()).getIdPlaylist(), PreferenceUtils.getSong(context));
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String kq=response.body();
                            if(kq.equals("updatesongsuccess")){
                                Toast.makeText(context,"Đã thêm vào "+mangplaylist.get(getPosition()).getTenPlaylist().toString(),Toast.LENGTH_LONG).show();
                                //clear song
                                //PreferenceUtils.saveSong(null,context);
                            }else if(kq.equals("updatesongfail")){
                                Log.d("alertdialogadapter","updatethatbai");
                            }else if(kq.equals("emty")){
                                Log.d("alertdialogadapter","idbaihat or idplaylist rỗng");
                            }else if(kq.equals("baihat_not_found")){
                                Log.d("alertdialogadapter","baihat_not_found");
                            }else if(kq.equals("songexisted")){
                                Toast.makeText(context,"Bài hát đã tồn tại trong playlist "+mangplaylist.get(getPosition()).getTenPlaylist().toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("alertdialogadapter","call fail");
                        }
                    });
                }
            });
        }
    }
}
