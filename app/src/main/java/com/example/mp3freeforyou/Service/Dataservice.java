package com.example.mp3freeforyou.Service;

import com.example.mp3freeforyou.Model.Album;
import com.example.mp3freeforyou.Model.Baihat;
import com.example.mp3freeforyou.Model.Casi;
import com.example.mp3freeforyou.Model.Chudebaihat;
import com.example.mp3freeforyou.Model.Hosonguoidung;
import com.example.mp3freeforyou.Model.Playlist;
import com.example.mp3freeforyou.Model.Quangcao;
import com.example.mp3freeforyou.Model.SearchAll;
import com.example.mp3freeforyou.Model.Theloaibaihat;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

//gửi phương thức tương tác với server và nhận data trả về từ server
public interface Dataservice {

    //gửi tương tác với server
    @GET("Server/songbanner.php")
    //Nhận dữ liệu từ server
    Call<List<Quangcao>> GetDataBanner();

    @GET("Server/playlistforcurrentday.php")
    Call<List<Playlist>> GetDataPlaylistCurrentDay();

    @GET("Server/chudeforcurrentday.php")
    Call<List<Chudebaihat>> GetDataChudeCurrentDay();

    @GET("Server/theloaiforcurrentday.php")
    Call<List<Theloaibaihat>> GetDataTheloaiCurrentDay();

    @GET("Server/albumforcurrentday.php")
    Call<List<Album>> GetDataAlbumCurrentDay();

    @GET("Server/top5baihatduocthichnhieunhat.php")
    Call<List<Baihat>> GetDataTop5BaihatDuocYeuThichNhat();

    @GET("Server/topbaihatduocthichnhieunhat.php")
    Call<List<Baihat>> GetDataBangxephangBaihatDuocYeuThichNhat();

    //tương tác lấy nhạc theo idquangcao
    @FormUrlEncoded
    @POST("Server/ds_baihat.php")
    Call<List<Baihat>> GetDanhSachBaihatTheoQuangcao(@Field("idquangcao") String idquangcao);

    //tương tác lấy nhạc theo idplaylist
    @FormUrlEncoded
    @POST("Server/ds_baihat.php")
    Call<List<Baihat>> GetDanhSachBaihatTheoPlaylist(@Field("idplaylist") String idplaylist);

    //lấy toàn bộ playlist
    @GET("Server/ds_playlist.php")
    Call<List<Playlist>> GetDanhSachPlaylist();

    //lấy toàn bộ chude cha
    @GET("Server/ds_chude.php")
    Call<List<Chudebaihat>> GetDanhSachChude();

    //lấy toàn bộ chude cha
    @GET("Server/ds_theloai.php")
    Call<List<Theloaibaihat>> GetDanhSachTheloai();

    //lấy toàn bộ chudecon cua mot chu de
    @FormUrlEncoded
    @POST("Server/ds_chudecon.php")
    Call<List<Chudebaihat>> GetDanhSachChudeCon(@Field("lachudeconcua") String lachudeconcua);

    //tương tác lấy nhạc theo idtheloai
    @FormUrlEncoded
    @POST("Server/ds_baihat.php")
    Call<List<Baihat>> GetDanhSachBaihatTheoTheLoaiBaiHat(@Field("idtheloai") String idtheloai);

    //Lấy danh sách thể loại với idchude truyen vao
    @FormUrlEncoded
    @POST("Server/ds_theloai.php")
    Call<List<Theloaibaihat>> GetDanhSachTheloaiTheoChude(@Field("idchude1") String idchude1);

    //lấy toàn bộ ds album
    @GET("Server/ds_album.php")
    Call<List<Album>> GetDanhSachAlbum();

    //lấy dsbai hát với idalbum truyền vào
    @FormUrlEncoded
    @POST("Server/ds_baihat.php")
    Call<List<Baihat>> GetDanhSachBaihatTheoAlbum(@Field("idalbum") String idalbum);

    //lấy dsbai hát với idcasi truyền vào
    @FormUrlEncoded
    @POST("Server/ds_baihat.php")
    Call<List<Baihat>> GetDanhSachBaihatTheoCasi(@Field("idcasi") String idcasi);

    //lấy dsbai lichsu hát với username truyền vào
    @FormUrlEncoded
    @POST("Server/ds_baihat.php")
    Call<List<Baihat>> GetDanhSachBaihatTheoLichsu(@Field("username") String username);

    //lấy dsbai lichsu hát với username truyền vào
    @FormUrlEncoded
    @POST("Server/ds_baihat.php")
    Call<List<Baihat>> GetDanhSachBaihatYeuthich(@Field("username_baihatyeuthich") String username_baihatyeuthich);

