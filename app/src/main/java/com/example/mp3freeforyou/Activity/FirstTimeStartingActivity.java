package com.example.mp3freeforyou.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import static com.example.mp3freeforyou.Ultils.Constants.KEY_ARRAYCASI;
import static com.example.mp3freeforyou.Ultils.Constants.KEY_ARRAYTHELOAI;

public class FirstTimeStartingActivity extends AppCompatActivity {

    private Button btnBoqua,btnQuiz,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_starting);
        Anhxa();
        checkFirstOpen(); //Kiểm tra đây có phải lần đăng nhập đầu tiên không

        //nút bỏ qua -> trang chủ (MainActivity)
        ExittomenuEvent();
        //đồng ý làm trắc nghiệm -> trang trắc nghiệm (QuizTheloaibaihatActivity)
        QuizEvent();
        //đăng nhập hoặc đăng ký  -> trang đăng nhập hoặc đăng ký (LoginActivity)
        LoginEvent();
    }

    private void LoginEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstTimeStartingActivity.this, LoginActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }

    private void QuizEvent() {
        btnQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstTimeStartingActivity.this, QuizTheloaibaihatActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }

    private void ExittomenuEvent() {
        btnBoqua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstTimeStartingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void Anhxa() {
        btnBoqua=findViewById(R.id.btnExttomenu);
        btnLogin=findViewById(R.id.btnDangnhaphoacdangky);
        btnQuiz=findViewById(R.id.btnQuiz);
    }

    private void checkFirstOpen(){
        String isFirstRun = PreferenceUtils.getFirsttime(getApplicationContext());

        //nếu không phải lần đầu
        if (isFirstRun.equals("no")) {
            Intent intent = new Intent(FirstTimeStartingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }else {
            //nếu là lần đầu update lại là NO để lần sau đăng nhập nó không hiện nữa
            PreferenceUtils.saveFirsttime("no",getApplicationContext());

            //clear tất cả thông tin sharedPreference nếu có
            PreferenceUtils.saveAvatar(null,getApplicationContext());
            PreferenceUtils.saveName(null,getApplicationContext());
            PreferenceUtils.saveUsername(null,getApplicationContext());
            PreferenceUtils.saveEmail(null,getApplicationContext());
            PreferenceUtils.savePassword(null,getApplicationContext());

            //clear quiz
            KEY_ARRAYTHELOAI.clear();//clear mang
            KEY_ARRAYCASI.clear();
            PreferenceUtils.saveListIdTheloaibaihatfromQuizChoice(KEY_ARRAYTHELOAI,getApplicationContext());
            PreferenceUtils.saveListIdCasifromQuizChoice(KEY_ARRAYCASI,getApplicationContext());

        }

    }
}
