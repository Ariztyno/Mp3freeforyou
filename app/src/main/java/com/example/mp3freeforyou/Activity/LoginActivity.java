package com.example.mp3freeforyou.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mp3freeforyou.Fragment.Fragment_UserControlBoard;
import com.example.mp3freeforyou.Model.Album;
import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.Model.Chudebaihat;
import com.example.mp3freeforyou.Model.Hosonguoidung;
import com.example.mp3freeforyou.Model.Nguoidung;
import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.Model.Quangcao;
import com.example.mp3freeforyou.Model.SearchAll;
import com.example.mp3freeforyou.Model.Theloaibaihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.example.mp3freeforyou.Ultils.StringUtils;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mp3freeforyou.Ultils.Constants.KEY_ARRAYCASI;
import static com.example.mp3freeforyou.Ultils.Constants.KEY_ARRAYTHELOAI;
import static com.example.mp3freeforyou.Ultils.Constants.KEY_BANLIST_BAIHAT;
import static com.example.mp3freeforyou.Ultils.Constants.KEY_BANLIST_CASI;

public class LoginActivity extends AppCompatActivity {

    TextView txtDangky,txtThongbaoLogin,txtRecover;
    EditText edtTenDangNhap,edtMatKhau;
    CardView btnLogin;
    Hosonguoidung hosonguoidung;
    Nguoidung nguoidung;
    LinearLayout linearLayoutLogin;
    LoginButton btnFacebooklogin;
    CheckBox cbFacebooklogin;


    //facebook
    CallbackManager callbackManager;
    CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        anhxa();
        Dangky();
        Dangnhap();

