package com.example.mp3freeforyou.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaylistAdapter extends ArrayAdapter<Playlist> {
    public PlaylistAdapter(@NonNull Context context, int resource, @NonNull List<Playlist> objects) {
        super(context, resource, objects);
    }
    class ViewHolder{
        TextView txttenplaylist;
        ImageView imgbg,imgplaylist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       ViewHolder viewHolder=null;
       if(convertView==null){
           LayoutInflater inflater=LayoutInflater.from(getContext());
           convertView=inflater.inflate(R.layout.row_playlist,null);
           viewHolder=new ViewHolder();
           viewHolder.txttenplaylist=convertView.findViewById(R.id.txtTenplaylist);
           viewHolder.imgplaylist=convertView.findViewById(R.id.imgPlaylist);
           viewHolder.imgbg=convertView.findViewById(R.id.imgBGRowplaylist);
           convertView.setTag(viewHolder);

       }else {
           viewHolder= (ViewHolder) convertView.getTag();
       }
       Playlist playlist=getItem(position);
       Picasso.with(getContext()).load(playlist.getHinhNen()).into(viewHolder.imgbg);
       Picasso.with(getContext()).load(playlist.getHinhIcon()).into(viewHolder.imgplaylist);
       viewHolder.txttenplaylist.setText(playlist.getTenPlaylist());
       return convertView;
    }
}
