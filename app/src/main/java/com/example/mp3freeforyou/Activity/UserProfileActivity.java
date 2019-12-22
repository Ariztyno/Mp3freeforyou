package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class UserProfileActivity extends AppCompatActivity {

    TextView txtThongbao,txtThongbaohoso,txtName;
    CardView cvDoimatkhau,cvDoihoso;
    EditText edtNewpass,edtRenewpass,edtNewname,edtNewemail,edtNewavatar;
    ImageView imageView;
    Button btnDoimatkhau,btnDoihoso,btnXacnhanDoimatkhau,btnXacnhanDoihoso,btnDoiavatar;
    CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        anhxa();
        init();
        ChangeAvatarevent();
        ChangeProfileEvent(); //not use
        Changepasswordevent();
        Xacnhandoimatkhauevent();
        Xacnhandoihosoevent();
    }

    private void ChangeAvatarevent() {
        btnDoiavatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void Xacnhandoihosoevent() {
        btnXacnhanDoihoso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtNewname.equals("") || edtNewemail.equals("")){
                    txtThongbaohoso.setText("Không được bỏ trống");
                    txtThongbaohoso.setVisibility(View.VISIBLE);
                }else {
                    Dataservice dataservice = APIService.getService();
                    Call<String> calback=dataservice.PostUpdateHosoNguoidung(PreferenceUtils.getUsername(getApplicationContext()),edtNewemail.getText().toString(),edtNewname.getText().toString());
                    calback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String kq=response.body();
                            if(kq.equals("success")){
                                //cập nhật lại
                                txtName.setText(edtNewname.getText().toString());
                                PreferenceUtils.saveName(edtNewname.getText().toString(),getApplicationContext());
                                PreferenceUtils.saveEmail(edtNewemail.getText().toString(),getApplicationContext());

                                Toast.makeText(getApplicationContext(),"Thành công cập nhật hồ sơ",Toast.LENGTH_LONG).show();
                                Log.d("Doihoso","Thành công");

                                /*Intent intent=new Intent(UserProfileActivity.this,MainActivity.class);
                                startActivity(intent);*/
                                Intent intent = getIntent();
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                finish();
                                startActivity(intent);
                            }else if(kq.equals("fail")){
                                Toast.makeText(getApplicationContext(),"Cập nhật thất bại",Toast.LENGTH_LONG).show();
                                Log.d("Doihoso","Thất bại");
                            }else{
                                txtThongbaohoso.setText("Có lỗi xảy ra!");
                                txtThongbaohoso.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("Doihoso","Callback bị lỗi");
                        }
                    });
                }
            }
        });
    }

    private void Xacnhandoimatkhauevent() {
        btnXacnhanDoimatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtNewpass.getText().equals("")|| edtRenewpass.getText().equals("")){
                    txtThongbao.setText("Không được để trống");
                    txtThongbao.setVisibility(View.VISIBLE);
                }else {
                    if(edtNewpass.getText().toString().equals(edtRenewpass.getText().toString())){
                        Dataservice dataservice= APIService.getService();
                        Call<String> callback=dataservice.PostUpdateMatKhauNguoidung(PreferenceUtils.getUsername(getApplicationContext()),edtNewpass.getText().toString());
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String ketqua=response.body();
                                if(ketqua.equals("success")){
                                    Log.d("Doimatkhau","Thành công!");
                                    Toast.makeText(getApplicationContext(),"Đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();
                                    Intent intent = getIntent();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    finish();
                                }else if(ketqua.equals("fail")){
                                    txtThongbao.setText("Đổi mật khẩu thất bại");
                                    txtThongbao.setVisibility(View.GONE);
                                    Log.d("Doimatkhau","Thất bại");
                                }else {
                                    Toast.makeText(getApplicationContext(),"Có lỗi xảy ra",Toast.LENGTH_SHORT).show();
                                    Log.d("Doimatkhau","Tài khoản bạn yêu cầu không tồn tại!"+PreferenceUtils.getUsername(getApplicationContext()));
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("Doimatkhau","Lỗi callback");
                            }
                        });
                    }else {
                        txtThongbao.setText("Hai mật khẩu không trùng!");
                        txtThongbao.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }



    private void init() {
        cvDoihoso.setVisibility(View.GONE);
        cvDoimatkhau.setVisibility(View.GONE);
        txtThongbao.setVisibility(View.GONE);
        txtThongbaohoso.setVisibility(View.GONE);
        edtNewname.setText(PreferenceUtils.getName(getApplicationContext()));
        edtNewemail.setText(PreferenceUtils.getEmail(getApplicationContext()));

        txtName.setText(PreferenceUtils.getName(getApplicationContext()));
        Picasso.with(getApplicationContext()).load(PreferenceUtils.getAvatar(getApplicationContext())).into(circleImageView);
    }

    private void Changepasswordevent() {
        btnDoimatkhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cvDoihoso.getVisibility()==View.VISIBLE){
                    cvDoihoso.setVisibility(View.GONE);
                    cvDoimatkhau.setVisibility(View.VISIBLE);
                }else {
                    if(cvDoimatkhau.getVisibility()==View.GONE){
                        cvDoimatkhau.setVisibility(View.VISIBLE);
                    }else {
                        cvDoimatkhau.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void ChangeProfileEvent() {
        btnDoihoso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cvDoimatkhau.getVisibility()==View.VISIBLE){
                    cvDoimatkhau.setVisibility(View.GONE);
                    cvDoihoso.setVisibility(View.VISIBLE);
                }else {
                    if(cvDoihoso.getVisibility()==View.GONE){
                        cvDoihoso.setVisibility(View.VISIBLE);
                    }else {
                        cvDoihoso.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void anhxa() {
        //cardview
        cvDoimatkhau=findViewById(R.id.cvDoimatkhau);
        cvDoihoso=findViewById(R.id.cvDoihoso);

        //btn
        btnDoimatkhau=findViewById(R.id.btnDoimatkhau);
        btnDoihoso=findViewById(R.id.btnDoihoso);
        btnDoiavatar=findViewById(R.id.btnDoiavatar);
        btnXacnhanDoimatkhau=findViewById(R.id.btnXacnhandoimatkhau);
        btnXacnhanDoihoso=findViewById(R.id.btnXacnhandoihoso);

        //edt
        edtNewpass=findViewById(R.id.edtNewpass);
        edtRenewpass=findViewById(R.id.edtReNewpass);
        edtNewname=findViewById(R.id.edtNewName);
        edtNewemail=findViewById(R.id.edtNewEmail);
        edtNewavatar=findViewById(R.id.edtNewAvatar);

        //txt
        txtThongbao=findViewById(R.id.txtThongBaoDoimatkhau);
        txtThongbaohoso=findViewById(R.id.txtThongBaoDoihoso);
        txtName=findViewById(R.id.txtUserProfile_Name);

        //img
        circleImageView=findViewById(R.id.imgUserProfile_Avatar);
    }
}
