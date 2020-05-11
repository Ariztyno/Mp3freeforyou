package com.example.mp3freeforyou.Ultils;

import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.Model.Theloaibaihat;

import java.util.ArrayList;

/**
 * Created by delaroy on 4/1/18.
 */

public class Constants {
    //lich su for no acc
    public static String KEY_LISTEN_HISTORY="0";

    //Preference string
    public static ArrayList<String> KEY_ARRAY_HISTORY=new ArrayList<>();
    public static String KEY_STRING_ARRAY_SEARCH_HISTORY="";

    public static String KEY_NAME = "name";
    public static String KEY_USERNAME = "username";
    public static String KEY_AVATAR = "avatar";
    public static String KEY_EMAIL = "email";
    public static String KEY_PASSWORD = "password";

    public static String KEY_SONG = "song";
    public static String KEY_PLAYLIST = "playlist"; //idplaylist
    public static String KEY_NAMEPLAYLIST = "nameplaylist";//tenplaylist
    public static String KEY_IMGICONPLAYLIST = "imgiconplaylist";//hinhicon cua playlist
    public static String KEY_IMGPLAYLIST = "imgplaylist";//hinhnen cua playlist
    public static String KEY_IDHOSONGUOIDUNGPLAYLIST = "idhosonguoidungplaylist";//idhosonguoidung cua playlist

    public static String KEY_STRING_ARRAYTHELOAI="arraytheloai";
    public static String KEY_STRING_ARRAYCASI="arraycasi";
    public static String KEY_STRING_BANLIST_ARRAYBAIHAT="arraybaihat";
    public static String KEY_STRING_BANLIST_ARRAYCASI="arraycasi";

    //---for starting app 1st time
    public static String KEY_FIRSTTIME="yes";

    //---Array theloai Quiz
    public static  ArrayList<Theloaibaihat> KEY_ARRAYTHELOAI= new ArrayList<>();
    //---Array casi Quiz
    public static  ArrayList<Casi> KEY_ARRAYCASI = new ArrayList<>();

    public static  ArrayList<Baihat> KEY_BANLIST_BAIHAT = new ArrayList<>();
    public static  ArrayList<Casi> KEY_BANLIST_CASI = new ArrayList<>();

    //banner sỉze
    public static int banner_size=5;

    //danh sách bài hát
    public static ArrayList<Baihat> KEY_ARRAY_MANGBAIHAT=new ArrayList<>();

    //bài hát
    public static Baihat KEY_BAIHAT=new Baihat();


}
