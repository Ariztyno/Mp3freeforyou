package com.example.mp3freeforyou.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchAll {

    @SerializedName("baihat")
    @Expose
    private List<Baihat> baihat = null;
    @SerializedName("casi")
    @Expose
    private List<Casi> casi = null;
    @SerializedName("album")
    @Expose
    private List<Album> album = null;
    @SerializedName("playlist")
    @Expose
    private List<Playlist> playlist = null;
    @SerializedName("theloaibaihat")
    @Expose
    private List<Theloaibaihat> theloaibaihat = null;
    @SerializedName("chudebaihat")
    @Expose
    private List<Chudebaihat> chudebaihat = null;

    public List<Baihat> getBaihat() {
        return baihat;
    }

    public void setBaihat(List<Baihat> baihat) {
        this.baihat = baihat;
    }

    public List<Casi> getCasi() {
        return casi;
    }

    public void setCasi(List<Casi> casi) {
        this.casi = casi;
    }

    public List<Album> getAlbum() {
        return album;
    }

    public void setAlbum(List<Album> album) {
        this.album = album;
    }

    public List<Playlist> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<Playlist> playlist) {
        this.playlist = playlist;
    }

    public List<Theloaibaihat> getTheloaibaihat() {
        return theloaibaihat;
    }

    public void setTheloaibaihat(List<Theloaibaihat> theloaibaihat) {
        this.theloaibaihat = theloaibaihat;
    }

    public List<Chudebaihat> getChudebaihat() {
        return chudebaihat;
    }

    public void setChudebaihat(List<Chudebaihat> chudebaihat) {
        this.chudebaihat = chudebaihat;
    }
}
