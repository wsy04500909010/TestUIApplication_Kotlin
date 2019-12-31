package com.wsy.testuiapplication.util

import java.io.UnsupportedEncodingException

class MD5Util {
    companion object {

        //获得MD5加密后的串 user相关
        fun getUserMD5Str(userStr: String): String {
            var userIdMD5 = ""
            if ("" == userStr || userStr.contains("@visitor.italkbb.com")) { //清空时 游客登录时 不加密处理
                return userStr
            } else {
                val array = userStr.split("@".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val headStr = array[0]
                try {
                    var utf8Bytes = ByteArray(0)
                    utf8Bytes = headStr.toByteArray(charset("UTF-8"))
                    //然后用utf-8 对这个字节数组解码成新的字符串
                    val utf8Str = String(utf8Bytes, charset("UTF-8"))
                    userIdMD5 = DFMD5.getMD5(utf8Str) + "@" + array[1]
                    return userIdMD5

                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                    return ""
                }

            }
        }
    }


//    java版
    //        String userIdMD5 = "";
    //        if ("".equals(userId) || userId.contains(AppConstants.VIRTUAL_USER_EMAIL)) {//清空时 游客登录时 不加密处理
    //            userIdMD5 = userId;
    //        } else {
    //            String[] array = userId.split("@");
    //            String headStr = array[0];
    //            try {
    //                byte[] utf8Bytes = new byte[0];
    //                utf8Bytes = headStr.getBytes("UTF-8");
    //                //然后用utf-8 对这个字节数组解码成新的字符串
    //                String utf8Str = new String(utf8Bytes, "UTF-8");
    //                userIdMD5 = DFMD5.getMD5(utf8Str) + "@" + array[1];
    //
    //            } catch (UnsupportedEncodingException e) {
    //                e.printStackTrace();
    //            }
    //
    //        }
}