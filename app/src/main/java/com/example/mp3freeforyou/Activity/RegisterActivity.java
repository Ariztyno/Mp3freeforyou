package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.example.mp3freeforyou.Ultils.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mp3freeforyou.Ultils.Constants.KEY_BANLIST_BAIHAT;

public class RegisterActivity extends AppCompatActivity {
    EditText txtUserName,txtPassword,txtRePassword,txtEmail,txtName;
    TextView txtThongbao,txt1;
    CheckBox cbSaoluudulieunghe;

    Button btnDangky;
    Toolbar tbRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Anhxa();
        init();
        btnDangkyonclick();
    }

    private void init() {
        setSupportActionBar(tbRegister);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đăng ký tài khoản");

        tbRegister.setTitleTextColor(Color.parseColor("#ff39aa"));
        tbRegister.setTitle("Đăng ký tài khoản");
        tbRegister.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void btnDangkyonclick() {

        btnDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUserName.getText().toString();
                String pass=txtPassword.getText().toString();
                String repass=txtRePassword.getText().toString();
                String hoten=txtName.getText().toString();
                String email=txtEmail.getText().toString();
                Log.d("Register",username+"_"+pass+"_"+repass+"_"+email+"_"+hoten);
                //KT
                if(username.equals("") || pass.equals("") || repass.equals("") || email.equals("") || hoten.equals(""))
                {
                    txtThongbao.setText("Không được để trống dòng");
                    txtThongbao.setVisibility(View.VISIBLE);
                }else {
                    if(pass.equals(repass)){
                        Dataservice dataservice= APIService.getService();
                        if(cbSaoluudulieunghe.isChecked()){
                            Call<String> callback=dataservice.PostTaoHoSoNguoiDung(username, StringUtils.unAccent(pass),email,hoten);
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String ketqua=response.body();
                                    if(ketqua.equals("success")){
                                        SaveListenDataToDB_For_Register(username);
                                        Toast.makeText(getApplicationContext(),"Đăng ký thành công",Toast.LENGTH_LONG).show();
                                        finish();
                                        Log.d("Register","Đăng ký thành công");
                                    }else if(ketqua.equals("Tên đăng nhập đã tồn tại")){
                                        txtThongbao.setText("Tên đăng nhập đã tồn tại");
                                        txtThongbao.setVisibility(View.VISIBLE);
                                    }else if(ketqua.equals("email")){
                                        txtThongbao.setText("Email tồn tại");
                                        txtThongbao.setVisibility(View.VISIBLE);
                                    }else {
                                        Log.d("Register","Đăng ký ko thành công");
                                        Toast.makeText(getApplicationContext(),"Đăng ký không thành công",Toast.LENGTH_LONG).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d("Register","Đăng ký bị lỗi callback");
                                }
                            });
                        }else{
                            Call<String> callback=dataservice.PostTaoHoSoNguoiDung(username,StringUtils.unAccent(pass),email,hoten);
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String ketqua=response.body();
                                    if(ketqua.equals("success")){
                                        Toast.makeText(getApplicationContext(),"Đăng ký thành công",Toast.LENGTH_LONG).show();
                                        finish();
                                        Log.d("Register","Đăng ký thành công");
                                    }else if(ketqua.equals("Tên đăng nhập đã tồn tại")){
                                        txtThongbao.setText("Tên đăng nhập đã tồn tại");
                                        txtThongbao.setVisibility(View.VISIBLE);
                                    }else if(ketqua.equals("email")){
                                        txtThongbao.setText("Email tồn tại");
                                        txtThongbao.setVisibility(View.VISIBLE);
                                    }else {
                                        Log.d("Register","Đăng ký ko thành công");
                                        Toast.makeText(getApplicationContext(),"Đăng ký không thành công",Toast.LENGTH_LONG).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d("Register","Đăng ký bị lỗi callback");
                                }
                            });
                        }
                    }else {
                        txtThongbao.setText("Mật khẩu nhập lại không trùng");
                        txtThongbao.setVisibility(View.VISIBLE);
                    }
                }
                //KT end
            }
        });
    }

    private void Anhxa() {
        txtUserName=findViewById(R.id.edtUserNameRegister);
        txtPassword=findViewById(R.id.edtPasswordRegister);
        txtRePassword=findViewById(R.id.edtRePasswordRegister);
        txtEmail=findViewById(R.id.edtEmailRegister);
        txtName=findViewById(R.id.edtNameRegister);
        txtThongbao=findViewById(R.id.txtThongBao);

        cbSaoluudulieunghe=findViewById(R.id.cbSaoluudata);

        tbRegister=findViewById(R.id.tbRegister);

        btnDangky=findViewById(R.id.btnRegister);
    }

    private void SaveListenDataToDB_For_Register(String username){
        //check các thông số sở thích và ban list nếu chúng null thì sẽ gán thành ""
        if(PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()) == null){
            PreferenceUtils.saveListIdCasifromQuizChoice("",getApplicationContext());
        }
        if(PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getApplicationContext()) == null){
            PreferenceUtils.saveListIdTheloaibaihatfromQuizChoice("",getApplicationContext());
        }
        if(PreferenceUtils.getBanListIdBaihat(getApplicationContext()) == null){
            PreferenceUtils.saveBanListIdBaihat("",getApplicationContext());
        }
        if(PreferenceUtils.getBanListIdCaSi(getApplicationContext()) == null){
            PreferenceUtils.saveBanListIdCaSi("",getApplicationContext());
        }
        if(PreferenceUtils.getSearchHistory(getApplicationContext())==null){
            PreferenceUtils.saveSearchHistory("",getApplicationContext());
        }

        Dataservice dataservice1=APIService.getService();
        Call<String> call1=dataservice1.postbanlistandquizlist_dangky(PreferenceUtils.getSearchHistory(getApplicationContext()),PreferenceUtils.getBanListIdCaSi(getApplicationContext()),PreferenceUtils.getBanListIdBaihat(getApplicationContext()),PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()),PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getApplicationContext()),username);
        call1.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq=response.body();
                if(kq.equals("success")){
                    Log.d("Register","Thành công bind sở thích & quiz");
                }else{
                    Log.d("Register","Thất bại bind sở thích & quiz");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Register","Thất bại: "+t);
            }
        });
    }
}
