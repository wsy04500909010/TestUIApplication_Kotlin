package com.wsy.testuiapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.wsy.testuiapplication.ad.bean.AdListBean;
import com.wsy.testuiapplication.util.ImageUtil;
import com.wsy.testuiapplication.util.Slog;
import com.wsy.testuiapplication.util.ToastUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by WSY on 2020/2/4.
 */
public class TVBAdActivity extends HXBaseActivity {

    private static final String TAG = TVBAdActivity.class.getSimpleName();

    private static final int AD_READY = 0;
    private static final int AD_ERROR = 1;
    private static final int AD_COMPLETE = 2;

    private ImageView mAdImage;
    private VideoView mVideoView;

    private List<AdListBean> mImageAdList;
    private List<AdListBean> mMediaAdList;
    private boolean mFinished;

    //===================广告新逻辑开始
    private ConstraintLayout mRootLayout;

    //    消息类型-请求计时
    private static final int TYPE_REQUEST_AD_TIME = 10;
    //    消息类型-图片加载完一张 切换到下一张
    private static final int TYPE_IMAGE_AD_TIME_COUNT = 11;


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

    //下一个图片/视频广告的坐标
    private int mNextAdImageIndex;
    private int mNextAdMediaIndex;

    // 视频广告等待时间
    private int mAdWaitTime;
    // 图片广告等待时间
    private static final int IMAGE_AD_DEFAULT_DISPLAYTIME = 3000;

//    private LauncherColorListBean.ResultBean.ColorListBean mColor;

    private Handler mHandler;
    private boolean mRunableFlag;//监测卡顿的runnable是否运行

    private boolean storagePermission;

    // 老进度 比较老进度和新进度有没有区别 没有区别说明发生了卡顿
    int old_duration = 0;


    Runnable runnable = new Runnable() {
        public void run() {
            if (mRunableFlag) {
                Slog.d(TAG, "waittime=" + mAdWaitTime);
                int duration = mVideoView.getCurrentPosition();
                if (old_duration == duration && mVideoView.isPlaying()) {

                    mAdWaitTime++;
                    Slog.d(TAG, "ad wait=" + mAdWaitTime + "");
                    if (mAdWaitTime == 10) {
                        mVideoView.stopPlayback();
                        startTVBPage();
                    }
                } else {
                    mAdWaitTime = 0;
                }
                old_duration = duration;

                mHandler.postDelayed(runnable, 1000);
            } else {
                old_duration = 0;//清0 为播放下一段视频广告准备
            }

        }
    };

