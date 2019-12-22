package com.example.mp3freeforyou.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Hosonguoidung implements Serializable {

    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("HoVaTen")
    @Expose
    private String hoVaTen;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Avatar")
    @Expose
    private String avatar;

    public Hosonguoidung(String id, String hoVaTen, String email, String avatar) {
        this.id = id;
        this.hoVaTen = hoVaTen;
        this.email = email;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHoVaTen() {
        return hoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        this.hoVaTen = hoVaTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}