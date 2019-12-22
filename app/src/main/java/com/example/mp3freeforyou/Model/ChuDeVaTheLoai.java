package com.example.mp3freeforyou.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChuDeVaTheLoai {

@SerializedName("theloaibaihat")
@Expose
private List<Theloaibaihat> theloaibaihat = null;
@SerializedName("chudebaihat")
@Expose
private List<Chudebaihat> chudebaihat = null;

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