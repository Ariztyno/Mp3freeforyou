package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

import com.example.mp3freeforyou.Adapter.Quiz_DSCasiAdapter;
import com.example.mp3freeforyou.Model.Casi;
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

public class QuizCasiActivity extends AppCompatActivity {
    Handler handler=new Handler();;
    Runnable runnable;

    Toolbar toolbar;
    RecyclerView reQuizDSCaSi;
    Button btnFinishQuiz;

    ArrayList<Casi> mangcasi;
    Quiz_DSCasiAdapter mangcasi_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_casi);
        Anhxa();
        init();
        DelayButtonbtnFinishQuiz(); //chờ 4s mới có thể ấn nút tiếp tục
        GetData();


    }

    private void DelayButtonbtnFinishQuiz() {
        runnable=new Runnable() {
            @Override
            public void run() {
                //Đến MainActivity
                HomeIntent();
            }
        };
        handler.postDelayed(runnable,4000);
    }

    private void HomeIntent() {
        btnFinishQuiz.setOnClickListener(v -> {
            //kiểm tra nếu ko chon ít nhất một thể loại thì không thể đi tiếp
            if(KEY_ARRAYCASI.isEmpty()){
                Toast.makeText(getApplicationContext(),"Bạn phải chọn ít nhất 1 ca sĩ",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(),"Đã lưu lựa chọn",Toast.LENGTH_SHORT).show();

                PreferenceUtils.saveListIdCasifromQuizChoice(KEY_ARRAYCASI,getApplicationContext());
                Log.d("KEY_STRING_ARRAYCASI",""+PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()));
                Log.d("qKS_ARRAYCASI",""+PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()));

                SentQuizAnswerToServer(); //Nếu đã đăng nhập thì mới gửi ko thì thôi

                Intent intent = new Intent(QuizCasiActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void SentQuizAnswerToServer() {
        if(PreferenceUtils.getUsername(getApplicationContext())!=null){
            //nếu đã đăng nhập thì sẽ gửi
            Dataservice dataservice_sentquizanswer=APIService.getService();
            Call<String> call=dataservice_sentquizanswer.postlistidcasiHosoNguoidung(PreferenceUtils.getUsername(getApplicationContext()),PreferenceUtils.getListIdCasifromQuizChoice(getApplicationContext()));
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

    private void GetData() {
        Dataservice dataservice= APIService.getService();
        Call<List<Casi>> calllistCasi=dataservice.GetDanhSachCasi();
        calllistCasi.enqueue(new Callback<List<Casi>>() {
            @Override
            public void onResponse(Call<List<Casi>> call, Response<List<Casi>> response) {
                mangcasi = (ArrayList<Casi>) response.body();
                mangcasi_adapter=new Quiz_DSCasiAdapter(QuizCasiActivity.this,mangcasi);
                reQuizDSCaSi.setLayoutManager(new GridLayoutManager(QuizCasiActivity.this,2));
                reQuizDSCaSi.setAdapter(mangcasi_adapter);
                Log.d("Quiz_DSTheloai","Lấy được:"+String.valueOf(mangcasi.size()));


            }

            @Override
            public void onFailure(Call<List<Casi>> call, Throwable t) {

            }
        });
    }

    private void init() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Hãy chọn ít nhất 1 ca sĩ");

        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void Anhxa() {
        toolbar=findViewById(R.id.tbQuizCasi);
        reQuizDSCaSi=findViewById(R.id.recycleviewQuiz_Casi);
        btnFinishQuiz=findViewById(R.id.btnFinishQuiz);
    }
}
