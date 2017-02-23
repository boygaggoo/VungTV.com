package com.vungtv.film.util;

/**
 *
 * Created by Mr Cuong on 1/21/2017.
 */

public class StringUtils {

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }
}
