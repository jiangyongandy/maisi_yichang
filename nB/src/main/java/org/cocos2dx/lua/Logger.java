package org.cocos2dx.lua;

/**
 * 功能
 * Created by Jiang on 2017/12/15.
 */
import android.util.Log;



public class Logger {
    public static void showLog(String msg, String tag) {
        if (!CommonConstant.isDebug) {
            return;
        }
        show(msg, Log.INFO, tag);
    }

    public static void show(String msg, int level, String tag) {
        switch (level) {
            case Log.VERBOSE:
                Log.v(tag, msg);
                break;
            case Log.DEBUG:
                Log.d(tag, msg);
                break;
            case Log.WARN:
                Log.w(tag, msg);
                break;
            case Log.ERROR:
                Log.e(tag, msg);
                break;
            default:
                Log.i(tag, msg);
                break;
        }
    }
}
