package com.example.mp3freeforyou.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Nguoidung {

@SerializedName("IdHoSoNguoiDung")
@Expose
private String idHoSoNguoiDung;
@SerializedName("TenDangNhap")
@Expose
private String tenDangNhap;
@SerializedName("MatKhau")
@Expose
private String matKhau;

public Nguoidung(String idHoSoNguoiDung, String tenDangNhap, String matKhau) {
    this.idHoSoNguoiDung = idHoSoNguoiDung;
    this.tenDangNhap = tenDangNhap;
    this.matKhau = matKhau;
}

    public String getIdHoSoNguoiDung() {
return idHoSoNguoiDung;
}

public void setIdHoSoNguoiDung(String idHoSoNguoiDung) {
this.idHoSoNguoiDung = idHoSoNguoiDung;
}

public String getTenDangNhap() {
return tenDangNhap;
}

public void setTenDangNhap(String tenDangNhap) {
this.tenDangNhap = tenDangNhap;
}

public String getMatKhau() {
return matKhau;
}

public void setMatKhau(String matKhau) {
this.matKhau = matKhau;
}

}