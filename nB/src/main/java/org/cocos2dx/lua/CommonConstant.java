package org.cocos2dx.lua;

import com.blankj.utilcode.util.SDCardUtils;
import com.zuiai.nn.R;

import java.io.File;

/**
 * 功能
 * Created by Jiang on 2017/12/1.
 */

public interface CommonConstant {

    //是否是正式发行版
    boolean isRelease = true;

    /* 是否是debug模式 */
    boolean isDebug = APPAplication.instance.getResources().getBoolean(R.bool.is_debug);

    public static final String PATH_DATA = APPAplication.instance.getCacheDir().getAbsolutePath() + File.separator + "data";

    public static final String PATH_CACHE = PATH_DATA + File.separator + "NetCache";

    public static final String FILE_STORAGE = SDCardUtils.getSDCardPaths() + "luandunFile" + File.separator;

}
