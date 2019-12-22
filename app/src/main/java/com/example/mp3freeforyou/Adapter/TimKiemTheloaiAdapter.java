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
