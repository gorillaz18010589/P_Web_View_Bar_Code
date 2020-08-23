package com.example.p_web_view_bar_code;
//條碼實作
//1.跟著實作ZBarScannerView.ResultHandler看看有沒有imporant到
//2.宣告ZBarScannerView
//3.onCreate ->初始化這邊要注意因為this是activity所以要依著生命週期去初始化
//4.onResume時 -> 設定setResultHandler監聽,並且啟動相機
//5.onPause -> 暫停時關閉相機



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScanActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler  {
    private ZBarScannerView mScannerView; //2.宣告ZBarScannerView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //3.onCreate ->初始化這邊要注意因為this是activity所以要依著生命週期去初始化
        mScannerView = new ZBarScannerView(this);
        setContentView(mScannerView);
    }

    //1.ZBarScannerView.ResultHandler 實作方法
    @Override
    public void handleResult(Result rawResult) {
        //6.掃好後資料取得
        String content = rawResult.getContents(); //getContents()只會拿到一次
        Log.v("brad", content); // Prints scan results
        Log.v("brad", rawResult.getBarcodeFormat().getName()); // Prints the scan format (qrcode, pdf417 etc.)

        // If you would like to resume scanning, call this method below:
        mScannerView.resumeCameraPreview(this);

        //7.重點觀念：content不為空代表資料有進來,就把資料帶回activty頁面因為是從那邊過來的所以直接intent finish然後用setResult傳遞參數
        if (content != null){
            Intent intent = new Intent();
            intent.putExtra("code", content);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    //4.onResume時 -> 設定setResultHandler監聽,並且啟動相機
    @Override
    protected void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
    }

    //5.onPause -> 暫停時關閉相機
    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }
}