    private void initHandler() {

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case AD_ERROR:
                        mNextAdMediaIndex++;
                        mRunableFlag = false;
                        if (mNextAdMediaIndex < mMediaAdList.size()) {
                            mVideoView.setVideoPath(mMediaAdList.get(mNextAdMediaIndex).getUrl());
                        } else {
                            //直接跳转
                            startTVBPage();
                        }
                        break;
                    case AD_READY:
                        //开始播放视频
                        mVideoView.start();
                        mAdImage.setVisibility(View.GONE);
                        mRunableFlag = true;

                        // 监测是否卡顿
                        mHandler.postDelayed(runnable, 0);
                        break;
                    case AD_COMPLETE:
                        mNextAdMediaIndex++;
                        mRunableFlag = false;
                        if (mNextAdMediaIndex < mMediaAdList.size()) {
                            mVideoView.setVideoPath(mMediaAdList.get(mNextAdMediaIndex).getUrl());
                        } else {
                            startTVBPage();
                        }
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
                        mNextAdImageIndex++;
                        if (mNextAdImageIndex < mImageAdList.size()) {
                            ImageUtil.showImage(TVBAdActivity.this, mImageAdList.get(mNextAdImageIndex).getUrl(),
                                    mAdImage);
//                            ImageUtil.showImage(TVBAdActivity.this, "http://b.zol-img.com.cn/desk/bizhi/image/5/1920x1200/1409194293467.jpg", mAdImage);
//                            Glide.with(TVBAdActivity.this).load("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png").into(mAdImage);
                            mHandler.sendEmptyMessageDelayed(TYPE_IMAGE_AD_TIME_COUNT,
                                    Long.parseLong(mImageAdList.get(mNextAdImageIndex).getDuration()));
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


    @Override
    protected void initView() {
        setContentView(R.layout.activity_tvb_ad);

        mRootLayout = findViewById(R.id.root_layout);
        mAdImage = (ImageView) findViewById(R.id.iv_ad);

        //动态添加 防止泄露
        if (mVideoView == null) {
            mVideoView = new VideoView(DFApplication.getInstance().getApplicationContext());
            mVideoView.setId(R.id.videoview);

            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(0, 0);
            layoutParams.startToStart = R.id.root_layout;
            layoutParams.endToEnd = R.id.root_layout;
            layoutParams.topToTop = R.id.root_layout;
            layoutParams.bottomToBottom = R.id.root_layout;

            mVideoView.setLayoutParams(layoutParams);
            mVideoView.setTag("videoview");

            mRootLayout.addView(mVideoView);
        }
        // 准备广告假数据
        prepareDatas();
//        getIntentData();
        initHandler();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        //requestCode就是requestPermissions()的第三个参数
        //permission就是requestPermissions()的第二个参数
        //grantResults是结果，0调试通过，-1表示拒绝

        switch (requestCode) {
            case 100:
                switch (permission[0]) {
                    case Manifest.permission.READ_EXTERNAL_STORAGE://权限1
                    case Manifest.permission.WRITE_EXTERNAL_STORAGE://权限2
                        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                            //        本地视频
                            String videoUrl = Environment.getExternalStorageDirectory().getPath() + "/yewen.mp4";
                            //设置视频路径
                            mVideoView.setVideoPath(videoUrl);

                        } else {
                            Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                }
                break;
            default:
        }
    }


    //准备假数据
    private void prepareDatas() {
        //图片广告
        mImageAdList = new ArrayList<>();

        AdListBean adListBean1 = new AdListBean();
        adListBean1.setUrl(
                "http://00.minipic.eastday.com/20170925/20170925141357_d41d8cd98f00b204e9800998ecf8427e_3.jpeg");
        adListBean1.setDuration("3");
        AdListBean adListBean2 = new AdListBean();
        adListBean2.setUrl(
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2026218730,2462529383&fm=11&gp=0.jpg");
        adListBean2.setDuration("3");
        AdListBean adListBean3 = new AdListBean();
        adListBean3.setUrl("http://b.zol-img.com.cn/desk/bizhi/image/5/1920x1200/1409194293467.jpg");
        adListBean3.setDuration("3");

        mImageAdList.add(adListBean1);
        mImageAdList.add(adListBean2);
        mImageAdList.add(adListBean3);

        //视频广告
        mMediaAdList = new ArrayList<>();

        AdListBean adListBean4 = new AdListBean();
        adListBean4.setUrl("http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4");
        AdListBean adListBean5 = new AdListBean();
//        adListBean5.setUrl("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        adListBean5.setUrl("http://vjs.zencdn.net/v/oceans.mp4");

        mMediaAdList.add(adListBean4);
        mMediaAdList.add(adListBean5);

        filterAds();

    }

    //过滤无效广告（没有Url的,图片广告如果没有duration，给个默认值）
    private void filterAds() {
        Iterator<AdListBean> it = mImageAdList.iterator();
        while (it.hasNext()) {
            AdListBean bean = it.next();
            if ("".equals(bean.getUrl())) {
                it.remove();
                continue;
            }
            if (bean.getDuration() == null || "".equals(bean.getDuration())) {
                bean.setDuration(IMAGE_AD_DEFAULT_DISPLAYTIME + "");
            } else {
                bean.setDuration(String.valueOf(Integer.valueOf(bean.getDuration()) * 1000));
            }
        }

        Iterator<AdListBean> it2 = mMediaAdList.iterator();
        while (it2.hasNext()) {
            AdListBean bean = it2.next();
            if ("".equals(bean.getUrl())) {
                it2.remove();
            }
        }

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
                mHandler.sendEmptyMessage(AD_READY);
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mHandler.sendEmptyMessage(AD_COMPLETE);
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Slog.d(TAG, "onError: play error.");
                mHandler.sendEmptyMessage(AD_ERROR);
                return true;
            }
        });

        //加载数据开始干3件事
        //1、计时
//        mHandler.sendEmptyMessageDelayed(TYPE_REQUEST_AD_TIME, 5000);

        //2、发送图片广告请求
//        requestForImageAd(false);
        //3、发送视频广告请求
//        requestForMediaAd(true);


//        mVideoView.setVideoPath("http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4");
        mVideoView.setVideoPath("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
//        mVideoView.setVideoPath("http://vjs.zencdn.net/v/oceans.mp4");

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

    private void playImageAd() {
        if (mImageAdList != null && mImageAdList.size() > 0) {
            ImageUtil.showImage(this, mImageAdList.get(0).getUrl(), mAdImage);
            mHandler.sendEmptyMessageDelayed(TYPE_IMAGE_AD_TIME_COUNT,
                    Long.parseLong(mImageAdList.get(0).getDuration()));

        } else {//没有需要显示的图片直接结束流程
            imageAdFinish();
        }

    }

    private void playMediaAd() {

//        if (Build.VERSION.SDK_INT >= 23) {
//            int REQUEST_CODE_PERMISSION_STORAGE = 100;
//            String[] permissions = {
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//            };
//
//            for (String str : permissions) {
//                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
//                    this.requestPermissions(permissions, REQUEST_CODE_PERMISSION_STORAGE);
//                    return;
//                }
//            }
//
//            String videoUrl = Environment.getExternalStorageDirectory().getPath() + "/yewen.mp4";
////        //设置视频路径
//            mVideoView.setVideoPath(videoUrl);
//        } else {

//        //网络视频
////        String videoUrl = "https://v-cdn.zjol.com.cn/280443.mp4";
////        String videoUrl = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
//        String videoUrl = "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4";
////        本地视频
////            String videoUrl = Environment.getExternalStorageDirectory().getPath() + "/yewen.mp4";
////        //设置视频路径
//        mVideoView.setVideoPath(videoUrl);

        if (mMediaAdList != null && mMediaAdList.size() > 0) {
            String videoUrl = mMediaAdList.get(0).getUrl();
            mVideoView.setVideoPath(videoUrl);

        }
//        }

    }


    private void startTVBPage() {
//        PosterClickUtil.gotoActivityByColorInfo(this, mColor);
        ToastUtil.showShortToast("这里应该跳转了");
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mMediaPlayer.reset();
//        mMediaPlayer.release();
//        mMediaPlayer = null;

        mVideoView.suspend();
        mVideoView.setOnErrorListener(null);
        mVideoView.setOnPreparedListener(null);
        mVideoView.setOnCompletionListener(null);
        mRootLayout.removeView(mRootLayout.findViewWithTag("videoview"));

        mVideoView = null;

        mHandler.removeCallbacksAndMessages(null);
    }


}
