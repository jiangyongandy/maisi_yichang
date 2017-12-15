package org.cocos2dx.lua;

import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.maisi.video.obj.video.AppInfo;
import com.zuiai.nn.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 功能
 * Created by Jiang on 2017/12/5.
 */

public class AppHelperUtils {

    public static String COLLECTAPPDATA = "COLLECTAPPDATA";

    public static int[] icons2 = {R.drawable.iv_util, R.drawable.iv_utils2
    };

    public static List<AppInfo> getCollectAPPdata(Context context) {
        return DataHelper.getDeviceData(context, AppHelperUtils.COLLECTAPPDATA);
    }

    public static boolean saveCollectAPPdata(Context context, AppInfo appInfo) {
        List<AppInfo> collectData = DataHelper.getDeviceData(context, AppHelperUtils.COLLECTAPPDATA);
        if (collectData == null) {
            collectData = new ArrayList<>();
        } else {

            for (int i = 0; i < collectData.size(); i++) {
                if (collectData.get(i).getPackageName().equals(appInfo.getPackageName()))
                    return false;
            }
        }
        collectData.add(appInfo);
        return DataHelper.saveDeviceData(context, COLLECTAPPDATA, collectData);
    }

    public static boolean deleteAppinfo(Context context, AppInfo appInfo) {
        List<AppInfo> collectData = DataHelper.getDeviceData(context, AppHelperUtils.COLLECTAPPDATA);
        if (collectData == null) {
            collectData = new ArrayList<>();
        } else {

            Iterator<AppInfo> iter = collectData.iterator();
            while (iter.hasNext()) {
                AppInfo s = iter.next();
                if (s.getPackageName().equals(appInfo.getPackageName())) {
                    iter.remove();
                }
            }
        }
        return DataHelper.saveDeviceData(context, COLLECTAPPDATA, collectData);
    }

}