    //update luot thích
    @FormUrlEncoded
    @POST("Server/updateluotthich.php")
    Call<String> GetLuotThichvaIdCuaBaiHat(@Field("luotthich") String luotthich,@Field("idbaihat") String idbaihat);

    @FormUrlEncoded
    @POST("Server/updateluotthich_nguoidung.php")
    Call<String> PostBoThichvaIdCuaBaiHat(@Field("luotthich") String luotthich,@Field("idbaihat") String idbaihat,@Field("username") String username);

    //gửi dữ liệu để tìm kiếm
    @FormUrlEncoded
    @POST("Server/searchbaihat.php")
    Call<List<Baihat>> GetKetquaTimkiem(@Field("tukhoa") String tukhoa);

    @FormUrlEncoded
    @POST("Server/search_all.php")
    Call<SearchAll> GetKetQuaTimKiem(@Field("tukhoa") String tukhoa);

    //timcassi
    @FormUrlEncoded
    @POST("Server/searchcasi.php")
    Call<List<Casi>> GetKetquaTimkiemCasi(@Field("tukhoa") String tukhoa);

    //tao ho so nguoi dung va tai khoan Nguoidung binh thuong
    @FormUrlEncoded
    @POST("Server/dangkytaikhoan_binhthuong.php")
    Call<String> PostTaoHoSoNguoiDung(@Field("tendangnhap") String tendangnhap,@Field("matkhau") String matkhau,@Field("email") String email,@Field("hoten") String hoten);

    //login nguoi dung binh thuong - lay thong tin dang nhap
    @FormUrlEncoded
    @POST("Server/dangnhap_binhthuong.php")
    Call<String> PostDangNhapNguoiDung(@Field("tendangnhap") String tendangnhap,@Field("matkhau") String matkhau);

    //login nguoi dung binh thuong - lay thong tin hosonguoidung
    @FormUrlEncoded
    @POST("Server/getHosonguoidung_BinhThuong.php")
    Call<Hosonguoidung> GetHoSoNguoiDung(@Field("tendangnhap") String tendangnhap, @Field("matkhau") String matkhau);

    //dangky facebook
    @FormUrlEncoded
    @POST("Server/dangkyvadangnhaptaikhoan_facebook.php")
    Call<String> PostTaoHoSoNguoiDungFacebook(@Field("idfacebook") String idfacebook,@Field("email") String email,@Field("hoten") String hoten);

    //doimatkhau_binhthuong
    @FormUrlEncoded
    @POST("Server/doimatkhau_binhthuong.php")
    Call<String> PostUpdateMatKhauNguoidung(@Field("tendangnhap") String tendangnhap,@Field("matkhau") String matkhau);

    //doihoso_binhthuong
    @FormUrlEncoded
    @POST("Server/doihoso_binhthuong.php")
    Call<String> PostUpdateHosoNguoidung(@Field("tendangnhap") String tendangnhap,@Field("email") String email,@Field("hoten") String hoten);

    //ADD bai hát vao lichsu khi bai hat đc phát
    @FormUrlEncoded
    @POST("Server/addlichsu.php")
    Call<String> PostUpdateLichsuNguoidung(@Field("tendangnhap") String tendangnhap,@Field("idbaihat") String idbaihat);

    //lấy các playlist theo ten nguoi dung
    @FormUrlEncoded
    @POST("Server/getPlaylist_nguoidung.php")
    Call<List<Playlist>> GetDanhSachPlaylistCuaNguoiDung(@Field("username") String username);

    //lấy các playlist yeu thich cua nguoi dung
    @FormUrlEncoded
    @POST("Server/getPlaylistyeuthich_nguoidung.php")
    Call<List<Playlist>> GetDanhSachPlaylistYeuthichCuaNguoiDung(@Field("username") String username);

    //del playlist cua nguoi dung
    @FormUrlEncoded
    @POST("Server/delplaylist.php")
    Call<String> PostDelPlayList(@Field("idplaylist") String idplaylist);

    //del playlistyeuthich cua nguoi dung
    @FormUrlEncoded
    @POST("Server/delplaylistyeuthich.php")
    Call<String> PostDelPlayListyethich(@Field("idplaylist") String idplaylist,@Field("username") String username);

    //doi ten playlist playlist cua nguoi dung
    @FormUrlEncoded
    @POST("Server/changeplaylistname.php")
    Call<String> PostChangenamePlayList(@Field("idplaylist") String idplaylist,@Field("tenplaylist") String tenplaylist);

