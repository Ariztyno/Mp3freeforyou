package com.example.mp3freeforyou.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
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
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.mp3freeforyou.Ultils.Constants.KEY_ARRAYTHELOAI;
import static com.example.mp3freeforyou.Ultils.Constants.KEY_STRING_ARRAYTHELOAI;

public class Quiz_DSTheLoaiAdapter extends RecyclerView.Adapter<Quiz_DSTheLoaiAdapter.ViewHolder> {
    Context context;
    ArrayList<Theloaibaihat> mangtheloai;

    Handler handler=new Handler();
    Runnable runnable;

    public Quiz_DSTheLoaiAdapter(Context context, ArrayList<Theloaibaihat> mangtheloai) {
        this.context = context;
        this.mangtheloai = mangtheloai;
    }

    @NonNull
    @Override
    public Quiz_DSTheLoaiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_quiz_dstheloai,parent,false);

        return new Quiz_DSTheLoaiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Quiz_DSTheLoaiAdapter.ViewHolder holder, int position) {
        Theloaibaihat theloai=mangtheloai.get(position);
        Picasso.with(context).load(theloai.getHinhTheLoai()).placeholder(R.drawable.loading).into(holder.imgNormal);
        Picasso.with(context).load(R.drawable.ic_itemchecked).into(holder.imgChecked);
        holder.txtTenTheLoai.setText(theloai.getTenTheLoai());

        /*if(KEY_STRING_ARRAYTHELOAI.matches(".*\\d.*")){
            //nếu idtheloai đã được lưu trước đó thì khi load trạng thái của thể loại sẽ được chuyển thành checked
            String[] listidtheloaibaihat = KEY_STRING_ARRAYTHELOAI.split(",");
            for(String x: listidtheloaibaihat){
                if(x.equals(theloai.getIdTheLoai())){
                    holder.imgChecked.setVisibility(View.VISIBLE);
                    Log.d("isnull_","inside for - > if");
                }
                Log.d("isnull_","inside for");
            }
            Log.d("isnull_","true null");
        }*/
    }

    @Override
    public int getItemCount() {
        return mangtheloai.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgNormal,imgChecked;
        TextView txtTenTheLoai;

        public ViewHolder(View itemview){
            super(itemview);
            imgNormal=itemview.findViewById(R.id.img_Quiz_DSTheloai_Normal);
            imgChecked=itemview.findViewById(R.id.img_Quiz_DSTheloai_Checked);
            txtTenTheLoai=itemview.findViewById(R.id.txtRow_Quiz_DSTheloai_TenTheLoai);

            runnable= () -> {
                if(PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(context).matches(".*\\d.*")){
                    //nếu idtheloai đã được lưu trước đó thì khi load trạng thái của thể loại sẽ được chuyển thành checked
                    String[] listidtheloaibaihat = PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(context).split(",");
                    for(String x: listidtheloaibaihat){
                        if(x.equals(mangtheloai.get(getLayoutPosition()).getIdTheLoai())){
                            imgNormal.setAlpha(0.5f);
                            imgChecked.setVisibility(View.VISIBLE);
                            Log.d("isnull_theloai_if",""+mangtheloai.get(getLayoutPosition()).getIdTheLoai());

                            //init độ mờ của image view sau khi chọn
                            imgNormal.setAlpha(0.5f);
                            //hiển thị checked
                            imgChecked.setVisibility(View.VISIBLE);

                            //Nếu theloai này chưa có thì add vào
                            if(!KEY_ARRAYTHELOAI.contains(mangtheloai.get(getLayoutPosition()))){
                                //add item vào mảng thể loại
                                KEY_ARRAYTHELOAI.add(mangtheloai.get(getLayoutPosition()));
                            }
                            for(Theloaibaihat z: KEY_ARRAYTHELOAI){
                                Log.d("KEY_ARRAYTHELOAI",""+z.getIdTheLoai());
                            }
                        }
                        Log.d("isnull_theloai","inside for");
                    }
                    Log.d("isnull_theloai","true null");
                }

                //khi click vào item thì hinh hiển thị của item sẽ thay đổi sang có dấu tích
                itemview.setOnClickListener(v -> {
                    if(imgChecked.getVisibility()==View.GONE && imgNormal.getDrawable()!=null){
                        //init độ mờ của image view sau khi chọn
                        imgNormal.setAlpha(0.5f);
                        //hiển thị checked
                        imgChecked.setVisibility(View.VISIBLE);

                        //Nếu theloai này chưa có thì add vào
                        if(!KEY_ARRAYTHELOAI.contains(mangtheloai.get(getLayoutPosition()))){
                            //add item vào mảng thể loại
                            KEY_ARRAYTHELOAI.add(mangtheloai.get(getLayoutPosition()));
                        }
                        for(Theloaibaihat x: KEY_ARRAYTHELOAI){
                            Log.d("KEY_ARRAYTHELOAI",""+x.getIdTheLoai());
                        }

                    }else {
                        //init độ mờ của image view sau khi chọn
                        imgNormal.setAlpha(1f);
                        //hiển thị checked
                        imgChecked.setVisibility(View.GONE);

                        //Nếu theloai này đã có thì remove khỏi mảng
                        //remove item khỏi mảng thể loại
                        KEY_ARRAYTHELOAI.remove(mangtheloai.get(getLayoutPosition()));

                        for(Theloaibaihat x: KEY_ARRAYTHELOAI){
                            Log.d("KEY_ARRAYTHELOAI",""+x.getIdTheLoai());
                        }

                    }

                /*Intent intent=new Intent(context, DanhsachbaihatActivity.class);
                intent.putExtra("itemtheloai",mangtheloai.get(getPosition()));
                context.startActivity(intent);*/
                });

            };
            handler.postDelayed(runnable,1800);



        }
    }
}
