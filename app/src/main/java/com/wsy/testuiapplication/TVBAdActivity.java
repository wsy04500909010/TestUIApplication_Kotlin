package com.wsy.testuiapplication;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.wsy.testuiapplication.ad.bean.ImageAdListBean;
import com.wsy.testuiapplication.util.ImageUtil;
import com.wsy.testuiapplication.util.Slog;

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

    private ImageAdListBean mImageAdBean;
    private boolean mFinished;

    //===================广告新逻辑开始
    //    消息类型-请求计时
    private static final int TYPE_REQUEST_AD_TIME = 2;
    //    消息类型-图片加载完一张 切换到下一张
    private static final int TYPE_IMAGE_AD_TIME_COUNT = 3;


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

    private int mCurrentAdImageIndex;

    // 视频广告等待时间
    private int mAdWaitTime;

//    private LauncherColorListBean.ResultBean.ColorListBean mColor;

    private Handler mHandler;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_tvb_ad);
        mAdImage = (ImageView) findViewById(R.id.iv_ad);
        mVideoView = (VideoView) findViewById(R.id.videoView);

        prepareDatas();
//        getIntentData();

        initHandler();

    }

    //准备假数据
    private void prepareDatas() {

        mImageAdBean = new ImageAdListBean();

        List<String> mAdImages = new ArrayList<>();
        mAdImages.add("http://00.minipic.eastday.com/20170925/20170925141357_d41d8cd98f00b204e9800998ecf8427e_3.jpeg");
        mAdImages.add("http://img1.imgtn.bdimg.com/it/u=3216941516,4089665709&fm=214&gp=0.jpg");
        mAdImages.add("http://b.zol-img.com.cn/desk/bizhi/image/5/1920x1200/1409194293467.jpg");

        mImageAdBean.setDisplayTime(3000);
        mImageAdBean.setImages(mAdImages);

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

                        // 监测是否卡顿
                        mHandler.postDelayed(runnable, 0);
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
                    case TYPE_IMAGE_AD_TIME_COUNT:
                        mCurrentAdImageIndex++;
                        if (mCurrentAdImageIndex < mImageAdBean.getImages().size()) {
//                            ImageUtil.showImage(TVBAdActivity.this, mImageAdBean.getImages().get(mCurrentAdImageIndex), mAdImage);
//                            ImageUtil.showImage(TVBAdActivity.this, "http://b.zol-img.com.cn/desk/bizhi/image/5/1920x1200/1409194293467.jpg", mAdImage);

                            Glide.with(TVBAdActivity.this).load("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png").into(mAdImage);
                            mHandler.sendEmptyMessageDelayed(TYPE_IMAGE_AD_TIME_COUNT, mImageAdBean.getDisplayTime());
                        } else {
                            imageAdFinish();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    int old_duration = 0;
    Runnable runnable = new Runnable() {
        public void run() {
            int duration = mVideoView.getCurrentPosition();
            if (old_duration == duration && mVideoView.isPlaying()) {

                mAdWaitTime++;
                if (mAdWaitTime == 10) {
                    mVideoView.stopPlayback();
                    startTVBPage();
                }
            } else {
                mAdWaitTime = 0;
            }
            old_duration = duration;

            mHandler.postDelayed(runnable, 1000);
        }
    };


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


//        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(MediaPlayer mediaPlayer, int what, int extra) {
//                switch (what) {
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//
//                        break;
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//
//                        break;
//                }
//
//                return false;
//            }
//        });

        //加载数据开始干3件事
        //1、计时
//        mHandler.sendEmptyMessageDelayed(TYPE_REQUEST_AD_TIME, 5000);

        //2、发送图片广告请求
//        requestForImageAd(true);
        //3、发送视频广告请求
//        requestForMediaAd(false);
//        requestForMediaAd(true);


        //网络视频
//        String videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        String videoUrl = Environment.getExternalStorageDirectory().getPath() + "/yewen.mp4";
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
        List<String> images = mImageAdBean.getImages();
        if (images != null && images.size() > 0) {
            ImageUtil.showImage(this, mImageAdBean.getImages().get(mCurrentAdImageIndex), mAdImage);
            mHandler.sendEmptyMessageDelayed(TYPE_IMAGE_AD_TIME_COUNT, mImageAdBean.getDisplayTime());

        } else {//没有需要显示的图片直接结束流程
            imageAdFinish();
        }

    }

    // TODO: 2020/2/4
    private void playMediaAd() {

        //网络视频
        String videoUrl = "https://v-cdn.zjol.com.cn/280443.mp4";
//        String videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        // String videoUrl = "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4";
//        Uri uri = Uri.parse(videoUrl);
//        //设置视频路径
//        mVideoView.setVideoURI(uri);

        mVideoView.setVideoPath(videoUrl);

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


}
