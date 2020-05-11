package com.example.mp3freeforyou.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Baihat implements Parcelable {

    @SerializedName("IdBaiHat")
    @Expose
    private String idBaiHat;
    @SerializedName("TenBaiHat")
    @Expose
    private String tenBaiHat;
    @SerializedName("HinhBaiHat")
    @Expose
    private String hinhBaiHat;
    @SerializedName("LinkBaiHat")
    @Expose
    private String linkBaiHat;
    @SerializedName("LinkVideo")
    @Expose
    private String linkVideo;
    @SerializedName("LuotThich")
    @Expose
    private String luotThich;
    @SerializedName("LuotNghe")
    @Expose
    private String luotNghe;
    @SerializedName("IdCaSi")
    @Expose
    private String idCaSi;
    @SerializedName("IdTheLoai")
    @Expose
    private String idTheLoai;
    @SerializedName("TenAlbum")
    @Expose
    private String tenAlbum;

    public Baihat(String idBaiHat, String tenBaiHat, String hinhBaiHat, String linkBaiHat, String linkVideo, String luotThich, String luotNghe, String idCaSi, String idTheLoai, String tenAlbum) {
        this.idBaiHat = idBaiHat;
        this.tenBaiHat = tenBaiHat;
        this.hinhBaiHat = hinhBaiHat;
        this.linkBaiHat = linkBaiHat;
        this.linkVideo = linkVideo;
        this.luotThich = luotThich;
        this.luotNghe = luotNghe;
        this.idCaSi = idCaSi;
        this.idTheLoai = idTheLoai;
        this.tenAlbum = tenAlbum;
    }

    public Baihat() {
        this.idBaiHat = "";
        this.tenBaiHat = "";
        this.hinhBaiHat = "";
        this.linkBaiHat = "";
        this.linkVideo = "";
        this.luotThich = "";
        this.luotNghe = "";
        this.idCaSi = "";
        this.idTheLoai = "";
        this.tenAlbum = "";
    }

    protected Baihat(Parcel in) {
        idBaiHat = in.readString();
        tenBaiHat = in.readString();
        hinhBaiHat = in.readString();
        linkBaiHat = in.readString();
        linkVideo= in.readString();
        luotThich = in.readString();
        luotNghe= in.readString();
        idCaSi = in.readString();
        idTheLoai = in.readString();
        tenAlbum = in.readString();
    }

    public static final Creator<Baihat> CREATOR = new Creator<Baihat>() {
        @Override
        public Baihat createFromParcel(Parcel in) {
            return new Baihat(in);
        }

        @Override
        public Baihat[] newArray(int size) {
            return new Baihat[size];
        }
    };

    public String getIdBaiHat() {
        return idBaiHat;
    }

    public void setIdBaiHat(String idBaiHat) {
        this.idBaiHat = idBaiHat;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getHinhBaiHat() {
        return hinhBaiHat;
    }

    public void setHinhBaiHat(String hinhBaiHat) {
        this.hinhBaiHat = hinhBaiHat;
    }

    public String getLinkBaiHat() {
        return linkBaiHat;
    }

    public void setLinkBaiHat(String linkBaiHat) {
        this.linkBaiHat = linkBaiHat;
    }

    public String getLinkVideo() {
        return linkVideo;
    }

    public void setLinkVideo(String linkVideo) {
        this.linkVideo = linkVideo;
    }

    public String getLuotThich() {
        return luotThich;
    }

    public void setLuotThich(String luotThich) {
        this.luotThich = luotThich;
    }

    public String getLuotNghe() {
        return luotNghe;
    }

    public void setLuotNghe(String luotNghe) {
        this.luotNghe = luotNghe;
    }

    public String getIdCaSi() {
        return idCaSi;
    }

    public void setIdCaSi(String idCaSi) {
        this.idCaSi = idCaSi;
    }

    public String getIdTheLoai() {
        return idTheLoai;
    }

    public void setIdTheLoai(String idTheLoai) {
        this.idTheLoai = idTheLoai;
    }

    public String getTenAlbum() {
        return tenAlbum;
    }

    public void setTenAlbum(String tenAlbum) {
        this.tenAlbum = tenAlbum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idBaiHat);
        dest.writeString(tenBaiHat);
        dest.writeString(hinhBaiHat);
        dest.writeString(linkBaiHat);
        dest.writeString(linkVideo);
        dest.writeString(luotThich);
        dest.writeString(luotNghe);
        dest.writeString(idCaSi);
        dest.writeString(idTheLoai);
        dest.writeString(tenAlbum);
    }
}