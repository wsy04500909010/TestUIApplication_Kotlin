package com.wsy.testuiapplication.util;

import java.util.List;

/**
 * Created by qwl on 2017/8/28.
 */

public class StringUtil {

    /**
     * Check if a string is empty or only has spaces.
     *
     * @param str Target string.
     * @return true: empty, false: not empty.
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Convert a null string to an empty string and never return null
     *
     * @param str source string
     * @return Converted string.
     */
    public static String getNonNull(String str) {
        return str == null ? "" : str;
    }

    public static String listToString(String delimiter, List<String> list) {
        if (list == null || list.size() == 0) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String temp = list.get(i);
            stringBuilder.append(temp);
            if (i < list.size() - 1) {
                stringBuilder.append(delimiter);
            }
        }
        return stringBuilder.toString();
    }
}
