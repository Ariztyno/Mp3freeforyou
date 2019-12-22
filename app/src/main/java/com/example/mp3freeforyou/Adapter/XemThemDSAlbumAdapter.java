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
import com.example.mp3freeforyou.Model.Album;
import com.example.mp3freeforyou.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class XemThemDSAlbumAdapter extends  RecyclerView.Adapter<XemThemDSAlbumAdapter.ViewHolder>{
    Context context;
    ArrayList<Album> mangalbum;
    public XemThemDSAlbumAdapter(Context context, ArrayList<Album> mangalbum) {
        this.context = context;
        this.mangalbum = mangalbum;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_xemthemdsalbum,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Album album=mangalbum.get(position);
        Picasso.with(context).load(album.getHinhAlbum()).into(holder.imghinhalbum);
        holder.txtTenAlbum.setText(album.getTenAlbum());
        holder.txtTenCasiAlbum.setText(album.getIdCaSi());
    }

    @Override
    public int getItemCount() {
        return mangalbum.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imghinhalbum;
        TextView txtTenAlbum,txtTenCasiAlbum;

        public ViewHolder(View itemview){
            super(itemview);
            imghinhalbum=itemview.findViewById(R.id.imgRowXTDSAlbum);
            txtTenAlbum=itemview.findViewById(R.id.txtRowTenAlbumXTDSAlbum);
            txtTenCasiAlbum=itemview.findViewById(R.id.txtRowTenCacCaSiXTDSAlbum);
            //khi click vào item của album sẽ trả ra Danhsachbaihatactivity
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DanhsachbaihatActivity.class);
                    intent.putExtra("itemalbum",mangalbum.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
