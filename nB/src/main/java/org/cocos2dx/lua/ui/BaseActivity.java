package org.cocos2dx.lua.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * 功能
 * Created by Jiang on 2017/10/17.
 */

public class BaseActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (useEventBus())
            EventBus.getDefault().register(this);
    }

    /**
     * 是否使用eventbus
     *
     * @return true
     */
    protected boolean useEventBus() {
        return true;
    }

    public void onBackView(View view){
        finish();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
