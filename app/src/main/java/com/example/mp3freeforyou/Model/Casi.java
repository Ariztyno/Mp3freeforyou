package com.example.mp3freeforyou.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Casi implements Serializable {

@SerializedName("IdCaSi")
@Expose
private String idCaSi;
@SerializedName("TenCaSi")
@Expose
private String tenCaSi;
@SerializedName("ThongTin")
@Expose
private String thongTin;
@SerializedName("HinhCaSi")
@Expose
private String hinhCaSi;

public String getIdCaSi() {
return idCaSi;
}

public void setIdCaSi(String idCaSi) {
this.idCaSi = idCaSi;
}

public String getTenCaSi() {
return tenCaSi;
}

public void setTenCaSi(String tenCaSi) {
this.tenCaSi = tenCaSi;
}

public String getThongTin() {
return thongTin;
}

public void setThongTin(String thongTin) {
this.thongTin = thongTin;
}

public String getHinhCaSi() {
return hinhCaSi;
}

public void setHinhCaSi(String hinhCaSi) {
this.hinhCaSi = hinhCaSi;
}

}