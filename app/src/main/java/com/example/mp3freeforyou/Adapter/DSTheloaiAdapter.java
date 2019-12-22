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

import com.example.mp3freeforyou.Activity.DanhsachbaihatActivity;
import com.example.mp3freeforyou.Model.Theloaibaihat;
import com.example.mp3freeforyou.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DSTheloaiAdapter extends RecyclerView.Adapter<DSTheloaiAdapter.ViewHolder>{

    Context context;
    ArrayList<Theloaibaihat> mangtheloai;
    public DSTheloaiAdapter(Context context, ArrayList<Theloaibaihat> mangtheloai) {
        this.context = context;
        this.mangtheloai = mangtheloai;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_dstheloai,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Theloaibaihat theloai=mangtheloai.get(position);
        Picasso.with(context).load(theloai.getHinhTheLoai()).into(holder.imghinhplaylist);
        holder.txtTenPlaylist.setText(theloai.getTenTheLoai());
    }

    @Override
    public int getItemCount() {
        return mangtheloai.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imghinhplaylist;
        TextView txtTenPlaylist;

        public ViewHolder(View itemview){
            super(itemview);
            imghinhplaylist=itemview.findViewById(R.id.imgRowDSTheloai);
            txtTenPlaylist=itemview.findViewById(R.id.txtRowDSTheloai);

            //khi click vào item của playlist sẽ trả ra Danhsachbaihatactivity
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DanhsachbaihatActivity.class);
                    intent.putExtra("itemtheloai",mangtheloai.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
