package com.wsy.testuiapplication.ad;

import android.content.Context;

import com.wsy.testuiapplication.constant.PlatformConstants;
import com.wsy.testuiapplication.util.FileUtil;
import com.wsy.testuiapplication.util.PreferencesUtil;
import com.wsy.testuiapplication.util.Slog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by qing on 2017/4/17.
 */

public class AdVideoManager {

    private static final String TAG = "AdVideoManager";

    private int downloadState = 0;
    private final int IDEL = 0;
    private final int DOWNLOADING = 1;

    private static AdVideoManager instance;

    public static AdVideoManager getInstance() {
        if (instance == null) {
            instance = new AdVideoManager();
        }

        return instance;
    }

    public interface OnAdVideoDownloadOverListener {
        void onAdVideoDownloadOver(String adPath);
    }

    private OnAdVideoDownloadOverListener mListener;

    public void setOnAdVideoDownloadOverListener(OnAdVideoDownloadOverListener listener) {
        this.mListener = listener;
    }

    public void onAdVideoDownloaded(String adFilePath) {
        if (mListener == null) {
            throw new RuntimeException("====请设置广告回掉监听====");
        }
        mListener.onAdVideoDownloadOver(adFilePath);
    }

    public void downloadAdVideo(Context context, String playUrl, File videoFileDir, File videoFilePath) {
        if (downloadState == DOWNLOADING) {
            return;
        }
        if (mListener == null) {
            throw new RuntimeException("====请设置广告回掉监听====");
        }
        downloadState = DOWNLOADING;

        //清除所有文件，保留根目录
        FileUtil.clearPath(videoFileDir, true);

        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(playUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(20000);
            int videoSize = connection.getContentLength();
            Slog.i(TAG, "The new ad video size is:" + videoSize);
            PreferencesUtil.putInt(context, PlatformConstants.AD_VIDEO_SIZE, videoSize);

            is = connection.getInputStream();
            fos = new FileOutputStream(videoFilePath);

            byte[] buff = new byte[4096];
            int len = 0;
            while ((len = is.read(buff)) != -1) {
                fos.write(buff, 0, len);
            }

            //下载完成，回调接口，播放广告视频
            Slog.i(TAG, "The new ad video download completed!");
            downloadState = IDEL;
            mListener.onAdVideoDownloadOver(videoFilePath.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            Slog.e(TAG, "The new ad video download failed!");
            downloadState = IDEL;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
