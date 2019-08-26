package com.code.testonline;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Activity父类
 */
public abstract class BaseActivity extends AppCompatActivity {

    //要申请的权限
    private final String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermission();
    }

    /**
     * 6.0及以上系统 需要进行动态权限申请
     */
    private void requestPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            for(String p: permissions){
                if(checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED){ //该权限没同意的，需要申请
                    requestPermissions(new String[]{p}, 100);
                }
            }
        }
    }
}
