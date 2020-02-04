package com.wsy.testuiapplication;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.wsy.testuiapplication.util.ImageUtil;
import com.wsy.testuiapplication.util.Slog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WSY on 2020/2/4.
 */
public class TVBAdActivity extends HXBaseActivity {

    private static final String TAG = TVBAdActivity.class.getSimpleName();

    private static final int AD_READY = 1;
    private static final int AD_ERROR = 0;

    private ImageView mAdImage;
    private VideoView mVideoView;

    private List<String> mAdImages;
    private boolean mFinished;

    String imageDir;
    String imagePath;

    //===================广告新逻辑开始
    //    消息类型-请求计时
    private static final int TYPE_REQUEST_AD_TIME = 2;

    private static final int TYPE_MEDIAAD_STATUS_NO_RESPONSE = 0;
    private static final int TYPE_MEDIAAD_STATUS_success_RESPONSE = 1;
    private static final int TYPE_MEDIAAD_STATUS_fail_RESPONSE = 2;

    //    图片广告是否播放完毕
    private boolean mImageAdFinishFlag;
    //    视频广告接口返回状态
    private int mMediaAdStatus = TYPE_MEDIAAD_STATUS_NO_RESPONSE;
    //    图片广告是否 等待响应超过5s
    private boolean mImageAdTimeOut;
    //    视频广告是否 等待响应超过5s
    private boolean mMediaAdTimeOut;
    //    图片广告是否获得响应
    private boolean mImageAdResponse;
    //    视频广告是否获得响应
    private boolean mMediaAdResponse;


//    private LauncherColorListBean.ResultBean.ColorListBean mColor;

    private Handler mHandler;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_tvb_ad);
        mAdImage = (ImageView) findViewById(R.id.iv_ad);
        mVideoView = (VideoView) findViewById(R.id.videoView);

        mAdImages = new ArrayList<>();
        mAdImages.add("http://00.minipic.eastday.com/20170925/20170925141357_d41d8cd98f00b204e9800998ecf8427e_3.jpeg");
//        mAdImages.add("http://img1.imgtn.bdimg.com/it/u=3216941516,4089665709&fm=214&gp=0.jpg");
//        mAdImages.add("http://b.zol-img.com.cn/desk/bizhi/image/5/1920x1200/1409194293467.jpg");
//        mAdImages.add(
//                "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1307/09/c1/23113144_1373337486840.jpg");
//        mAdImages.add(
//                "http://img.pconline.com.cn/images/upload/upc/tx/wallpaper/1308/05/c0/24160717_1375684275708.jpg");


        imageDir = getFilesDir() + File.separator + "image_ad";
        imagePath = imageDir + File.separator + "new_ad.jpg";

        getIntentData();

        initHandler();
        checkNewADImage();

    }

    private void initHandler() {

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case AD_ERROR:
                        //直接跳转
                        startTVBPage();
                        break;
                    case AD_READY:
                        //开始播放视频
                        mVideoView.start();
                        mAdImage.setVisibility(View.GONE);
                        break;
                    case TYPE_REQUEST_AD_TIME://计时到了5s
                        if (!mImageAdResponse) {
                            mImageAdTimeOut = true;
                            mImageAdFinishFlag = true;
                            notifyMediaAd();
                        }
                        if (!mMediaAdResponse) {
                            mMediaAdTimeOut = true;
                            mMediaAdStatus = TYPE_MEDIAAD_STATUS_fail_RESPONSE;
                            if (mImageAdFinishFlag) {
                                startTVBPage();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }


    private void getIntentData() {
//        mColor = (LauncherColorListBean.ResultBean.ColorListBean) getIntent().getSerializableExtra("color");
    }

    @Override
    protected void loadData() {
        ImageUtil.showImage(this, "file:///android_asset/default_ad.jpg", mAdImage);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mHandler.sendEmptyMessageDelayed(AD_READY, 2000);
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                startTVBPage();
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Slog.d(TAG, "onError: play error.");

                mHandler.sendEmptyMessageDelayed(AD_ERROR, 2000);
                return true;
            }
        });
        //加载数据开始干3件事
        //1、计时
        mHandler.sendEmptyMessageDelayed(TYPE_REQUEST_AD_TIME, 5000);

        //2、发送图片广告请求
        requestForImageAd(true);
        //3、发送视频广告请求
        requestForMediaAd(false);


        //网络视频
        String videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
//        String videoUrl = "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4";
        Uri uri = Uri.parse(videoUrl);
        //设置视频路径
        mVideoView.setVideoURI(uri);


    }

    //这里的flag 代表接口实际调用回来时的成功与失败
    private void requestForImageAd(boolean flag) {
        if (!mImageAdTimeOut) {//本请求未超时
            mImageAdResponse = true;

            if (flag) {//返回成功
                playImageAd();
            } else {//返回失败
                imageAdFinish();
            }
        }


    }

    private void requestForMediaAd(boolean flag) {
        if (!mMediaAdTimeOut) {
            mMediaAdResponse = true;

            if (flag) {//返回成功
                mMediaAdStatus = TYPE_MEDIAAD_STATUS_success_RESPONSE;
                if (mImageAdFinishFlag) {//图片广告流程完毕
                    playMediaAd();
                }
            } else {//返回失败
                mMediaAdStatus = TYPE_MEDIAAD_STATUS_fail_RESPONSE;
                if (mImageAdFinishFlag) {//图片广告流程完毕
                    startTVBPage();
                }
            }
        }
    }

    // 图片广告 流程完毕后 走这里
    private void imageAdFinish() {
        mImageAdFinishFlag = true;
        notifyMediaAd();
    }

    // 根据视频广告加载状态决定下一步操作
    private void notifyMediaAd() {
        switch (mMediaAdStatus) {
            case TYPE_MEDIAAD_STATUS_NO_RESPONSE:

                break;
            case TYPE_MEDIAAD_STATUS_success_RESPONSE:
                playMediaAd();
                break;
            case TYPE_MEDIAAD_STATUS_fail_RESPONSE:
                startTVBPage();
                break;
        }
    }

    // TODO: 2020/2/4  
    private void playImageAd() {

    }

    // TODO: 2020/2/4
    private void playMediaAd() {

    }


    private void startTVBPage() {
//        PosterClickUtil.gotoActivityByColorInfo(this, mColor);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mMediaPlayer.reset();
//        mMediaPlayer.release();
//        mMediaPlayer = null;
//        mHandler.removeCallbacksAndMessages(null);
    }


    private void checkNewADImage() {
        // TODO: 2020-01-09 这里应该通过接口确定是否有新广告图片,这里我们当做有 直接操作下载

    }

}