        Danhnhapfacebook();
        Quenmatkhau();
    }

    private void anhxa() {
        txtDangky=findViewById(R.id.txtSignup);
        edtTenDangNhap=findViewById(R.id.edtUsername);
        edtMatKhau=findViewById(R.id.edtPassword);
        btnLogin=findViewById(R.id.btnLogin);
        txtThongbaoLogin=findViewById(R.id.txtThongBaoLogin);
        txtRecover=findViewById(R.id.txtRecover);
        linearLayoutLogin=findViewById(R.id.linearlayoutTaiKhoan);
        btnFacebooklogin=findViewById(R.id.btnFacebooklogin);
        cbFacebooklogin=findViewById(R.id.cbFacebooklogin);
    }

    private void Dangky() {
        txtDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Dangnhap() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtTenDangNhap.getText().equals("") || edtMatKhau.getText().equals(""))
                {
                    txtThongbaoLogin.setText("Không được để trống");
                    txtThongbaoLogin.setVisibility(View.VISIBLE);
                }else {
                    final Dataservice dataservice= APIService.getService();
                    Call<String> callback=dataservice.PostDangNhapNguoiDung(edtTenDangNhap.getText().toString(), StringUtils.unAccent(edtMatKhau.getText().toString()));
                    callback.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String ketqua=response.body();
                            if(ketqua.equals("success")){

                                Call<Hosonguoidung> callHSNDback=dataservice.GetHoSoNguoiDung(edtTenDangNhap.getText().toString(),edtMatKhau.getText().toString());
                                callHSNDback.enqueue(new Callback<Hosonguoidung>() {
                                    @Override
                                    public void onResponse(Call<Hosonguoidung> call, Response<Hosonguoidung> response) {
                                        hosonguoidung=response.body();
                                        nguoidung=new Nguoidung(hosonguoidung.getId().toString(),edtTenDangNhap.getText().toString(),edtMatKhau.getText().toString());
                                        Log.d("Login", "" + hosonguoidung.getId());

                                        String email=hosonguoidung.getEmail().toString().trim();
                                        String password=nguoidung.getMatKhau().toString().trim();
                                        String username=nguoidung.getTenDangNhap().toString().trim();
                                        String name=hosonguoidung.getHoVaTen().toString().trim();
                                        String avatar=hosonguoidung.getAvatar().toString().trim();
                                        //Luu shared preference
                                        PreferenceUtils.saveEmail(email, getApplicationContext());
                                        PreferenceUtils.savePassword(password, getApplicationContext());
                                        PreferenceUtils.saveUsername(username,getApplicationContext());
                                        PreferenceUtils.saveName(name,getApplicationContext());
                                        PreferenceUtils.saveAvatar(avatar,getApplicationContext());

                                        if(cbFacebooklogin.isChecked()){
                                            SaveListenDataToDB_for_Loging(PreferenceUtils.getUsername(getApplicationContext()));
                                        }

                                        //clear quiz
                                        KEY_ARRAYTHELOAI.clear();//clear mang
                                        KEY_ARRAYCASI.clear();
                                        PreferenceUtils.saveListIdTheloaibaihatfromQuizChoice(KEY_ARRAYTHELOAI,getApplicationContext());
                                        PreferenceUtils.saveListIdCasifromQuizChoice(KEY_ARRAYCASI,getApplicationContext());

                                        //clear ban list
                                        KEY_BANLIST_BAIHAT.clear();
                                        KEY_BANLIST_CASI.clear();
                                        PreferenceUtils.saveBanListIdBaihat(KEY_BANLIST_BAIHAT,getApplicationContext());
                                        PreferenceUtils.saveBanListIdCaSi(KEY_BANLIST_CASI,getApplicationContext());
                                        PreferenceUtils.saveListenHistoryForNoAcc("",getApplicationContext());


                                        Toast.makeText(getApplicationContext(),"Đăng nhập thành công",Toast.LENGTH_SHORT).show();

                                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<Hosonguoidung> call, Throwable t) {
                                        Log.d("Login","Đăng nhập ko thành công1");
                                    }
                                });
                                Log.d("Register","Đăng nhập thành công");
                            }else if(ketqua.equals("fail1")){
                                txtThongbaoLogin.setText("Tên đăng nhập không tồn tại");
                                txtThongbaoLogin.setVisibility(View.VISIBLE);
                            }else if(ketqua.equals("fail2")){
                                txtThongbaoLogin.setText("Sai mật khẩu");
                                txtThongbaoLogin.setVisibility(View.VISIBLE);
                            } else {
                                Log.d("Login","Đăng nhập ko thành công");
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d("LoginFrag","Callback error");
                        }
                    });
                }
            }
        });
    }

    private void Danhnhapfacebook() {
        //callback init
        callbackManager = CallbackManager.Factory.create();

        //permission for login
        btnFacebooklogin.setReadPermissions(Arrays.asList("email","public_profile"));

        //register facebook
        btnFacebooklogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void Quenmatkhau() {
        txtRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog=new AlertDialog.Builder(LoginActivity.this).create();
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View dialogView = inflater.inflate(R.layout.alert_dialog_recoverpassword, null);

                final EditText edtEmail = (EditText) dialogView.findViewById(R.id.edtNewpasswordRecover);
                Button btnSubmit = (Button) dialogView.findViewById(R.id.btnSubmitRecover);
                Button btnCancel = (Button) dialogView.findViewById(R.id.btnCancelRecover);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edtEmail.getText().toString().equals("")){
                            Log.d("Recover","Email bị rỗng");
                            Toast.makeText(getApplicationContext(),"Không được để email rỗng",Toast.LENGTH_LONG).show();
                        }else {
                            //add sent mail and update pass lên db
                            Dataservice dataservice= APIService.getService();
                            Call<String> callback=dataservice.PostRecoverPassByEmail(edtEmail.getText().toString(),getAlphaNumericString(10)) ;
                            callback.enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    String kq=response.body();
                                    if(kq.equals("success")){
                                        Log.d("Recover","Gửi mail thành công");
                                        Toast.makeText(getApplicationContext(),"Gửi mail thành công",Toast.LENGTH_LONG).show();
                                    }else if(kq.equals("fail")){
                                        Log.d("Recover","update thất bại");
                                        Toast.makeText(getApplicationContext(),"Lỗi kết nối",Toast.LENGTH_LONG).show();
                                    }else if(kq.equals("emty")){
                                        Toast.makeText(getApplicationContext(),"Email không được để trống",Toast.LENGTH_LONG).show();
                                        Log.d("Recover","mail bị rỗng");
                                    }else {
                                        Log.d("Recover","Tài khoản không tồn tại");
                                        Toast.makeText(getApplicationContext(),"Tài khoản không tồn tại",Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d("UserPlaylist","callback add fail");
                                }
                            });
                            alertDialog.dismiss();

                            /*new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    overridePendingTransition( 0, 0);
                                    startActivity(getIntent());
                                    overridePendingTransition( 0, 0);
                                }
                            }, 3000);*/
                        }
                    }
                });

                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });
    }

    //for facebook Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Accesstoken facbook
    AccessTokenTracker tokenTracker=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                PreferenceUtils.saveAvatar(null, getApplicationContext());
                PreferenceUtils.saveName(null, getApplicationContext());
                PreferenceUtils.saveUsername(null, getApplicationContext());
                PreferenceUtils.saveEmail(null, getApplicationContext());
                PreferenceUtils.savePassword(null, getApplicationContext());

                //clear quiz
                KEY_ARRAYTHELOAI.clear();//clear mang
                KEY_ARRAYCASI.clear();
                PreferenceUtils.saveListIdTheloaibaihatfromQuizChoice(KEY_ARRAYTHELOAI,getApplicationContext());
                PreferenceUtils.saveListIdCasifromQuizChoice(KEY_ARRAYCASI,getApplicationContext());

                //clear ban list
                KEY_BANLIST_BAIHAT.clear();
                KEY_BANLIST_CASI.clear();
                PreferenceUtils.saveBanListIdBaihat(KEY_BANLIST_BAIHAT,getApplicationContext());
                PreferenceUtils.saveBanListIdCaSi(KEY_BANLIST_CASI,getApplicationContext());
                PreferenceUtils.saveListenHistoryForNoAcc("",getApplicationContext());
                /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();*/
            } else{
                loadfacebookprofile(currentAccessToken);
            }

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    };

    //load profile facbook
    private void loadfacebookprofile(AccessToken newAccessToken){
        GraphRequest request=GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name=object.getString("first_name");
                    String last_name=object.getString("last_name");
                    String email=object.getString("email");
                    String id=object.getString("id");
                    String image_url="https://graph.facebook.com"+id+"/picture?type=normal";

                    PreferenceUtils.saveAvatar(image_url,getApplicationContext());
                    PreferenceUtils.saveName(first_name+" "+last_name,getApplicationContext());
                    PreferenceUtils.saveUsername(id,getApplicationContext());
                    PreferenceUtils.saveEmail(email,getApplicationContext());

                    //clear quiz
                    KEY_ARRAYTHELOAI.clear();//clear mang
                    KEY_ARRAYCASI.clear();
                    PreferenceUtils.saveListIdTheloaibaihatfromQuizChoice(KEY_ARRAYTHELOAI,getApplicationContext());
                    PreferenceUtils.saveListIdCasifromQuizChoice(KEY_ARRAYCASI,getApplicationContext());

                    //clear ban list & history
                    KEY_BANLIST_BAIHAT.clear();
                    KEY_BANLIST_CASI.clear();
                    PreferenceUtils.saveBanListIdBaihat(KEY_BANLIST_BAIHAT,getApplicationContext());
                    PreferenceUtils.saveBanListIdCaSi(KEY_BANLIST_CASI,getApplicationContext());
                    PreferenceUtils.saveListenHistoryForNoAcc("",getApplicationContext());

                    //kiem tra facebook id có tồn tại trong db chưa?
                    //nếu có rồi thì chuyển trang
                    //ko thì lưu rồi chuyển trang
                    Dataservice dataservice=APIService.getService();
                    if(cbFacebooklogin.isChecked()){
                        Call<String> callback=dataservice.PostTaoHoSoNguoiDungFacebook(id,email,last_name+" "+first_name);
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String ketqua=response.body();
                                if(ketqua.equals("exist")){
                                    //đã tồn tại nên sẽ update banlist và quizlist
                                    SaveListenDataToDB_for_Loging(id);
                                    Log.d("LoginFB","update fb db");
                                }else if(ketqua.equals("success")){
                                    //chưa có nên sẽ insert banlist và quizlist
                                    SaveListenDataToDB_for_Register(id);
                                    Log.d("LoginFB","create fb on db success");
                                }else if(ketqua.equals("fail1")){
                                    Log.d("LoginFB","loi tao hosonguoidung");
                                }else if(ketqua.equals("fail2")){
                                    Log.d("LoginFB","loi tao nguoidungfb");
                                }else{
                                    Log.d("LoginFB","sth wrong");
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("LoginFB","that bai");
                            }
                        });
                    }else {
                        Call<String> callback=dataservice.PostTaoHoSoNguoiDungFacebook(id,email,last_name+" "+first_name);
                        callback.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String ketqua=response.body();
                                if(ketqua.equals("exist")){
                                    Log.d("LoginFB","update fb db");
                                /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);*/
                                }else if(ketqua.equals("success")){
                                    Log.d("LoginFB","create fb on db success");
                                /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);*/
                                }else if(ketqua.equals("fail1")){
                                    Log.d("LoginFB","loi tao hosonguoidung");
                                }else if(ketqua.equals("fail2")){
                                    Log.d("LoginFB","loi tao nguoidungfb");
                                }else{
                                    Log.d("LoginFB","sth wrong");
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.d("LoginFB","that bai");
                            }
                        });
                    }
                    /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters=new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void SaveListenDataToDB_for_Register(String username) {
        //check các thông số sở thích và ban list nếu chúng null thì sẽ gán thành ""
        if (PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()) == null) {
            PreferenceUtils.saveListIdCasifromQuizChoice("", getApplicationContext());
        }
        if (PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getApplicationContext()) == null) {
            PreferenceUtils.saveListIdTheloaibaihatfromQuizChoice("", getApplicationContext());
        }
        if (PreferenceUtils.getBanListIdBaihat(getApplicationContext()) == null) {
            PreferenceUtils.saveBanListIdBaihat("", getApplicationContext());
        }
        if (PreferenceUtils.getBanListIdCaSi(getApplicationContext()) == null) {
            PreferenceUtils.saveBanListIdCaSi("", getApplicationContext());
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

    private void SaveListenDataToDB_for_Loging(String username){
        //check các thông số sở thích và ban list nếu chúng null thì sẽ gán thành ""
        if (PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()) == null) {
            PreferenceUtils.saveListIdCasifromQuizChoice("", getApplicationContext());
        }
        if (PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getApplicationContext()) == null) {
            PreferenceUtils.saveListIdTheloaibaihatfromQuizChoice("", getApplicationContext());
        }
        if (PreferenceUtils.getBanListIdBaihat(getApplicationContext()) == null) {
            PreferenceUtils.saveBanListIdBaihat("", getApplicationContext());
        }
        if (PreferenceUtils.getBanListIdCaSi(getApplicationContext()) == null) {
            PreferenceUtils.saveBanListIdCaSi("", getApplicationContext());
        }

        Dataservice dataservice1 = APIService.getService();
        Call<String> call1 = dataservice1.postbanlistandquizlist_dangnhap(PreferenceUtils.getBanListIdCaSi(getApplicationContext()), PreferenceUtils.getBanListIdBaihat(getApplicationContext()), PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()), PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getApplicationContext()), username);
        call1.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String kq = response.body();
                if (kq.equals("success")) {
                    Log.d("Login", "Thành công bind sở thích & quiz");
                } else {
                    Log.d("Login", "Thất bại bind sở thích & quiz");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Login", "Thất bại: " + t);
            }
        });

        if(!PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext()).equals("") && PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext())!=null){
            Call<String> call2=APIService.getService().postlisthistory_dangnhap(PreferenceUtils.getListenHistoryForNoAcc(getApplicationContext()),username);
            call2.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String kq=response.body();
                    if(!kq.equals("")){
                        Log.d("Login", "Thành công bind lịch sử nghe");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("Login", ""+t);
                }
            });
        }
    }

    //tạo pass random
    String getAlphaNumericString(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
