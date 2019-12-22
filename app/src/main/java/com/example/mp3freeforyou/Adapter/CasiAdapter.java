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

public class CasiAdapter extends RecyclerView.Adapter<CasiAdapter.ViewHolder> {

    Context context;
    ArrayList<Casi> mangcasi;

    public CasiAdapter(Context context, ArrayList<Casi> mangcasi) {
        this.context = context;
        this.mangcasi = mangcasi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_fragment_timkiem_casi,parent,false);
        return new CasiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Casi casi=mangcasi.get(position);
        holder.txtTenCasi.setText(casi.getTenCaSi());
        holder.txtThongtinCasi.setText(casi.getThongTin());
        Picasso.with(context).load(casi.getHinhCaSi()).into(holder.imgCasi);
    }

    @Override
    public int getItemCount() {
        return mangcasi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        // List items of baihat list
        private TextView txtTenCasi;
        private TextView txtThongtinCasi;
        private ImageView imgCasi,Casilikebtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenCasi=itemView.findViewById(R.id.txtRowFragmenTimkiemCasiTenCaSi);
            txtThongtinCasi=itemView.findViewById(R.id.txtRowFragmenTimkiemCasiThongtinCaSi);
            imgCasi=itemView.findViewById(R.id.imgRowFragmentTimKiemCasi);
            Casilikebtn=itemView.findViewById(R.id.imgRowFragmentTimKiemCasiLikebtn);

            //nếu chưa đăng nhập thì ko thể thích
            if(!PreferenceUtils.getUsername(context).toString().equals("")) {
                Casilikebtn.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //INIT LƯỢT THÍCH
                        Dataservice dataservice= APIService.getService();
                        Call<String> callkt=dataservice.PostKtCasiyeuthich(mangcasi.get(getPosition()).getIdCaSi(), PreferenceUtils.getUsername(context));
                        callkt.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String kq=response.body();
                                if(kq.equals("exist")){
                                    //floatingActionLikeButton.setEnabled(false);
                                    Casilikebtn.setImageResource(R.drawable.iconloved);
                                }else{
                                    //floatingActionLikeButton.setEnabled(true);
                                    Casilikebtn.setImageResource(R.drawable.iconlove);
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });

                        //onclick luotthich
                        Casilikebtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(Casilikebtn.getDrawable().getConstantState()==context.getResources().getDrawable(R.drawable.iconlove).getConstantState()){
                                    Log.d("1", "1");
                                    Casilikebtn.setImageResource(R.drawable.iconloved);
                                    Dataservice dataservice= APIService.getService();
                                    Call<String> call=dataservice.PostDelInsertCasiyeuthich(mangcasi.get(getPosition()).getIdCaSi(), PreferenceUtils.getUsername(context));
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String kq=response.body();
                                            if(kq.equals("deletesuccess")){
                                                Toast.makeText(context,"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                            }else if (kq.equals("deletefail")){
                                                Log.d("unlikecasi1","unlike fail");
                                            }else if (kq.equals("addsuccess")){
                                                Toast.makeText(context,"Đã thích",Toast.LENGTH_SHORT).show();
                                                Log.d("unlikecasi1","like success");
                                            }else if (kq.equals("addfail")){
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
                                    Casilikebtn.setImageResource(R.drawable.iconlove);
                                    Dataservice dataservice= APIService.getService();
                                    Call<String> call=dataservice.PostDelInsertCasiyeuthich(mangcasi.get(getPosition()).getIdCaSi(), PreferenceUtils.getUsername(context));
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String kq=response.body();
                                            if(kq.equals("deletesuccess")){
                                                Toast.makeText(context,"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                            }else if (kq.equals("deletefail")){
                                                Log.d("unlikecasi1","unlike fail");
                                            }else if (kq.equals("addsuccess")){
                                                Toast.makeText(context,"Đã thích",Toast.LENGTH_SHORT).show();
                                                Log.d("unlikecasi1","like success");
                                            }else if (kq.equals("addfail")){
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
                                }
                            }
                        });

                    }
                },5000);
            }else {
                Casilikebtn.setVisibility(View.GONE);
            }


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position=getAdapterPosition();
                    Intent intent=new Intent(context, DanhsachbaihatActivity.class);
                    intent.putExtra("itemcasi",mangcasi.get(position));
                    context.startActivity(intent);
                }
            });
        }
    }
}
