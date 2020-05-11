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

public class TimKiemTheloaiAdapter extends RecyclerView.Adapter<TimKiemTheloaiAdapter.ViewHolder> {
    Context context;
    ArrayList<Theloaibaihat> mangtl;

    public TimKiemTheloaiAdapter(Context context, ArrayList<Theloaibaihat> mangtl) {
        this.context = context;
        this.mangtl = mangtl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_fragment_timkiem_theloai,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Theloaibaihat theloaibaihat=mangtl.get(position);
        holder.txtTenTheLoai.setText(theloaibaihat.getTenTheLoai());
        Picasso.with(context).load(theloaibaihat.getHinhTheLoai()).into(holder.imgTheloai);
    }

    @Override
    public int getItemCount() {
        return mangtl.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenTheLoai;
        ImageView imgTheloai,TheloaiLikeBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenTheLoai=itemView.findViewById(R.id.imgRowFragmentTimKiemPlaylistTenTheLoai);
            imgTheloai=itemView.findViewById(R.id.imgRowFragmentTimKiemTheLoai);
            TheloaiLikeBtn=itemView.findViewById(R.id.imgRowFragmentTimKiemTheLoaiLikebtn);

            //nếu chưa đăng nhập thì ko thể thích
            if(PreferenceUtils.getUsername(context)!=null) {
                TheloaiLikeBtn.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //INIT LƯỢT THÍCH
                        Dataservice dataservice= APIService.getService();
                        Call<String> callkt=dataservice.PostKtTheloaibaihatyeuthich(mangtl.get(getPosition()).getIdTheLoai(), PreferenceUtils.getUsername(context));
                        callkt.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String kq=response.body();
                                if(kq.equals("exist")){
                                    //floatingActionLikeButton.setEnabled(false);
                                    TheloaiLikeBtn.setImageResource(R.drawable.iconloved);
                                }else{
                                    //floatingActionLikeButton.setEnabled(true);
                                    TheloaiLikeBtn.setImageResource(R.drawable.iconlove);
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });

                        //onclick luotthich
                        TheloaiLikeBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(TheloaiLikeBtn.getDrawable().getConstantState()==context.getResources().getDrawable(R.drawable.iconlove).getConstantState()){
                                    Log.d("1", "1");
                                    TheloaiLikeBtn.setImageResource(R.drawable.iconloved);
                                    Dataservice dataservice= APIService.getService();
                                    Call<String> call=dataservice.PostDelInsertTheloaibaihatyeuthich(mangtl.get(getPosition()).getIdTheLoai(), PreferenceUtils.getUsername(context));
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String kq=response.body();
                                            if(kq.equals("deletesuccess")){
                                                Toast.makeText(context,"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                            }else if (kq.equals("deletefail")){
                                                Log.d("unliketheloai2","unlike fail");
                                            }else if (kq.equals("addsuccess")){
                                                Toast.makeText(context,"Đã thích",Toast.LENGTH_SHORT).show();
                                                Log.d("unlikecasi2","like success");
                                            }else if (kq.equals("addfail")){
                                                Log.d("unliketheloai2","like fail");
                                            }else {
                                                Log.d("unliketheloai2","emty sent");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Log.d("unliketheloai2","call fail");
                                        }
                                    });
                                }else {
                                    Log.d("2", "2");
                                    TheloaiLikeBtn.setImageResource(R.drawable.iconlove);
                                    Dataservice dataservice= APIService.getService();
                                    Call<String> call=dataservice.PostDelInsertTheloaibaihatyeuthich(mangtl.get(getPosition()).getIdTheLoai(), PreferenceUtils.getUsername(context));
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String kq=response.body();
                                            if(kq.equals("deletesuccess")){
                                                Toast.makeText(context,"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                            }else if (kq.equals("deletefail")){
                                                Log.d("unliketheloai1","unlike fail");
                                            }else if (kq.equals("addsuccess")){
                                                Toast.makeText(context,"Đã thích",Toast.LENGTH_SHORT).show();
                                                Log.d("unliketheloai1","like success");
                                            }else if (kq.equals("addfail")){
                                                Log.d("unliketheloai1","like fail");
                                            }else {
                                                Log.d("unliketheloai1","emty sent");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Log.d("unliketheloai1","call fail");
                                        }
                                    });
                                }
                            }
                        });

                    }
                },5000);
            }else {
                TheloaiLikeBtn.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position=getAdapterPosition();
                    Intent intent=new Intent(context, DanhsachbaihatActivity.class);
                    intent.putExtra("itemtheloai",mangtl.get(position));
                    context.startActivity(intent);
                }
            });
        }
    }
}
