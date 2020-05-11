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
import com.example.mp3freeforyou.Activity.UserSingerActivity;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mp3freeforyou.Ultils.Constants.KEY_BANLIST_CASI;

public class UserCasiAdapter extends RecyclerView.Adapter<UserCasiAdapter.ViewHolder> {
    TextView btnLike,btnBlock;
    Handler handler=new Handler();

    final Context context;
    ArrayList<Casi> mangcasi;

    public UserCasiAdapter(Context context, ArrayList<Casi> mangcasi) {
        this.context = context;
        this.mangcasi = mangcasi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.row_usercasi,parent,false);
        return new UserCasiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Casi casi=mangcasi.get(position);
        holder.txtTenCasi.setText(casi.getTenCaSi());
        Picasso.with(context).load(casi.getHinhCaSi()).into(holder.imghinhcasi);
    }

    @Override
    public int getItemCount() {
        return mangcasi.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenCasi;
        ImageView imghinhcasi,btnUnlike,imgExplore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenCasi=itemView.findViewById(R.id.imgRowFragmentUserCasiTenCasi);
            imghinhcasi=itemView.findViewById(R.id.imgRowUserCasi);
            btnUnlike=itemView.findViewById(R.id.imgRowUserCasiDelbtn);
            imgExplore=itemView.findViewById(R.id.imgExplore);

            //sự kiện explore on click
            imgExplore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog=new AlertDialog.Builder(context).create();
                    LayoutInflater inflater = LayoutInflater.from(context);
                    View dialogView = inflater.inflate(R.layout.alert_dialog_explorer_casi, null);

                    btnLike=dialogView.findViewById(R.id.txtYeuthich);
                    btnBlock=dialogView.findViewById(R.id.txtBochan);

                    btnLike.setClickable(false);
                    btnBlock.setClickable(false);

                    //int chan status
                    //init name txtTenCaSi
                    if(PreferenceUtils.getUsername(context)!=null){
                        Dataservice dataservice=APIService.getService();
                        Call<String> checkbanstatus=dataservice.PostGetBanStatusForSinger(PreferenceUtils.getUsername(context),mangcasi.get(getLayoutPosition()).getIdCaSi());
                        checkbanstatus.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String kq=response.body();
                                if(kq.equals("yes")){
                                    String txtBtnBlock="  Bỏ chặn ca sĩ "+mangcasi.get(getLayoutPosition()).getTenCaSi();
                                    btnBlock.setText(txtBtnBlock);
                                    Log.d("init txtTenCaSi","yes");
                                }else if(kq.equals("no")){
                                    String txtBtnBlock="  Chặn ca sĩ "+mangcasi.get(getLayoutPosition()).getTenCaSi();
                                    btnBlock.setText(txtBtnBlock);
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

                            boolean contain= Arrays.asList(mangidbanlistcasi).contains(mangcasi.get(getLayoutPosition()).getIdCaSi());
                            if(contain){
                                //có thì hiện bỏ
                                String txtBtnBlock="  Bỏ chặn ca sĩ "+mangcasi.get(getLayoutPosition()).getTenCaSi();
                                btnBlock.setText(txtBtnBlock);
                            }else{
                                //thêm vô thôi bro
                                String txtBtnBlock="  Chặn ca sĩ "+mangcasi.get(getLayoutPosition()).getTenCaSi();
                                btnBlock.setText(txtBtnBlock);
                            }
                        }else{
                            //truong hop danh sach von da rong tu dau
                            //thêm vô thôi bro
                            String txtBtnBlock="  Chặn ca sĩ "+mangcasi.get(getLayoutPosition()).getTenCaSi();
                            btnBlock.setText(txtBtnBlock);
                        }
                    }

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnLike.setClickable(true);
                            btnBlock.setClickable(true);
                        }
                    },2000);

                    btnBlock.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(PreferenceUtils.getUsername(context)!=null){
                                //đã đăng nhập
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
                                            btnBlock.setText(name);
                                            Bochancasi(mangcasi.get(getLayoutPosition()));
                                            //Toast.makeText(context,"Đã có trong danh sách chặn",Toast.LENGTH_SHORT).show();
                                        }else{
                                            if(call1.equals("success")){
                                                String name="  Bỏ chặn ca sĩ "+mangcasi.get(getLayoutPosition()).getTenCaSi();
                                                btnBlock.setText(name);
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
                                        btnBlock.setText(name);
                                        Bochancasi(mangcasi.get(getLayoutPosition()));
                                        //Toast.makeText(context,"Đã có trong danh sách chặn",Toast.LENGTH_SHORT).show();
                                    }else{
                                        //thêm vô thôi bro
                                        String name="  Bỏ chặn ca sĩ "+mangcasi.get(getLayoutPosition()).getTenCaSi();
                                        btnBlock.setText(name);
                                        String themmoi=PreferenceUtils.getBanListIdCaSi(context)+","+mangcasi.get(getLayoutPosition()).getIdCaSi();
                                        PreferenceUtils.saveBanListIdCaSi(themmoi,context);
                                        Toast.makeText(context,"Đã chặn",Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    //truong hop danh sach von da rong tu dau
                                    //thêm vô thôi bro
                                    String name="  Bỏ chặn ca sĩ "+mangcasi.get(getLayoutPosition()).getTenCaSi();
                                    btnBlock.setText(name);
                                    String themmoi=mangcasi.get(getLayoutPosition()).getIdCaSi();
                                    PreferenceUtils.saveBanListIdCaSi(themmoi,context);
                                    Toast.makeText(context,"Đã chặn",Toast.LENGTH_SHORT).show();
                                }
                            }
                            KEY_BANLIST_CASI.clear();
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
                                                alertDialog.dismiss();
                                                Toast.makeText(context,"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ((UserSingerActivity)context).finish();
                                                        Intent intent=new Intent(context, UserSingerActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                        context.startActivity(intent);
                                                    }
                                                },3000);
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
                                                alertDialog.dismiss();
                                                Toast.makeText(context,"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ((UserSingerActivity)context).finish();
                                                        Intent intent=new Intent(context, UserSingerActivity.class);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                        context.startActivity(intent);
                                                    }
                                                },3000);
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

            //xóa ca si
            /*btnUnlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dataservice dataservice= APIService.getService();
                    Call<String> call=dataservice.PostDelInsertCasiyeuthich(mangcasi.get(getPosition()).getIdCaSi(), PreferenceUtils.getUsername(context));
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String kq=response.body();
                            if(kq.equals("deletesuccess")){
                                Toast.makeText(context,"Đã bỏ thích",Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ((UserSingerActivity)context).finish();
                                        Intent intent=new Intent(context, UserSingerActivity.class);
                                        intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                                        context.startActivity(intent);
                                    }
                                },3000);
                            }else {
                                Log.d("unlikecasi","something fail");
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("unlikecasi","call fail");
                        }
                    });
                }
            });*/

            //itemview onclick
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, DanhsachbaihatActivity.class);
                    intent.putExtra("itemcasi",mangcasi.get(getPosition()));
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
