package com.example.mp3freeforyou.Adapter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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

public class UserPlaylistAdapter extends RecyclerView.Adapter<UserPlaylistAdapter.ViewHolder> {
    final Context context;
    ArrayList<Playlist> mangplaylist;

    public UserPlaylistAdapter(Context context, ArrayList<Playlist> mangplaylist) {
        this.context = context;
        this.mangplaylist = mangplaylist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_userplaylist,parent,false);
        return new UserPlaylistAdapter.ViewHolder(view);
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
        ImageView imgPlaylist,PlaylistDelBtn,PlaylistChangeName;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            txtTenPlaylist=itemView.findViewById(R.id.imgRowFragmentUserPlaylistTenPlaylist);
            imgPlaylist=itemView.findViewById(R.id.imgRowUserPlaylist);
            PlaylistDelBtn=itemView.findViewById(R.id.imgRowUserPlaylistDelbtn);
            PlaylistChangeName=itemView.findViewById(R.id.imgRowUserPlaylistChangnamebtn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //lưu idplaylist
                    PreferenceUtils.savePlaylist(mangplaylist.get(getPosition()).getIdPlaylist(),context);
                    PreferenceUtils.saveNamePlaylist(mangplaylist.get(getPosition()).getTenPlaylist(),context);
                    PreferenceUtils.saveImgIconPlaylist(mangplaylist.get(getPosition()).getHinhIcon(),context);
                    PreferenceUtils.saveImgPlaylist(mangplaylist.get(getPosition()).getHinhNen(),context);
                    PreferenceUtils.saveIdHoSoNguoiDungPlaylist(mangplaylist.get(getPosition()).getIdHoSoNguoiDung(),context);

                    Intent intent=new Intent(context, DanhsachbaihatActivity.class);
                    intent.putExtra("itemplaylist",mangplaylist.get(getPosition()));
                    context.startActivity(intent);
                }
            });

            PlaylistDelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dataservice dataservice= APIService.getService();
                    Call<String> callback=dataservice.PostDelPlayList(mangplaylist.get(getPosition()).getIdPlaylist().toString()) ;
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String kq=response.body();
                            if(kq.equals("success")){
                                Log.d("UserPlaylistAdapter","xóa playlist thành công");
                                Toast.makeText(context,"Đã xóa thành công",Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((UserPlaylistActivity)context).finish();
                                        Intent intent=new Intent(context,UserPlaylistActivity.class);
                                        intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        context.startActivity(intent);
                                    }
                                }, 3000);
                            }else if(kq.equals("fail")){
                                Log.d("UserPlaylistAdapter","server ko nhận đc id playlist");
                            }else if(kq.equals("fail1")){
                                Log.d("UserPlaylistAdapter","ko lấy đc ds bai hat can cap nhat");
                            }else if(kq.equals("fai2")){
                                Log.d("UserPlaylistAdapter","xóa playlist thất bại");
                            }else {
                                Log.d("UserPlaylistAdapter","vấn đề khác");
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("UserPlaylistAdapter","callback xóa fail");
                        }
                    });

                }
            });

            PlaylistChangeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog=new AlertDialog.Builder(context).create();
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.alert_dialog_user_playlist_changename, null);

                    final EditText edtNewplaylistname = (EditText) dialogView.findViewById(R.id.edtNewplaylistname);
                    Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmit);
                    Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancel);

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                    btnSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(edtNewplaylistname.getText().toString().equals("")){
                                Log.d("UserPlaylistAdapter","Tên bị rỗng");
                                Toast.makeText(context,"Không được để tên rỗng",Toast.LENGTH_LONG).show();
                            }else if(edtNewplaylistname.getText().toString().equals(txtTenPlaylist.getText().toString())){
                                Log.d("UserPlaylistAdapter","Tên bị trùng");
                                Toast.makeText(context,"Trùng tên cũ",Toast.LENGTH_LONG).show();
                            }else {
                                //Đổi tên trên db
                                Dataservice dataservice= APIService.getService();
                                Call<String> callback=dataservice.PostChangenamePlayList(mangplaylist.get(getPosition()).getIdPlaylist().toString(),edtNewplaylistname.getText().toString()) ;
                                callback.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String kq=response.body();
                                        if(kq.equals("success")){
                                            Log.d("UserPlaylistAdapter","đổi tên playlist thành công");
                                        }else if(kq.equals("fail")){
                                            Log.d("UserPlaylistAdapter","server ko nhận đc id playlist");
                                        }else {
                                            Log.d("UserPlaylistAdapter","vấn đề khác");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.d("UserPlaylistAdapter","callback đổi tên fail");
                                    }
                                });

                                //Đổi tên text view
                                txtTenPlaylist.setText(edtNewplaylistname.getText().toString());
                                alertDialog.dismiss();

                                /*Intent intent=new Intent(context, UserPlaylistActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                context.startActivity(intent);*/
                            }
                        }
                    });
                    alertDialog.setView(dialogView);
                    alertDialog.show();
                }
            });
        }
    }
}
