package org.cocos2dx.lua;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.Utils;
import com.maisi.video.obj.video.PlayLineEntity;
import com.maisi.video.obj.video.UpdateEntity;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.maisi.video.obj.WXAPIConst;

import org.cocos2dx.lua.service.Service;
import org.cocos2dx.lua.ui.ChargeActivity;
import org.cocos2dx.lua.ui.MainActivity;
import org.cocos2dx.lua.ui.fragment.HomeFragment;

import java.util.ArrayList;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class APPAplication extends Application {

    public static APPAplication instance;
    public boolean qbsdkIsInit = false;
    public QbInitListener qbInitListener;
    public static IWXAPI api = null;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        instance = this;
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                qbsdkIsInit = true;
                if (qbInitListener != null) {
                    qbInitListener.onQbInit();
                }
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                Log.d("app", " onCoreInitFinished ");
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
        //友盟
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setDebugMode(true);
        //上报内核错误信息
//		MobclickAgent.reportError(this, WebView.getCrashExtraMessage(this));
        Log.e("app", "x5内核错误信息------------------" + WebView.getCrashExtraMessage(this));

        Utils.init(this);
        //友盟注册微信等平台
        if(CommonConstant.isDebug)
            Config.DEBUG = true;
        PlatformConfig.setWeixin(WXAPIConst.APP_ID, WXAPIConst.AppSecret);
        PlatformConfig.setQQZone("1106486857", "mYnWmd6Caa28d5zJ");
        PlatformConfig.setSinaWeibo("1643151326", "b8bc9ed471127abab76e55548ae2a276", "http://39.108.151.95:8000/");

        if (CommonConstant.isRelease) {

            VipHelperUtils.getInstance().updatePlayLineByServer();
        }
    }

    public interface QbInitListener {
        void onQbInit();
    }

}
