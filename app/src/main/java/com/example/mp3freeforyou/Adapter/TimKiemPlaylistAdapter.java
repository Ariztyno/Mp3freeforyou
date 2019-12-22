package com.example.mp3freeforyou.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.DanhsachbaihatActivity;
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

public class TimKiemPlaylistAdapter extends RecyclerView.Adapter<TimKiemPlaylistAdapter.ViewHolder>{
    Context context;
    ArrayList<Playlist> mangplaylist;

    public TimKiemPlaylistAdapter(Context context, ArrayList<Playlist> mangplaylist) {
        this.context = context;
        this.mangplaylist = mangplaylist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_fragment_timkiem_playlist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
        ImageView imgPlaylist,PlaylistLikeBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenPlaylist = itemView.findViewById(R.id.imgRowFragmentTimKiemPlaylistTenPlaylist);
            imgPlaylist = itemView.findViewById(R.id.imgRowFragmentTimKiemPlaylist);
            PlaylistLikeBtn = itemView.findViewById(R.id.imgRowFragmentTimKiemPlaylistLikebtn);

            if (PreferenceUtils.getUsername(context)!=null) {
                PlaylistLikeBtn.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //init nút like
                        Dataservice dataservice= APIService.getService();
                        Call<String> call=dataservice.PostKtYeuthichPlaylist(mangplaylist.get(getPosition()).getIdPlaylist().toString(),PreferenceUtils.getUsername(context).toString());
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String ketqua=response.body();
                                if(ketqua.equals("exist")){
                                    //floatingActionLikeButton.setEnabled(false);
                                    PlaylistLikeBtn.setImageResource(R.drawable.iconloved);
                                }else{
                                    //floatingActionLikeButton.setEnabled(true);
                                    PlaylistLikeBtn.setImageResource(R.drawable.iconlove);
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("kt_likeplaylist","call fail");
                            }
                        });

                        //nút thích playlist
                        PlaylistLikeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(PlaylistLikeBtn.isEnabled()){
                                    Log.d("likeplaylist_enable","true");

                                    //nếu chưa thích
                                    if(PlaylistLikeBtn.getDrawable().getConstantState()==context.getResources().getDrawable(R.drawable.iconlove).getConstantState()){
                                        PlaylistLikeBtn.setImageResource(R.drawable.iconloved);
                                        Dataservice dataservice=APIService.getService();
                                        Call<String> callback=dataservice.PostYeuthichPlaylist(mangplaylist.get(getPosition()).getIdPlaylist().toString(), PreferenceUtils.getUsername(context).toString());
                                        callback.enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, Response<String> response) {
                                                String ketqua=response.body();
                                                if(ketqua.equals("success")){
                                                    Toast.makeText(context,"Đã thích",Toast.LENGTH_SHORT).show();
                                                }else if(ketqua.equals("delsuccess")){
                                                    Toast.makeText(context,"Đã bỏ thích ",Toast.LENGTH_SHORT).show();
                                                    Log.d("LikePlaylist","delsuccess");
                                                }else if(ketqua.equals("delfail")){
                                                    Log.d("LikePlaylist","delfail");
                                                }else if(ketqua.equals("fail2")){
                                                    Log.d("LikePlaylist","thêm vào ds yêu thích thất bại");
                                                }else{
                                                    Log.d("LikePlaylist","thông tin truyền vào bị rỗng");
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<String> call, Throwable t) {
                                                Log.d("Likebtnplaylist","call fail");
                                            }
                                        });
                                    }else {
                                        PlaylistLikeBtn.setImageResource(R.drawable.iconlove);
                                        Dataservice dataservice=APIService.getService();
                                        Call<String> callback=dataservice.PostYeuthichPlaylist(mangplaylist.get(getPosition()).getIdPlaylist().toString(), PreferenceUtils.getUsername(context).toString());
                                        callback.enqueue(new Callback<String>() {
                                            @Override
                                            public void onResponse(Call<String> call, Response<String> response) {
                                                String ketqua=response.body();
                                                if(ketqua.equals("success")){
                                                    Toast.makeText(context,"Đã thích",Toast.LENGTH_SHORT).show();
                                                }else if(ketqua.equals("delsuccess")){
                                                    Toast.makeText(context,"Đã bỏ thích ",Toast.LENGTH_SHORT).show();
                                                    Log.d("LikePlaylist","delsuccess");
                                                }else if(ketqua.equals("delfail")){
                                                    Log.d("LikePlaylist","delfail");
                                                }else if(ketqua.equals("fail2")){
                                                    Log.d("LikePlaylist","thêm vào ds yêu thích thất bại");
                                                }else{
                                                    Log.d("LikePlaylist","thông tin truyền vào bị rỗng");
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<String> call, Throwable t) {
                                                Log.d("Likebtnplaylist","call fail");
                                            }
                                        });
                                    }

                                    //floatingActionLikeButton.setEnabled(false);
                                }else{
                                    Log.d("likeplaylist_enable","false");
                                }
                            }
                        });
                    }
                }, 5000);
            }else {
                PlaylistLikeBtn.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DanhsachbaihatActivity.class);
                    intent.putExtra("itemplaylist",mangplaylist.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
