package com.example.mp3freeforyou.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.DSChudeconActivity;
import com.example.mp3freeforyou.Model.Chudebaihat;
import com.example.mp3freeforyou.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChudeAdapter extends RecyclerView.Adapter<ChudeAdapter.ViewHolder> {

    Context context;
    ArrayList<Chudebaihat> mangchude;

    public ChudeAdapter(Context context, ArrayList<Chudebaihat> mangchude) {
        this.context = context;
        this.mangchude = mangchude;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_chude,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chudebaihat chudebaihat=mangchude.get(position);
        Picasso.with(context).load(chudebaihat.getHinhChuDe()).into(holder.imghinhchude);
    }

    @Override
    public int getItemCount() {
        return mangchude.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imghinhchude;
        public ViewHolder(View itemview){
            super(itemview);
            imghinhchude=itemview.findViewById(R.id.imgBGRowchude);
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DSChudeconActivity.class);
                    intent.putExtra("itemchudecon",mangchude.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
