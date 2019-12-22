package com.example.mp3freeforyou.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.Objects.*;

public class Top5baihatduocyeuthichnhatAdapter extends ArrayAdapter<Baihat> {

    //init for rePlaylist
    ArrayList<Playlist> mangplaylist = new ArrayList<Playlist>();
    Alert_Dialog_AddsongtoplaylistAdapter adapter;

    public Top5baihatduocyeuthichnhatAdapter(@NonNull Context context, int resource, @NonNull List<Baihat> objects) {
        super(context, resource, objects);
    }

    class ViewHolder{
        TextView txttenbaihat,txttencasi;
        ImageView imghinhbaihat,imglike,imgaddtoplaylist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView=inflater.inflate(R.layout.row_top5baihatduocyeuthichnhat,null);
            viewHolder=new ViewHolder();
            viewHolder.txttenbaihat=convertView.findViewById(R.id.txtRowTop5TenBaiHat);
            viewHolder.txttencasi=convertView.findViewById(R.id.txtRowTop5Tencasi);
            viewHolder.imghinhbaihat=convertView.findViewById(R.id.imgRowTop5);
            viewHolder.imglike=convertView.findViewById(R.id.imgLikeBtnRowTop5);
            viewHolder.imgaddtoplaylist=convertView.findViewById(R.id.imgAddtoplaylistBtnRowTop5);

            //kiểm tra nếu chưa đã đăng nhập thì mới hiện nút addtoplaylist
            if(PreferenceUtils.getUsername(getContext())==null){
                viewHolder.imgaddtoplaylist.setVisibility(View.GONE);
                viewHolder.imglike.setVisibility(View.GONE);
            }else{
                viewHolder.imgaddtoplaylist.setVisibility(View.VISIBLE);
                viewHolder.imglike.setVisibility(View.VISIBLE);
            }

            convertView.setTag(viewHolder);

        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        final Baihat baihat=getItem(position);
        Picasso.with(getContext()).load(baihat.getHinhBaiHat()).into(viewHolder.imghinhbaihat);
        viewHolder.txttenbaihat.setText(baihat.getTenBaiHat());
        viewHolder.txttencasi.setText(baihat.getIdCaSi());

        //UPDATE LUOT THICH
        final ViewHolder finalViewHolder = viewHolder;

        //kiểm tra nếu đã đăng nhập thì mới hiện nút addtoplaylist và nút
        if(PreferenceUtils.getUsername(getContext())!=null){
            viewHolder.imgaddtoplaylist.setVisibility(View.VISIBLE);
            viewHolder.imglike.setVisibility(View.VISIBLE);

            //init nút thích
            Dataservice dataservice=APIService.getService();
            Call<String> call=dataservice.PostKtYeuthichBaihat(baihat.getIdBaiHat().toString(),PreferenceUtils.getUsername(getContext()).toString());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String ketqua=response.body();
                    if(ketqua.equals("exist")){
                        //floatingActionLikeButton.setEnabled(false);
                        finalViewHolder.imglike.setImageResource(R.drawable.iconloved);
                    }else{
                        //floatingActionLikeButton.setEnabled(true);
                        finalViewHolder.imglike.setImageResource(R.drawable.iconlove);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("kt_likeplaylist","call fail");
                }
            });

