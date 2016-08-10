package com.cnziz.updatelib.utils;

import android.util.Log;

public class LogUtils {
    // 容许打印日志的类型，默认是true，设置为false则不打印
    public static boolean debug = true;
 
    public static void d(String tag, String content) {
        if (!debug)
            return;
            Log.d(tag, content);
    }
 
    public static void e(String tag, String content) {
        if (!debug)
            return;
            Log.e(tag, content);
    }
 
    public static void i(String tag, String content) {
        if (!debug)
            return;
            Log.i(tag, content);
    }
 
    public static void v(String tag, String content) {
        if (!debug)
            return;
            Log.v(tag, content);
    }
 
    public static void w(String tag, String content) {
        if (!debug)
            return;
        Log.w(tag, content);
    }
}
