package com.example.mp3freeforyou.Ultils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.Model.Hosonguoidung;
import com.example.mp3freeforyou.Model.Theloaibaihat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Callback;


/**
 * Created by delaroy on 3/26/18.
 */

public class PreferenceUtils {

    public PreferenceUtils(){

    }

    public static boolean saveIdHoSoNguoiDungPlaylist(String idhosonguoidungplaylist, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_IDHOSONGUOIDUNGPLAYLIST, idhosonguoidungplaylist);
        prefsEditor.apply();
        return true;
    }

    public static String getIdHoSoNguoiDungPlaylist(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_IDHOSONGUOIDUNGPLAYLIST, null);
    }

    public static boolean saveImgPlaylist(String imgplaylist, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_IMGPLAYLIST, imgplaylist);
        prefsEditor.apply();
        return true;
    }

    public static String getImgPlaylist(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_IMGPLAYLIST, null);
    }

    public static boolean saveImgIconPlaylist(String imgiconplaylist, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_IMGICONPLAYLIST, imgiconplaylist);
        prefsEditor.apply();
        return true;
    }

    public static String getImgIconPlaylist(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_IMGICONPLAYLIST, null);
    }

    public static boolean saveNamePlaylist(String nameplaylist, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_NAMEPLAYLIST, nameplaylist);
        prefsEditor.apply();
        return true;
    }

    public static String getNamePlaylist(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_NAMEPLAYLIST, null);
    }

    public static boolean savePlaylist(String playlist, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_PLAYLIST, playlist);
        prefsEditor.apply();
        return true;
    }

    public static String getPlaylist(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_PLAYLIST, null);
    }

    public static boolean saveSong(String song, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_SONG, song);
        prefsEditor.apply();
        return true;
    }

    public static String getSong(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_SONG, null);
    }

    public static boolean saveAvatar(String avatar, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_AVATAR, avatar);
        prefsEditor.apply();
        return true;
    }

    public static String getAvatar(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_AVATAR, null);
    }

    public static boolean saveName(String username, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_NAME, username);
        prefsEditor.apply();
        return true;
    }

    public static String getName(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_NAME, null);
    }

    public static boolean saveUsername(String username, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_USERNAME, username);
        prefsEditor.apply();
        return true;
    }

    public static String getUsername(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_USERNAME, null);
    }

    public static boolean saveEmail(String email, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_EMAIL, email);
        prefsEditor.apply();
        return true;
    }

    public static String getEmail(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_EMAIL, null);
    }

    public static boolean savePassword(String password, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_PASSWORD, password);
        prefsEditor.apply();
        return true;
    }

    public static String getPassword(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_PASSWORD, null);
    }

    //FIRSTTME OPEN APP CHECKER
    public static boolean saveFirsttime(String firsttime, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_FIRSTTIME, firsttime);
        prefsEditor.apply();
        return true;
    }

    public static String getFirsttime(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_FIRSTTIME, "yes");
    }

    //ARRAYTHELOAI
    public static boolean saveListIdTheloaibaihatfromQuizChoice(ArrayList<Theloaibaihat> arrayList, Context context) {
        StringBuilder idtheloaibaihatlist= new StringBuilder();
        //phân giải mảng lấy id của từng theloai tao thành chuổi để lưu
        for(int i=0;i<arrayList.size();i++){

            if(i==arrayList.size()-1){
                idtheloaibaihatlist.append(arrayList.get(i).getIdTheLoai());
            }else{
                idtheloaibaihatlist.append(arrayList.get(i).getIdTheLoai()).append(",");
            }
        }

        //lưu SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_STRING_ARRAYTHELOAI, idtheloaibaihatlist.toString());
        prefsEditor.apply();
        return true;
    }

    public static boolean saveListIdTheloaibaihatfromQuizChoice(String arrayList, Context context) {
        //lưu SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_STRING_ARRAYTHELOAI, arrayList);
        prefsEditor.apply();
        return true;
    }

    public static String getListIdTheloaibaihatfromQuizChoice(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_STRING_ARRAYTHELOAI, null);
    }

    //ARRAYCASI
    public static boolean saveListIdCasifromQuizChoice(ArrayList<Casi> arrayList, Context context) {
        StringBuilder idcasilist= new StringBuilder();
        //phân giải mảng lấy id của từng Casi tao thành chuổi để lưu
        for(int i=0;i<arrayList.size();i++){

            if(i==arrayList.size()-1){
                idcasilist.append(arrayList.get(i).getIdCaSi());
            }else{
                idcasilist.append(arrayList.get(i).getIdCaSi()).append(",");
            }
        }

        //lưu SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_STRING_ARRAYCASI, idcasilist.toString());
        prefsEditor.apply();
        return true;
    }

    public static boolean saveListIdCasifromQuizChoice(String arrayList, Context context) {
        //lưu SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_STRING_ARRAYCASI, arrayList);
        prefsEditor.apply();
        return true;
    }

    public static String getListIdCasifromQuizChoice(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_STRING_ARRAYCASI, null);
    }

    //ARRAY banlist baihat
    public static boolean saveBanListIdBaihat(ArrayList<Baihat> arrayList, Context context) {
        StringBuilder idbaihatlist= new StringBuilder();
        //phân giải mảng lấy id của từng Casi tao thành chuổi để lưu
        for(int i=0;i<arrayList.size();i++){

            if(i==arrayList.size()-1){
                idbaihatlist.append(arrayList.get(i).getIdBaiHat());
            }else{
                idbaihatlist.append(arrayList.get(i).getIdBaiHat()).append(",");
            }
        }

        //lưu SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_STRING_BANLIST_ARRAYBAIHAT, idbaihatlist.toString());
        prefsEditor.apply();
        return true;
    }

    public static boolean saveBanListIdBaihat(String arrayList, Context context) {
        //lưu SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_STRING_BANLIST_ARRAYBAIHAT, arrayList);
        prefsEditor.apply();
        return true;
    }

    public static String getBanListIdBaihat(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_STRING_BANLIST_ARRAYBAIHAT, null);
    }

    //ARRAY banlist casi
    public static boolean saveBanListIdCaSi(ArrayList<Casi> arrayList, Context context) {
        StringBuilder idbaihatlist= new StringBuilder();
        //phân giải mảng lấy id của từng Casi tao thành chuổi để lưu
        for(int i=0;i<arrayList.size();i++){

            if(i==arrayList.size()-1){
                idbaihatlist.append(arrayList.get(i).getIdCaSi());
            }else{
                idbaihatlist.append(arrayList.get(i).getIdCaSi()).append(",");
            }
        }

        //lưu SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_STRING_BANLIST_ARRAYCASI, idbaihatlist.toString());
        prefsEditor.apply();
        return true;
    }

    public static boolean saveBanListIdCaSi(String arrayList, Context context) {
        //lưu SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_STRING_BANLIST_ARRAYCASI, arrayList);
        prefsEditor.apply();
        return true;
    }

    public static String getBanListIdCaSi(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_STRING_BANLIST_ARRAYCASI, null);
    }

    //search history
    public static boolean saveSearchHistory(ArrayList<String> arrayList, Context context) {
        StringBuilder listquery= new StringBuilder();
        //phân giải mảng lấy id của từng Casi tao thành chuổi để lưu
        for(int i=0;i<arrayList.size();i++){

            if(i==arrayList.size()-1){
                listquery.append(arrayList.get(i));
            }else{
                listquery.append(arrayList.get(i)).append(",");
            }
        }

        //lưu SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_STRING_BANLIST_ARRAYCASI, listquery.toString());
        prefsEditor.apply();
        return true;
    }

    public static boolean saveSearchHistory(String arrayList, Context context) {
        //lưu SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_STRING_ARRAY_SEARCH_HISTORY, arrayList);
        prefsEditor.apply();
        return true;
    }

    public static boolean saveSearchHistoryByGiveStringThatNeedToRemove(String item, Context context) {

        String[] t=PreferenceUtils.getSearchHistory(context).split(",");
        ArrayList<String> temp=new ArrayList<>(Arrays.asList(t));

        //bỏ tất cả những biến trùng
        Set<String> set = new HashSet<>(temp);
        temp.clear();
        temp.addAll(set);


        //có thì remove
        for(int i=0;i<temp.size();i++){
            if(temp.get(i).equals(item)){
                temp.remove(item);
            }
        }

        StringBuilder listquery= new StringBuilder();
        //phân giải mảng lấy id của từng Casi tao thành chuổi để lưu
        for(int i=0;i<temp.size();i++){

            if(i==temp.size()-1){
                listquery.append(temp.get(i));
            }else{
                listquery.append(temp.get(i)).append(",");
            }
        }

        //lưu SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_STRING_ARRAY_SEARCH_HISTORY, listquery.toString());
        prefsEditor.apply();
        return true;
    }

    public static String getSearchHistory(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_STRING_ARRAY_SEARCH_HISTORY, "");
    }

    public static boolean saveListenHistoryForNoAcc(ArrayList<String> arrayList, Context context) {
        StringBuilder idbaihatlist= new StringBuilder();

        //bỏ tất cả những biến trùng
        Set<String> set = new HashSet<>(arrayList);
        arrayList.clear();
        arrayList.addAll(set);

        //phân giải mảng lấy id của từng Casi tao thành chuổi để lưu
        for(int i=0;i<arrayList.size();i++){

            if(i==arrayList.size()-1){
                idbaihatlist.append(arrayList.get(i));
            }else{
                idbaihatlist.append(arrayList.get(i)).append(",");
            }
        }

        //lưu SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_LISTEN_HISTORY, idbaihatlist.toString());
        prefsEditor.apply();
        return true;
    }

    public static boolean saveListenHistoryForNoAcc(String arrayList, Context context) {
        //lưu SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_LISTEN_HISTORY, arrayList);
        prefsEditor.apply();
        return true;
    }

    public static String getListenHistoryForNoAcc(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_LISTEN_HISTORY, "");
    }
}
