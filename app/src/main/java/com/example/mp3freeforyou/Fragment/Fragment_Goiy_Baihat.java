package com.example.mp3freeforyou.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mp3freeforyou.Activity.Goiy_baihat_XemThemDSBaihatActivity;
import com.example.mp3freeforyou.Activity.MusicPlayerActivity;
import com.example.mp3freeforyou.Adapter.Top5baihatduocyeuthichnhatAdapter;
import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Goiy_Baihat extends Fragment {
    View view;
    ListView listView;
    ImageView imgLikeBtn;

    TextView txtXemThem,txtTile;

    ArrayList<Baihat> mangbaihat=new ArrayList<Baihat>();
    Top5baihatduocyeuthichnhatAdapter top5;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_top5baihatduocyeuthichnhat,container,false);
        listView=view.findViewById(R.id.lvTop5);
        //imgLikeBtn=view.findViewById(R.id.imgLikeBtnRowTop5);
        txtXemThem=view.findViewById(R.id.txtViewmoreGoiybaihat);
        txtTile=view.findViewById(R.id.txtTitleTop5);
        txtTile.setText("BÀI HÁT");
        txtTile.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        txtXemThem.setVisibility(View.VISIBLE);
        if(PreferenceUtils.getUsername(getContext())==null){
            getDataIfNotLogged();
        }else{
            getDataIfLogged();
        }
        Xemthem();
        AnHienContent();
        return view;
    }

    private void getDataIfNotLogged() {
        Dataservice dataservice= APIService.getService();
        if(PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getContext()).matches(".*\\d.*")||PreferenceUtils.getListIdCasifromQuizChoice(getContext()).matches(".*\\d.*")||PreferenceUtils.getBanListIdCaSi(getContext()).matches(".*\\d.*")||PreferenceUtils.getBanListIdBaihat(getContext()).matches(".*\\d.*") || PreferenceUtils.getListenHistoryForNoAcc(getContext()).matches(".*\\d.*")){

            //check if banlist hoặc quiz choice is null replace with ""
            if(PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getContext()) == null){
                PreferenceUtils.saveListIdTheloaibaihatfromQuizChoice("",getContext());
            }
            if(PreferenceUtils.getListIdCasifromQuizChoice(getContext()) == null){
                PreferenceUtils.saveListIdCasifromQuizChoice("",getContext());
            }
            if(PreferenceUtils.getBanListIdCaSi(getContext()) == null){
                PreferenceUtils.saveBanListIdCaSi("",getContext());
            }
            if(PreferenceUtils.getBanListIdBaihat(getContext()) == null){
                PreferenceUtils.saveBanListIdBaihat("",getContext());
            }
            if(PreferenceUtils.getListenHistoryForNoAcc(getContext()) == null){
                PreferenceUtils.saveListenHistoryForNoAcc("",getContext());
            }

            Call<List<Baihat>> callback=dataservice.PostGoiyBaihatForNoAcc(PreferenceUtils.getListenHistoryForNoAcc(getContext()),PreferenceUtils.getListIdTheloaibaihatfromQuizChoice(getContext()),PreferenceUtils.getListIdCasifromQuizChoice(getContext()),PreferenceUtils.getBanListIdCaSi(getContext()),PreferenceUtils.getBanListIdBaihat(getContext()));
            callback.enqueue(new Callback<List<Baihat>>() {
                @Override
                public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                    mangbaihat= (ArrayList<Baihat>) response.body();
                    top5=new Top5baihatduocyeuthichnhatAdapter(getActivity(),android.R.layout.simple_list_item_1,mangbaihat);
                    listView.setAdapter(top5);
                    setListViewHeightBasedOnChildren(listView);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent=new Intent(getContext(), MusicPlayerActivity.class);
                            intent.putExtra("Cakhuc", mangbaihat.get(position));
                            //Log.d("Top5baihat_Cakhuc",String.valueOf(mangbaihat.get(position).getTenBaiHat()));
                            startActivity(intent);
                        }
                    });
                    Log.d("Top5baihat",String.valueOf(mangbaihat.size()));
                }

                @Override
                public void onFailure(Call<List<Baihat>> call, Throwable t) {

                }
            });
        }else{
            Call<List<Baihat>> callback=dataservice.PostGoiyBaihatForNoAccNoQuiz();
            callback.enqueue(new Callback<List<Baihat>>() {
                @Override
                public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                    mangbaihat= (ArrayList<Baihat>) response.body();
                    top5=new Top5baihatduocyeuthichnhatAdapter(getActivity(),android.R.layout.simple_list_item_1,mangbaihat);
                    listView.setAdapter(top5);
                    setListViewHeightBasedOnChildren(listView);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent=new Intent(getContext(), MusicPlayerActivity.class);
                            intent.putExtra("Cakhuc", mangbaihat.get(position));
                            //Log.d("Top5baihat_Cakhuc",String.valueOf(mangbaihat.get(position).getTenBaiHat()));
                            startActivity(intent);
                        }
                    });
                    Log.d("Top5baihat",String.valueOf(mangbaihat.size()));
                }

                @Override
                public void onFailure(Call<List<Baihat>> call, Throwable t) {

                }
            });
        }
    }

    private void AnHienContent() {
        txtTile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listView.getVisibility()==View.VISIBLE){
                    listView.setVisibility(View.GONE);
                }else {
                    listView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void Xemthem() {
        txtXemThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), Goiy_baihat_XemThemDSBaihatActivity.class);
                intent.setFlags(intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }

    private void getDataIfLogged() {
        Dataservice dataservice= APIService.getService();
        Call<List<Baihat>> callback=dataservice.PostGoiyBaihat(PreferenceUtils.getUsername(getContext()));
        callback.enqueue(new Callback<List<Baihat>>() {
            @Override
            public void onResponse(Call<List<Baihat>> call, Response<List<Baihat>> response) {
                mangbaihat= (ArrayList<Baihat>) response.body();
                top5=new Top5baihatduocyeuthichnhatAdapter(getActivity(),android.R.layout.simple_list_item_1,mangbaihat);
                listView.setAdapter(top5);
                setListViewHeightBasedOnChildren(listView);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent=new Intent(getContext(), MusicPlayerActivity.class);
                        intent.putExtra("Cakhuc", mangbaihat.get(position));
                        //Log.d("Top5baihat_Cakhuc",String.valueOf(mangbaihat.get(position).getTenBaiHat()));
                        startActivity(intent);
                    }
                });
                Log.d("Top5baihat",String.valueOf(mangbaihat.size()));
            }

            @Override
            public void onFailure(Call<List<Baihat>> call, Throwable t) {

            }
        });

    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if(listItem != null){
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
