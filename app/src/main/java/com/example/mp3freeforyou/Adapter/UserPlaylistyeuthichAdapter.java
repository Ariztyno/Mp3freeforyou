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

public class UserPlaylistyeuthichAdapter extends RecyclerView.Adapter<UserPlaylistyeuthichAdapter.ViewHolder> {
    final Context context;
    ArrayList<Playlist> mangplaylist;

    public UserPlaylistyeuthichAdapter(Context context, ArrayList<Playlist> mangplaylist) {
        this.context = context;
        this.mangplaylist = mangplaylist;
    }
    @NonNull
    @Override
    public UserPlaylistyeuthichAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_userplaylist,parent,false);
        return new UserPlaylistyeuthichAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPlaylistyeuthichAdapter.ViewHolder holder, int position) {
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
            PlaylistChangeName.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DanhsachbaihatActivity.class);
                    intent.putExtra("itemplaylist",mangplaylist.get(getPosition()));
                    context.startActivity(intent);
                }
            });

            //xóa khỏi ds yeu thich
            PlaylistDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dataservice dataservice= APIService.getService();
                    Call<String> callback=dataservice.PostDelPlayListyethich(mangplaylist.get(getPosition()).getIdPlaylist().toString(), PreferenceUtils.getUsername(context).toString()) ;
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String kq=response.body();
                            if(kq.equals("success")){
                                Log.d("UserPlaylistAdapter","xóa playlist thành công");
                                Toast.makeText(context,"Đã bỏ thích thành công",Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((UserPlaylistActivity)context).finish();
                                        Intent intent=new Intent(context, UserPlaylistActivity.class);
                                        intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        context.startActivity(intent);
                                    }
                                }, 3000);
                            }else if(kq.equals("fail")){
                                Log.d("UserPlaylistAdapter","server ko nhận đc id playlist");
                            }else {
                                Log.d("UserPlaylistAdapter","xóa thất bại");
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("UserPlaylistAdapter","callback xóa fail");
                        }
                    });

                }
            });
        }
    }
}
