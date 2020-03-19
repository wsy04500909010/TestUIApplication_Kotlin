package com.wsy.testuiapplication.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.wsy.testuiapplication.DFApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by qwl on 2017/8/28.
 */

public class FileUtil {

    private static final String TAG = "FileUtil";

    /**
     * 16进制字符集
     */
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 指定算法为MD5的MessageDigest
     */
    private static MessageDigest messageDigest = null;

    /** * 初始化messageDigest的加密算法为MD5 */
    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Safely check file existence according to a path.
     *
     * @param path Target file path.
     * @return true: exist, false: not exist.
     */
    public static boolean exists(String path) {
        if (StringUtil.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return file.exists();
    }

    /**
     * Safely delete files (a specified file or a directory's whole content). And you can choose if
     * need to reserve the root Directory after this operation by specify the param `reserveDir`
     *
     * @param file       The target file to be removed
     * @param reserveDir reserve the root directory or not
     * @return Whether the path has been safely deleted or not. True: the target has been deleted
     * safely. False: the target was not deleted or there was an error or the path is not
     * exists
     */
    public static boolean clearPath(File file, boolean reserveDir) {
        if (file == null || !file.exists()) {
            Log.d(TAG, "file is null or file not exists");
            return false;
        }

        if (file.isFile()) {
            return file.delete();
        }

        return deleteCertainDirectory(file, reserveDir);
    }

    /**
     * Write data to a file.
     *
     * @param data       Source data.
     * @param path       Destination file path.
     * @param autoCreate if file for `path` not exists, auto create a file.
     * @return true: write succeeded, false: write failed
     */
    public static boolean writeToFile(byte[] data, String path, boolean autoCreate) {
        if (path == null) {
            return false;
        }
        return writeToFile(data, new File(path), autoCreate);
    }

    /**
     * Write data to a file.
     *
     * @param data       Source data.
     * @param file       Destination file.
     * @param autoCreate if file not exists, auto create a file.
     * @return true: write succeeded, false: write failed
     */
    public static boolean writeToFile(byte[] data, File file, boolean autoCreate) {
        if (data == null || file == null) {
            return false;
        }

        if (file.exists() && file.isFile()
                || ((!file.exists() || file.isDirectory()) && autoCreate && createFile(file,
                false))) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(data);
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    private static boolean createFile(File file, boolean isDir) {
        if (file == null) {
            return false;
        }

        try {
            if (!file.exists()) {
                if (isDir) {
                    return file.mkdirs();
                } else {
                    File parent = file.getParentFile();
                    return parent.exists() ? file.createNewFile()
                            : (parent.mkdirs() && file.createNewFile());
                }
            } else {
                if ((file.isFile() && isDir) || (file.isDirectory() && !isDir)) {
                    if (!clearPath(file, false)) {
                        Log.d(TAG, "file already exist, but failed to be deleted");
                        return false;
                    }
                    return isDir ? file.mkdir() : file.createNewFile();
                } else {
                    Log.d(TAG, "file already exists");
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean deleteCertainDirectory(File dir, boolean reserveDir) {
        File[] files = dir.listFiles();

        if (files == null) {
            Log.d(TAG, "Occured an error when list files");
            return false;
        }

        for (File f : files) {
            if (f.isFile()) {
                if (!f.delete()) {
                    Log.d(TAG, "Occured an error when delete a file in list");
                    return false;
                }
            } else {
                if (!deleteCertainDirectory(f, false)) {
                    return false;
                }
            }
        }

        if (!reserveDir) {
            return dir.delete();
        }

        return true;
    }

    /**
     * * 获取文件的MD5值
     *
     * @param file 目标文件
     * @return MD5字符串
     */
    public static String getFileMD5String(File file) {
        String ret = "";
        FileInputStream in = null;
        FileChannel ch = null;
        try {
            in = new FileInputStream(file);
            ch = in.getChannel();
            ByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            messageDigest.update(byteBuffer);
            ret = bytesToHex(messageDigest.digest());
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ch != null) {
                try {
                    ch.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    /**
     * * 将字节数组转换成16进制字符串
     *
     * @param bytes 目标字节数组
     * @return 转换结果
     */
    public static String bytesToHex(byte bytes[]) {
        return bytesToHex(bytes, 0, bytes.length);

    }

    /**
     * * 将字节数组中指定区间的子数组转换成16进制字符串
     *
     * @param bytes 目标字节数组
     * @param start 起始位置（包括该位置）
     * @param end   结束位置（不包括该位置）
     * @return 转换结果
     */
    public static String bytesToHex(byte bytes[], int start, int end) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < start + end; i++) {
            sb.append(byteToHex(bytes[i]));
        }
        return sb.toString();

    }

    /**
     * * 将单个字节码转换成16进制字符串
     *
     * @param bt 目标字节
     * @return 转换结果
     */
    public static String byteToHex(byte bt) {
        return HEX_DIGITS[(bt & 0xf0) >> 4] + "" + HEX_DIGITS[bt & 0xf];

    }

    /**
     * get the file size
     *
     * @param path the file-path
     * @return the size
     */
    public static long getFileSize(String path) {
        long count = 0;
        File orgFile = new File(path);
        if (orgFile != null && orgFile.exists()) {
            if (orgFile.isDirectory()) {
                File[] file = orgFile.listFiles();
                for (int i = 0; i < (file != null ? file.length : 0); i++) {
                    File temp = file[i];
                    count += (temp != null ? temp.length() : 0);
                }
            } else {
                count = orgFile.length();
            }
        }
        return count;
    }

    /**
     * init the cache dir
     *
     * @return
     */
    public static String initCacheDir() {
        File cacheDir = null;
        try {
            if (isSDCardAvailable()) {
                String path = DFApplication.getInstance().getExternalCacheDir() + File.separator
                        + "dftv";
                cacheDir = new File(path);
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }
            } else {
                cacheDir = DFApplication.getInstance().getApplicationContext().getCacheDir();
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }
            }
        } catch (Exception e) {
            cacheDir = DFApplication.getInstance().getApplicationContext().getCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
        }
        return cacheDir.getAbsolutePath();
    }

    /**
     * 判断sdcard是否可用
     *
     * @return
     */
    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 下载apk缓存目录
     *
     * @return
     */
    public static String getApkCacheDir() {
        String path = initCacheDir() + File.separator + "apk";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }

    /**
     * 图片缓存目录
     *
     * @return
     */
    public static String getImageCacheDir() {
        String path = initCacheDir() + File.separator + "images";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }

    /**
     * 接口数据缓存目录
     *
     * @return
     */
    public static String getVolleyCacheDir() {
        String path = initCacheDir() + File.separator + "volley";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return path;
    }

    /**
     * copy file
     *
     * @param source input file
     * @param target output file
     */
    public static void copy(File source, File target) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileInputStream = new FileInputStream(source);
            fileOutputStream = new FileOutputStream(target);
            byte[] buffer = new byte[1024];
            while (fileInputStream.read(buffer) > 0) {
                fileOutputStream.write(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //================================================

    /**
     * 根据URI获取文件真实路径（兼容多机型）
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePathByUri(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {

            int sdkVersion = Build.VERSION.SDK_INT;
            if (sdkVersion >= 19) { // api >= 19
                return getRealPathFromUriAboveApi19(context, uri);
            } else { // api < 19
                return getRealPathFromUriBelowAPI19(context, uri);
            }
        } else {
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        return null;
    }

    /**
     * 适配api19及以上,根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    @SuppressLint("NewApi")
    private static String getRealPathFromUriAboveApi19(Context context, Uri uri) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if (isMediaDocument(uri)) { // MediaProvider
                // 使用':'分割
                String type = documentId.split(":")[0];
                String id = documentId.split(":")[1];

                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = {id};

                //
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else {
                    if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else {
                        if ("audio".equals(type)) {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }
                    }
                }

                filePath = getDataColumn(context, contentUri, selection, selectionArgs);
            } else {
                if (isDownloadsDocument(uri)) { // DownloadsProvider
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(documentId));
                    filePath = getDataColumn(context, contentUri, null, null);
                } else {
                    if (isExternalStorageDocument(uri)) {
                        // ExternalStorageProvider
                        final String docId = DocumentsContract.getDocumentId(uri);
                        final String[] split = docId.split(":");
                        final String type = split[0];
                        if ("primary".equalsIgnoreCase(type)) {
                            filePath = Environment.getExternalStorageDirectory() + "/" + split[1];
                        }
                    } else {
                        //Log.e("路径错误");
                    }
                }
            }
        } else {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                // 如果是 content 类型的 Uri
                filePath = getDataColumn(context, uri, null, null);
            } else {
                if ("file".equals(uri.getScheme())) {
                    // 如果是 file 类型的 Uri,直接获取图片对应的路径
                    filePath = uri.getPath();
                }
            }
        }
        return filePath;
    }

    /**
     * 适配api19以下(不包括api19),根据uri获取图片的绝对路径
     *
     * @param context 上下文对象
     * @param uri     图片的Uri
     * @return 如果Uri对应的图片存在, 那么返回该图片的绝对路径, 否则返回null
     */
    private static String getRealPathFromUriBelowAPI19(Context context, Uri uri) {
        return getDataColumn(context, uri, null, null);
    }

    /**
     * 获取数据库表中的 _data 列，即返回Uri对应的文件路径
     *
     * @return
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        String path = null;

        String[] projection = new String[]{MediaStore.Images.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(projection[0]);
                path = cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return path;
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is MediaProvider
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri the Uri to check
     * @return Whether the Uri authority is DownloadsProvider
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
}
