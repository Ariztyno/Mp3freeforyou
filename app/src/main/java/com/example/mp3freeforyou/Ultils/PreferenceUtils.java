package com.example.mp3freeforyou.Ultils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.mp3freeforyou.Model.Hosonguoidung;

import retrofit2.Callback;


/**
 * Created by delaroy on 3/26/18.
 */

public class PreferenceUtils {

    public PreferenceUtils(){

    }

    public static boolean saveIdHoSoNguoiDungPlaylist(String idhosonguoidungplaylist, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((Context) context);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((Context) context);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((Context) context);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((Context) context);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((Context) context);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((Context) context);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((Context) context);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((Context) context);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((Context) context);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((Context) context);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((Context) context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_PASSWORD, password);
        prefsEditor.apply();
        return true;
    }

    public static String getPassword(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_PASSWORD, null);
    }
}
