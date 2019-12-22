package com.example.mp3freeforyou.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mp3freeforyou.Activity.DanhsachbaihatActivity;
import com.example.mp3freeforyou.Activity.GoiYActivity;
import com.example.mp3freeforyou.Activity.MainActivity;
import com.example.mp3freeforyou.Activity.UserAlbumActivity;
import com.example.mp3freeforyou.Activity.UserPlaylistActivity;
import com.example.mp3freeforyou.Activity.UserProfileActivity;
import com.example.mp3freeforyou.Activity.UserSingerActivity;
import com.example.mp3freeforyou.Model.Hosonguoidung;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_UserControlBoard extends Fragment {
    View view;
    TextView txtName,txtUsername,txtEmail;
    Button btnLogout;
    LoginButton btnLogoutFacebook;
    ImageButton btnLichsu,btnBaihat,btnAlbum,btnPlaylist,btnSinger,btnGoiy,btnProfile;
    CircleImageView circleImageViewAvatar;
    String email;
    String password;
    String username;
    String name;
    Hosonguoidung hosonguoidung;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_usercontrolboard,container,false);
        Anhxa();

        //facebook
        checkfbstatuslogin();
        Settext_image(); //cap nhat layout

        Historiybtn();
        ProfileBtn();
        PlaylistBtn();
        LikedSong();
        LikedSinger();
        LikedAlbum();
        Goiy();
        Logout();
        return view;
    }

    private void Goiy() {
        btnGoiy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), GoiYActivity.class);
                startActivity(intent);
            }
        });
    }

    private void LikedAlbum() {
        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), UserAlbumActivity.class);
                startActivity(intent);
            }
        });
    }

    private void LikedSinger() {
        btnSinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), UserSingerActivity.class);
                startActivity(intent);
            }
        });
    }

    private void LikedSong() {
        btnBaihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), DanhsachbaihatActivity.class);
                String text=PreferenceUtils.getUsername(getContext());
                intent.putExtra("baihatyeuthich",text);
                startActivity(intent);
            }
        });
    }

    private void PlaylistBtn() {
        btnPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), UserPlaylistActivity.class);
                startActivity(intent);
            }
        });
    }

    private void ProfileBtn() {
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), UserProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void Historiybtn() {
        btnLichsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), DanhsachbaihatActivity.class);
                String text=PreferenceUtils.getUsername(getContext());
                intent.putExtra("lichsu",text);
                startActivity(intent);
            }
        });
    }

    //dawg xuất bình thườn
    private void Logout() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceUtils.saveAvatar(null,getContext());
                PreferenceUtils.saveName(null,getContext());
                PreferenceUtils.saveUsername(null,getContext());
                PreferenceUtils.saveEmail(null,getContext());
                PreferenceUtils.savePassword(null,getContext());


                /*FragmentTransaction fm = getFragmentManager().beginTransaction();
                fm.replace(R.id.fragment_container,new Fragment_BeforeLogin());
                fm.commit();*/
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //cap nhạt dũ liệu hiển thị trên layout
    private void Settext_image() {
        username= PreferenceUtils.getUsername(getContext());
        if(!username.equals("")){
            txtEmail.setText(PreferenceUtils.getEmail(getContext()));
            txtName.setText(PreferenceUtils.getName(getContext()));
            txtUsername.setText(username);
            Picasso.with(getContext()).load(PreferenceUtils.getAvatar(getContext())).into(circleImageViewAvatar);
        }
    }

    //find view by id
    private void Anhxa() {
        txtEmail=view.findViewById(R.id.txtEmailFrgUserControlBoard);
        txtName=view.findViewById(R.id.txtNameFrgUserControlBoard);
        txtUsername=view.findViewById(R.id.txtUserNameFrgUserControlBoard);
        btnLogout=view.findViewById(R.id.btnLogout);
        btnLichsu=view.findViewById(R.id.btnLichsu);
        btnBaihat=view.findViewById(R.id.btnBaihat);
        btnAlbum=view.findViewById(R.id.btnAlbum);
        btnPlaylist=view.findViewById(R.id.btnPlaylist);
        btnSinger=view.findViewById(R.id.btnSinger);
        btnGoiy=view.findViewById(R.id.btnStar);
        circleImageViewAvatar=view.findViewById(R.id.imgCircleUserControlBoard);
        btnLogoutFacebook=view.findViewById(R.id.btnLogoutFacebook);
        btnProfile=view.findViewById(R.id.btnProfile);
    }

    //sự kiện theo dõi AccessToken dùng cho việc đăng xuất hoặc load trang đã đăng nhập thành công
    AccessTokenTracker tokenTracker=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null){
                PreferenceUtils.saveAvatar(null,getContext());
                PreferenceUtils.saveName(null,getContext());
                PreferenceUtils.saveUsername(null,getContext());
                PreferenceUtils.saveEmail(null,getContext());
                PreferenceUtils.savePassword(null,getContext());

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                /*FragmentTransaction fm = getFragmentManager().beginTransaction();
                fm.replace(R.id.fragment_container,new Fragment_BeforeLogin());
                fm.commit();*/
            }else
                loadfacebookprofile(currentAccessToken); //gọi hàm load profile
        }
    };

    //tải profile facebook
    private void loadfacebookprofile(AccessToken newAccessToken){
        GraphRequest request=GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name=object.getString("first_name");
                    String last_name=object.getString("last_name");
                    String email=object.getString("email");
                    String id=object.getString("id");
                    String image_url="https://graph.facebook.com/"+id+"/picture?type=normal";
                    //String image_url=object.getJSONObject("picture").getJSONObject("object").getString("url");


                    PreferenceUtils.saveAvatar(image_url,getContext());
                    PreferenceUtils.saveName(first_name+" "+last_name,getContext());
                    PreferenceUtils.saveUsername(id,getContext());
                    PreferenceUtils.saveEmail(email,getContext());

                    //hosonguoidung.setAvatar(PreferenceUtils.getAvatar(getContext()));
                    //hosonguoidung.setEmail(PreferenceUtils.getEmail(getContext()));
                    //hosonguoidung.setHoVaTen(PreferenceUtils.getName(getContext()));
                    //hosonguoidung.setId();

                    Picasso.with(getContext()).load(PreferenceUtils.getAvatar(getContext())).into(circleImageViewAvatar);

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

    //kiem tra tinh trang dang nhap va cap permission cho facebook
    private void checkfbstatuslogin(){
        if(AccessToken.getCurrentAccessToken()!=null){
            btnLogoutFacebook.setReadPermissions(Arrays.asList("email","public_profile"));
            loadfacebookprofile(AccessToken.getCurrentAccessToken());
            btnLogoutFacebook.setVisibility(View.VISIBLE);

            btnLogoutFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreferenceUtils.saveAvatar(null,getContext());
                    PreferenceUtils.saveName(null,getContext());
                    PreferenceUtils.saveUsername(null,getContext());
                    PreferenceUtils.saveEmail(null,getContext());
                    PreferenceUtils.savePassword(null,getContext());

                }
            });

            btnLogout.setVisibility(View.GONE);
            btnProfile.setVisibility(View.GONE);
        }else {
            btnLogoutFacebook.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
            btnProfile.setVisibility(View.VISIBLE);
        }
    }
}
