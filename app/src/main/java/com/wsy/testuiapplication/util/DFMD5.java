package com.wsy.testuiapplication.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by valenti on 30/11/2017.
 */

public class DFMD5 {

    public static String getMD5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            return getHashString(digest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getHashString(MessageDigest d) {
        StringBuilder builder = new StringBuilder();
        for (byte b : d.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }
}
