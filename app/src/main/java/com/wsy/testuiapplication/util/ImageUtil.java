package com.wsy.testuiapplication.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wsy.testuiapplication.DFApplication;

import java.io.File;
import java.lang.ref.SoftReference;

/**
 * Created by qwl on 2017/8/28.
 */

public class ImageUtil {

    /**
     * Display the image specified by 'url' in 'imageView', without place holder.
     *
     * @param context   The caller's context, activity.
     * @param url       Image url.
     * @param imageView The Target ImageView
     */
    public static void showImage(Context context, String url, ImageView imageView) {
        if (context == null) return;
        showImage(DFApplication.getInstance().getApplicationContext(), url, 0, imageView);
    }

    /**
     * Display the image specified by 'url' in 'imageView', with a place holder.
     *
     * @param context        The caller's context, activity.
     * @param url            Image url.
     * @param placeHolderRes Place holder resource id.
     * @param imageView      The Target ImageView.
     */
    public static void showImage(Context context, String url, int placeHolderRes,
                                 ImageView imageView) {
        if (imageView == null) return;
        SoftReference<ImageView> softReference = new SoftReference<>(imageView);
        Glide.with(DFApplication.getInstance().getApplicationContext()).load(url).placeholder
                (placeHolderRes).into(softReference.get());
    }

    /**
     * Display the image specified by 'resource' in 'imageView'.
     *
     * @param context    The caller's context, activity.
     * @param resourceId Image resource id.
     * @param imageView  The Target ImageView.
     */
    public static void showImage(Context context, int resourceId, ImageView imageView) {
        if (imageView == null) return;
        SoftReference<ImageView> softReference = new SoftReference<>(imageView);
        Glide.with(DFApplication.getInstance().getApplicationContext()).load(resourceId).into
                (softReference.get());
    }

    /**
     * Display the image specified by 'url' in 'imageView', without place holder, without animation.
     *
     * @param context   The caller's context, activity.
     * @param url       Image url.
     * @param imageView The Target ImageView
     */
    public static void showImageWithoutAnimate(Context context, String url, ImageView imageView) {
        if (imageView == null) return;
        showImageWithoutAnimate(context, url, 0, imageView);
    }

    /**
     * Display the image specified by 'url' in 'imageView', with a place holder, without animation.
     *
     * @param context        The caller's context, activity.
     * @param url            Image url.
     * @param placeHolderRes Place holder resource id.
     * @param imageView      The Target ImageView.
     */
    public static void showImageWithoutAnimate(Context context, String url, int placeHolderRes,
                                               ImageView imageView) {
        if (imageView == null) return;
        SoftReference<ImageView> softReference = new SoftReference<>(imageView);
        Glide.with(DFApplication.getInstance().getApplicationContext()).load(url).dontAnimate()
                .placeholder(placeHolderRes).into(softReference.get());
    }

    /**
     * Display the image specified by 'url' in 'imageView', with a place holder(use resource which
     * widget display now), without animation.
     *
     * @param context
     * @param url
     * @param placeHolderRes
     * @param imageView
     */

    public static void showImageWithoutAnimateWithCurrentDisplay(Context context, String url,
                                                                 Drawable placeHolderRes,
                                                                 ImageView imageView) {
        if (imageView == null) return;
        SoftReference<ImageView> softReference = new SoftReference<>(imageView);
        Glide.with(DFApplication.getInstance().getApplicationContext()).load(url).dontAnimate()
                .placeholder(placeHolderRes).into(softReference.get());
    }


    /**
     * Display the image specified by 'resource' in 'imageView', without animation.
     *
     * @param context    The caller's context, activity.
     * @param resourceId Image resource id.
     * @param imageView  The Target ImageView.
     */
    public static void showImageWithoutAnimate(Context context, int resourceId, ImageView
            imageView) {
        if (imageView == null) return;
        SoftReference<ImageView> softReference = new SoftReference<>(imageView);
        Glide.with(DFApplication.getInstance().getApplicationContext()).load(resourceId)
                .dontAnimate()
                .into(softReference.get());
    }

