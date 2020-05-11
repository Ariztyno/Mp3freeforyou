package com.example.mp3freeforyou.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.QuanLyDSChanActivity;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mp3freeforyou.Ultils.Constants.KEY_BANLIST_CASI;

public class AlertDialogCasiAdapter extends RecyclerView.Adapter<AlertDialogCasiAdapter.ViewHolder> {
    Context context;
    ArrayList<Casi> mangcasi;

    public AlertDialogCasiAdapter(Context context, ArrayList<Casi> mangcasi) {
        this.context = context;
        this.mangcasi = mangcasi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_alert_dialog_block,parent,false);
        return new AlertDialogCasiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertDialogCasiAdapter.ViewHolder holder, int position) {
        Casi casi=mangcasi.get(position);

        //init name txtTenCaSi
        if(PreferenceUtils.getUsername(context)!=null){
            Dataservice dataservice=APIService.getService();
            Call<String> checkbanstatus=dataservice.PostGetBanStatusForSinger(PreferenceUtils.getUsername(context),casi.getIdCaSi());
            checkbanstatus.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String kq=response.body();
                    if(kq.equals("yes")){
                        String name="  Bỏ chặn ca sĩ "+casi.getTenCaSi();
                        holder.txtTenCasi.setText(name);
                        Log.d("init txtTenCaSi","yes");
                    }else if(kq.equals("no")){
                        String name="  Chặn ca sĩ "+casi.getTenCaSi();
                        holder.txtTenCasi.setText(name);
                        Log.d("init txtTenCaSi","yes");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("init txtTenCaSi",""+t);
                }
            });
        }else{
            if(PreferenceUtils.getBanListIdCaSi(context)!=null && !PreferenceUtils.getBanListIdCaSi(context).equals("")){
                //kiểm tra xem phần tử sắp thêm vào có trong chuỗi banlist hay chưa
                String[] mangidbanlistcasi = PreferenceUtils.getBanListIdCaSi(context).split(",");

                boolean contain= Arrays.asList(mangidbanlistcasi).contains(casi.getIdCaSi());
                if(contain){
                    //có thì hiện bỏ
                    String name="  Bỏ chặn ca sĩ "+casi.getTenCaSi();
                    holder.txtTenCasi.setText(name);
                }else{
                    //thêm vô thôi bro
                    String name="  Chặn ca sĩ "+casi.getTenCaSi();
                    holder.txtTenCasi.setText(name);
                }
            }else{
                //truong hop danh sach von da rong tu dau
                //thêm vô thôi bro
                String name="  Chặn ca sĩ "+casi.getTenCaSi();
                holder.txtTenCasi.setText(name);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mangcasi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTenCasi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenCasi=itemView.findViewById(R.id.txtTencasi);

            txtTenCasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(PreferenceUtils.getUsername(context)!=null){
                        //đã đăng nhập
                        //sử dụng shared reference để lưu ban list id bài hát
                        //kiểm tra nếu dsbanlist null or ko
                        if(!KEY_BANLIST_CASI.contains(mangcasi.get(getLayoutPosition()))){
                            KEY_BANLIST_CASI.add(mangcasi.get(getLayoutPosition()));
                        }
                        PreferenceUtils.saveBanListIdCaSi(KEY_BANLIST_CASI,context);

                        Dataservice dataservice1= APIService.getService();
                        Call<String> call1=dataservice1.postaddidcasitobanlist(PreferenceUtils.getBanListIdCaSi(context),PreferenceUtils.getUsername(context));
                        call1.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String call1=response.body();
                                if(call1.equals("NOTCHANGE")){
                                    String name="  Chặn ca sĩ "+mangcasi.get(getLayoutPosition()).getTenCaSi();
                                    txtTenCasi.setText(name);
                                    Bochancasi(mangcasi.get(getLayoutPosition()));
                                    //Toast.makeText(context,"Đã có trong danh sách chặn",Toast.LENGTH_SHORT).show();
                                }else{
                                    if(call1.equals("success")){
                                        String name="  Bỏ chặn ca sĩ "+mangcasi.get(getLayoutPosition()).getTenCaSi();
                                        txtTenCasi.setText(name);
                                        Toast.makeText(context,"Đã chặn",Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(context,"Có lỗi xảy ra",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(context,""+t,Toast.LENGTH_SHORT).show();
                            }
                        });
                        //Toast.makeText(context,"Đã chặn",Toast.LENGTH_SHORT).show();
                    }else {
                        //chưa đăng nhập
                        //sử dụng shared reference để lưu ban list id bài hát

                        if(PreferenceUtils.getBanListIdCaSi(context)!=null && !PreferenceUtils.getBanListIdCaSi(context).equals("")){
                            //kiểm tra xem phần tử sắp thêm vào có trong chuỗi banlist hay chưa
                            String[] mangidbanlistcasi = PreferenceUtils.getBanListIdCaSi(context).split(",");

                            boolean contain= Arrays.asList(mangidbanlistcasi).contains(mangcasi.get(getLayoutPosition()).getIdCaSi());
                            if(contain){
                                //có thì ko thêm và báo đã có + hiện dialog mới
                                String name="  Chặn ca sĩ "+mangcasi.get(getLayoutPosition()).getTenCaSi();
                                txtTenCasi.setText(name);
                                Bochancasi(mangcasi.get(getLayoutPosition()));
                                //Toast.makeText(context,"Đã có trong danh sách chặn",Toast.LENGTH_SHORT).show();
                            }else{
                                //thêm vô thôi bro
                                String name="  Bỏ chặn ca sĩ "+mangcasi.get(getLayoutPosition()).getTenCaSi();
                                txtTenCasi.setText(name);
                                String themmoi=PreferenceUtils.getBanListIdCaSi(context)+","+mangcasi.get(getLayoutPosition()).getIdCaSi();
                                PreferenceUtils.saveBanListIdCaSi(themmoi,context);
                                Toast.makeText(context,"Đã chặn",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            //truong hop danh sach von da rong tu dau
                            //thêm vô thôi bro
                            String name="  Bỏ chặn ca sĩ "+mangcasi.get(getLayoutPosition()).getTenCaSi();
                            txtTenCasi.setText(name);
                            String themmoi=mangcasi.get(getLayoutPosition()).getIdCaSi();
                            PreferenceUtils.saveBanListIdCaSi(themmoi,context);
                            Toast.makeText(context,"Đã chặn",Toast.LENGTH_SHORT).show();
                        }
                    }
                    KEY_BANLIST_CASI.clear();
                }
            });
        }
    }

    private void Bochancasi(Casi casi) {
        if (PreferenceUtils.getUsername(context) != null) {
            Dataservice dataservice = APIService.getService();
            Call<String> call = dataservice.postBoChan_Casi(PreferenceUtils.getUsername(context), casi.getIdCaSi());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String kq = response.body();
                    if (kq.equals("success")) {
                        Toast.makeText(context, "Đã bỏ chặn", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(context, "Lỗi:" + t, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //chưa đăng nhập
            String newidlist = "";
            //sử dụng shared reference để lưu ban list id CA SĨ
            String[] mangidbanlistcasi = PreferenceUtils.getBanListIdCaSi(context).split(",");
            for (int i = 0; i < mangidbanlistcasi.length; i++) {
                if (mangidbanlistcasi[i].equals(casi.getIdCaSi())) {
                    //ko thêm vô chuỗi mới
                } else {
                    if (i == mangidbanlistcasi.length - 1) {
                        newidlist += mangidbanlistcasi[i];
                    } else {
                        newidlist += mangidbanlistcasi[i];
                        newidlist += ",";
                    }
                }
            }

            PreferenceUtils.saveBanListIdCaSi(newidlist, context);
            Toast.makeText(context, "Đã bỏ chặn", Toast.LENGTH_SHORT).show();
        }
    }
}