            viewHolder.imglike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(baihat.getIdBaiHat().equals(""))
                    {
                        Log.d("LoiThich",baihat.getIdBaiHat());
                    }else {

                        if(finalViewHolder.imglike.getDrawable().getConstantState()==getContext().getResources().getDrawable(R.drawable.iconlove).getConstantState()){
                            Log.d("1", "1");
                            finalViewHolder.imglike.setImageResource(R.drawable.iconloved);
                            Dataservice dataservice= APIService.getService();
                            Call<String> callback=dataservice.PostBoThichvaIdCuaBaiHat("1",baihat.getIdBaiHat().toString(),PreferenceUtils.getUsername(getContext()));
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String ketqua=response.body();
                                    if(ketqua.equals("success")){
                                        Toast.makeText(getContext(),"Đã thích",Toast.LENGTH_SHORT).show();
                                    }else if(ketqua.equals("unlikesuccess")){
                                        Toast.makeText(getContext(),"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                    }else if(ketqua.equals("unlikefail")){
                                        Log.d("Bỏ thích", "thất bại");
                                    }else if(ketqua.equals("deletefail1")){
                                        Log.d("Bỏ thích", "xóa trước khi bỏ thích thất bại");
                                    }else if(ketqua.equals("deletefail2")){
                                        Log.d("Thích", "thêm trước khi thích thất bại");
                                    }else if(ketqua.equals("fail")){
                                        Log.d("Thích", "Thích thất bại");
                                    }else {
                                        Log.d("Thông tin thích", "Rỗng");
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d("Thông tin thích", "call error");
                                }
                            });
                        }else{
                            Log.d("2", "2");
                            finalViewHolder.imglike.setImageResource(R.drawable.iconlove);
                            Dataservice dataservice= APIService.getService();
                            Call<String> callback=dataservice.PostBoThichvaIdCuaBaiHat("1",baihat.getIdBaiHat().toString(),PreferenceUtils.getUsername(getContext()));
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String ketqua=response.body();
                                    if(ketqua.equals("success")){
                                        Toast.makeText(getContext(),"Đã thích",Toast.LENGTH_SHORT).show();
                                    }else if(ketqua.equals("unlikesuccess")){
                                        Toast.makeText(getContext(),"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                    }else if(ketqua.equals("unlikefail")){
                                        Log.d("Bỏ thích", "thất bại");
                                    }else if(ketqua.equals("deletefail1")){
                                        Log.d("Bỏ thích", "xóa trước khi bỏ thích thất bại");
                                    }else if(ketqua.equals("deletefail2")){
                                        Log.d("Thích", "thêm trước khi thích thất bại");
                                    }else if(ketqua.equals("fail")){
                                        Log.d("Thích", "Thích thất bại");
                                    }else {
                                        Log.d("Thông tin thích", "Rỗng");
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d("Thông tin thích", "call error");
                                }
                            });
                        }
                    }
                }
            });


            //sự kiện addtoplaylist click
            viewHolder.imgaddtoplaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //lưu id bài hát
                    PreferenceUtils.saveSong(baihat.getIdBaiHat(),getContext());

                    final AlertDialog alertDialog=new AlertDialog.Builder(getContext()).create();
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    View dialogView = inflater.inflate(R.layout.alert_dialog_addsongtoplaylist, null);

                    Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancelAddSongtoplaylist);
                    final RecyclerView rePlaylist= (RecyclerView) dialogView.findViewById(R.id.reAddsongtoplaylist);



                    //getdata
                    final Dataservice dataservice=APIService.getService();
                    Call<List<Playlist>> callback=dataservice.GetDanhSachPlaylistCuaNguoiDung(PreferenceUtils.getUsername(getContext()));
                    callback.enqueue(new Callback<List<Playlist>>() {
                        @Override
                        public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                            mangplaylist = (ArrayList<Playlist>) response.body();
                            adapter=new Alert_Dialog_AddsongtoplaylistAdapter(getContext(),mangplaylist);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            rePlaylist.setLayoutManager(linearLayoutManager);
                            rePlaylist.setAdapter(adapter);
                            Log.d("UserPlaylistActivity","Lấy được:"+String.valueOf(mangplaylist.size()));
                        }

                        @Override
                        public void onFailure(Call<List<Playlist>> call, Throwable t) {
                            Log.d("Top5baihatdcyeuthich","callback alert playlist fail");
                        }
                    });

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            PreferenceUtils.saveSong(null,getContext());
                        }
                    });


                    alertDialog.setView(dialogView);
                    alertDialog.show();
                }
            });
        }else{
            viewHolder.imgaddtoplaylist.setVisibility(View.GONE);
        }
        return convertView;
    }
}