    //Add new playlist
    @FormUrlEncoded
    @POST("Server/addplaylist.php")
    Call<String> PostAddnewPlayList(@Field("username") String username,@Field("tenplaylist") String tenplaylist);

    //yeuthich playlist
    @FormUrlEncoded
    @POST("Server/yeuthichplaylist.php")
    Call<String> PostYeuthichPlaylist(@Field("idplaylist") String idplaylist,@Field("username") String username);

    //kt hien thi nut like playlist
    @FormUrlEncoded
    @POST("Server/kt_yeuthichplaylist.php")
    Call<String> PostKtYeuthichPlaylist(@Field("idplaylist") String idplaylist,@Field("username") String username);

    //kt hien thi nut like baihat
    @FormUrlEncoded
    @POST("Server/kt_yeuthichbaihat.php")
    Call<String> PostKtYeuthichBaihat(@Field("idbaihat") String idbaihat,@Field("username") String username);

    //add song to playlist
    @FormUrlEncoded
    @POST("Server/addsongtoplaylist.php")
    Call<String> PostAddsongtoplaylist(@Field("idplaylist") String idplaylist,@Field("idbaihat") String idbaihat);

    //del song from playlist
    @FormUrlEncoded
    @POST("Server/delsongfromplaylist.php")
    Call<String> PostDelsongfromplaylist(@Field("idplaylist") String idplaylist,@Field("idbaihat") String idbaihat);

    //del/insert casiyeuthich
    @FormUrlEncoded
    @POST("Server/updatecasi_nguoidung.php")
    Call<String> PostDelInsertCasiyeuthich(@Field("idcasi") String idcasi,@Field("username") String username);

    //GET LIST casiyeuthich
    @FormUrlEncoded
    @POST("Server/getCasiyeuthich_nguoidung.php")
    Call<List<Casi>> GetCasiyeuthich(@Field("username") String username);

    //kt casiyeuthich
    @FormUrlEncoded
    @POST("Server/kt_yeuthichcasi.php")
    Call<String> PostKtCasiyeuthich(@Field("idcasi") String idcasi,@Field("username") String username);

    //del/insert albumyeuthich
    @FormUrlEncoded
    @POST("Server/updatealbum_nguoidung.php")
    Call<String> PostDelInsertAlbumyeuthich(@Field("idalbum") String idalbum,@Field("username") String username);

    //GET LIST casiyeuthich
    @FormUrlEncoded
    @POST("Server/getAlbumyeuthich_nguoidung.php")
    Call<List<Album>> GetAlbumyeuthich(@Field("username") String username);

    //kt casiyeuthich
    @FormUrlEncoded
    @POST("Server/kt_yeuthichalbum.php")
    Call<String> PostKtAlbumyeuthich(@Field("idalbum") String idalbum,@Field("username") String username);


    //Goi y-----------------------------------------------------------------------------------------------------------------
    //baihat goiy
    @FormUrlEncoded
    @POST("Server/goiy_baihat.php")
    Call<List<Baihat>> PostGoiyBaihat(@Field("username") String username);

    //baihat goiy xemthem
    @FormUrlEncoded
    @POST("Server/goiybaihat_xemthem.php")
    Call<List<Baihat>>  PostGoiyBaihatXemthem(@Field("username") String username);

    //theloai goiy
    @FormUrlEncoded
    @POST("Server/goiy_theloai.php")
    Call<List<Theloaibaihat>> PostGoiyTheloai(@Field("username") String username);

    //theloai goiy xemthem
    @FormUrlEncoded
    @POST("Server/goiytheloai_xemthem.php")
    Call<List<Theloaibaihat>>  PostGoiyTheloaiXemthem(@Field("username") String username);

    //theloai goiy
    @FormUrlEncoded
    @POST("Server/goiy_casi.php")
    Call<List<Casi>> PostGoiyCasi(@Field("username") String username);

    //theloai goiy xemthem
    @FormUrlEncoded
    @POST("Server/goiycasi_xemthem.php")
    Call<List<Casi>>  PostGoiyCasiXemthem(@Field("username") String username);

    //theloai goiy
    @FormUrlEncoded
    @POST("Server/goiy_album.php")
    Call<List<Album>> PostGoiyAlbum(@Field("username") String username);

    //album goiy xemthem
    @FormUrlEncoded
    @POST("Server/goiyalbum_xemthem.php")
    Call<List<Album>>  PostGoiyAlbumXemthem(@Field("username") String username);
    //=------------------------------------------------------------------------------------------

    //recover pass
    @FormUrlEncoded
    @POST("mail/forgot_password.php")
    Call<String> PostRecoverPassByEmail(@Field("email") String email,@Field("Newpassord") String Newpassord);
}
