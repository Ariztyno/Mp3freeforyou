package com.example.mp3freeforyou.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Chudebaihat implements Serializable {

    @SerializedName("IdChuDe")
    @Expose
    private String idChuDe;
    @SerializedName("TenChuDe")
    @Expose
    private String tenChuDe;
    @SerializedName("HinhChuDe")
    @Expose
    private String hinhChuDe;
    @SerializedName("LaChuDeConCua")
    @Expose
    private String laChuDeConCua;

    public String getIdChuDe() {
        return idChuDe;
    }

    public void setIdChuDe(String idChuDe) {
        this.idChuDe = idChuDe;
    }

    public String getTenChuDe() {
        return tenChuDe;
    }

    public void setTenChuDe(String tenChuDe) {
        this.tenChuDe = tenChuDe;
    }

    public String getHinhChuDe() {
        return hinhChuDe;
    }

    public void setHinhChuDe(String hinhChuDe) {
        this.hinhChuDe = hinhChuDe;
    }

    public String getLaChuDeConCua() {
        return laChuDeConCua;
    }

    public void setLaChuDeConCua(String laChuDeConCua) {
        this.laChuDeConCua = laChuDeConCua;
    }

}