    /**
     * download image
     *
     * @param context context
     * @param url     image url
     * @param path    image save path
     */
//    public static void downLoadImage(final Context context, final String url, final String path) {
//        if (context == null) return;
//        ThreadManager.getSingleThreadPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    FutureTarget<File> future = Glide.with(context).load(url)
//                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
//                    File file = future.get();
//                    File appDir = context.getFilesDir();
//                    File destFile = new File(appDir, path);
//                    FileUtil.copy(file, destFile);
//                    // notify gallery of updates
//                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                            Uri.fromFile(new File(destFile.getPath()))));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    /**
     * Display the image specified by 'url' in 'imageView', without place holder.
     *
     * @param context   The caller's context, activity.
     * @param url       Image url.
     * @param imageView The Target ImageView
     */
    public static void showRoundImage(Context context, String url, ImageView imageView) {
        if (imageView == null) return;
        showRoundImage(DFApplication.getInstance().getApplicationContext(), url, 0, imageView);
    }

    /**
     * Display the image specified by 'url' in 'imageView', with place holder.
     *
     * @param context        The caller's context, activity.
     * @param url            Image url.
     * @param placeHolderRes Place holder resource id.
     * @param imageView      The Target ImageView
     */
    public static void showRoundImage(Context context, String url, int placeHolderRes,
                                      ImageView imageView) {
        if (imageView == null) return;
        SoftReference<ImageView> softReference = new SoftReference<>(imageView);
        Glide.with(DFApplication.getInstance().getApplicationContext()).load(url).placeholder
                (placeHolderRes)
                .transform(new GlideRoundTransform(context)).into(softReference.get());
    }

    /**
     * Display the image specified by 'url' in 'imageView', without place holder.
     *
     * @param context   The caller's context, activity.
     * @param url       Image url.
     * @param imageView The Target ImageView
     * @param radius    The radius of corner
     */
    public static void showRoundImage(Context context, String url, ImageView imageView,
                                      int radius) {
        if (imageView == null) return;
        showRoundImage(DFApplication.getInstance().getApplicationContext(), url, 0, imageView,
                radius);
    }

    /**
     * Display the image specified by 'url' in 'imageView', with place holder.
     *
     * @param context        The caller's context, activity.
     * @param url            Image url.
     * @param placeHolderRes Place holder resource id.
     * @param imageView      The Target ImageView
     * @param radius         The radius of corner
     */
    public static void showRoundImage(Context context, String url, int placeHolderRes,
                                      ImageView imageView, int radius) {
        if (imageView == null) return;
        SoftReference<ImageView> softReference = new SoftReference<>(imageView);
        Glide.with(DFApplication.getInstance().getApplicationContext()).load(url).placeholder
                (placeHolderRes)
                .transform(new GlideRoundTransform(context, radius)).into(softReference.get());
    }

    public interface Listener {
        void onSuccess();

        void onFailed();
    }

    /**
     * Display the image specified by 'url' in 'imageView', without place holder.
     *
     * @param context   The caller's context, activity.
     * @param url       Image url.
     * @param imageView The Target ImageView.
     * @param listener  Loading result callback.
     */
    public static void showImage(Context context, String url, ImageView imageView,
                                 final Listener listener) {
        if (imageView == null) return;
        showImage(DFApplication.getInstance().getApplicationContext(), url, 0, imageView, listener);
    }

    /**
     * Display the image specified by 'url' in 'imageView', with a place holder.
     *
     * @param context        The caller's context.
     * @param url            Image url.
     * @param placeHolderRes Place holder resource id.
     * @param imageView      The Target ImageView.
     * @param listener       Loading result callback.
     */
    public static void showImage(Context context, String url, int placeHolderRes,
                                 ImageView imageView, final Listener listener) {
        if (imageView == null) return;
        SoftReference<ImageView> softReference = new SoftReference<>(imageView);
        Glide.with(DFApplication.getInstance().getApplicationContext()).load(url).placeholder
                (placeHolderRes)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable>
                            target,
                                               boolean isFirstResource) {
                        if (listener != null) {
                            listener.onFailed();
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target, boolean
                                                           isFromMemoryCache,
                                                   boolean isFirstResource) {
                        if (listener != null) {
                            listener.onSuccess();
                        }
                        return false;
                    }
                }).into(softReference.get());
    }


    /**
     * 预加载图片资源
     *
     * @param context
     * @param url
     */
    public static void preLoadImage(Context context, String url) {
        if (context == null) return;
        Glide.with(DFApplication.getInstance().getApplicationContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .preload();
    }


    /**
     * 读取preload方式提前缓存的图片 进行显示
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadPreLoadImage(Context context, String url, ImageView imageView) {
        if (imageView == null) return;
        SoftReference<ImageView> softReference = new SoftReference<>(imageView);
        Glide.with(DFApplication.getInstance().getApplicationContext())
                .load(url)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(softReference.get());
    }

}
