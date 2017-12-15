package org.cocos2dx.lua.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.blankj.utilcode.util.AppUtils;
import com.google.gson.Gson;
import com.maisi.video.obj.video.AppInfo;
import com.maisi.video.obj.video.ChargeRequestEntity;
import com.maisi.video.obj.video.PayResult;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.maisi.video.obj.WeiChatUserInfo;
import com.maisi.video.obj.video.UserInfoEntity;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.AppHelperUtils;
import org.cocos2dx.lua.BoyiRxUtils;
import org.cocos2dx.lua.CommonConstant;
import org.cocos2dx.lua.EventBusTag;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.service.Service;
import org.cocos2dx.lua.ui.AppListActivity;
import org.simple.eventbus.EventBus;

import java.util.Map;
import java.util.concurrent.Callable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 功能
 * Created by Jiang on 2017/11/30.
 */

public class UserModel {

    private static UserModel instance = null;
    //def -1 charge 0
    private int action = -1;

    private Class intent;

    public static UserModel getInstance() {
        if(instance == null)
            instance = new UserModel();
        return  instance;
    }

    public void login(final Activity activity) {

        if (VipHelperUtils.getInstance().isWechatLogin())
            return;

        final UMAuthListener umUserInfoListener = new UMAuthListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Toast.makeText(activity, "成功返回用户信息", Toast.LENGTH_LONG).show();
                final WeiChatUserInfo userInfo = new WeiChatUserInfo();
                userInfo.setHeadimgurl(map.get("iconurl"));
                userInfo.setNickname(map.get("name"));
                userInfo.setUnionid(map.get("uid"));
                VipHelperUtils.getInstance().setUserInfo(userInfo);
                //需要登录自己的后台
                Service.getComnonService().requestLogin(userInfo.getUnionid())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Toast.makeText(
                                        APPAplication.instance,
                                        "错误:" + throwable.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        })
                        .subscribe(new Subscriber<UserInfoEntity>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(
                                        APPAplication.instance,
                                        e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(UserInfoEntity result) {
                                Toast.makeText(
                                        APPAplication.instance,
                                        "登陆成功~~",
                                        Toast.LENGTH_SHORT).show();
                                VipHelperUtils.getInstance().setWechatLogin(true);
                                result.setWeiChatUserInfo(userInfo);
                                VipHelperUtils.getInstance().setVipUserInfo(result);
                                EventBus.getDefault().post(result, EventBusTag.TAG_LOGIN_SUCCESS);
                                if (result.getVipLeft() > 0) {
                                    VipHelperUtils.getInstance().setValidVip(true);
                                } else {
                                    String temp ="";
                                    if (!CommonConstant.isRelease) {
                                        temp = "需要重新激活VIP才能正常使用哦~.~";
                                    }else {
                                        temp = "需要重新激活VIP才能正常观看哦~.~";
                                    }
                                    Toast.makeText(
                                            APPAplication.instance,
                                            temp,
                                            Toast.LENGTH_SHORT).show();
                                    VipHelperUtils.getInstance().setValidVip(false);
                                }
                                if(intent != null) {
                                    Intent intent1 = new Intent(activity, intent);
                                    activity.startActivity(intent1);
                                    intent = null;
                                }
                            }
                        });

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        };

        UMAuthListener umAuthListener = new UMAuthListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                UMShareAPI.get(APPAplication.instance).getPlatformInfo(activity, SHARE_MEDIA.WEIXIN, umUserInfoListener);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        };

        UMShareAPI.get(APPAplication.instance).doOauthVerify(activity, SHARE_MEDIA.WEIXIN, umAuthListener);

    }

    public void refreshUserInfo() {

        //需要登录自己的后台
        Service.getComnonService().requestLogin(VipHelperUtils.getInstance().getVipUserInfo().getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(
                                APPAplication.instance,
                                "错误:" + throwable.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .subscribe(new Subscriber<UserInfoEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                APPAplication.instance,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(UserInfoEntity result) {
                        Toast.makeText(
                                APPAplication.instance,
                                "刷新信息~~",
                                Toast.LENGTH_SHORT).show();
                        result.setWeiChatUserInfo(VipHelperUtils.getInstance().getVipUserInfo().getWeiChatUserInfo());
                        VipHelperUtils.getInstance().setVipUserInfo(result);
                        EventBus.getDefault().post(result, EventBusTag.TAG_LOGIN_SUCCESS);
                        if (result.getVipLeft() > 0) {
                            VipHelperUtils.getInstance().setValidVip(true);
                        } else {
                            VipHelperUtils.getInstance().setValidVip(false);
                        }
                    }
                });
    }

    public void requestCharge(final Activity activity, double amount, double points, String commendNo) {

        ChargeRequestEntity chargeRequestEntity = new ChargeRequestEntity();
        chargeRequestEntity.setAmount(amount);
        chargeRequestEntity.setPoints(points);
        chargeRequestEntity.setCommendno(commendNo);
        chargeRequestEntity.setUid(VipHelperUtils.getInstance().getVipUserInfo().getUid());

        if(VipHelperUtils.getInstance().isWechatLogin()) {
            //直接充值
            Gson gson = new Gson();
            String toJson = gson.toJson(chargeRequestEntity);
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), toJson);
            Service.getComnonServiceForString().createOrder(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Toast.makeText(
                                    APPAplication.instance,
                                    "错误:" + throwable.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(
                                    APPAplication.instance,
                                    e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(String  result) {
                            payV2(result, activity);

                        }
                    });
        }else {
            action = 0;
            login(activity);
        }
    }

    /**
     * 支付宝支付业务
     *
     */
    public void payV2(final String orderInfo, final Activity activity) {

        BoyiRxUtils.makeObservable(new Callable<Map<String, String>>() {
            @Override
            public Map<String, String> call() throws Exception {

                PayTask alipay = new PayTask(activity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                return result;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1< Map<String, String>>() {
                    @Override
                    public void call( Map<String, String> aBoolean) {
                        PayResult payResult = new PayResult((Map<String, String>) aBoolean);
                        /**
                         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(
                                APPAplication.instance,
                                throwable.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void launchActivity(Activity context, Class intent) {
        if (VipHelperUtils.getInstance().isWechatLogin()) {
            Intent intent1 = new Intent(context, intent);
            context.startActivity(intent1);
        }else {
            this.intent = intent;
            login(context);
        }
    }

}
