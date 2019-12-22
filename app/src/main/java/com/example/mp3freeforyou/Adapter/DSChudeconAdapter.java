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

import com.example.mp3freeforyou.Activity.DSTheloai;
import com.example.mp3freeforyou.Model.Chudebaihat;
import com.example.mp3freeforyou.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DSChudeconAdapter extends RecyclerView.Adapter<DSChudeconAdapter.ViewHolder> {
    Context context;
    ArrayList<Chudebaihat> mangchudebaihat;

    public DSChudeconAdapter(Context context, ArrayList<Chudebaihat> mangchudebaihat) {
        this.context = context;
        this.mangchudebaihat = mangchudebaihat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_dschudecon,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chudebaihat chudebaihat=mangchudebaihat.get(position);
        Picasso.with(context).load(chudebaihat.getHinhChuDe()).into(holder.imghinhchudecon);
        holder.txttenchudecon.setText(chudebaihat.getTenChuDe());
    }

    @Override
    public int getItemCount() {
        return mangchudebaihat.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imghinhchudecon;
        TextView txttenchudecon;
        public  ViewHolder(View itemview){
            super(itemview);
            imghinhchudecon=itemview.findViewById(R.id.imgRowDSChudecon);
            txttenchudecon=itemview.findViewById(R.id.txtRowTenDSChudecon);
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DSTheloai.class);
                    intent.putExtra("itemchude",mangchudebaihat.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
