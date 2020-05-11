package com.example.mp3freeforyou.Adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.Model.Theloaibaihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.mp3freeforyou.Ultils.Constants.KEY_ARRAYCASI;
import static com.example.mp3freeforyou.Ultils.Constants.KEY_STRING_ARRAYCASI;
import static com.example.mp3freeforyou.Ultils.Constants.KEY_STRING_ARRAYTHELOAI;

public class Quiz_DSCasiAdapter extends RecyclerView.Adapter<Quiz_DSCasiAdapter.ViewHolder> {
    Context context;
    ArrayList<Casi> mangcasi;

    Handler handler=new Handler();
    Runnable runnable;

    public Quiz_DSCasiAdapter(Context context, ArrayList<Casi> mangcasi) {
        this.context = context;
        this.mangcasi = mangcasi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_quiz_dscasi,parent,false);

        return new Quiz_DSCasiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Casi casi=mangcasi.get(position);
        Picasso.with(context).load(casi.getHinhCaSi()).placeholder(R.drawable.loading).into(holder.imgNormal);
        Picasso.with(context).load(R.drawable.ic_itemchecked).into(holder.imgChecked);
        holder.txtTenCasi.setText(casi.getTenCaSi());

        /*if(KEY_STRING_ARRAYCASI.matches(".*\\d.*")){
            //nếu idtheloai đã được lưu trước đó thì khi load trạng thái của thể loại sẽ được chuyển thành checked
            String[] listidcasi = KEY_STRING_ARRAYCASI.split(",");
            for(String x: listidcasi){
                if(x.equals(casi.getIdCaSi())){
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
        return mangcasi.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgNormal,imgChecked;
        TextView txtTenCasi;

        public ViewHolder(@NonNull View itemview) {
            super(itemview);
            imgNormal=itemview.findViewById(R.id.img_Quiz_DSCasi_Normal);
            imgChecked=itemview.findViewById(R.id.img_Quiz_DSCasi_Checked);
            txtTenCasi=itemview.findViewById(R.id.txtRow_Quiz_DSCasi_TenCasi);

            runnable= () -> {
                if(PreferenceUtils.getListIdCasifromQuizChoice(context).matches(".*\\d.*")){
                    //nếu idtheloai đã được lưu trước đó thì khi load trạng thái của thể loại sẽ được chuyển thành checked
                    String[] listidcasi = PreferenceUtils.getListIdCasifromQuizChoice(context).split(",");
                    for(String x: listidcasi){
                        if(x.equals(mangcasi.get(getLayoutPosition()).getIdCaSi())){
                            imgNormal.setAlpha(0.5f);
                            imgChecked.setVisibility(View.VISIBLE);
                            Log.d("isnull_casi_if",""+mangcasi.get(getLayoutPosition()).getIdCaSi());

                            //Nếu casi này chưa có thì add vào mảng lưu tạm
                            if(!KEY_ARRAYCASI.contains(mangcasi.get(getLayoutPosition()))){
                                //add item vào mảng thể loại
                                KEY_ARRAYCASI.add(mangcasi.get(getLayoutPosition()));
                            }
                            for(Casi z: KEY_ARRAYCASI){
                                Log.d("KEY_ARRAYCASI",""+z.getIdCaSi());
                            }
                        }
                        Log.d("isnull_casi","inside for");
                    }
                    Log.d("isnull_casi","true null");
                }


                //khi click vào item thì hinh hiển thị của item sẽ thay đổi sang có dấu tích
                itemview.setOnClickListener(v -> {
                    if(imgChecked.getVisibility()==View.GONE && imgNormal.getDrawable()!=null){
                        //init độ mờ của image view sau khi chọn
                        imgNormal.setAlpha(0.5f);
                        //hiển thị checked
                        imgChecked.setVisibility(View.VISIBLE);

                        //Nếu casi này chưa có thì add vào
                        if(!KEY_ARRAYCASI.contains(mangcasi.get(getLayoutPosition()))){
                            //add item vào mảng thể loại
                            KEY_ARRAYCASI.add(mangcasi.get(getLayoutPosition()));
                        }
                        for(Casi x: KEY_ARRAYCASI){
                            Log.d("KEY_ARRAYCASI",""+x.getIdCaSi());
                        }

                    }else {
                        //init độ mờ của image view sau khi chọn
                        imgNormal.setAlpha(1f);
                        //hiển thị checked
                        imgChecked.setVisibility(View.GONE);

                        //Nếu casi này đã có thì remove khỏi mảng
                        //remove item khỏi mảng thể loại
                        KEY_ARRAYCASI.remove(mangcasi.get(getLayoutPosition()));

                        for(Casi x: KEY_ARRAYCASI){
                            Log.d("KEY_ARRAYCASI",""+x.getIdCaSi());
                        }

                    }

                });
            };
            handler.postDelayed(runnable,1800);

        }
    }
}
