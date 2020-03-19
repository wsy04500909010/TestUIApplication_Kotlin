package com.wsy.testuiapplication;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by WSY on 2020/2/6.
 */
public class MediaPlayerActivity extends Activity {


    private SurfaceView surfaceView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mediaplayer);

        surfaceView = findViewById(R.id.sv);

        initData();


    }

    private void initData() {
        //下面开始实例化MediaPlayer对象
        final MediaPlayer player = new MediaPlayer();

        //设置数据数据源，也就播放文件地址，可以是网络地址
        String dataPath = "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4";
//        String dataPath = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";//这个播不出来
//        String dataPath = "https://v-cdn.zjol.com.cn/280443.mp4";
//        String dataPath = Environment.getExternalStorageDirectory().getPath()+"/yewen.mp4";
        try {
            player.setDataSource(dataPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //只有当播放器准备好了之后才能够播放，所以播放的出发只能在触发了prepare之后
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });

        /*
            向player中设置dispay，也就是SurfaceHolder。
            但此时有可能SurfaceView还没有创建成功，所以需要监听SurfaceView的创建事件
         */
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //将播放器和SurfaceView关联起来
                player.setDisplay(holder);

                //异步缓冲当前视频文件，也有一个同步接口
                player.prepareAsync();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }



}
