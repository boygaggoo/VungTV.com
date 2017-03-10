package com.vungtv.film.data.source.local;

/**
 * Content class.
 * <p>
 * Created by Mr Cuong on 3/9/2017.
 * Email: vancuong2941989@gmail.com
 */

public class RemoteConfigManager {

    public static String getFanpageUrl() {
        return "https://www.facebook.com/vungtv/";
    }

    public static String getFanpageId() {
        return "1618045615167092";
    }

    public static String getEmail() {
        return "support@Vungtv.vn";
    }

    public static int getVersionCode() {
        return 2;
    }

    public static String getVersionname() {
        return "2.0";
    }

    public static String getContentUpdate() {
        return "Vừng TV đã có phiên bản 2.0<br/>Cập nhật thêm tính năng mới.<br/>Sửa 1 số lỗi.";
    }

    public static boolean isUpdateFromStore() {
        return true;
    }
}
