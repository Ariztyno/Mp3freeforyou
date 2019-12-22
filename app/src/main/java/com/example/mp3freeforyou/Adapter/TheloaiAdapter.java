package com.example.mp3freeforyou.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.DanhsachbaihatActivity;
import com.example.mp3freeforyou.Model.Chudebaihat;
import com.example.mp3freeforyou.Model.Theloaibaihat;
import com.example.mp3freeforyou.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class TheloaiAdapter extends RecyclerView.Adapter<TheloaiAdapter.ViewHolder> {

    Context context;
    ArrayList<Theloaibaihat> mangtheloai;

    public TheloaiAdapter(Context context, ArrayList<Theloaibaihat> mangtheloai) {
        this.context = context;
        this.mangtheloai = mangtheloai;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_theloai,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Theloaibaihat theloaibaihat=mangtheloai.get(position);
        Picasso.with(context).load(theloaibaihat.getHinhTheLoai()).into(holder.imghinhtheloai);
    }

    @Override
    public int getItemCount() {
        return mangtheloai.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imghinhtheloai;
        public ViewHolder(View itemview){
            super(itemview);
            imghinhtheloai=itemview.findViewById(R.id.imgBGRowtheloai);
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position=getAdapterPosition();
                    Intent intent=new Intent(context,DanhsachbaihatActivity.class);
                    intent.putExtra("itemtheloai",mangtheloai.get(position));
                    context.startActivity(intent);
                }
            });

        }
    }
}
