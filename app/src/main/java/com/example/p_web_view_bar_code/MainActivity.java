package com.example.p_web_view_bar_code;
//在專案中就不需要開權限因為在本幾,但是在外面就要開網路
//掃條碼功能,透過webView寫入遠端mySql
//掃條碼功能,透過webView寫入遠端mySql
//只是要寫一個介紹給他的物件名字叫hank然後該物件裡面有這個方法 ->hank.callFromJS();
//將安著掃好的條碼code傳遞給js知道

//1.初始化WebView設定連到自己寫的WebView畫面
//2.寫一個自己java物件方法讓JS認識知道所以方法名一樣
//public class MyHankJs{ @JavascriptInterface  //加入JavascriptInterface才可以讓Ｊs知道
//3.js跟安著java要互相認識 webView.addJavascriptInterface(new MyHankJs(),"hank");//讓Ｊs認識我的物件,設定叫什麼名字
//4.斑馬條碼器 ->https://github.com/dm77/barcodescanner implementation 'me.dm7.barcodescanner:zbar:1.9.13'
//5.他實作是一個全部版面的activity->
//6.他寫的api要掃跳馬需要開啟相機權限,而相機權限依專案為主,有的人在啟動頁就叫然後把你同意參數存起來,到相機頁面就直接不用要權限,有的是到相機頁面權限才去要
//7.到掃條碼頁面用
//8.條碼頁面的結果回來在此接收onActivityResult
//9.如果Scan頁面的結果code等於OK有回來,取得條碼資料if(resultCode == RESULT_OK){String code = data.getStringExtra("code");
//10.將這段code傳給webView的方法顯示在html上webView.loadUrl("javascript:showCode('" + code + "')");

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //6.他寫的api要掃跳馬需要開啟相機權限,而相機權限依專案為主,有的人在啟動頁就叫然後把你同意參數存起來,到相機頁面就直接不用要權限,有的是到相機頁面權限才去要
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    123);

        }
        webView = findViewById(R.id.webView);
        initWebView();
    }

    //1.初始化WebView設定連到自己寫的WebView畫面
    private void initWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        WebViewClient webViewClient = new WebViewClient();
        webView.setWebViewClient(webViewClient);

        //3.js跟安著java要互相認識
        webView.addJavascriptInterface(new MyHankJs(),"hank");//讓Ｊs認識我的物件,設定叫什麼名字

        webView.loadUrl("file:///android_asset/hank.html");
    }

    //2.寫一個自己java物件方法讓JS認識知道所以方法名一樣
    public class MyHankJs{
        @JavascriptInterface  //加入JavascriptInterface才可以讓Ｊs知道
        public void callFromJS(){
            //這邊就專心寫條碼的事情,去掃條碼的頁面
            gotoScan();
        }
    }

    //7.到掃條碼頁面用
    private void gotoScan() {
        startActivityForResult(new Intent(MainActivity.this, ScanActivity.class),123);
    }

    //8.條碼頁面的結果回來在此接收onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            //9.如果Scan頁面的結果code等於OK有回來,取得條碼資料
            if(resultCode == RESULT_OK){
                String code = data.getStringExtra("code");

                //10.將這段code傳給webView的方法顯示在html上
                webView.loadUrl("javascript:showCode('" + code + "')");

            }
    }
}
