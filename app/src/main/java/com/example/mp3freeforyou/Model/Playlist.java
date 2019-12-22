package com.example.mp3freeforyou.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Playlist implements Serializable {

@SerializedName("IdPlaylist")
@Expose
private String idPlaylist;
@SerializedName("TenPlaylist")
@Expose
private String tenPlaylist;
@SerializedName("HinhNen")
@Expose
private String hinhNen;
@SerializedName("HinhIcon")
@Expose
private String hinhIcon;
@SerializedName("IdHoSoNguoiDung")
@Expose
private String idHoSoNguoiDung;

public Playlist(String idPlaylist, String tenPlaylist, String hinhNen, String hinhIcon, String idHoSoNguoiDung) {
    this.idPlaylist = idPlaylist;
    this.tenPlaylist = tenPlaylist;
    this.hinhNen = hinhNen;
    this.hinhIcon = hinhIcon;
    this.idHoSoNguoiDung = idHoSoNguoiDung;
}

public String getIdPlaylist() {
return idPlaylist;
}

public void setIdPlaylist(String idPlaylist) {
this.idPlaylist = idPlaylist;
}

public String getTenPlaylist() {
return tenPlaylist;
}

public void setTenPlaylist(String tenPlaylist) {
this.tenPlaylist = tenPlaylist;
}

public String getHinhNen() {
return hinhNen;
}

public void setHinhNen(String hinhNen) {
this.hinhNen = hinhNen;
}

public String getHinhIcon() {
return hinhIcon;
}

public void setHinhIcon(String hinhIcon) {
this.hinhIcon = hinhIcon;
}

public String getIdHoSoNguoiDung() {
return idHoSoNguoiDung;
}

public void setIdHoSoNguoiDung(String idHoSoNguoiDung) {
this.idHoSoNguoiDung = idHoSoNguoiDung;
}

}

