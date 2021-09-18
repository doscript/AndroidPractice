package cn.xiaolvkeji.helloworld;

import android.Manifest;
import android.annotation.NonNull;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import android.os.sprdpower.IPowerManagerEx;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.xiaolvkeji.helloworld.R;


public class MainActivity extends Activity {


    private static final String TAG = "MyMain";
    private static final String COPY_RIGHT = "epKuVIj0I0jdIFMHoEOfEXpqsdZo2v8hZkJ3sZ8huC+biV"
            +
            "Y0SHRkvAjwutc0FYgfgVWzLhLBzLfnd/CfcPFSjsMOSY4pn4NTNmQ4rMuUBsht6TbOq9UJoe1dIUTEHawWOtM"
            +
            "ii0FmEaIHeh6682JMzrpauvKIpz6wXB3enh2h0lo=";

    public static final int CATEGORY_ITEM_EXCEL = 1;
    public static final int CATEGORY_ITEM_PPT = 2;
    public static final int CATEGORY_ITEM_WORD = 3;
    public static final int CATEGORY_ITEM_PDF = 4;
    public static final int CATEGORY_ITEM_OFD = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
    }


    //check 文件读写权限
    private static int PERMISSION_REQUEST_CODE = 1000;
    private void checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean isAllGranted = true;
            for (String perm:getPermissions()) {
                if(checkSelfPermission(perm)== PackageManager.PERMISSION_GRANTED)
                    continue;
                else{
                    isAllGranted = false;
                    break;
                }
            }
            if (isAllGranted) {
                ;
            }else{
                requestPermissions(getPermissions(), PERMISSION_REQUEST_CODE);
            }
        }
    }
    private String[] getPermissions(){
        return new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
    }
    @Override @TargetApi(Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if(requestCode==PERMISSION_REQUEST_CODE){
            int index = 0;
            List<String> permsNotGrantedYet = new ArrayList<String>();
            for (String permRet:permissions ) {
                if(!(grantResults[index] == PackageManager.PERMISSION_GRANTED)) {
                    permsNotGrantedYet.add(permRet);
                }
                index++;
            }
            if(permsNotGrantedYet.size()>0){
                requestPermissions((String[]) permsNotGrantedYet.toArray(),PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void makeSkippingFileStrictMode() {
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void write2TxtAndroid(View view) {
        String filePath = "/sdcard/1.txt";
        write2File(filePath);
    }

    private void write2File(String filePath) {
        BufferedWriter out = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.getDefault());
        String date = sdf.format(new java.util.Date());


        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filePath, true)));
            out.newLine();
            out.write(date);


        } catch (Exception e) {
            Toast.makeText(this, "cannot write to " + filePath, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            try {
                if(out!=null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        Toast.makeText(this, "Successfully write to " + filePath, Toast.LENGTH_LONG).show();
    }

    public void write2TxtYX(View view) {
        String filePath = "/data/userdata/1.txt";
        write2File(filePath);
    }


    public void openFile(View view) {
       openFile("/sdcard/1.txt");
    }

    public void openFileYX(View view) {
        openFile("/data/userdata/1.txt");
    }



    private void openFile(String name) {
        try {
            File file = new File( name);
            Uri uri = Uri.fromFile(file);
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setDataAndType(uri, "text/plain");
//            startActivity(intent);

            WebView webView = (WebView) findViewById(R.id.webview);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setAllowContentAccess(true);

            webView.loadUrl("file://" + name);

            webView.setWebViewClient(new WebViewClient(){
            });


        } catch (Exception ex) {
            Log.w("DEMO", ex.getMessage());
        }
        Log.i("DEMO", String.format(">>> %s", name));
    }



}
