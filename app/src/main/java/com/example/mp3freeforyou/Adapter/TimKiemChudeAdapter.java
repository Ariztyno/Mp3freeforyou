package com.example.mp3freeforyou.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.DSChudeconActivity;
import com.example.mp3freeforyou.Activity.DSTheloai;
import com.example.mp3freeforyou.Model.Chudebaihat;
import com.example.mp3freeforyou.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TimKiemChudeAdapter extends RecyclerView.Adapter<TimKiemChudeAdapter.ViewHolder> {
    Context context;
    ArrayList<Chudebaihat> mangchude;

    public TimKiemChudeAdapter(Context context, ArrayList<Chudebaihat> mangchude) {
        this.context = context;
        this.mangchude = mangchude;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_fragment_timkiem_chude,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chudebaihat chudebaihat=mangchude.get(position);
        holder.txtTenChude.setText(chudebaihat.getTenChuDe());
        Picasso.with(context).load(chudebaihat.getHinhChuDe()).into(holder.imgChude);

    }

    @Override
    public int getItemCount() {
        return mangchude.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenChude,txtlachudecon;
        ImageView imgChude,ChudeLikeBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenChude=itemView.findViewById(R.id.imgRowFragmentTimKiemChuDeTenChuDe);
            imgChude=itemView.findViewById(R.id.imgRowFragmentTimKiemChuDe);
            ChudeLikeBtn=itemView.findViewById(R.id.imgRowFragmentTimKiemChuDeLikebtn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mangchude.get(getPosition()).getLaChuDeConCua().equals("0")){
                        Intent intent=new Intent(context, DSChudeconActivity.class);
                        intent.putExtra("itemchudecon",mangchude.get(getPosition()));
                        context.startActivity(intent);
                    }else {
                        Intent intent=new Intent(context, DSTheloai.class);
                        intent.putExtra("itemchude",mangchude.get(getPosition()));
                        context.startActivity(intent);
                    }

                }
            });
            /*if(txtlachudecon.getText().equals("0")){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, DSChudeconActivity.class);
                        intent.putExtra("itemchudecon",mangchude.get(getPosition()));
                        context.startActivity(intent);
                    }
                });
            }
            if(!txtlachudecon.getText().equals("0")){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, DSTheloai.class);
                        intent.putExtra("itemchude",mangchude.get(getPosition()));
                        context.startActivity(intent);
                    }
                });
            }*/
            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemView.mangchude.get(getPosition()).getLaChuDeConCua().equals("0"))
                    {
                        Intent intent=new Intent(context, DSChudeconActivity.class);
                        intent.putExtra("itemchudecon",mangchude.get(getPosition()));
                        context.startActivity(intent);
                    }else {
                        Intent intent=new Intent(context, DSTheloai.class);
                        intent.putExtra("itemchude",mangchude.get(getAdapterPosition()));
                        context.startActivity(intent);
                    }
                }
            });*/


        }
    }
}
