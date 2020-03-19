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
 * Created by wsy on 2020/1/9.
 */

public class AdImageManager {

    private static final String TAG = "AdImageManager";

    private int downloadState = 0;
    private final int IDEL = 0;
    private final int DOWNLOADING = 1;

    private static AdImageManager instance;

    public static AdImageManager getInstance() {
        if (instance == null) {
            instance = new AdImageManager();
        }

        return instance;
    }

    public interface OnAdImageDownloadOverListener {
        void onAdImageDownloadOver(String adPath);
    }

    private OnAdImageDownloadOverListener mListener;

    public void setOnAdImageDownloadOverListener(OnAdImageDownloadOverListener listener) {
        this.mListener = listener;
    }

    public void onAdImageDownloaded(String adFilePath) {
        if (mListener == null) {
            throw new RuntimeException("====请设置广告回掉监听====");
        }
        mListener.onAdImageDownloadOver(adFilePath);
    }

    public void downloadAdImage(Context context, String imageUrl, File imageFileDir, File imageFilePath) {
        if (downloadState == DOWNLOADING) {
            return;
        }
        if (mListener == null) {
            throw new RuntimeException("====请设置广告回掉监听====");
        }
        downloadState = DOWNLOADING;

        //清除所有文件，保留根目录
        FileUtil.clearPath(imageFileDir, true);

        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(20000);
            int imageSize = connection.getContentLength();
            Slog.i(TAG, "The new ad image size is:" + imageSize);
            PreferencesUtil.putInt(context, PlatformConstants.AD_IMAGE_SIZE, imageSize);

            is = connection.getInputStream();
            fos = new FileOutputStream(imageFilePath);

            byte[] buff = new byte[4096];
            int len = 0;
            while ((len = is.read(buff)) != -1) {
                fos.write(buff, 0, len);
            }

            //下载完成，回调接口，播放广告视频
            Slog.i(TAG, "The new ad image download completed!");
            downloadState = IDEL;
            mListener.onAdImageDownloadOver(imageFilePath.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            Slog.e(TAG, "The new ad image download failed!");
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
