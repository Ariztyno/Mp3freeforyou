package com.example.mp3freeforyou.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mp3freeforyou.Activity.DanhsachbaihatActivity;
import com.example.mp3freeforyou.Activity.QuanLyDSChanActivity;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mp3freeforyou.Ultils.Constants.KEY_BANLIST_CASI;

public class DSCasiChanAdapter extends RecyclerView.Adapter<DSCasiChanAdapter.ViewHolder> {
    TextView btnLike,btnUnBlock;
    Handler handler=new Handler();
    Context context;
    ArrayList<Casi> mangcasi;
    public DSCasiChanAdapter(Context context, ArrayList<Casi> mangbaihat) {
        this.context = context;
        this.mangcasi = mangbaihat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_ds_chan_casi,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Casi casi=mangcasi.get(position);
        holder.txtTenCasi.setText(casi.getTenCaSi());
        holder.txtThongtinCasi.setText(casi.getThongTin());
        Picasso.with(context).load(casi.getHinhCaSi()).into(holder.imgCasi);
    }

    @Override
    public int getItemCount() {
        return mangcasi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenCasi,txtThongtinCasi;
        ImageView imgCasi,imgexplore;
        public ViewHolder(final View itemview) {
            super(itemview);
            txtTenCasi=itemView.findViewById(R.id.txtRowFragmenTimkiemCasiTenCaSi);
            txtThongtinCasi=itemView.findViewById(R.id.txtRowFragmenTimkiemCasiThongtinCaSi);
            imgCasi=itemView.findViewById(R.id.imgRowFragmentTimKiemCasi);
            imgexplore=itemView.findViewById(R.id.imgRowFragmentTimKiemCasiExplore);

            imgexplore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog=new AlertDialog.Builder(context).create();
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.alert_dialog_explorer_casi, null);

                    btnLike=dialogView.findViewById(R.id.txtYeuthich);
                    btnUnBlock=dialogView.findViewById(R.id.txtBochan);

                    String txtBtnBlock="  Bỏ chặn ca sĩ "+mangcasi.get(getLayoutPosition()).getTenCaSi();
                    btnUnBlock.setText(txtBtnBlock);

                    btnLike.setClickable(false);
                    btnUnBlock.setClickable(false);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnLike.setClickable(true);
                            btnUnBlock.setClickable(true);
                        }
                    },2000);

                    btnUnBlock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(PreferenceUtils.getUsername(context)!=null){
                                Dataservice dataservice=APIService.getService();
                                Call<String> call=dataservice.postBoChan_Casi(PreferenceUtils.getUsername(context),mangcasi.get(getLayoutPosition()).getIdCaSi());
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        String kq=response.body();
                                        if(kq.equals("success")){
                                            Toast.makeText(context,"Đã bỏ chặn",Toast.LENGTH_SHORT).show();

                                            alertDialog.dismiss();

                                            //load lại trang khi bỏ thích
                                            ((QuanLyDSChanActivity)context).finish();
                                            Intent intent=new Intent(context, QuanLyDSChanActivity.class);
                                            String text=PreferenceUtils.getUsername(context)+"1";
                                            intent.putExtra("baihatyeuthich",text);
                                            intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            context.startActivity(intent);
                                        }else{
                                            Toast.makeText(context,"Có lỗi xảy ra",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Toast.makeText(context,"Lỗi:"+t,Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                //chưa đăng nhập
                                String newidlist="";
                                //sử dụng shared reference để lưu ban list id bài hát
                                String[] mangidbanlistcasi = PreferenceUtils.getBanListIdCaSi(context).split(",");
                                for (int i = 0; i < mangidbanlistcasi.length; i++) {
                                    if(mangidbanlistcasi[i].equals(mangcasi.get(getLayoutPosition()).getIdCaSi())){
                                        //ko thêm vô chuỗi mới
                                    }else{
                                        if(i==mangidbanlistcasi.length-1){
                                            newidlist += mangidbanlistcasi[i];
                                        }else{
                                            newidlist += mangidbanlistcasi[i];
                                            newidlist += ",";
                                        }
                                    }
                                }

                                PreferenceUtils.saveBanListIdCaSi(newidlist,context);
                                Toast.makeText(context,"Đã bỏ chặn",Toast.LENGTH_SHORT).show();

                                alertDialog.dismiss();

                                //load lại trang khi bỏ thích
                                ((QuanLyDSChanActivity)context).finish();
                                Intent intent=new Intent(context, QuanLyDSChanActivity.class);
                                String text=PreferenceUtils.getUsername(context)+"1";
                                //intent.putExtra("baihatyeuthich",text);
                                intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                                context.startActivity(intent);
                            }
                        }
                    });

                    if(PreferenceUtils.getUsername(context)!=null){
                        //INIT LƯỢT THÍCH
                        Dataservice dataservice= APIService.getService();
                        Call<String> callkt=dataservice.PostKtCasiyeuthich(mangcasi.get(getPosition()).getIdCaSi(), PreferenceUtils.getUsername(context));
                        callkt.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String kq=response.body();
                                if(kq.equals("exist")){
                                    //floatingActionLikeButton.setEnabled(false);
                                    btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconloved_20,0,0,0);
                                    btnLike.setText("  Bỏ thích");
                                }else{
                                    //floatingActionLikeButton.setEnabled(true);
                                    btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconlove_20,0,0,0);
                                    btnLike.setText("  Yêu thích");
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });

                        //onclick luotthich
                        btnLike.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(btnLike.getResources().getDrawable(R.drawable.iconlove_20).getConstantState()==context.getResources().getDrawable(R.drawable.iconlove_20).getConstantState()){
                                    Log.d("1", "1");

                                    Dataservice dataservice= APIService.getService();
                                    Call<String> call=dataservice.PostDelInsertCasiyeuthich(mangcasi.get(getPosition()).getIdCaSi(), PreferenceUtils.getUsername(context));
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String kq=response.body();
                                            if(kq.equals("deletesuccess")){
                                                btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconlove_20,0,0,0);
                                                btnLike.setText("  Yêu thích");
                                                Toast.makeText(context,"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                            }else if (kq.equals("deletefail")){
                                                Log.d("unlikecasi1","unlike fail");
                                            }else if (kq.equals("addsuccess")){
                                                btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconloved_20,0,0,0);
                                                btnLike.setText("  Bỏ thích");
                                                Toast.makeText(context,"Đã thích",Toast.LENGTH_SHORT).show();
                                                Log.d("unlikecasi1","like success");
                                            }else if (kq.equals("addfail")){
                                                Log.d("unlikecasi1","like fail");
                                            }else {
                                                Log.d("unlikecasi1","emty sent");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Log.d("unlikecasi1","call fail");
                                        }
                                    });
                                }else {
                                    Log.d("2", "2");
                                    Dataservice dataservice= APIService.getService();
                                    Call<String> call=dataservice.PostDelInsertCasiyeuthich(mangcasi.get(getPosition()).getIdCaSi(), PreferenceUtils.getUsername(context));
                                    call.enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            String kq=response.body();
                                            if(kq.equals("deletesuccess")){
                                                btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconlove_20,0,0,0);
                                                btnLike.setText("  Yêu thích");
                                                Toast.makeText(context,"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                            }else if (kq.equals("deletefail")){
                                                Log.d("unlikecasi1","unlike fail");
                                            }else if (kq.equals("addsuccess")){
                                                btnLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.iconloved_20,0,0,0);
                                                btnLike.setText("  Bỏ thích");
                                                Toast.makeText(context,"Đã thích",Toast.LENGTH_SHORT).show();
                                                Log.d("unlikecasi1","like success");
                                            }else if (kq.equals("addfail")){
                                                Log.d("unlikecasi1","like fail");
                                            }else {
                                                Log.d("unlikecasi1","emty sent");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                            Log.d("unlikecasi1","call fail");
                                        }
                                    });
                                }
                            }
                        });
                    }else{
                        btnLike.setVisibility(View.GONE);
                        btnLike.setClickable(false);
                    }

                    alertDialog.setView(dialogView);
                    alertDialog.show();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position=getAdapterPosition();
                    Intent intent=new Intent(context, DanhsachbaihatActivity.class);
                    intent.putExtra("itemcasi",mangcasi.get(position));
                    context.startActivity(intent);
                }
            });
        }
    }

    private void AlertDialogAlreadyblocked_Casi(Casi casi){
        final AlertDialog alertDialog=new AlertDialog.Builder(context).create();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.alert_dialog_already_ban, null);

        TextView txtTitle,btnUnBlock,btnGoToBlockManage;

        txtTitle=dialogView.findViewById(R.id.txtTitleBan);
        btnUnBlock=dialogView.findViewById(R.id.txtBochan);
        btnGoToBlockManage=dialogView.findViewById(R.id.txtQLchan);

        //init
        String title="Ca sĩ "+casi.getTenCaSi()+" đã bị chặn từ trước";
        String bochan="  Bỏ chặn ca sĩ "+casi.getTenCaSi();
        txtTitle.setText(title);
        btnUnBlock.setText(bochan);

        //sự kiện onclick
        btnUnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PreferenceUtils.getUsername(context)!=null){
                    Dataservice dataservice=APIService.getService();
                    Call<String> call=dataservice.postBoChan_Casi(PreferenceUtils.getUsername(context),casi.getIdCaSi());
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String kq=response.body();
                            if(kq.equals("success")){
                                Toast.makeText(context,"Đã bỏ chặn",Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }else{
                                Toast.makeText(context,"Có lỗi xảy ra",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(context,"Lỗi:"+t,Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    //chưa đăng nhập
                    String newidlist="";
                    //sử dụng shared reference để lưu ban list id bài hát
                    String[] mangidbanlistcasi = PreferenceUtils.getBanListIdCaSi(context).split(",");
                    for (int i = 0; i < mangidbanlistcasi.length; i++) {
                        if(mangidbanlistcasi[i].equals(casi.getIdCaSi())){
                            //ko thêm vô chuỗi mới
                        }else{
                            if(i==mangidbanlistcasi.length-1){
                                newidlist += mangidbanlistcasi[i];
                            }else{
                                newidlist += mangidbanlistcasi[i];
                                newidlist += ",";
                            }
                        }
                    }

                    PreferenceUtils.saveBanListIdCaSi(newidlist,context);
                    Toast.makeText(context,"Đã bỏ chặn",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnGoToBlockManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent= new Intent(context, QuanLyDSChanActivity.class);
                context.startActivity(intent);
            }
        });
        //sự kiện onclick end


        alertDialog.setView(dialogView);
        alertDialog.show();
    }
}
