package com.milfrost.frek.utils;

import com.milfrost.frek.BuildConfig;

/**
 * Created by ASUS on 04/12/2017.
 */

public final class LogHelper {

    public static final boolean DEBUG = BuildConfig.ENABLE_DEBUG;
    public static final boolean ERROR = BuildConfig.ENABLE_ERROR_REPORT;

    public static void debug(String tag, String string) {
        if (DEBUG) android.util.Log.d(tag, string);
    }
    public static void error(String tag, String string) {
        if (ERROR) android.util.Log.e(tag, string);
    }

    public static void debug(Class className, String string){
        debug(className.getName(), string);
    }

    public static void error(Class className, String string){
        error(className.getName(), string);
    }
}

