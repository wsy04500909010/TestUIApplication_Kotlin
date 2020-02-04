package com.wsy.testuiapplication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.wsy.testuiapplication.util.MD5Util;
import com.wsy.testuiapplication.util.Slog;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private DFRoundTextView mLoginBtn, mSignupBtn;
    private ImageView close, iv_logo;
    private TextView mVersion;
    private DFRoundTextView mKotlinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        Slog.e("smallestScreenWidth", dealSize() + "");
        Slog.e("dpi", String.valueOf(dm.density));
        Slog.e("Densitydpi", String.valueOf(dm.densityDpi));

        mLoginBtn = findViewById(R.id.tv_login_activity);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DrawerActivity.class));
            }
        });

        mSignupBtn = findViewById(R.id.tv_signup_activity);
        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
            }
        });

        close = findViewById(R.id.iv_close_login);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "getInstanceId failed", task.getException());
                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();

                                // Log and toast
//                                String msg = getString(R.string.msg_token_fmt, token);
//                                Log.d(TAG, msg);
                                Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        iv_logo = findViewById(R.id.iv_logo);
        iv_logo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", "这里是要复制的文字");
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                return false;
            }
        });
        iv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,TVBAdActivity.class));
//                startActivity(new Intent(MainActivity.this,CustomViewActivity.class));
            }
        });

        mVersion = findViewById(R.id.tv_forget);
        mVersion.setText(getLocalVersion(this) + "");


        mKotlinBtn = findViewById(R.id.tv_kotlin);
        mKotlinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,KotlinMainActivity.class));
            }
        });

        Log.d("MD5", MD5Util.Companion.getUserMD5Str("siye.wang@net263.com"));
    }

    private float dealSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightP = metrics.heightPixels;
        int widthP = metrics.widthPixels;
        float density = metrics.density;
        float smallestWidthDP;
        if (heightP < widthP) {
            smallestWidthDP = heightP / density;
        } else {
            smallestWidthDP = widthP / density;
        }
        return smallestWidthDP;
    }


    public static int getLocalVersion(Context ctx) {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
            Log.d("TAG", "本软件的版本号。。" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }
}
