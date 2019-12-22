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
import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class XemThemDSPlaylistAdapter extends RecyclerView.Adapter<XemThemDSPlaylistAdapter.ViewHolder> {
    Context context;
    ArrayList<Playlist> mangplaylist;
    public XemThemDSPlaylistAdapter(Context context, ArrayList<Playlist> mangplaylist) {
        this.context = context;
        this.mangplaylist = mangplaylist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_xemthemdsplaylist,parent,false);
        return new ViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist playlist=mangplaylist.get(position);
        Picasso.with(context).load(playlist.getHinhIcon()).into(holder.imghinhplaylist);
        holder.txtTenPlaylist.setText(playlist.getTenPlaylist());
    }

    @Override
    public int getItemCount() {
        return mangplaylist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imghinhplaylist;
        TextView txtTenPlaylist;

        public ViewHolder(View itemview){
            super(itemview);
            imghinhplaylist=itemview.findViewById(R.id.imgRowXTDSPlylist);
            txtTenPlaylist=itemview.findViewById(R.id.txtRowTenPlaylistXTDSPlaylist);

            //khi click vào item của playlist sẽ trả ra Danhsachbaihatactivity
            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DanhsachbaihatActivity.class);
                    intent.putExtra("itemplaylist",mangplaylist.get(getPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
