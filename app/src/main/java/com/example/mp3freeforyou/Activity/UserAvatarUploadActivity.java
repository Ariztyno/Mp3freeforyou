package com.example.mp3freeforyou.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.mp3freeforyou.Model.Hosonguoidung;
import com.example.mp3freeforyou.Model.Nguoidung;
import com.example.mp3freeforyou.R;
import com.example.mp3freeforyou.Service.APIService;
import com.example.mp3freeforyou.Service.Dataservice;
import com.example.mp3freeforyou.Ultils.PreferenceUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserAvatarUploadActivity extends AppCompatActivity {


    //reload profile
    private static Hosonguoidung hosonguoidung;
    private static Nguoidung nguoidung;

    //upload image
    /*-- CUSTOMIZE --*/
    /*-- you can customize these options for your convenience --*/
    private static String webview_url   = "https://mp3freeforyou.000webhostapp.com/uploads/index.html";    // web address or local file location you want to open in webview
    private static String file_type     = "image/*";    // file types to be allowed for upload
    private boolean multiple_files      = true;         // allowing multiple file upload

    /*-- MAIN VARIABLES --*/
    WebView webView;

    private static final String TAG = MainActivity.class.getSimpleName();

    private String cam_file_data = null;        // for storing camera file information
    private ValueCallback<Uri> file_data;       // data/header received after file selection
    private ValueCallback<Uri[]> file_path;     // received file(s) temp. location

    private final static int file_req_code = 1;
    //upload image end



    String CUSTOM_PACKAGE="com.android.chrome";
    String WebsiteUrl="https://mp3freeforyou.000webhostapp.com/uploads/index.html";

    CustomTabsClient customTabsClient;
    CustomTabsSession customTabsSession;
    CustomTabsServiceConnection customTabsServiceConnection;
    CustomTabsIntent customTabsIntent;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(Build.VERSION.SDK_INT >= 21){
            Uri[] results = null;

            /*-- if file request cancelled; exited camera. we need to send null value to make future attempts workable --*/
            if (resultCode == Activity.RESULT_CANCELED) {
                if (requestCode == file_req_code) {
                    file_path.onReceiveValue(null);
                    return;
                }
            }

            /*-- continue if response is positive --*/
            if(resultCode== Activity.RESULT_OK){
                if(requestCode == file_req_code){
                    if(null == file_path){
                        return;
                    }

                    ClipData clipData;
                    String stringData;
                    try {
                        clipData = intent.getClipData();
                        stringData = intent.getDataString();
                    }catch (Exception e){
                        clipData = null;
                        stringData = null;
                    }

                    if (clipData == null && stringData == null && cam_file_data != null) {
                        results = new Uri[]{Uri.parse(cam_file_data)};
                    }else{
                        if (clipData != null) { // checking if multiple files selected or not
                            final int numSelectedFiles = clipData.getItemCount();
                            results = new Uri[numSelectedFiles];
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                results[i] = clipData.getItemAt(i).getUri();
                            }
                        } else {
                            results = new Uri[]{Uri.parse(stringData)};
                        }
                    }
                }
            }
            file_path.onReceiveValue(results);
            file_path = null;
        }else{
            if(requestCode == file_req_code){
                if(null == file_data) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                file_data.onReceiveValue(result);
                file_data = null;
            }
        }
    }

    //private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_avatar_upload);
        //initNew();
        anhxa();
        init();
    }

    private void initNew() {

    }

    private void init() {
        //webView.loadUrl("https://mp3freeforyou.000webhostapp.com/uploads/index.html"); // địa chỉ sẽ load khi mở trang web

        //cài đặt cho web
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAllowFileAccess(true);

        //load profile lên web
        // The java script string to execute in web view after page loaded
        // First 3 line put a value in input type "text"
        final String js = "javascript:document.getElementById('tendangnhap').value='"+PreferenceUtils.getUsername(getApplicationContext())+"';"+
                "document.getElementById('email').value='"+ PreferenceUtils.getEmail(getApplicationContext())+"';" +
                "document.getElementById('hoten').value='"+ PreferenceUtils.getUsername(getApplicationContext())+"';";

        if(Build.VERSION.SDK_INT >= 21){
            webSettings.setMixedContentMode(0);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }else if(Build.VERSION.SDK_INT >= 19){
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.setWebViewClient(new Callback());
        webView.loadUrl(webview_url);

        webView.setWebChromeClient(new WebChromeClient() {

            /*--
            openFileChooser is not a public Android API and has never been part of the SDK.
            handling input[type="file"] requests for android API 16+; I've removed support below API 21 as it was failing to work along with latest APIs.
            --*/
        /*    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                file_data = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType(file_type);
                if (multiple_files) {
                    i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                startActivityForResult(Intent.createChooser(i, "File Chooser"), file_req_code);
            }
        */
            /*-- handling input[type="file"] requests for android API 21+ --*/
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

                if(file_permission() && Build.VERSION.SDK_INT >= 21) {
                    file_path = filePathCallback;
                    Intent takePictureIntent = null;
                    Intent takeVideoIntent = null;

                    boolean includeVideo = false;
                    boolean includePhoto = false;

                    /*-- checking the accept parameter to determine which intent(s) to include --*/
                    paramCheck:
                    for (String acceptTypes : fileChooserParams.getAcceptTypes()) {
                        String[] splitTypes = acceptTypes.split(", ?+"); // although it's an array, it still seems to be the whole value; split it out into chunks so that we can detect multiple values
                        for (String acceptType : splitTypes) {
                            switch (acceptType) {
                                case "*/*":
                                    includePhoto = true;
                                    includeVideo = true;
                                    break paramCheck;
                                case "image/*":
                                    includePhoto = true;
                                    break;
                                case "video/*":
                                    includeVideo = true;
                                    break;
                            }
                        }
                    }

                    if (fileChooserParams.getAcceptTypes().length == 0) {   //no `accept` parameter was specified, allow both photo and video
                        includePhoto = true;
                        includeVideo = true;
                    }

                    if (includePhoto) {
                        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(UserAvatarUploadActivity.this.getPackageManager()) != null) {
                            File photoFile = null;
                            try {
                                photoFile = create_image();
                                takePictureIntent.putExtra("PhotoPath", cam_file_data);
                            } catch (IOException ex) {
                                Log.e(TAG, "Image file creation failed", ex);
                            }
                            if (photoFile != null) {
                                cam_file_data = "file:" + photoFile.getAbsolutePath();
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            } else {
                                cam_file_data = null;
                                takePictureIntent = null;
                            }
                        }
                    }

                    if (includeVideo) {
                        takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        if (takeVideoIntent.resolveActivity(UserAvatarUploadActivity.this.getPackageManager()) != null) {
                            File videoFile = null;
                            try {
                                videoFile = create_video();
                            } catch (IOException ex) {
                                Log.e(TAG, "Video file creation failed", ex);
                            }
                            if (videoFile != null) {
                                cam_file_data = "file:" + videoFile.getAbsolutePath();
                                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
                            } else {
                                cam_file_data = null;
                                takeVideoIntent = null;
                            }
                        }
                    }

                    Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    contentSelectionIntent.setType(file_type);
                    if (multiple_files) {
                        contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    }

                    Intent[] intentArray;
                    if (takePictureIntent != null && takeVideoIntent != null) {
                        intentArray = new Intent[]{takePictureIntent, takeVideoIntent};
                    } else if (takePictureIntent != null) {
                        intentArray = new Intent[]{takePictureIntent};
                    } else if (takeVideoIntent != null) {
                        intentArray = new Intent[]{takeVideoIntent};
                    } else {
                        intentArray = new Intent[0];
                    }

                    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                    chooserIntent.putExtra(Intent.EXTRA_TITLE, "File chooser");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                    startActivityForResult(chooserIntent, file_req_code);
                    return true;
                } else {
                    return false;
                }
            }
        });
        //webView.loadUrl("https://mp3freeforyou.000webhostapp.com/uploads/index.html"); // địa chỉ sẽ load khi mở trang web
    }

    /*-- callback reporting if error occurs --*/
    public class Callback extends WebViewClient{
        final String js = "javascript:document.getElementById('tendangnhap').value='"+PreferenceUtils.getUsername(getApplicationContext())+"';"+
                "document.getElementById('email').value='"+ PreferenceUtils.getEmail(getApplicationContext())+"';" +
                "document.getElementById('hoten').value='"+ PreferenceUtils.getUsername(getApplicationContext())+"';";

        @Override
        public void onPageFinished(WebView view, String url) {
            if(Build.VERSION.SDK_INT >= 19){
                view.evaluateJavascript(js, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
            }
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl){
            Toast.makeText(getApplicationContext(), "Failed loading app!", Toast.LENGTH_SHORT).show();
        }
    }

    /*-- checking and asking for required file permissions --*/
    public boolean file_permission(){
        if(Build.VERSION.SDK_INT >=23 && (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(UserAvatarUploadActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;
        }else{
            return true;
        }
    }

    /*-- creating new image file here --*/
    private File create_image() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_"+timeStamp+"_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName,".jpg",storageDir);
    }

    /*-- creating new video file here --*/
    private File create_video() throws IOException {
        @SuppressLint("SimpleDateFormat")
        String file_name    = new SimpleDateFormat("yyyy_mm_ss").format(new Date());
        String new_name     = "file_"+file_name+"_";
        File sd_directory   = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(new_name, ".3gp", sd_directory);
    }

    /*-- back/down key handling --*/
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    private void anhxa() {
        webView=findViewById(R.id.UserAvatarUpload);
    }

    //Hỗ trợ cho việc trở về trang trước
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
