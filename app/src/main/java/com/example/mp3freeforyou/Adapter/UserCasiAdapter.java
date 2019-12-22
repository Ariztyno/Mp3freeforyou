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
import com.example.mp3freeforyou.Activity.UserSingerActivity;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCasiAdapter extends RecyclerView.Adapter<UserCasiAdapter.ViewHolder> {
    final Context context;
    ArrayList<Casi> mangcasi;

    public UserCasiAdapter(Context context, ArrayList<Casi> mangcasi) {
        this.context = context;
        this.mangcasi = mangcasi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_usercasi,parent,false);
        return new UserCasiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Casi casi=mangcasi.get(position);
        holder.txtTenCasi.setText(casi.getTenCaSi());
        Picasso.with(context).load(casi.getHinhCaSi()).into(holder.imghinhcasi);
    }

    @Override
    public int getItemCount() {
        return mangcasi.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenCasi;
        ImageView imghinhcasi,btnUnlike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenCasi=itemView.findViewById(R.id.imgRowFragmentUserCasiTenCasi);
            imghinhcasi=itemView.findViewById(R.id.imgRowUserCasi);
            btnUnlike=itemView.findViewById(R.id.imgRowUserCasiDelbtn);

            //xóa ca si
            btnUnlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dataservice dataservice= APIService.getService();
                    Call<String> call=dataservice.PostDelInsertCasiyeuthich(mangcasi.get(getPosition()).getIdCaSi(), PreferenceUtils.getUsername(context));
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String kq=response.body();
                            if(kq.equals("deletesuccess")){
                                Toast.makeText(context,"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((UserSingerActivity)context).finish();
                                        Intent intent=new Intent(context, UserSingerActivity.class);
                                        intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        context.startActivity(intent);
                                    }
                                },3000);
                            }else {
                                Log.d("unlikecasi","something fail");
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("unlikecasi","call fail");
                        }
                    });
                }
            });

            //itemview onclick
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DanhsachbaihatActivity.class);
                    intent.putExtra("itemcasi",mangcasi.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
