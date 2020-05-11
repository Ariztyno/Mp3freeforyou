package com.example.mp3freeforyou.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.mp3freeforyou.Adapter.BannerAdapter;
import com.example.mp3freeforyou.Model.Quangcao;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mp3freeforyou.Ultils.Constants.banner_size;

public class Fragment_Banner extends Fragment {
    int temp=0;

    View view;
    ViewPager viewPager;
    CircleIndicator circleIndicator;

    BannerAdapter bannerAdapter;
    //chuyen page sau thời gian nhất định
    Runnable runnable,refreshrunnable;
    Handler handler,refreshHanler;
    int currentitem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_banner,container,false);
        anhxa();
        refreshHanler=new Handler();
        refreshrunnable= () -> {
            getData();
        };
        refreshHanler.postDelayed(refreshrunnable,3000);
        //getData();
        return view;
    }

    private void anhxa() {
        viewPager=view.findViewById(R.id.Bannerviewpager);
        circleIndicator=view.findViewById(R.id.Bannerindicator);
    }

    private void getData() {
        Dataservice dataservice = APIService.getService();//khởi tạo phường thức lấy dữ liệu phía sẻver
        Call<List<Quangcao>> callback= dataservice.GetDataBanner(); //gọi phương thức lấy dữ liệu từ banner
        callback.enqueue(new Callback<List<Quangcao>>() {
            @Override
            public void onResponse(Call<List<Quangcao>> call, Response<List<Quangcao>> response) {
                ArrayList<Quangcao> banners= (ArrayList<Quangcao>) response.body();
                bannerAdapter=new BannerAdapter(getActivity(),banners);
                viewPager.setAdapter(bannerAdapter);
                circleIndicator.setViewPager(viewPager);
                handler=new Handler();
                runnable= () -> {
                    currentitem=viewPager.getCurrentItem();
                    temp=currentitem;
                    currentitem+=1;

                    if(currentitem-temp==1){
                        if(currentitem >= banners.size()){
                            currentitem=0;
                        }
                        viewPager.setCurrentItem(currentitem,true);
                        handler.postDelayed(runnable,4500);
                    }else {
                        handler.postDelayed(refreshrunnable,4500);
                    }
                };
                handler.postDelayed(runnable,4500);
                Log.d("Banner",String.valueOf(banners.size()));//Số quảng cáo lấy được thành công
            }

            @Override
            public void onFailure(Call<List<Quangcao>> call, Throwable t) {
                Log.d("Banner","Lỗi Banner");//Số quảng cáo lấy được không thành công
            }
        }); //lắng nghe dữ liệu trả về
    }

}
