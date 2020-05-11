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
import com.example.mp3freeforyou.Activity.UserTheloaibaihatActivity;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.Model.Theloaibaihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserTheloaibaihatAdapter extends RecyclerView.Adapter<UserTheloaibaihatAdapter.ViewHolder> {
    final Context context;
    ArrayList<Theloaibaihat> mangtheloai;

    public UserTheloaibaihatAdapter(Context context, ArrayList<Theloaibaihat> mangtheloai) {
        this.context = context;
        this.mangtheloai = mangtheloai;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_usertheloai,parent,false);
        return new UserTheloaibaihatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Theloaibaihat theloaibaihat=mangtheloai.get(position);
        holder.txtTenTheloai.setText(theloaibaihat.getTenTheLoai());
        Picasso.with(context).load(theloaibaihat.getHinhTheLoai()).into(holder.imghinhtheloai);
    }

    @Override
    public int getItemCount() {
        return mangtheloai.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenTheloai;
        ImageView imghinhtheloai,btnUnlike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenTheloai=itemView.findViewById(R.id.txtRowUserTheloaiTenTheloai);
            imghinhtheloai=itemView.findViewById(R.id.imgRowUserTheloai);
            btnUnlike=itemView.findViewById(R.id.imgRowUserTheloaiLikebtn);

            //xóa the loai
            btnUnlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dataservice dataservice= APIService.getService();
                    Call<String> call=dataservice.PostDelInsertTheloaibaihatyeuthich(mangtheloai.get(getPosition()).getIdTheLoai(), PreferenceUtils.getUsername(context));
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String kq=response.body();
                            if(kq.equals("deletesuccess")){
                                Toast.makeText(context,"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((UserTheloaibaihatActivity)context).finish();
                                        Intent intent=new Intent(context, UserTheloaibaihatActivity.class);
                                        intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        context.startActivity(intent);
                                    }
                                },3500);
                            }else {
                                Log.d("unliketheloai","something fail");
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("unliketheloai","call fail");
                        }
                    });
                }
            });

            //itemview onclick
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DanhsachbaihatActivity.class);
                    intent.putExtra("itemcasi",mangtheloai.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
