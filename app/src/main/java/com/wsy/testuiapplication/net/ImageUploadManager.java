package com.wsy.testuiapplication.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import com.wsy.testuiapplication.bean.MessageBean;
import com.wsy.testuiapplication.util.Slog;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by WSY on 2020/3/24.
 */
public class ImageUploadManager {

    private static class InstanceHolder {
        private static ImageUploadManager sInstance = new ImageUploadManager();
    }

    public static ImageUploadManager getInstance() {
        return InstanceHolder.sInstance;
    }

    private static final String TAG = "ImageUploadManager";
    //设置请求的内容类型
    private static final String boundary = "------" + System.currentTimeMillis();
    private static final String mimeType = "multipart/form-data; boundary=" + boundary;

    //保存内容的数组集合
    private static byte[] multipartBody;

    //需要上传的图片
    private static Bitmap bitmap;
    private static String fileName = "jpgtest.jpeg";

    //通过MultipartRequest上传图片的方法
    public static void uploadAvatarByMultipart(Context context) {

        //根据图片的路径，将其转换为bitmap类型
//        File file = new File(Environment.getDataDirectory() + File.separator + "data" + File.separator +
//                             "com.wsy.testuiapplication" + File.separator + "files" + File.separator + "icon1.png");
        File file = new File(Environment.getDataDirectory() + File.separator + "data" + File.separator +
                             "com.wsy.testuiapplication" + File.separator + "files" + File.separator + fileName);
        String path = file.getAbsolutePath();
        bitmap = BitmapFactory.decodeFile(path);
        //调用图片转数组的方法，获得图片的数组数据
        multipartBody = getImageBytes(bitmap);
        //上传图片到服务器的请求地址

        String url = "http://211.100.75.244:9999/user/v1/video/avatars?auth_key=cbd1DilQ7OT_ZFDgzAiRZTSQiyo51a6J&consumer_email=ran.cui22@net263.com";

        //执行MultipartRequest请求，设置上传的内容和内容类型，executeRequest会将请求添加到请求队列中。

        Volley.newRequestQueue(context).add(
                new MultipartRequest(Request.Method.POST, url, MessageBean.class, null, mimeType, boundary,
                        multipartBody, fileName, listener));
    }

    private static Listener listener = new Listener() {
        @Override
        public void onSuccess(Object response) {
            Slog.d(TAG, "success\n" + response.toString());
        }

        @Override
        public void onFail(Object error) {
            Slog.d(TAG, "failed\n" + error.toString());
        }
    };

    public static byte[] getImageBytes(Bitmap bmp) {
        if (bmp == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return imageBytes;
    }

    public interface Listener {
        /**
         * Called on success in main process.
         *
         * @param response Bean object or the image url.
         */
        void onSuccess(Object response);

        /**
         * Called on fail in main process.
         *
         * @param error Error code, see DFDataError.
         */
        void onFail(Object error);
    }
}
