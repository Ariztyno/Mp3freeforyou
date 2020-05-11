package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.Preference;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mp3freeforyou.Adapter.Quiz_DSTheLoaiAdapter;
import com.example.mp3freeforyou.Model.Theloaibaihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.Constants;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mp3freeforyou.Ultils.Constants.KEY_ARRAYCASI;
import static com.example.mp3freeforyou.Ultils.Constants.KEY_ARRAYTHELOAI;
import static com.example.mp3freeforyou.Ultils.Constants.KEY_STRING_ARRAYTHELOAI;

public class QuizTheloaibaihatActivity extends AppCompatActivity {
    Handler handler=new Handler();;
    Runnable runnable;

    Toolbar toolbar;
    RecyclerView reQuizDSTheloai;
    Button btnToQuizCaSi;

    ArrayList<Theloaibaihat> mangtheloai;
    Quiz_DSTheLoaiAdapter mangtheloai_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_theloaibaihat);
        anhxa();
        init();
        DelaybtnToQuizCaSi(); //chờ 4s mới có thể đi tới quiz ca sĩ
        GetData();


    }

    private void DelaybtnToQuizCaSi() {
        runnable=new Runnable() {
            @Override
            public void run() {
                //Đến Quiz Casi
                QuizCaSiIntent();
            }
        };
        handler.postDelayed(runnable,4000);
    }

    private void QuizCaSiIntent() {
        btnToQuizCaSi.setOnClickListener(v -> {
            //kiểm tra nếu ko chon ít nhất một thể loại thì không thể đi tiếp
            if(KEY_ARRAYTHELOAI.isEmpty()){
                Toast.makeText(getApplicationContext(),"Bạn phải chọn ít nhất 1 thể loại",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(),"Đã lưu lựa chọn",Toast.LENGTH_SHORT).show();

                //lưu dannh sách Casi dạng sharedpreference với TinyDB
                PreferenceUtils.saveListIdTheloaibaihatfromQuizChoice(KEY_ARRAYTHELOAI,getApplicationContext());
                Log.d("KEY_STRING_ARRAYTHELOAI",""+PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getApplicationContext()));

                Log.d("qKS_ARRAYTHELOAI",""+ PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getApplicationContext()));

                SentQuizAnswerToServer();

                Intent intent = new Intent(QuizTheloaibaihatActivity.this, QuizCasiActivity.class);
                startActivity(intent);
            }
        });
    }

    private void GetData() {
        Dataservice dataservice= APIService.getService();
        Call<List<Theloaibaihat>> callback=dataservice.GetDanhSachTheloai();
        callback.enqueue(new Callback<List<Theloaibaihat>>() {
            @Override
            public void onResponse(Call<List<Theloaibaihat>> call, Response<List<Theloaibaihat>> response) {
                mangtheloai = (ArrayList<Theloaibaihat>) response.body();
                mangtheloai_adapter=new Quiz_DSTheLoaiAdapter(QuizTheloaibaihatActivity.this,mangtheloai);
                reQuizDSTheloai.setLayoutManager(new GridLayoutManager(QuizTheloaibaihatActivity.this,2));
                reQuizDSTheloai.setAdapter(mangtheloai_adapter);
                Log.d("Quiz_DSTheloai","Lấy được:"+String.valueOf(mangtheloai.size()));
            }

            @Override
            public void onFailure(Call<List<Theloaibaihat>> call, Throwable t) {

            }
        });
    }

    private void SentQuizAnswerToServer() {
        if(PreferenceUtils.getUsername(getApplicationContext())!=null){
            //nếu đã đăng nhập thì sẽ gửi
            Dataservice dataservice_sentquizanswer=APIService.getService();
            Call<String> call=dataservice_sentquizanswer.postlistidtheloaiHosoNguoidung(PreferenceUtils.getUsername(getApplicationContext()),PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getApplicationContext()));
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String kq=response.body();
                    if(kq.equals("success")){
                        Log.d("QuizCasiActivity","success to send data to server");
                    }else if(kq.equals("fail")){
                        Log.d("QuizCasiActivity","fail to send data to server");
                    }else{
                        Log.d("QuizCasiActivity","ko true ko fail");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("QuizCasiActivity","onFailure send data to server");
                }
            });
        }
    }

    private void anhxa() {
        toolbar=findViewById(R.id.tbQuizTheloai);
        reQuizDSTheloai=findViewById(R.id.recycleviewQuiz_theloai);
        btnToQuizCaSi=findViewById(R.id.btnToQuizCaSi);
    }

    private void init() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Hãy chọn ít nhất 1 thể loại");

        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
