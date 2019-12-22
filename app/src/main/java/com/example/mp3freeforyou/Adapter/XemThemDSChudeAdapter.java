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
import com.example.mp3freeforyou.Model.Chudebaihat;
import com.example.mp3freeforyou.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class XemThemDSChudeAdapter extends RecyclerView.Adapter<XemThemDSChudeAdapter.ViewHolder>{

    Context context;
    ArrayList<Chudebaihat> mangchudebaihat;

    public XemThemDSChudeAdapter(Context context, ArrayList<Chudebaihat> mangchudebaihat) {
        this.context = context;
        this.mangchudebaihat = mangchudebaihat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_xemthemdschude,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chudebaihat chude=mangchudebaihat.get(position);
        Picasso.with(context).load(chude.getHinhChuDe()).into(holder.imghinhchude);
        holder.txtTenchude.setText(chude.getTenChuDe());
    }

    @Override
    public int getItemCount() {
        return mangchudebaihat.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imghinhchude;
        TextView txtTenchude;

        public ViewHolder(View itemview){
            super(itemview);
            imghinhchude=itemview.findViewById(R.id.imgRowXTDSChude);
            txtTenchude=itemview.findViewById(R.id.txtRowTenPlaylistXTDSChude);

            //khi click vào item của playlist sẽ trả ra Danhsachbaihatactivity
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DSChudeconActivity.class);
                    intent.putExtra("itemchudecon",mangchudebaihat.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
